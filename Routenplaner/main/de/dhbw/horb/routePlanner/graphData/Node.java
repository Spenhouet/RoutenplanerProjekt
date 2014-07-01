package de.dhbw.horb.routePlanner.graphData;

/**
 * Klasse die einen Knoten beschreibt.
 * 
 * @author Sebastian
 */
public class Node {

	private String id;
	private Double latitude;
	private Double longitude;

	/**
	 * Konstruktor des Knoten.
	 * 
	 * @param name
	 *            Name des Knotens (Stadt, Kreuzung, Abfahrt oder Zufahrt).
	 * @param latitude
	 *            Breitengrad auf dem sich der Knoten befindet.
	 * @param longitude
	 *            Längengrad auf dem sich der Knoten befindet.
	 */
	public Node(String id, Double latitude, Double longitude) {
		setId(id);
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

	public String getId() {
		return id;
	}

	private void setId(String id) {
		this.id = id;
	}
}
