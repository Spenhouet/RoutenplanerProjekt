package de.dhbw.horb.routePlanner.graphData;

public class Node {

	private String name;
	private float latitude;
	private float longitude;
	
	
	public Node(String name, float latitude, float longitude) {
		
		setName(name);
		setLatitude(latitude);
		setLongitude(longitude);
	}
	
	public float getLatitude() {
		return latitude;
	}

	public void setLatitude(float latitude) {
		this.latitude = latitude;
	}

	public float getLongitude() {
		return longitude;
	}

	public void setLongitude(float longitude) {
		this.longitude = longitude;
	}

	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}


	

	
}
