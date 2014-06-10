package de.dhbw.horb.routePlanner.graphData;
/**
 * Klasse die einen Knoten beschreibt.
 * @author Sebastian
 */
public class Node {

	private String name;
	private Double latitude;
	private Double longitude;
	
	/**
	 * Konstruktor des Knoten.
	 * @param name Name des Knotens (Stadt, Kreuzung, Abfahrt oder Zufahrt).
	 * @param latitude Breitengrad auf dem sich der Knoten befindet.
	 * @param longitude Längengrad auf dem sich der Knoten befindet.
	 */
	public Node(String name, Double latitude, Double longitude) {
		
		setName(name);
		setLatitude(latitude);
		setLongitude(longitude);
	}
	
	public Double getLatitude() {
		return latitude;
	}

	public void setLatitude(Double latitude) {
		this.latitude = latitude;
	}

	public Double getLongitude() {
		return longitude;
	}

	public void setLongitude(Double longitude) {
		this.longitude = longitude;
	}

	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}


	

	
}
