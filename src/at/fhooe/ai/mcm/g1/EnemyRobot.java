package at.fhooe.ai.mcm.g1;

import org.w3c.dom.events.EventTarget;

import robocode.AdvancedRobot;
import robocode.ScannedRobotEvent;

public class EnemyRobot{
	private double bearing;
	private double bearingRadians;
	private double distance;
	private double energy;
	private double heading;
	private double headingRadians;
	private String name;
	private double velocity;
    private double scannedX;
    private double scannedY;
    private double angle;
    private int threatLevel;
    private double damageGiven;
    
    public EnemyRobot() {
		this.bearing = 0.0;
		this.bearingRadians = 0.0;
		this.distance = 0.0;
		this.energy = 0.0;
		this.heading = 0.0;
		this.headingRadians = 0.0;
		this.name = "";
		this.velocity = 0.0;
		this.scannedX = 0;
		this.scannedY = 0;
		this.angle = 0.0;
		this.threatLevel = 0;
		this.damageGiven = 0;
	}
    
    public EnemyRobot(ScannedRobotEvent event, AdvancedRobot robot) {
		setEnemyRobot(event, robot);
	}
	
	public double getBearing() {
		return bearing;
	}

	public void setBearing(double bearing) {
		this.bearing = bearing;
	}

	public double getBearingRadians() {
		return bearingRadians;
	}

	public void setBearingRadians(double bearingRadians) {
		this.bearingRadians = bearingRadians;
	}

	public double getDistance() {
		return distance;
	}

	public double getVelocity() {
		return velocity;
	}

	public void setVelocity(double velocity) {
		this.velocity = velocity;
	}

	public void setDistance(double distance) {
		this.distance = distance;
	}

	public double getEnergy() {
		return energy;
	}

	public void setEnergy(double energy) {
		this.energy = energy;
	}

	public double getHeading() {
		return heading;
	}

	public void setHeading(double heading) {
		this.heading = heading;
	}

	public double getHeadingRadians() {
		return headingRadians;
	}

	public void setHeadingRadians(double headingRadians) {
		this.headingRadians = headingRadians;
	}

	public String getName() {
		return name;
	}

	@Override
	public String toString() {
		return "EnemyRobot [bearing=" + bearing + ", bearingRadians=" + bearingRadians + ", distance=" + distance
				+ ", energy=" + energy + ", heading=" + heading + ", headingRadians=" + headingRadians + ", name="
				+ name + ", velocity=" + velocity + ", scannedX=" + scannedX + ", scannedY=" + scannedY + ", angle="
				+ angle + "]";
	}

	public void setName(String name) {
		this.name = name;
	}

	
	
	public double getAngle() {
		return angle;
	}
	
	public int getThreatLevel() {
		return threatLevel;
	}

	public void setAngle(double angle) {
		this.angle = angle;
	}

	public double getScannedX() {
		return scannedX;
	}

	public void setScannedX(int scannedX) {
		this.scannedX = scannedX;
	}

	public double getScannedY() {
		return scannedY;
	}

	public void setScannedY(int scannedY) {
		this.scannedY = scannedY;
	}
	
	public void setThreatLevel(int threatLevel) {
		this.threatLevel = threatLevel;
	}
	
	public double getDamageGiven() {
		return this.damageGiven;
	}
	
	public void setDamageGiven(double damageGiven) {
		this.damageGiven = damageGiven;
	}
	
	public void addDamageGiven(double newDamageGiven) {
		this.damageGiven += newDamageGiven;
	}
	
	public void setEnemyRobot(ScannedRobotEvent event, AdvancedRobot robot) {
		this.heading = event.getHeading();
		this.bearing = event.getBearing();
		this.bearingRadians = event.getBearingRadians();
		this.distance = event.getDistance();
		this.energy = event.getEnergy();
		this.headingRadians = event.getHeadingRadians();
		this.name = event.getName();
		this.velocity = event.getVelocity();
		this.angle = calcBearing(robot.getHeading(), event.getBearing());
		this.scannedX = robot.getX() + Math.sin(angle) * event.getDistance();
		this.scannedY = robot.getY() + Math.cos(angle) * event.getDistance();
		this.threatLevel = evaluateThreatLevel(event);
		
	}
	
	private int evaluateThreatLevel(ScannedRobotEvent event) {
		//return 1 - (int)event.getDistance(); //old evaluation using distance
		return (int) this.getDamageGiven(); // new evaluation using damage given
	}
	
	public double getFutureX(double x, long time) {
		return x + Math.sin(Math.toRadians(getHeading())) * getVelocity() * time;
	}
	
	public double getFutureY(double y, long time) {
		return y + Math.cos(Math.toRadians(getHeading())) * getVelocity() * time;
	}
	
	private double calcBearing(double myHeading, double enemyBearing) {
		double bearing = myHeading + enemyBearing;
		if (bearing < 0) bearing += 360;
		return Math.toRadians(bearing);
	}
	
}
