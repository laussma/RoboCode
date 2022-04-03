package at.fhooe.ai.mcm.g1;

import java.util.Iterator;

import robocode.AdvancedRobot;
import robocode.ScannedRobotEvent;

public class FirstRobot extends AdvancedRobot {
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

//		while(true) {
//			ahead(100);
//			turnLeft(90);
//		}
		
	}

	
	@Override
	public void onScannedRobot(ScannedRobotEvent event) {
		EnemyRobot r1 = new EnemyRobot(event);
		
	}
	
	public double[] calcDistances(double BFHeight, double BFWidth, double X, double Y) {
		// distances in order: Top, Right, Bottom, Left
		double distances[] = {BFHeight - Y, BFWidth- X, Y, X};
		return distances;
	}
}
