package de.dhbw.horb.routePlanner.graphData;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import de.dhbw.horb.routePlanner.Constants;
import de.dhbw.horb.routePlanner.SupportMethods;

public class Route {

	private String number = null;

	private String departureNodeID = null;
	private String destinationNodeID = null;

	private List<String> wayIDs;

	private Map<String, Object> lastNode = null;

	private Long durationInMilliseconds = 0L;
	private Double distance = 0.0;
	private Double newDistance = 0.0;

	public Route(String id, Double lat, Double lon) {
		lastNode = new HashMap<String, Object>();
		wayIDs = new LinkedList<String>();
		this.departureNodeID = id;
		lastNode.put(Constants.NODE_ID, id);
		lastNode.put(Constants.NODE_LATITUDE, lat);
		lastNode.put(Constants.NODE_LONGITUDE, lon);
	}

	public void addNode(String id, Double lat, Double lon) {
		destinationNodeID = id;
		newDistance = SupportMethods
				.fromLatLonToDistanceInKM((Double)lastNode.get(Constants.NODE_LATITUDE), (Double)lastNode.get(Constants.NODE_LONGITUDE), lat, lon);
		if (!((String)lastNode.get(Constants.NODE_ID)).equals(id)){
			lastNode = new HashMap<String, Object>();
			lastNode.put(Constants.NODE_ID, id);
			lastNode.put(Constants.NODE_LATITUDE, lat);
			lastNode.put(Constants.NODE_LONGITUDE, lon);
		}
	}

	public void finalizeWay(String wayID, String nr, int speed) {

		if (wayID == null)
			return;

		number = nr;

		wayIDs.add(wayID);

		durationInMilliseconds += SupportMethods.fromDistanceAndSpeedToMilliseconds(newDistance, speed);

		distance += newDistance;
		newDistance = 0.0;
	}

	public String getWayIDsAsCommaString() {
		return SupportMethods.strListToCommaStr(wayIDs);
	}

	public String getNumber() {
		return number;
	}

	public String getDepartureNodeID() {
		return departureNodeID;
	}

	public String getDestinationNodeID() {
		return destinationNodeID;
	}

	public Long getDurationInSeconds() {
		return SupportMethods.millisecondsToSeconds(durationInMilliseconds.doubleValue()).longValue();
	}

	public Double getDistance() {
		return distance;
	}
}
