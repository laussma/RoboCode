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
			ahead(70);
			turnLeft(30);
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
		angleBarrelRotation += getHeadingRadians() - getGunHeadingRadians(); 
		
		double power = (distanceBullet / time - 20) / (-3);
		
		//super.onScannedRobot(event);
		//this.r.setEnemyRobot(event, getX(), getY());
		//System.out.println(angleBarrelRotation);
	
		turnGunLeftRadians(angleBarrelRotation); //might change it to not always use left rotation
		fire(power);
	}
	
	public void onPaint(java.awt.Graphics2D g) {
		g.setColor(new Color(0xff, 0x00, 0x00, 0x80));

	    // Draw a line from our robot to the scanned robot
	    g.drawLine(r.getScannedX(), r.getScannedY(), (int)getX(), (int)getY());

	    // Draw a filled square on top of the scanned robot that covers it
	    g.fillRect(r.getScannedX() - 20, r.getScannedY() - 20, 40, 40);
	};
}
