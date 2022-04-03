package at.fhooe.ai.mcm.g1;

import java.util.HashMap;

public class StrategyEvaluator {
	HashMap<String, EnemyRobot> threatList;
	public StrategyEvaluator() {
		threatList = new HashMap<>();
	}
	
	public void updateThreatList(EnemyRobot robot) {
		threatList.put(robot.getName(), robot);
	}
	
	public EnemyRobot getHighestThreat() {
		EnemyRobot highestThreat = null;
		for(String it: threatList.keySet()) {
			if(highestThreat == null) {
				highestThreat = threatList.get(it);
			}
			if(threatList.get(it).getThreatLevel() > highestThreat.getThreatLevel()) {
				highestThreat = threatList.get(it);
			}
		}
		return highestThreat;
	}
	
	public void removeThreat(String robotName) {
		threatList.remove(robotName);
	}
	
	public double[] getMinMaxBearing() {
		double minBearing = 180;
		double maxBearing = -180;
		for(EnemyRobot it: threatList.values()) {
			if(minBearing > it.getBearing()) {
				minBearing = it.getBearing();
			}
			if(maxBearing < it.getBearing()) {
				maxBearing = it.getBearing();
			}
		}
		return new double[] {minBearing, maxBearing};
	}
	
}
