package at.fhooe.ai.mcm.g1;

import robocode.AdvancedRobot;
import robocode.ScannedRobotEvent;

public class FirstRobot extends AdvancedRobot {
	@Override
	public void run() {
		setAdjustGunForRobotTurn(true);
		setAdjustRadarForGunTurn(true);
		setTurnRadarRight(Double.POSITIVE_INFINITY);

		while(true) {
			ahead(100);
			turnLeft(90);
		}
		
	}

	
	@Override
	public void onScannedRobot(ScannedRobotEvent event) {
		EnemyRobot r1 = new EnemyRobot(event, getX(), getY());
		double time = 1; // 1 tick
		
		double distanceEnemyTurn = r1.getVelocity() * time;
		double XEnemyNew = r1.getScannedX() + Math.sin(r1.getAngle()) * distanceEnemyTurn;
		double YEnemyNew = r1.getScannedY() + Math.cos(r1.getAngle()) * distanceEnemyTurn;
		
		double distanceBullet = Math.sqrt(Math.pow(XEnemyNew - getX(), 2) + Math.pow(YEnemyNew - getY(), 2));
		
		double angleBarrelRotation = Math.acos((XEnemyNew - getX()) / distanceBullet);
		
		double power = (distanceBullet / time - 20) / (-3);
		
		turnGunLeft(angleBarrelRotation); //might change it to not always use left rotation
		fire(power);
	}
}
