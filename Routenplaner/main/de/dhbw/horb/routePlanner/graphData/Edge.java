package de.dhbw.horb.routePlanner.graphData;

public class Edge {

	private Double latitude;
	private Double longitude;

	public Edge(Double latitude, Double longitude) {
		setLatitude(latitude);
		setLongitude(longitude);
	}

	public Double getLatitude() {
		return latitude;
	}

	private void setLatitude(Double latitude) {
		this.latitude = latitude;
	}

	public Double getLongitude() {
		return longitude;
	}

	private void setLongitude(Double longitude) {
		this.longitude = longitude;
	}
}
