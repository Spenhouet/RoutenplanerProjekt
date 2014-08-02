package de.dhbw.horb.routePlanner.evaluation.aStar;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import de.dhbw.horb.routePlanner.SupportMethods;
import de.dhbw.horb.routePlanner.data.DomStAXMapRouteParser;
import de.dhbw.horb.routePlanner.data.StAXNodeParser;

public class AStar {

    public static void main(String[] args) {
	String departure = "Kreuz Hamburg-Ost";
	String destination = "Kreuz Duisburg";
	AStar a = new AStar(departure, destination);
	a.calculateWay();
    }

    private DomStAXMapRouteParser routeParser;
    private List<Map<String, String>> openEdge;

    private List<Map<String, String>> closedEdge;
    private String departure;
    private String destination;

    public AStar(String departure, String destination) {
	openEdge = new LinkedList<Map<String, String>>();
	closedEdge = new LinkedList<Map<String, String>>();
	routeParser = new DomStAXMapRouteParser();
	this.departure = departure;
	this.destination = destination;
    }

    public void calculateWay() {
	openEdge.add(createEdge(departure, departure, "0"));
	findDestination();
	System.out.println("Strecke: " + getWay(this.destination));
	System.out.println("Dauer: " + getWeightBack(this.departure, this.destination));
    }

    private List<String> getWay(String destination) {
	List<String> nodes = new LinkedList<String>();
	Map<String, String> mp = getEdge(closedEdge, destination);

	if (mp != null) {
	    String newDestination = mp.get("A");

	    if (newDestination != null) {

		if (!newDestination.equals(departure)) {
		    nodes = getWay(newDestination);
		    nodes.add(destination);
		} else {
		    nodes.add(newDestination);
		    nodes.add(destination);
		    return nodes;
		}
	    }
	}

	if (nodes != null && !nodes.isEmpty())
	    return nodes;
	return null;
    }

    private Double getWeightBack(String departure, String destination) {
	Double w = 0.0;
	Map<String, String> mp = getEdge(closedEdge, destination);

	if (mp != null) {
	    String newDestination = mp.get("A");
	    String newWeight = mp.get("G");

	    if (newDestination != null && newWeight != null && SupportMethods.isNumeric(newWeight)) {

		if (!newDestination.equals(departure))
		    w = getWeightBack(departure, newDestination) + Double.valueOf(newWeight);
		else
		    return Double.valueOf(newWeight);
	    }
	}

	if (w != 0L)
	    return w;
	return null;
    }

    private void addNighboarToList(String nameORid) {
	List<String> ids = StAXNodeParser.getStAXNodeParser().getIDsForName(nameORid);

	for (String id : ids) {

	    Map<String, String> rm = routeParser.getRoute(id);
	    if (rm == null)
		continue;

	    Map<String, String> edge = createEdge(StAXNodeParser.getStAXNodeParser().getNameForID(id), StAXNodeParser
		    .getStAXNodeParser().getNameForID(rm.get("destinationNodeID")), rm.get("distance"));

	    //	    if (destination.equals(edge.get("B")))

	    if (containsName(closedEdge, edge.get("B")))
		continue;

	    if (containsName(openEdge, edge.get("B"))) {
		Double newWeight = null;
		Double oldWeight = null;

		if (edge.get("G") != null && SupportMethods.isNumeric(edge.get("G")))
		    newWeight = Double.valueOf(edge.get("G"));

		Map<String, String> mp = getEdge(openEdge, edge.get("B"));

		if (mp.get("G") != null && SupportMethods.isNumeric(mp.get("G")))
		    oldWeight = Double.valueOf(mp.get("G"));

		if (newWeight != null && mp != null && oldWeight != null) {
		    Double wb = getWeightBack(mp.get("A"), edge.get("B"));

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

    private void findDestination() {
	Map<String, String> mp = getSmallestEdge();
	openEdge.remove(mp);
	closedEdge.add(mp);
	addNighboarToList(mp.get("B"));
	if (!openEdge.isEmpty())
	    findDestination();
    }

    private Map<String, String> getSmallestEdge() {
	Double weight = null;

	Map<String, String> rMp = null;
	for (Map<String, String> mp : openEdge) {
	    if (SupportMethods.isNumeric(mp.get("G"))) {
		Double nw = Double.valueOf(mp.get("G"));
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
	    if (edge.get(i).get("B").equals(name))
		return edge.get(i);
	}
	return null;
    }

    private Map<String, String> createEdge(String departure, String destination, String duration) {
	Map<String, String> edge = new HashMap<String, String>();
	edge.put("A", departure);
	edge.put("B", destination);
	edge.put("G", duration);
	return edge;
    }
}
