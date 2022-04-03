package at.fhooe.ai.mcm.g1;
import java.awt.geom.Point2D;
import java.awt.*;

import robocode.AdvancedRobot;
import robocode.HitByBulletEvent;
import robocode.RobotDeathEvent;
import robocode.ScannedRobotEvent;

public class FirstRobot extends AdvancedRobot {
	
	StrategyEvaluator se = new StrategyEvaluator();
	long fireTime = 0;
	private byte moveDirection = 1;
	private byte radarDirection = 1;
	private static final double MAX_RADAR_TRACKING_AMOUNT = 360 / 4;
	private long robotFoundTimestamp = 0;
	private byte radarTurning = 0;
	
	@Override
	public void run() {
		
		double BFHeight = getBattleFieldHeight();
		double BFWidth = getBattleFieldWidth();
		
		System.out.println("BFHeight: " + BFHeight);
		System.out.println("BFWidth: " + BFWidth);

		double rotation = getHeading();


		double X = getX();
		double Y = getY();
		
		double distances[] = calcDistances(BFHeight, BFWidth, X, Y);
		
		setAdjustGunForRobotTurn(true);
		setAdjustRadarForGunTurn(true);
		setBodyColor(new Color(128, 128, 50));
        setGunColor(new Color(50, 50, 20));
        setRadarColor(new Color(200, 200, 70));
        setScanColor(Color.white);
        setBulletColor(Color.blue);
		setTurnRadarRight(Double.POSITIVE_INFINITY);
		
		while(true) {
			doMove();
//			moveRadar();
			//fire
		}
	}

	
	@Override
	public void onScannedRobot(ScannedRobotEvent event) {
		se.updateThreatList(new EnemyRobot(event, this));
		robotFoundTimestamp = getTime();
		
		EnemyRobot highestThreat = se.getHighestThreat();
		
		double bearing = highestThreat.getBearingRadians() + getHeadingRadians();
		double latVel = highestThreat.getVelocity() * Math.sin(highestThreat.getHeadingRadians() - bearing);
		setTurnRadarLeftRadians(getRadarTurnRemainingRadians());
		
		
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
		double firePower = Math.min(500 / robot.getDistance(), 3);
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
			fire(1);
		}
	}
	
	public double[] calcDistances(double BFHeight, double BFWidth, double X, double Y) {
		// distances in order: Top, Right, Bottom, Left
		double distances[] = {BFHeight - Y, BFWidth- X, Y, X};
		return distances;
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
		
		ahead((200 / divisor) * moveDirection);
	}
	
	public void checkHighestThreatTurretHeading() {
		EnemyRobot highestThreat = se.getHighestThreat();
		
		
	}
	
	public void moveRadar() {
		// track robots inside a certain slice
		// get min- and max-bearing
		double[] minmax = se.getMinMaxBearing();
		double minBearing = minmax[0];
		double maxBearing = minmax[1];
		double turner = robocode.util.Utils.normalRelativeAngleDegrees(minBearing + (getHeading() - getRadarHeading()));
		System.out.println(getHeading());
		System.out.println(getRadarHeading());
		System.out.println(turner);
		
//		turner = nonZero(turner);
//		turner += radarDirection * (MAX_RADAR_TRACKING_AMOUNT / 2);
		setTurnRadarLeft(turner);
		
	}
	
	private double calcBearing(double radarHeading, double enemyBearing) {
		double bearing = radarHeading + enemyBearing;
		if(bearing < 0 ) bearing += 360;
		return Math.toRadians(bearing);
	}
	
	private double nonZero(double value) {
		if (value < 0) {
			return value * -1;
		}
		return value * -1;
	}
}
