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
 * Klasse Dijkstra Berechnet kürzeste bzw schnellste Route von Start zu Zielknoten
 * 
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
    private Map<String, Map<String, String>> openNeighbours = new HashMap<String, Map<String, String>>();
    private Map<String, Map<String, String>> goneEdges = new HashMap<String, Map<String, String>>();
    private Paths allPaths;
    private Paths rightPaths;
    private boolean targetReached;
    private String calcMethod;
    private boolean error;
    private String nearestNodeId;

    public Dijkstra(String startnode, String endnode) {

	try {
	    this.nodeMap = StAXMapGraphDataParser.getNodeXMLMap();
	    this.routes = StAXMapGraphDataParser.getRouteXMLMap();
	} catch (FileNotFoundException | XMLStreamException e) {
	    e.printStackTrace();
	}
	this.startnode = startnode;
	this.endnode = endnode;
	this.nearestNode = startnode;
	initializeNodePrice();
	this.allPaths = new Paths();
	this.rightPaths = new Paths();
	this.allPaths.add(new Way(startnode, endnode));
    }

    /**
     * Berechnet neue Nachbarpreise, initialisiert Wege und sortiert Abarbeitungsschlange der offenen Knoten
     * 
     * @param calcMethod String für Unterscheidung der Berechnung (nach Dauer oder Strecke)
     * @return Gibt eine Liste von Maps zurück, die die einzelnen Wege darstellen
     */
    public List<Map<String, String>> calculateRoute(String calcMethod) {

	this.calcMethod = calcMethod;

	while (!this.targetReached) {
	    calcNewNodePrices(this.nearestNode);

	    if (nearestNode.equals("Ragow (2)"))
		breakMethod();

	    System.out.println(this.nearestNode);

	    initializeRoute();

	    sortPrioQue();

	    if (!this.prioQue.isEmpty() && nodeMap.get(this.prioQue.getFirst()).get(0).equals(this.endnode)
		    || this.error == true) {
		this.targetReached = true;
		pickRightWays();
		if (!this.rightPaths.isEmpty())
		    this.rightPaths.initializeCheapestWay();
		else if (this.rightPaths.isEmpty())
		    return null;

		return this.rightPaths.getCheapestWay().getEdges();
	    }
	}
	return null;
    }

    private void breakMethod() {
	System.out.println("so");
    }

    /**
     * Berechnet Preise (Dauer oder Strecke) zu einzelnen Nachbarn
     * 
     * @param initialNode Ausgangsknoten von dem Nachbarn in Betracht gezogen werden
     */
    private void calcNewNodePrices(String initialNode) {
	initializeCurrentNeighbours(initialNode);

	Set<String> keys = this.currentNeighbours.keySet();

	for (String focusedNeighbour : keys)
	    if (getValue(this.currentNeighbours.get(focusedNeighbour)) + this.nodeDuration.get(nearestNodeId) < this.nodeDuration
		    .get(this.currentNeighbours.get(focusedNeighbour).get(Constants.NEW_ROUTE_DESTINATIONNODEID))
		    || this.nodeDuration.get(this.currentNeighbours.get(focusedNeighbour).get(
		            Constants.NEW_ROUTE_DESTINATIONNODEID)) == 0) {

		// if (this.nodeDuration.get(focusedNeighbour) != 0)
		// deleteWay(focusedNeighbour);

		setNodeDuration(currentNeighbours.get(focusedNeighbour).get(Constants.NEW_ROUTE_DESTINATIONNODENAME),
		        getValue(this.currentNeighbours.get(focusedNeighbour)) + this.nodeDuration.get(nearestNodeId));

		if (!this.prioQue.contains(currentNeighbours.get(focusedNeighbour).get(
		        Constants.NEW_ROUTE_DESTINATIONNODEID))
		        && !focusedNeighbour.equals(this.startnode))
		    this.cheapNeighbours.add(focusedNeighbour);
	    }
	if (!this.prioQue.isEmpty())
	    this.prioQue.removeFirst();
    }

    /**
     * Initialisiert Wege
     */
    private void initializeRoute() {

	int numberOfIterations = 0;
	Double gonePrice = (double) 0;
	List<String> goneNodes = null;
	List<Map<String, String>> goneEdges = null;

	if (!this.currentNeighbours.isEmpty()) {
	    numberOfIterations = this.allPaths.size() - 1;
	    for (int i = 0; i <= numberOfIterations; i++) {
		if (this.allPaths.get(i).getLastNode().equals(this.nearestNode)) {
		    gonePrice = this.allPaths.get(i).getPrice();
		    goneNodes = this.allPaths.get(i).getNodes();
		    goneEdges = this.allPaths.get(i).getEdges();
		    addNewWays(goneNodes, gonePrice, goneEdges);
		    this.allPaths.remove(this.allPaths.get(i));
		    numberOfIterations--;
		}
	    }
	}
	this.goneNodes.add(this.nearestNodeId);
    }

    /**
     * Initialisert aktuelle Nachbarn
     * 
     * @param initialNode Ausgangsknoten von dem Nachbarn initialisiert werden
     */
    private void initializeCurrentNeighbours(String initialNode) {
	List<String> ids = new ArrayList<String>();

	if (this.nearestNode.equals(this.startnode))
	    ids = this.nodeMap.get(initialNode);
	else
	    ids.add(this.openNeighbours.get(initialNode).get(Constants.NEW_ROUTE_DESTINATIONNODEID));

	for (String id : ids) {

	    List<Map<String, String>> maps = this.routes.get(id);

	    if (maps != null)
		for (Map<String, String> map : maps) {

		    Map<String, String> neighbour = map;

		    if (!id.equals(neighbour.get(Constants.NEW_ROUTE_DESTINATIONNODEID)))
			if (!this.currentNeighbours.containsKey(neighbour)
			        || getValue(this.currentNeighbours.get(neighbour)) > Double.valueOf(map
			                .get(Constants.NEW_ROUTE_DURATION))) {
			    this.currentNeighbours.put(getName(neighbour), map);
			    this.openNeighbours.put(getName(neighbour), map);
			    this.goneEdges.put(getName(neighbour), map);
			}
		}
	}
    }

    /**
     * Sortiert Liste für noch abzuarbeitende Knoten nach deren Preis (Dauer oder Strecke)
     */
    private void sortPrioQue() {

	for (String cheapNeighbour : this.cheapNeighbours)
	    this.prioQue.add(currentNeighbours.get(cheapNeighbour).get(Constants.NEW_ROUTE_DESTINATIONNODEID));
	this.cheapNeighbours.clear();

	if (!this.prioQue.isEmpty()) {
	    this.nearestNode = nodeMap.get(this.prioQue.getFirst()).get(0);
	    this.nearestNodeId = goneEdges.get(nearestNode).get(Constants.NEW_ROUTE_DESTINATIONNODEID);
	} else if (this.prioQue.isEmpty() && this.nearestNode != this.startnode) {
	    this.error = true;
	}
	this.currentNeighbours.clear();

    }

    /**
     * Sortieralgorithmus für Sortieren der Liste
     */
    private String prioQuePickCheapest() {
	Double finalPrice = null;
	String finalId = null;
	for (String id : prioQue) {
	    if (finalId == null) {
		finalId = nodeMap.get(id).get(0);
		finalPrice = nodeDuration.get(nodeMap.get(id).get(0));
	    } else if (finalPrice != null && nodeDuration.get(nodeMap.get(id).get(0)) < finalPrice) {
		finalPrice = nodeDuration.get(nodeMap.get(id).get(0));
		finalId = nodeMap.get(id).get(0);
	    }
	}
	return finalId;
    }

    /**
     * 
     * @param numberOfNewWays Anzahl der neuen Wege
     * @param goneNodes Knoten, die bis dahin gegangen wurden
     * @param price Preis (Dauer oder Strecke) bis dahin
     * @param edges Strecken, die bis dahin gegangen wurden
     */
    private void addNewWays(List<String> goneNodes, Double gonePrice, List<Map<String, String>> edges) {

	Set<String> neighbours = this.currentNeighbours.keySet();

	for (String focusedNeighbour : neighbours)
	    if (!this.goneNodes.contains(currentNeighbours.get(focusedNeighbour).get(
		    Constants.NEW_ROUTE_DESTINATIONNODEID)))
		this.allPaths.add(new Way(goneNodes, gonePrice, focusedNeighbour, getValue(this.currentNeighbours
		        .get(focusedNeighbour)), edges, this.currentNeighbours.get(focusedNeighbour)));
    }

    /**
     * Initialisiert Hilfsmap für Preise der Knoten
     */
    private void initializeNodePrice() {
	List<String> ids = new ArrayList<String>();
	Set<String> keys = this.nodeMap.keySet();
	this.nearestNodeId = nodeMap.get(startnode).get(0);
	for (String singleKey : keys) {
	    ids = nodeMap.get(singleKey);
	    for (String id : ids) {
		if (!SupportMethods.isNumeric(singleKey))
		    this.nodeDuration.put(id, (double) 0);
	    }
	}
    }

    /**
     * Gibt Preis in Abhängigkeit von der Berechnungsmethode Dauer oder Strecke zurück
     * 
     * @param map Kante, für die die Dauer oder die Strecke zurückgegeben wird
     * @return Preis
     */
    private Double getValue(Map<String, String> edge) {
	String valueString;
	switch (this.calcMethod) {
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

    private String getName(Map<String, String> map) {
	return map.get(Constants.NEW_ROUTE_DESTINATIONNODENAME);
    }

    public void printNodes() {
	Way way = this.rightPaths.getCheapestWay();
	for (String node : way.getNodes())
	    System.out.println(node);
    }

    /**
     * Löscht alle Wege, die als letzten Knoten den Parameter als Knoten besitzen
     * 
     * @param initialNode Knoten, nach dem gelöscht wird
     */
    private void deleteWay(String initialNode) {
	for (int i = 0; i <= this.allPaths.size() - 1; i++)
	    if (this.allPaths.get(i).getLastNode().equals(initialNode))
		this.allPaths.remove(this.allPaths.get(i));
    }

    /**
     * Speichert alle Wege, die am richtigen Zielknoten angekommen sind
     */
    private void pickRightWays() {
	for (int i = 0; i <= this.allPaths.size() - 1; i++)
	    if (this.allPaths.get(i).getLastNode().equals(this.endnode))
		this.rightPaths.add(this.allPaths.get(i));
    }

    /**
     * Setzt den Preis für einen Knoten
     * 
     * @param name Key, für den Preis gesetzt werden soll
     * @param price Preis der gesetzt wird
     */
    public void setNodeDuration(String name, Double price) {
	this.nodeDuration.put(name, price);
    }

}