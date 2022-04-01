package at.fhooe.ai.mcm.g1;

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
    private int scannedX;
    private int scannedY;
    private double angle;
	
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
	}
	
	public double getAngle() {
		return angle;
	}

	public void setAngle(double angle) {
		this.angle = angle;
	}

	public int getScannedX() {
		return scannedX;
	}

	public void setScannedX(int scannedX) {
		this.scannedX = scannedX;
	}

	public int getScannedY() {
		return scannedY;
	}

	public void setScannedY(int scannedY) {
		this.scannedY = scannedY;
	}

	public EnemyRobot(ScannedRobotEvent event, double X, double Y) {
		this.angle = Math.toRadians((getHeading() + event.getBearing()) % 360);
		this.bearing = event.getBearing();
		this.bearingRadians = event.getBearingRadians();
		this.distance = event.getDistance();
		this.energy = event.getEnergy();
		this.heading = event.getHeading();
		this.headingRadians = event.getHeadingRadians();
		this.name = event.getName();
		this.velocity = event.getVelocity();
		this.scannedX = (int)(X + Math.sin(angle) * event.getDistance());
		this.scannedY = (int)(Y + Math.cos(angle) * event.getDistance());

	}

}
