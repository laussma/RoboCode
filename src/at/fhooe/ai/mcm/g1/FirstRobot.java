package at.fhooe.ai.mcm.g1;

import robocode.AdvancedRobot;

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

}
