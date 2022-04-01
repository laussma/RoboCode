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
		EnemyRobot r1 = new EnemyRobot(event);
		
	}
}
