package de.dhbw.horb.routePlanner.evaluation.dijkstra;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.xml.stream.XMLStreamException;

import de.dhbw.horb.routePlanner.Constants;
import de.dhbw.horb.routePlanner.SupportMethods;
import de.dhbw.horb.routePlanner.data.StAXMapGraphDataParser;

/**
 * Klasse Dijkstra
 * Berechnet kürzeste Route von Start zu  Zielknoten
 * @author Simon
 *
 */
public class Dijkstra {

    private String startnode;
    private String endnode;
    private String nearestNode;
    private Map<String, List<Map<String, String>>> routes;
    private Map<String, List<String>> nodeMap;
    private Map<String, Long> nodeDuration = new HashMap<String, Long>();

    private LinkedList<String> prioQue = new LinkedList<String>();
    private List<String> goneNodes = new ArrayList<String>();
    private List<String> cheapNeighbours = new ArrayList<String>();
    private Map<String, Map<String, String>> currentNeighbours = new HashMap<String, Map<String, String>>();
    private Paths allPaths;
    private Paths rightPaths;
    private boolean targetReached;
    private String calcMethod;
    String test;

    public Dijkstra(String startnode, String endnode) {

	try {
	    nodeMap = StAXMapGraphDataParser.getNodeXMLMap();
	    routes = StAXMapGraphDataParser.getRouteXMLMap();
	} catch (FileNotFoundException | XMLStreamException e) {
	    e.printStackTrace();
	}
	this.startnode = startnode;
	this.endnode = endnode;
	this.nearestNode = startnode;
	initializeNodeDuration();
	allPaths = new Paths(endnode);
	rightPaths = new Paths(endnode);
	allPaths.add(new Way(startnode, endnode));
    }

    public void calculateRoute(String calcMethod) {

	this.calcMethod = calcMethod;

	while (!targetReached) {
	    calcNewNodePrices(nearestNode);

	    initializeRoute();

	    sortPrioQue();

	    if (prioQue.getFirst().equals(endnode)) {
		targetReached = true;
		System.out.print("Ziel erreicht");
		pickRightWays();
		rightPaths.initializeCheapestWay();
	    }
	}
    }

    private void calcNewNodePrices(String initialNode) {

	initializeCurrentNeighbours(initialNode);

	Set<String> keys = currentNeighbours.keySet();

	for (String focusedNeighbour : keys) {

	    if (Long.valueOf(currentNeighbours.get(focusedNeighbour).get(Constants.EVALUATION_CALCULATION_DURATION))
		    + nodeDuration.get(initialNode) < nodeDuration.get(focusedNeighbour)
		    || nodeDuration.get(focusedNeighbour) == 0) {

		// Wenn es einen billigeren Weg zu einer bestimmten Kreuzung gibt sollen alle anderen Wege zu dieser Kreuzung gelöscht werden
		if (nodeDuration.get(focusedNeighbour) != 0) {
		    deleteWay(focusedNeighbour);
		}

		setNodeDuration(
			focusedNeighbour,
			Long.valueOf(currentNeighbours.get(focusedNeighbour).get(
				Constants.EVALUATION_CALCULATION_DURATION))
				+ nodeDuration.get(initialNode));

		if (!prioQue.contains(focusedNeighbour) && !focusedNeighbour.equals(startnode))
		    cheapNeighbours.add(focusedNeighbour);
	    }
	}
	if (!prioQue.isEmpty()) {
	    prioQue.removeFirst();
	}
    }

    private void initializeRoute() {

	int numberOfNewWays = 0;
	Long duration = (long) 0;
	List<String> goneNodes = null;
	// Lösche Wege die nur wieder zurückgehen könnten um ans Ziel zu kommen
	if (nearestNode != startnode && currentNeighbours.size() == 1 && nearestNode != endnode) {
	    deleteWay(nearestNode);
	    return;
	}

	for (int i = 0; i <= allPaths.size() - 1; i++) {
	    if (allPaths.get(i).getLastNode() == nearestNode) {
		duration = allPaths.get(i).getPrice();
		goneNodes = allPaths.get(i).getNodes();
		numberOfNewWays++;
		allPaths.remove(allPaths.get(i));
	    }
	}
	addNewWays(numberOfNewWays, goneNodes, duration);
	// Füge gegangenen Knoten zu goneNodes hinzu
	this.goneNodes.add(nearestNode);

	currentNeighbours.clear();
    }

