package de.dhbw.horb.routePlanner.evaluation.aStar;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import de.dhbw.horb.routePlanner.Constants;
import de.dhbw.horb.routePlanner.SupportMethods;
import de.dhbw.horb.routePlanner.data.DomStAXMapRouteParser;
import de.dhbw.horb.routePlanner.data.StAXNodeParser;
import de.dhbw.horb.routePlanner.ui.UIEvaluationInterface;

public class AStar {

    public static void main(String[] args) {
	String departure = "Bispingen";
	String destination = "Kreuz Kamp-Lintfort";
	AStar a = new AStar(departure, destination);
	a.calculateWay(Constants.NEW_ROUTE_DISTANCE);
    }

    private DomStAXMapRouteParser routeParser;
    private Map<String, List<String>> nodeMap;

    private List<Map<String, String>> openEdge;
    private List<Map<String, String>> closedEdge;
    private String departure;
    private String destination;

    private String calculateMethod;

    public AStar(String departure, String destination) {
	openEdge = new LinkedList<Map<String, String>>();
	closedEdge = new LinkedList<Map<String, String>>();
	routeParser = new DomStAXMapRouteParser();
	nodeMap = StAXNodeParser.getNodeMap();
	this.departure = departure;
	this.destination = destination;
    }

    public void calculateWay(String calculateMethod) {
	this.calculateMethod = calculateMethod;
	if (calculateMethod == null
		|| (!calculateMethod.equals(Constants.NEW_ROUTE_DISTANCE) && !calculateMethod
			.equals(Constants.NEW_ROUTE_DURATION))) {
	    System.err.println("Unknown calculating method!");
	    return;
	}

	openEdge.add(createEdge(departure));
	findDestination();
	UIEvaluationInterface.printRoute(getWays(this.destination));
    }

    private List<Map<String, String>> getWays(String destination) {
	List<Map<String, String>> nodes = new LinkedList<Map<String, String>>();
	Map<String, String> mp = getEdge(closedEdge, destination);

	if (mp != null) {
	    String newDestination = mp.get(Constants.NEW_ROUTE_DEPARTURENODENAME);

	    if (newDestination != null) {

		if (!newDestination.equals(departure)) {
		    nodes = getWays(newDestination);
		    nodes.add(mp);
		} else {
		    nodes.add(mp);
		    return nodes;
		}
	    }
	}

	if (nodes != null && !nodes.isEmpty())
	    return nodes;
	return null;
    }

    private Double getWeightBack(String departure, String destination) {
	Double w = null;
	Map<String, String> mp = getEdge(closedEdge, destination);

	if (mp != null) {
	    String newDestination = mp.get(Constants.NEW_ROUTE_DEPARTURENODENAME);
	    String newWeight = mp.get(Constants.NEW_ROUTE_WEIGHT);

	    if (newDestination != null && newWeight != null && SupportMethods.isNumeric(newWeight)) {

		if (!newDestination.equals(departure))
		    w = getWeightBack(departure, newDestination) + Double.valueOf(newWeight);
		else
		    return Double.valueOf(newWeight);
	    }
	}
	return w;
    }

    private void findDestination() {
	Map<String, String> mp = getSmallestEdge();
	openEdge.remove(mp);
	closedEdge.add(mp);
	addNeighbourToOpenList(mp.get(Constants.NEW_ROUTE_DESTINATIONNODENAME));
	if (!openEdge.isEmpty())
	    findDestination();
    }

    private void addNeighbourToOpenList(String nameORid) {
	List<String> ids = nodeMap.get(nameORid);

	for (String id : ids) {

	    Map<String, String> edge = createEdge(id);

	    if (edge == null)
		continue;

	    if (containsName(closedEdge, edge.get(Constants.NEW_ROUTE_DESTINATIONNODENAME)))
		continue;

	    if (containsName(openEdge, edge.get(Constants.NEW_ROUTE_DESTINATIONNODENAME))) {
		Double newWeight = null;
		Double oldWeight = null;

		if (edge.get(Constants.NEW_ROUTE_WEIGHT) != null
			&& SupportMethods.isNumeric(edge.get(Constants.NEW_ROUTE_WEIGHT)))
		    newWeight = Double.valueOf(edge.get(Constants.NEW_ROUTE_WEIGHT));

		Map<String, String> mp = getEdge(openEdge, edge.get(Constants.NEW_ROUTE_DESTINATIONNODENAME));

		if (mp.get(Constants.NEW_ROUTE_WEIGHT) != null
			&& SupportMethods.isNumeric(mp.get(Constants.NEW_ROUTE_WEIGHT)))
		    oldWeight = Double.valueOf(mp.get(Constants.NEW_ROUTE_WEIGHT));

		if (newWeight != null && mp != null && oldWeight != null) {
		    Double wb = getWeightBack(mp.get(Constants.NEW_ROUTE_DEPARTURENODENAME),
			    edge.get(Constants.NEW_ROUTE_DESTINATIONNODENAME));

		    if (wb == null)
			continue;

		    newWeight += wb;
		    if (newWeight >= oldWeight)
			continue;
		    openEdge.remove(mp);
		}
	    }
	    openEdge.add(edge);
	}
    }

    private Map<String, String> getSmallestEdge() {
	Double weight = null;

	Map<String, String> rMp = null;
	for (Map<String, String> mp : openEdge) {
	    if (SupportMethods.isNumeric(mp.get(Constants.NEW_ROUTE_WEIGHT))) {
		Double nw = Double.valueOf(mp.get(Constants.NEW_ROUTE_WEIGHT));
		if (weight == null || nw < weight) {
		    rMp = mp;
		    weight = nw;
		}
	    }
	}
	return rMp;
    }

    private Boolean containsName(List<Map<String, String>> edge, String name) {
	return getEdge(edge, name) != null;
    }

    private Map<String, String> getEdge(List<Map<String, String>> edge, String name) {
	for (int i = (edge.size() - 1); i >= 0; i--) {
	    if (edge.get(i).get(Constants.NEW_ROUTE_DESTINATIONNODENAME).equals(name))
		return edge.get(i);
	}
	return null;
    }

    private Map<String, String> createEdge(String id) {

	if (id != null && !id.isEmpty() && !SupportMethods.isNumeric(id)) {
	    Map<String, String> sr = new HashMap<String, String>();
	    sr.put(Constants.NEW_ROUTE_DEPARTURENODENAME, id);
	    sr.put(Constants.NEW_ROUTE_DESTINATIONNODENAME, id);
	    sr.put(Constants.NEW_ROUTE_WEIGHT, "0");

	    return sr;
	}
	Map<String, String> rm = routeParser.getRoute(id);
	if (rm == null)
	    return null;
	rm.put(Constants.NEW_ROUTE_DEPARTURENODENAME, nodeMap.get(id).get(0));
	rm.put(Constants.NEW_ROUTE_DESTINATIONNODENAME,
		nodeMap.get(rm.get(Constants.NEW_ROUTE_DESTINATIONNODEID)).get(0));

	switch (calculateMethod) {
	case Constants.NEW_ROUTE_DISTANCE:
	    rm.put(Constants.NEW_ROUTE_WEIGHT, rm.get(Constants.NEW_ROUTE_DISTANCE));
	    break;
	case Constants.NEW_ROUTE_DURATION:
	    rm.put(Constants.NEW_ROUTE_WEIGHT, rm.get(Constants.NEW_ROUTE_DURATION));
	    break;
	default:
	    System.err.println("Unknown calculating method!");
	}

	return rm;
    }
}
