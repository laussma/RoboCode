package at.fhooe.ai.mcm.g1;

import java.util.Iterator;

import javax.security.auth.x500.X500Principal;

import java.awt.Color;
import java.awt.geom.Point2D;

import robocode.AdvancedRobot;
import robocode.DeathEvent;
import robocode.RobotDeathEvent;
import robocode.ScannedRobotEvent;

public class FirstRobot extends AdvancedRobot {
	
	StrategyEvaluator se = new StrategyEvaluator();
	long fireTime = 0;
	
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
		setTurnRadarRight(Double.POSITIVE_INFINITY);
		
		/*if(getGunTurnRemaining() == 0) {
			fire(1);
		}*/

		while(true) {
			ahead(70);
			turnLeft(30);
			//fire
		}
	}

	
	@Override
	public void onScannedRobot(ScannedRobotEvent event) {
		se.updateThreatList(new EnemyRobot(event, this));
		
		EnemyRobot highestThreat = se.getHighestThreat();
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
	
}
