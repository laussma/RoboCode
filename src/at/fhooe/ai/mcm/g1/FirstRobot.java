package at.fhooe.ai.mcm.g1;

import java.awt.Color;

import robocode.AdvancedRobot;
import robocode.ScannedRobotEvent;

public class FirstRobot extends AdvancedRobot {
	EnemyRobot r = new EnemyRobot();
	
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
		// TODO Auto-generated method stub
		super.onScannedRobot(event);
		this.r.setEnemyRobot(event, getX(), getY());
		
	}
	
	public void onPaint(java.awt.Graphics2D g) {
		g.setColor(new Color(0xff, 0x00, 0x00, 0x80));

	    // Draw a line from our robot to the scanned robot
	    g.drawLine(r.getScannedX(), r.getScannedY(), (int)getX(), (int)getY());

	    // Draw a filled square on top of the scanned robot that covers it
	    g.fillRect(r.getScannedX() - 20, r.getScannedY() - 20, 40, 40);
	};
}
