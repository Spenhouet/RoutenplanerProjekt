package de.dhbw.horb.routePlanner.graphData;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import de.dhbw.horb.routePlanner.Constants;
import de.dhbw.horb.routePlanner.SupportMethods;
import de.dhbw.horb.routePlanner.parser.DomMapNodeParser;
import de.dhbw.horb.routePlanner.parser.DomMapWayParser;

public class Route {

	private String number = null;

	private String departureNodeID = null;
	private String destinationNodeID = null;

	private List<String> wayIDs;

	private Map<String, String> lastNode = null;

	private Long durationInMilliseconds = 0L;
	private Double distance = 0.0;

	private DomMapNodeParser nodeMapDom;
	private DomMapWayParser wayMapDom;
	
	Boolean firstRun = false;

	public Route(String nodeID, DomMapNodeParser nodeMapDom, DomMapWayParser wayMapDom) {
		this.nodeMapDom = nodeMapDom;
		this.wayMapDom = wayMapDom;
		
		this.wayIDs = new LinkedList<String>();
		
		this.departureNodeID = nodeID;
		this.lastNode = nodeMapDom.getNode(nodeID);
	}

	public void addNode(String nodeID, String wayID) {
		if (destinationNodeID == null) {
			destinationNodeID = nodeID;
		}
		
		if(!wayIDs.contains(wayID))
			wayIDs.add(wayID);
		
		Map<String, String> node = nodeMapDom.getNode(nodeID);
		Double lat1 = Double.valueOf(lastNode.get(Constants.NODE_LATITUDE));
		Double lon1 = Double.valueOf(lastNode.get(Constants.NODE_LONGITUDE));
		Double lat2 = Double.valueOf(node.get(Constants.NODE_LATITUDE));
		Double lon2 = Double.valueOf(node.get(Constants.NODE_LONGITUDE));
		
		Double newDistance = null;
		
		if(lat1 != null && lon1 != null && lat2 != null && lon2 != null)
			newDistance = SupportMethods.fromLatLonToDistanceInKM(lat1, lon1, lat2, lon2);
		
		Map<String, String> way = wayMapDom.getWay(wayID);
		if(way != null && newDistance != null){
			
			String maxspeed = way.get(Constants.WAY_MAXSPEED);
			if (maxspeed == null || maxspeed.isEmpty() || !SupportMethods.isNumeric(maxspeed)) {
				String highway = way.get(Constants.WAY_HIGHWAY);
				if (highway != null && highway.equals(Constants.WAY_MOTORWAY_LINK)) {
					maxspeed = "60";
				} else if (highway != null && highway.equals(Constants.WAY_MOTORWAY)) {
					maxspeed = "120";
				} else {
					maxspeed = "120";
				}
			}
			
			durationInMilliseconds += (SupportMethods.fromDistanceAndSpeedToMilliseconds(newDistance, Integer.valueOf(maxspeed)));
			this.distance += newDistance;
			
			
			String nr = way.get(Constants.WAY_REF);
			if((number == null || number.isEmpty()) && nr != null && !nr.isEmpty())
				this.number = nr;
		}
		
		lastNode = node;
		
	}
	
	public void firstRun(){
		this.firstRun = true;
	}
	
	public Boolean hadRun(){
		return firstRun;
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
