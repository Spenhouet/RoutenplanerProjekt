package de.dhbw.horb.routePlanner.graphData;

/**
 * Klasse die einen Knoten beschreibt.
 * 
 * @author Sebastian
 */
public class Node {

	private Long id;
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
	 *            Lšngengrad auf dem sich der Knoten befindet.
	 */
	public Node(Long id, Double latitude, Double longitude) {

		setID(id);
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

	public Long getID() {
		return id;
	}

	public void setID(Long id) {
		this.id = id;
	}

}
