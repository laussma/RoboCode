package at.fhooe.ai.mcm.g1;

import java.util.Iterator;
import java.awt.Color;

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
		se.updateThreatList(new EnemyRobot(event, getX(), getY()));
		
		EnemyRobot highestThreat = se.getHighestThreat();
		evaluateShot(highestThreat);
		aim(highestThreat);
		
		
//		
//		EnemyRobot r1 = new EnemyRobot(event, getX(), getY());
//		double time = 1; // 1 tick
//		
//		double distanceEnemyTurn = r1.getVelocity() * time;
//		
//		double XEnemyNew = r1.getScannedX() + Math.sin(r1.getAngle()) * distanceEnemyTurn;
//		double YEnemyNew = r1.getScannedY() + Math.cos(r1.getAngle()) * distanceEnemyTurn;
//		
//		double distanceBullet = Math.sqrt(Math.pow(XEnemyNew - getX(), 2) + Math.pow(YEnemyNew - getY(), 2));
//		
//		double angleBarrelRotation = Math.acos((XEnemyNew - getX()) / distanceBullet);
//		angleBarrelRotation += getHeadingRadians() - getGunHeadingRadians(); 
//		
//		double power = (distanceBullet / time - 20) / (-3);
//		
//		//super.onScannedRobot(event);
//		//this.r.setEnemyRobot(event, getX(), getY()); 
//		//System.out.println(angleBarrelRotation);
//		
//		
//	
//		 //might change it to not always use left rotation
//		//fire(power);
//		//execute();
	}
	
	public void aim(EnemyRobot robot) {
		double absoluteBearing = getHeadingRadians() + robot.getBearingRadians();
				
		setTurnGunRightRadians(
		    robocode.util.Utils.normalRelativeAngle(absoluteBearing - 
		        getGunHeadingRadians()));
		fireTime = getTime() + 1;
	}
	
	public void evaluateShot(EnemyRobot robot) {
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
}
