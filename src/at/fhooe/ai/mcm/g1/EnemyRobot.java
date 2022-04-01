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
	}
	
	public EnemyRobot(ScannedRobotEvent event) {
		this.bearing = event.getBearing();
		this.bearingRadians = event.getBearingRadians();
		this.distance = event.getDistance();
		this.energy = event.getEnergy();
		this.heading = event.getHeading();
		this.headingRadians = event.getHeadingRadians();
		this.name = event.getName();
		this.velocity = event.getVelocity();
	}

}
