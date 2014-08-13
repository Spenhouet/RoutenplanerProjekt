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
 * Berechnet kürzeste bzw schnellste Route von Start zu  Zielknoten
 * @author Simon
 *
 */
public class Dijkstra {

    private String startnode;
    private String endnode;
    private String nearestNode;
    private Map<String, List<Map<String, String>>> routes;
    private Map<String, List<String>> nodeMap;
    private Map<String, Double> nodeDuration = new HashMap<String, Double>();

    private LinkedList<String> prioQue = new LinkedList<String>();
    private List<String> goneNodes = new ArrayList<String>();
    private List<String> cheapNeighbours = new ArrayList<String>();
    private Map<String, Map<String, String>> currentNeighbours = new HashMap<String, Map<String, String>>();
    private Paths allPaths;
    private Paths rightPaths;
    private boolean targetReached;
    private String calcMethod;

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
	initializeNodePrice();
	allPaths = new Paths();
	rightPaths = new Paths();
	allPaths.add(new Way(startnode, endnode));
    }

    /**
     * Berechnet neue Nachbarpreise, initialisiert Wege und sortiert Abarbeitungsschlange der offenen Knoten
     * @param calcMethod Algorithmus berechnet nach Strecke oder Dauer
     * @return Liste mit einzelnen Wegen in Form von Maps
     */
    public List<Map<String, String>> calculateRoute(String calcMethod) {

	this.calcMethod = calcMethod;

	while (!targetReached) {
	    calcNewNodePrices(nearestNode);

	    initializeRoute();

	    sortPrioQue();

	    if (prioQue.getFirst().equals(endnode)) {
		targetReached = true;
		pickRightWays();
		if (!rightPaths.isEmpty())
		    rightPaths.initializeCheapestWay();
		else if (rightPaths.isEmpty())
		    return null;

		return rightPaths.getCheapestWay().getEdges();
	    }
	}
	return null;
    }

    /**
     * Berechnet Preise (Dauer oder Strecke) zu einzelnen Nachbarn
     * @param initialNode Ausgangsknoten von dem Nachbarn in Betracht gezogen werden
     */
    private void calcNewNodePrices(String initialNode) {

	initializeCurrentNeighbours(initialNode);

	Set<String> keys = currentNeighbours.keySet();

	for (String focusedNeighbour : keys) {

	    if (getValue(currentNeighbours.get(focusedNeighbour)) + nodeDuration.get(initialNode) < nodeDuration
		    .get(focusedNeighbour) || nodeDuration.get(focusedNeighbour) == 0) {

		if (nodeDuration.get(focusedNeighbour) != 0) {
		    deleteWay(focusedNeighbour);
		}

		setNodeDuration(focusedNeighbour,
			getValue(currentNeighbours.get(focusedNeighbour)) + nodeDuration.get(initialNode));

		if (!prioQue.contains(focusedNeighbour) && !focusedNeighbour.equals(startnode))
		    cheapNeighbours.add(focusedNeighbour);
	    }
	}
	if (!prioQue.isEmpty()) {
	    prioQue.removeFirst();
	}
    }

    /**
     * Initialisiert Wege
     */
    private void initializeRoute() {

	int numberOfNewWays = 0;
	Double price = (double) 0;
	List<String> goneNodes = null;
	List<Map<String, String>> edges = null;

	for (int i = 0; i <= allPaths.size() - 1; i++) {
	    if (allPaths.get(i).getLastNode().equals(nearestNode)) {
		price = allPaths.get(i).getPrice();
		goneNodes = allPaths.get(i).getNodes();
		edges = allPaths.get(i).getEdges();
		numberOfNewWays++;
		allPaths.remove(allPaths.get(i));
	    }
	}
	addNewWays(numberOfNewWays, goneNodes, price, edges);
	this.goneNodes.add(nearestNode);

	currentNeighbours.clear();
    }

    /**
     * Initialisert aktuelle Nachbarn
     * @param initialNode Ausgangsknoten von dem Nachbarn initialisiert werden
     */
    private void initializeCurrentNeighbours(String initialNode) {

	List<String> ids = nodeMap.get(initialNode);

	for (String id : ids) {

	    List<Map<String, String>> maps = routes.get(id);

	    if (maps != null) {
		for (Map<String, String> map : maps) {

		    String neighbour = map.get(Constants.NEW_ROUTE_DESTINATIONNODENAME);

		    if (!currentNeighbours.containsKey(neighbour)
			    || getValue(currentNeighbours.get(neighbour)) > Double.valueOf(map
				    .get(Constants.NEW_ROUTE_DURATION)))
			currentNeighbours.put(neighbour, map);
		}
	    }
	}

    }

    /**
     * Sortiert Liste für noch abzuarbeitende Knoten nach deren Preis (Dauer oder Strecke)
     */
    private void sortPrioQue() {

	for (String cheapNeighbour : cheapNeighbours) {
	    prioQue.add(cheapNeighbour);
	}
	cheapNeighbours.clear();
	prioQueInsertionSort();
	if (!prioQue.isEmpty())
	    nearestNode = prioQue.getFirst();

    }

    /**
     * Sortieralgorithmus für Sortieren der Liste
     */
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

    /**
     * 
     * @param numberOfNewWays Anzahl der neuen Wege
     * @param goneNodes Knoten, die bis dahin gegangen wurden
     * @param price Preis (Dauer oder Strecke) bis dahin
     * @param edges Strecken, die bis dahin gegangen wurden
     */
    private void addNewWays(int numberOfNewWays, List<String> goneNodes, Double gonePrice,
	    List<Map<String, String>> edges) {

	Set<String> neighbours = currentNeighbours.keySet();

	for (String focusedNeighbour : neighbours) {

	    for (int i = 0; i < numberOfNewWays; i++) {
		if (!this.goneNodes.contains(focusedNeighbour)) {
		    allPaths.add(new Way(goneNodes, gonePrice, focusedNeighbour, getValue(currentNeighbours
			    .get(focusedNeighbour)), edges, currentNeighbours.get(focusedNeighbour)));
		}

	    }
	}
    }

    /**
     * Initialisiert Hilfsmap für Preise der Knoten
     */
    private void initializeNodePrice() {

	Set<String> keys = nodeMap.keySet();

	for (String singleKey : keys) {

	    if (!SupportMethods.isNumeric(singleKey))
		nodeDuration.put(singleKey, (double) 0);
	}
    }

    /**
     * Gibt Preis in Abhängigkeit von der Berechnungsmethode Dauer oder Strecke zurück
     * @param map Kante, für die die Dauer oder die Strecke zurückgegeben wird
     * @return Preis
     */
    private Double getValue(Map<String, String> edge) {
	String valueString;
	switch (calcMethod) {
	case Constants.EVALUATION_CALCULATION_DURATION:
	    valueString = edge.get(Constants.NEW_ROUTE_DURATION);
	    break;
	case Constants.EVALUATION_CALCULATION_DISTANCE:
	    valueString = edge.get(Constants.NEW_ROUTE_DISTANCE);
	    break;
	default:
	    valueString = edge.get(Constants.NEW_ROUTE_DURATION);
	}
	if (!SupportMethods.isNumeric(valueString))
	    return null;
	return Double.valueOf(valueString);
    }

    //    public void printNodes() {
    //	Way way = rightPaths.getCheapestWay();
    //	for (String node : way.getNodes()) {
    //	    System.out.println(node);
    //	}
    //    }
    //

    /**
     * Löscht alle Wege, die als letzten Knoten den Parameter als Knoten besitzen
     * @param initialNode Knoten, nach dem gelöscht wird
     */
    private void deleteWay(String initialNode) {
	for (int i = 0; i <= allPaths.size() - 1; i++) {
	    if (allPaths.get(i).getLastNode().equals(initialNode)) {
		allPaths.remove(allPaths.get(i));
	    }
	}
    }

    /**
     * Speichert alle Wege, die am richtigen Zielknoten angekommen sind
     */
    private void pickRightWays() {
	for (int i = 0; i <= allPaths.size() - 1; i++) {
	    if (allPaths.get(i).getLastNode().equals(endnode))
		rightPaths.add(allPaths.get(i));
	}
    }

    /**
     * Setzt den Preis für einen Knoten
     * @param name Key, für den Preis gesetzt werden soll
     * @param price Preis der gesetzt wird
     */
    public void setNodeDuration(String name, Double price) {
	nodeDuration.put(name, price);
    }

}