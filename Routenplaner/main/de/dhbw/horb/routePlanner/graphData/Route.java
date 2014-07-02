package de.dhbw.horb.routePlanner.graphData;

import java.util.LinkedList;
import java.util.List;

import de.dhbw.horb.routePlanner.SupportMethods;

public class Route {

	private String number = null;

	private String departureNodeID = null;
	private String destinationNodeID = null;

	private List<String> wayIDs;

	private Node lastNode = null;

	private Long durationInSeconds = 0L;
	private Double distance = 0.0;
	private Double newDistance = 0.0;

	public Route(Node departureNode) {
		wayIDs = new LinkedList<String>();
		this.departureNodeID = departureNode.getId();
		lastNode = departureNode;
	}

	public void addNode(Node nextNode) {
		departureNodeID = nextNode.getId();
		newDistance = SupportMethods.fromLatLonToDistanceInKM(lastNode, nextNode);
	}

	public void finalizeWay(String wayID, String nr, int speed) {

		if (wayID == null)
			return;

		if (number == null && nr != null)
			number = nr;

		wayIDs.add(wayID);

		durationInSeconds += SupportMethods.fromDistanceAndSpeedToSeconds(newDistance, speed);

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
		return durationInSeconds;
	}

	public Double getDistance() {
		return distance;
	}
}
