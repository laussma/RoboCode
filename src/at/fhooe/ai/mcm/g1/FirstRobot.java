package at.fhooe.ai.mcm.g1;
import java.awt.geom.Point2D;
import java.awt.*;

import robocode.AdvancedRobot;
import robocode.HitByBulletEvent;
import robocode.RobotDeathEvent;
import robocode.ScannedRobotEvent;
import robocode.WinEvent;

public class FirstRobot extends AdvancedRobot {
	
	StrategyEvaluator se = new StrategyEvaluator();
	long fireTime = 0;
	private byte moveDirection = 1;
	private double firePower = 1;
	private double BFHeight = 0;
	private double BFWidth = 0;
	private double distance_top = 0;
	private double distance_right = 0;
	private double distance_bottom = 0;
	private double distance_left = 0;
	
	
	@Override
	public void run() {
		
		setAdjustGunForRobotTurn(true);
		setAdjustRadarForGunTurn(true);
		setBodyColor(new Color(128, 128, 50));
        setGunColor(new Color(50, 50, 20));
        setRadarColor(new Color(200, 200, 70));
        setScanColor(Color.white);
        setBulletColor(Color.blue);
		setTurnRadarRight(Double.POSITIVE_INFINITY);
		
		BFHeight = getBattleFieldHeight();
		BFWidth = getBattleFieldWidth();
		
		while(true) {
			doMove();
		}
	}

	
	@Override
	public void onScannedRobot(ScannedRobotEvent event) {
		se.updateThreatList(new EnemyRobot(event, this));
		
		EnemyRobot highestThreat = se.getHighestThreat();
		moveRadar();
		fireGun(highestThreat);
		aim(highestThreat);
	}
	
	
	
	public void aim(EnemyRobot robot) {
//		aimWithoutPrediction(robot);
		
		aimWithTargetPrediction(robot);
		
		fireTime = getTime() + 1;
	}

	private void aimWithTargetPrediction(EnemyRobot robot) {
		// firepower is based on the distance
		firePower = Math.min(500 / robot.getDistance(), 3);
		// calculate the speed of the bullet
		double bulletSpeed = 20 - firePower * 3;
		long time = (long)(robot.getDistance() / bulletSpeed);
		
		// calculate gun turn to predicted location (future X and Y)
		double futureX = robot.getFutureX(robot.getScannedX(), time);
		double futureY = robot.getFutureY(robot.getScannedY(), time);
		double absDeg = absoluteBearing(getX(), getY(), futureX, futureY);
		
		setTurnGunRight(normalizeBearing(absDeg - getGunHeading()));
	}

	private void aimWithoutPrediction(EnemyRobot robot) {
		// Calculation of angle to aim at enemy
		double absoluteBearing = getHeadingRadians() + robot.getBearingRadians();
		setTurnGunRightRadians(
		    robocode.util.Utils.normalRelativeAngle(absoluteBearing - 
		        getGunHeadingRadians()));
	}
	
	public void fireGun(EnemyRobot robot) {
		if(fireTime == getTime() && getGunTurnRemaining() == 0) {
			fire(firePower);
		}
	}
	
	public void calcDistances() {
		double X = getX();
		double Y = getY();
		// distances in order: Top, Right, Bottom, Left
		distance_top = BFHeight - Y;
		distance_right = BFWidth- X;
		distance_bottom = Y;
		distance_left =  X;
	}
	
	@Override
	public void onRobotDeath(RobotDeathEvent event) {
		se.removeThreat(event.getName());
	}

	@Override
	public void onHitByBullet(HitByBulletEvent event) {
		EnemyRobot enemy = se.threatList.get(event.getName());
		enemy.addDamageGiven(event.getPower());
	}


	// computes the absolute bearing between two points
	double absoluteBearing(double x1, double y1, double x2, double y2) {
		double xo = x2-x1;
		double yo = y2-y1;
		double hyp = Point2D.distance(x1, y1, x2, y2);
		double arcSin = Math.toDegrees(Math.asin(xo / hyp));
		double bearing = 0;

		if (xo > 0 && yo > 0) { // both pos: lower-Left
			bearing = arcSin;
		} else if (xo < 0 && yo > 0) { // x neg, y pos: lower-right
			bearing = 360 + arcSin; // arcsin is negative here, actuall 360 - ang
		} else if (xo > 0 && yo < 0) { // x pos, y neg: upper-left
			bearing = 180 - arcSin;
		} else if (xo < 0 && yo < 0) { // both neg: upper-right
			bearing = 180 - arcSin; // arcsin is negative here, actually 180 + ang
		}

		return bearing;
	}
	
	// normalizes a bearing to between +180 and -180
	double normalizeBearing(double angle) {
		while (angle >  180) angle -= 360;
		while (angle < -180) angle += 360;
		return angle;
	}
	
	public void doMove() {
		wallAvoidance();
		// if wall or robot is hit, we turn around
		if (getVelocity() == 0) {
			moveDirection *= -1;
		} else if(se.getHighestThreat().getDistance() < 300) {
			turnRight(se.getHighestThreat().getBearing() + 90);
			//turnLeft(getGunHeading() + 90); // position the tank 90 degrees to gun heading
			
			if(getTime() % 4 == 0) {
				moveDirection *= -1;
			}
		} else {
			turnLeft(30);
		}
		
		long divisor = getTime() % 4 != 0 ? getTime() % 4 : 1;
		
		ahead((100 / divisor) * moveDirection);
	}
	
	public void checkHighestThreatTurretHeading() {
		EnemyRobot highestThreat = se.getHighestThreat();
		
		
	}
	
	public void moveRadar() {
		if (getOthers() == 1) {
			setTurnRadarLeft(getRadarTurnRemaining());
		}
	}
	
	@Override
	public void onWin(WinEvent event) {
		stop(true);
		for (int i = 0; i < 50; i++) {
			turnRight(30);
			turnLeft(30);
		}
		resume();
	}
	
	public void wallAvoidance() {
		double redzoneFactor = 0.1;
		int turnDirection = 1;
		calcDistances();
		double heading = getHeading();
		if(distance_top < BFHeight * redzoneFactor) {
			if(270 < heading && heading < 360) {
				turnDirection = -1;
			} else {
				turnDirection = 1;
			}
			double turnAngle = calcTurnAngle(distance_top, BFHeight * redzoneFactor);
			turnRight(turnAngle * turnDirection);
			ahead(100);
		}
		if(distance_right < BFWidth * redzoneFactor) {
			turnDirection = (int)(90 - heading);
			turnDirection = (int)turnDirection/turnDirection;
			double turnAngle = calcTurnAngle(distance_right, BFWidth * redzoneFactor);
			turnRight(turnAngle * turnDirection);
			ahead(100);
		}
		if(distance_bottom < BFHeight * redzoneFactor) {
			turnDirection = (int)(90-heading);
			turnDirection = (int)turnDirection/turnDirection;
			double turnAngle = calcTurnAngle(distance_bottom, BFHeight * redzoneFactor);
			turnRight(turnAngle * turnDirection);
			ahead(100);
		}
		if(distance_left < BFWidth * redzoneFactor) {
			turnDirection = (int)(90-heading);
			turnDirection = (int)turnDirection/turnDirection;
			double turnAngle = calcTurnAngle(distance_left, BFWidth * redzoneFactor);
			turnRight(turnAngle * turnDirection);
			ahead(100);
		}
	}
	
	public double calcTurnAngle(double distance, double redzone) {
		int maxAngle = 90;
		return maxAngle - (redzone - distance)/redzone * maxAngle;
	}
}
