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

    private enum ListType {
	open, closed
    }

    public static void main(String[] args) {
	String departure = "Bispingen";
	String destination = "Kreuz Kamp-Lintfort";

	AStar a = new AStar(departure, destination);
	a.calculateWay(Constants.NEW_ROUTE_DISTANCE);
    }

    private DomStAXMapRouteParser routeParser;
    private Map<String, List<String>> nodeMap;

    private Map<String, String> openEdgesPredecessor;
    private Map<String, Map<String, String>> openEdgesRoute;
    private Map<String, Double> openEdgesWeight;
    private Map<String, String> closedEdgesPredecessor;
    private Map<String, Map<String, String>> closedEdgesRoute;
    private Map<String, Double> closedEdgesWeight;
    private String departure;
    private String destination;
    private String openEdgesSmallestFirst;
    private String openEdgesSmallestSecond;
    private String calculateMethod;

    public AStar(String departure, String destination) {
	openEdgesPredecessor = new HashMap<String, String>();
	openEdgesRoute = new HashMap<String, Map<String, String>>();
	openEdgesWeight = new HashMap<String, Double>();
	closedEdgesPredecessor = new HashMap<String, String>();
	closedEdgesRoute = new HashMap<String, Map<String, String>>();
	closedEdgesWeight = new HashMap<String, Double>();
	routeParser = new DomStAXMapRouteParser();
	nodeMap = StAXNodeParser.getNodeMap();
	this.departure = departure;
	this.destination = destination;
	openEdgesSmallestFirst = null;
	openEdgesSmallestSecond = null;
    }

    public void calculateWay(String calculateMethod) {
	this.calculateMethod = calculateMethod;
	addNeighbourToOpenList(departure);
	findDestination();
	UIEvaluationInterface.printRoute(getWays(this.destination));
    }

    private List<Map<String, String>> getWays(String destination) {
	List<Map<String, String>> nodes = new LinkedList<Map<String, String>>();
	Map<String, String> mp = closedEdgesRoute.get(destination);

	if (mp != null) {
	    String newDestination = closedEdgesPredecessor.get(destination);

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
	String newDestination = closedEdgesPredecessor.get(destination);
	Double newWeight = closedEdgesWeight.get(destination);

	if (newDestination != null && newWeight != null) {

	    if (!newDestination.equals(departure))
		w = getWeightBack(departure, newDestination) + newWeight;
	    else
		return newWeight;
	}
	return w;
    }

    private void findDestination() {
	if (openEdgesSmallestFirst != null) {
	    Map<String, String> mp = openEdgesRoute.get(openEdgesSmallestFirst);

	    removeEdge(ListType.open, mp);
	    addEdge(ListType.closed, mp);
	    addNeighbourToOpenList(mp.get(Constants.NEW_ROUTE_DESTINATIONNODENAME));
	} else {
	    System.err.println("Fehler");
	}

	if (!openEdgesRoute.isEmpty())
	    findDestination();
    }

    private void addNeighbourToOpenList(String name) {
	List<String> ids = nodeMap.get(name);
	for (String id : ids) {

	    Map<String, String> edge = routeParser.getRoute(id);
	    if (edge == null)
		continue;

	    String destinationNodeName = nodeMap.get(edge.get(Constants.NEW_ROUTE_DESTINATIONNODEID)).get(0);

	    edge.put(Constants.NEW_ROUTE_DEPARTURENODENAME, name);
	    edge.put(Constants.NEW_ROUTE_DESTINATIONNODENAME, destinationNodeName);

	    if (closedEdgesPredecessor.containsKey(destinationNodeName))
		continue;

	    if (openEdgesPredecessor.containsKey(destinationNodeName)) {
		Double newWeight = getWeight(edge);
		Double oldWeight = openEdgesWeight.get(destinationNodeName);

		if (newWeight != null && oldWeight != null) {
		    Double wb = getWeightBack(openEdgesPredecessor.get(destinationNodeName), destinationNodeName);

		    if (wb == null)
			continue;

		    newWeight += wb;
		    if (newWeight > oldWeight)
			continue;
		    removeEdge(ListType.open, openEdgesRoute.get(destinationNodeName));
		}
	    }
	    addEdge(ListType.open, edge);
	}
    }

    private Double getWeight(Map<String, String> edge) {
	String w;
	switch (calculateMethod) {
	case Constants.NEW_ROUTE_DISTANCE:
	    w = edge.get(Constants.NEW_ROUTE_DISTANCE);
	    break;
	case Constants.NEW_ROUTE_DURATION:
	    w = edge.get(Constants.NEW_ROUTE_DURATION);
	    break;
	default:
	    w = edge.get(Constants.NEW_ROUTE_DURATION);
	}
	if (!SupportMethods.isNumeric(w))
	    return null;
	return Double.valueOf(w);
    }

    private void addEdge(ListType t, Map<String, String> edge) {
	if (edge == null || edge.isEmpty())
	    return;
	String destinationName = edge.get(Constants.NEW_ROUTE_DESTINATIONNODENAME);

	Double weight = getWeight(edge);

	if (weight == null || destinationName == null)
	    return;

	switch (t) {
	case open:
	    openEdgesRoute.put(destinationName, edge);
	    openEdgesPredecessor.put(destinationName, edge.get(Constants.NEW_ROUTE_DEPARTURENODENAME));
	    openEdgesWeight.put(destinationName, weight);

	    Double oldSmallest = openEdgesWeight.get(openEdgesSmallestFirst);
	    if ((oldSmallest == null || weight <= oldSmallest) && !destinationName.equals(openEdgesSmallestFirst)) {
		openEdgesSmallestSecond = openEdgesSmallestFirst;
		openEdgesSmallestFirst = destinationName;
	    }
	    break;

	case closed:
	    closedEdgesRoute.put(destinationName, edge);
	    closedEdgesPredecessor.put(destinationName, edge.get(Constants.NEW_ROUTE_DEPARTURENODENAME));
	    closedEdgesWeight.put(destinationName, weight);
	    break;

	default:
	    break;
	}
    }

    private String getSmallest() {
	String key = null;
	Double value = null;

	for (Map.Entry<String, Double> entry : openEdgesWeight.entrySet()) {
	    if (value == null || entry.getValue() <= value) {
		key = entry.getKey();
		value = entry.getValue();
	    }
	}
	return key;
    }

    private Map<String, String> removeEdge(ListType t, Map<String, String> edge) {
	if (edge == null || edge.isEmpty())
	    return null;
	String destinationName = edge.get(Constants.NEW_ROUTE_DESTINATIONNODENAME);

	switch (t) {
	case open:
	    if (openEdgesRoute == null || openEdgesRoute.isEmpty() || !openEdgesRoute.containsKey(destinationName))
		return null;

	    openEdgesPredecessor.remove(destinationName);
	    openEdgesWeight.remove(destinationName);

	    if (destinationName.equals(openEdgesSmallestFirst)) {
		if (openEdgesSmallestSecond == null)
		    openEdgesSmallestFirst = getSmallest();
		else {
		    openEdgesSmallestFirst = openEdgesSmallestSecond;
		    openEdgesSmallestSecond = null;
		}
	    }

	    return openEdgesRoute.remove(destinationName);

	case closed:
	    if (closedEdgesRoute == null || closedEdgesRoute.isEmpty()
		    || !closedEdgesRoute.containsKey(destinationName))
		return null;

	    closedEdgesPredecessor.remove(destinationName);
	    closedEdgesWeight.remove(destinationName);
	    return closedEdgesRoute.remove(destinationName);

	default:
	    return null;
	}
    }
}
