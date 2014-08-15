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
    private String nearestNodeId;
    private Map<String, List<Map<String, String>>> routes;
    private Map<String, List<String>> nodeMap;
    private Map<String, Double> nodeDuration = new HashMap<String, Double>();
    private Map<String, Map<String, String>> currentNeighbours = new HashMap<String, Map<String, String>>();
    private Map<String, Map<String, String>> openNeighbours = new HashMap<String, Map<String, String>>();
    private Map<String, Map<String, String>> goneEdges = new HashMap<String, Map<String, String>>();

    private LinkedList<String> prioQue = new LinkedList<String>();
    private List<String> goneNodes = new ArrayList<String>();
    private List<String> cheapNeighbours = new ArrayList<String>();
    private Paths allPaths;
    private Paths rightPaths;
    private boolean targetReached;
    private String calcMethod;
    private boolean error;

    public Dijkstra(String startnode, String endnode) {

	try {
	    nodeMap = StAXMapGraphDataParser.getNodeXMLMap();
	    routes = StAXMapGraphDataParser.getRouteXMLMap();
	} catch (FileNotFoundException | XMLStreamException e) {
	    e.printStackTrace();
	}
	this.startnode = startnode;
	this.endnode = endnode;
	nearestNode = startnode;
	initializeNodePrice();
	allPaths = new Paths();
	rightPaths = new Paths();
	allPaths.add(new Way(startnode, endnode));
    }

    /**
     * Berechnet neue Preise, initialisiert Wege und bearbeitet Abarbeitungsschlange von noch offenen Knoten
     * 
     * @param calcMethod String für Unterscheidung der Berechnung (nach Dauer oder Strecke)
     * @return Gibt eine Liste von Maps zurück, die die einzelnen Streckenabschnitte darstellen
     */
    public List<Map<String, String>> calculateRoute(String calcMethod) {

	this.calcMethod = calcMethod;

	while (!targetReached) {
	    calcNewNodePrices(nearestNode);

	    initializeRoute();

	    maintainPrioQue();

	    if (!prioQue.isEmpty() && nodeMap.get(prioQue.getFirst()).get(0).equals(endnode) || error == true) {
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
     * Berechnet Preise (nach Dauer oder Strecke) zu einzelnen Knoten
     * 
     * @param initialNode Ausgangsknoten von dem Nachbarknoten in Betracht gezogen werden
     */
    private void calcNewNodePrices(String initialNode) {
	initializeCurrentNeighbours(initialNode);

	Set<String> keys = currentNeighbours.keySet();

	for (String focusedNeighbour : keys)
	    if (getValue(currentNeighbours.get(focusedNeighbour)) + nodeDuration.get(nearestNodeId) < nodeDuration
		    .get(getId(focusedNeighbour))
		    || nodeDuration.get(getId(focusedNeighbour)) == 0) {

		setNodeDuration(getName(currentNeighbours.get(focusedNeighbour)), getValue(currentNeighbours
		        .get(focusedNeighbour))
		        + nodeDuration.get(nearestNodeId));

		if (!prioQue.contains(getId(focusedNeighbour)) && !focusedNeighbour.equals(startnode))
		    cheapNeighbours.add(focusedNeighbour);
	    }
	if (!prioQue.isEmpty())
	    prioQue.removeFirst();
    }

    /**
     * Initialisiert Wege. Für jeden Weg dessen letzter Knoten dem aktuell bearbeitenden Knoten gleicht werden (Anzahl
     * Nachbarknoten des aktuell bearbeitenden Knoten)-viele neue Wege angelegt
     */
    private void initializeRoute() {

	int numberOfIterations = 0;
	Double gonePrice = (double) 0;
	List<String> goneNodes = null;
	List<Map<String, String>> goneEdges = null;

	if (!currentNeighbours.isEmpty()) {
	    numberOfIterations = allPaths.size() - 1;
	    for (int i = 0; i <= numberOfIterations; i++) {
		if (allPaths.get(i).getLastNode().equals(nearestNode)) {
		    gonePrice = allPaths.get(i).getPrice();
		    goneNodes = allPaths.get(i).getNodes();
		    goneEdges = allPaths.get(i).getEdges();
		    addNewWays(goneNodes, gonePrice, goneEdges);
		    allPaths.remove(allPaths.get(i));
		    numberOfIterations--;
		}
	    }
	}
	this.goneNodes.add(nearestNodeId);
    }

    /**
     * Initialisert aktuelle Nachbarn
     * 
     * @param initialNode Ausgangsknoten von dem Nachbarn initialisiert werden
     */
    private void initializeCurrentNeighbours(String initialNode) {
	List<String> ids = new ArrayList<String>();

	if (nearestNode.equals(startnode))
	    ids = nodeMap.get(initialNode);
	else
	    ids.add(openNeighbours.get(initialNode).get(Constants.NEW_ROUTE_DESTINATIONNODEID));

	for (String id : ids) {

	    List<Map<String, String>> maps = routes.get(id);

	    if (maps != null)
		for (Map<String, String> map : maps) {

		    Map<String, String> neighbour = map;

		    if (!id.equals(neighbour.get(Constants.NEW_ROUTE_DESTINATIONNODEID)))
			if (!currentNeighbours.containsKey(neighbour)
			        || getValue(currentNeighbours.get(neighbour)) > Double.valueOf(map
			                .get(Constants.NEW_ROUTE_DURATION))) {
			    currentNeighbours.put(getName(neighbour), map);
			    openNeighbours.put(getName(neighbour), map);
			    goneEdges.put(getName(neighbour), map);
			}
		}
	}
    }

    /**
     * Verwaltet Abarbeitungsschlange Bestimmt nächsten zu bearbeitenden Knoten anhand von geringstem Preis der offenen
     * Knoten
     */
    private void maintainPrioQue() {

	for (String cheapNeighbour : cheapNeighbours)
	    prioQue.add(currentNeighbours.get(cheapNeighbour).get(Constants.NEW_ROUTE_DESTINATIONNODEID));
	cheapNeighbours.clear();

	if (!prioQue.isEmpty()) {
	    nearestNode = nodeMap.get(prioQuePickCheapest()).get(0);
	    nearestNodeId = goneEdges.get(nearestNode).get(Constants.NEW_ROUTE_DESTINATIONNODEID);
	} else if (prioQue.isEmpty() && nearestNode != startnode) {
	    error = true;
	}
	currentNeighbours.clear();
    }

    /**
     * Sucht Knoten aus Warteschlange mit geringstem Preis (Dauer oder Strecke)
     * 
     * @return finalId Gibt Id des "billigsten" Knotens zurück
     */
    private String prioQuePickCheapest() {
	Double finalPrice = null;
	String finalId = null;
	for (String id : prioQue) {
	    if (finalId == null) {
		finalId = id;
		finalPrice = nodeDuration.get(id);
	    } else if (finalPrice != null && nodeDuration.get(nodeMap.get(id).get(0)) < finalPrice) {
		finalPrice = nodeDuration.get(id);
		finalId = id;
	    }
	}
	return finalId;
    }

    /**
     * Fügt neue Wege hinzu
     * 
     * @param goneNodes Knoten, die bis dahin gegangen wurden
     * @param price Preis (Dauer oder Strecke), der bis dahin beansprucht wurde
     * @param edges Strecken, die bis dahin gegangen wurden
     */
    private void addNewWays(List<String> goneNodes, Double gonePrice, List<Map<String, String>> edges) {

	Set<String> neighbours = currentNeighbours.keySet();

	for (String focusedNeighbour : neighbours)
	    if (!this.goneNodes.contains(currentNeighbours.get(focusedNeighbour).get(
		    Constants.NEW_ROUTE_DESTINATIONNODEID)))
		allPaths.add(new Way(goneNodes, gonePrice, focusedNeighbour, getValue(currentNeighbours
		        .get(focusedNeighbour)), edges, currentNeighbours.get(focusedNeighbour)));
    }

    /**
     * Initialisiert Hilfsmap für Preise der Knoten
     */
    private void initializeNodePrice() {
	List<String> ids = new ArrayList<String>();
	Set<String> keys = nodeMap.keySet();
	nearestNodeId = nodeMap.get(startnode).get(0);
	for (String singleKey : keys) {
	    ids = nodeMap.get(singleKey);
	    for (String id : ids) {
		if (!SupportMethods.isNumeric(singleKey))
		    nodeDuration.put(id, (double) 0);
	    }
	}
    }

    /**
     * Gibt Preis in Abhängigkeit von der Berechnungsmethode Dauer oder Strecke zurück
     * 
     * @param map Kante, für die die Dauer oder die Strecke zurückgegeben wird
     * @return Preis für die Kante
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

    /**
     * Gibt den Zielnamen von dem übergebenen Streckenabschnitt zurück
     * 
     * @param edge Kante von der Zielnamen zurückgegeben wird
     * @return Zielnamen von Kante
     */
    private String getName(Map<String, String> edge) {
	return edge.get(Constants.NEW_ROUTE_DESTINATIONNODENAME);
    }

    /**
     * Gibt Id für Name des Knoten zurück
     * 
     * @param focusedNeighbour Knoten für den Id zurückgegeben wird
     * @return Id für Knoten
     */
    private String getId(String focusedNeighbour) {
	return currentNeighbours.get(focusedNeighbour).get(Constants.NEW_ROUTE_DESTINATIONNODEID);
    }

    /**
     * Speichert alle Wege, die am richtigen Zielknoten angekommen sind
     */
    private void pickRightWays() {
	for (int i = 0; i <= allPaths.size() - 1; i++)
	    if (allPaths.get(i).getLastNode().equals(endnode))
		rightPaths.add(allPaths.get(i));
    }

    /**
     * Setzt den Preis für einen Knoten
     * 
     * @param name Key, für den Preis gesetzt werden soll
     * @param price Preis der gesetzt wird
     */
    public void setNodeDuration(String name, Double price) {
	nodeDuration.put(name, price);
    }

}