    private void initializeCurrentNeighbours(String initialNode) {

	List<String> ids = nodeMap.get(initialNode);

	for (String id : ids) {

	    List<Map<String, String>> maps = routes.get(id);

	    if (maps != null) {
		for (Map<String, String> map : maps) {

		    String neighbour = map.get(Constants.NEW_ROUTE_DESTINATIONNODENAME);

		    if (currentNeighbours.isEmpty()
			    || Long.valueOf(currentNeighbours.get(neighbour).get(
				    Constants.EVALUATION_CALCULATION_DURATION)) == null
			    || Long.valueOf(currentNeighbours.get(neighbour).get(
				    Constants.EVALUATION_CALCULATION_DURATION)) > Long.valueOf(map
				    .get(Constants.NEW_ROUTE_DURATION))) {
			currentNeighbours.put(neighbour, map);

			Map<String, String> innereMap = currentNeighbours.get(neighbour);
			test = innereMap.get(Constants.EVALUATION_CALCULATION_DURATION);
		    }
		}
	    }
	}

    }

    private void sortPrioQue() {

	for (String cheapNeighbour : cheapNeighbours) {
	    prioQue.add(cheapNeighbour);
	}
	cheapNeighbours.clear();
	prioQueInsertionSort();
	if (!prioQue.isEmpty())
	    nearestNode = prioQue.getFirst();

    }

    private void prioQueInsertionSort() {

	for (int i = 0; i < prioQue.size(); i++) {
	    String temp = prioQue.get(i);
	    int j;
	    for (j = i - 1; j >= 0 && nodeDuration.get(temp) < nodeDuration.get(prioQue.get(j)); j--) {
		prioQue.set(j + 1, prioQue.get(j));
	    }
	    prioQue.set(j + 1, temp);
	}
    }

    private void addNewWays(int numberOfNewWays, List<String> goneNodes, Long duration) {

	Set<String> neighbours = currentNeighbours.keySet();

	for (String focusedNeighbour : neighbours) {

	    for (int i = 0; i < numberOfNewWays; i++) {
		// Gehe nicht zum vorherigen Punkt zurück
		if (!this.goneNodes.contains(focusedNeighbour)) {
		    // Erweitere alle Wege die für nearestNode in Frage kommen
		    allPaths.add(new Way(goneNodes, duration, focusedNeighbour, Long.valueOf(currentNeighbours.get(
			    focusedNeighbour).get(Constants.EVALUATION_CALCULATION_DURATION)), currentNeighbours
			    .get(focusedNeighbour)));
		}

	    }
	}
    }

    private void initializeNodeDuration() {

	Set<String> keys = nodeMap.keySet();

	for (String singleKey : keys) {

	    if (!SupportMethods.isNumeric(singleKey))
		nodeDuration.put(singleKey, (long) 0);
	}
    }

    public void printNodes() {
	Way way = rightPaths.getCheapestWay();
	for (String node : way.getNodes()) {
	    System.out.println(node);
	}
    }

    private void deleteWay(String initialNode) {
	for (int i = 0; i <= allPaths.size() - 1; i++) {
	    if (allPaths.get(i).getLastNode().equals(initialNode)) {
		allPaths.remove(allPaths.get(i));
	    }
	}
    }

    private void pickRightWays() {
	for (int i = 0; i <= allPaths.size() - 1; i++) {
	    if (allPaths.get(i).getLastNode().equals(endnode))
		rightPaths.add(allPaths.get(i));
	}
    }

    public void setNodeDuration(String name, Long duration) {
	nodeDuration.put(name, duration);
    }

    private boolean duration() {
	return calcMethod.equals(Constants.EVALUATION_CALCULATION_DURATION);
    }

    private boolean distance() {
	return calcMethod.equals(Constants.EVALUATION_CALCULATION_DISTANCE);
    }

}
