package de.dhbw.horb.routePlanner.evaluation.dijkstra;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.xml.stream.XMLStreamException;

import de.dhbw.horb.routePlanner.Constants;
import de.dhbw.horb.routePlanner.data.StAXMapGraphDataParser;

/**
 * Klasse Dijkstra
 * Berechnet kürzeste Route von Start zu  Zielknoten
 * @author Simon
 *
 */
public class Dijkstra {

    private Junction startnode;
    private Junction endnode;
    private Junction nearestNode;
    private Map<String, List<Map<String, String>>> routes;
    private Map<String, List<String>> nodeMap;
    private Map<Junction, Integer> nodePrice = new HashMap<Junction, Integer>();

    private LinkedList<Junction> prioQue = new LinkedList<Junction>();
    private List<Junction> goneNodes = new ArrayList<Junction>();
    private List<Junction> cheapNeighbours = new ArrayList<Junction>();
    private List<Junction> currentNeighbours = new ArrayList<Junction>();
    private Paths paths;
    private boolean targetReached;
    private String calcMethod;

    public Dijkstra(String startnode, String endnode) {

	try {
	    nodeMap = StAXMapGraphDataParser.getNodeXMLMap();
	    routes = StAXMapGraphDataParser.getRouteXMLMap();
	} catch (FileNotFoundException | XMLStreamException e) {
	    e.printStackTrace();
	}
	this.startnode = new Junction(startnode, nodeMap.get(startnode));
	this.endnode = new Junction(endnode, nodeMap.get(endnode));
	this.nearestNode = this.startnode;
	paths = new Paths(this.startnode, this.endnode);
	paths.add(new Way(this.startnode, this.endnode));
    }

    private Junction buildJunctionByDeparture(String departureNodeName) {
	Junction j = new Junction(departureNodeName, nodeMap.get(departureNodeName));
	j.setName(departureNodeName);
	return j;
    }

    private Junction buildJunctionByDestination(String destinationNodeName, String cost) {
	if (duration()) {
	    Junction j = new Junction(destinationNodeName, nodeMap.get(destinationNodeName), Long.valueOf(cost));
	} else if (distance()) {
	    Junction j = new Junction(destinationNodeName, nodeMap.get(destinationNodeName), Double.valueOf(cost));
	}
	return null;
    }

    private Junction initializeCurrentNeighbours(Junction initialNode) {

	for (String id : initialNode.getIds()) {

	    List<Map<String, String>> maps = routes.get(id);

	    for (Map<String, String> map : maps) {

		//		if (duration()) {
		//		    buildJunctionByDestination(entry.get(Constants.NEW_ROUTE_DESTINATIONNODENAME),
		//			    m.get(Constants.NEW_ROUTE_DURATION));
		//		} else if (distance()) {
		//		    buildJunctionByDestination(entry.get(Constants.NEW_ROUTE_DESTINATIONNODENAME),
		//			    m.get(Constants.NEW_ROUTE_DISTANCE));
		//		}

		Junction neighbour = new Junction(map.get(Constants.NEW_ROUTE_DESTINATIONNODENAME), nodeMap.get(map
			.get(Constants.NEW_ROUTE_DESTINATIONNODENAME)));
		neighbour.setDuration(Long.valueOf(map.get(Constants.NEW_ROUTE_DURATION)));
		neighbour.setDistance(Double.valueOf(map.get(Constants.NEW_ROUTE_DISTANCE)));
		currentNeighbours.add(neighbour);
	    }

	    //	    for (String id2 : nodeMap.get(routes.get(id).get(Constants.NEW_ROUTE_DESTINATIONNODENAME))) {
	    //		System.out.println(id2);
	    //	    }
	}
	return null;
    }

    /**
     * Hauptmethode
     *  - Solange Ziel nicht erreicht ist, werden neue Knotenpreise berechnet,
     *    Wege neu initialisiert und die Prioritätswarteschlange neusortiert
     */
    public void calculateRoute(String calcMethod) {

	this.calcMethod = calcMethod;

	while (!targetReached) {
	    calcNewNodePrices(nearestNode);
	    initializeRoute();
	    sortPrioQue();
	}
	if (isEqual(prioQue.getFirst(), endnode)) {
	    targetReached = true;
	    System.out.print("Ziel erreicht");
	    //	    paths.initializeCheapestWay();
	}
    }

    /**
     * Berechnet ausgehend von zur bearbeitenden Kreuzung die Kosten der Nachbarkreuzungen
     *  - Wenn es schon einen kürzeren Weg zu einem dieser Nachbarkreuzungen gibt, wird Kostenwert nicht überschrieben
     *  - Wenn der Kostenwert kleiner als der Aktuelle ist, wird Wert überschrieben 
     *    und alle Wege gelöscht, die als letzte Kreuzung diese Nachbarkreuzung besitzen
     * @param initialNode Aktuelle Kreuzung
     */
    private void calcNewNodePrices(Junction initialNode) {

	//	numberOfNeighbours = 
	initializeCurrentNeighbours(initialNode);

	//	for (int i = 0; i < numberOfNeighbours; i++) {
	//	    Junction focusedNeighbour = currentNeighbours.get(i);
	//
	//	    if (/*Abstand initialNode zu focusedNeighbour*/+initialNode.getDuration() < focusedNeighbour.getDuration()
	//		    || focusedNeighbour.getDuration() == 0) {
	//
	//		// Wenn es einen billigeren Weg zu einer bestimmten Kreuzung gibt sollen alle anderen Wege zu dieser Kreuzung gelöscht werden
	//		if (focusedNeighbour.getDuration() != 0) {
	//		    //		    deleteWay(focusedNeighbour);		    
	//		}
	//
	//		/*Setze Knotenpreis mit Abstand von initialNode zu focusedNeighbour + initialNode.getPrice()*/
	//		if (!prioQue.contains(focusedNeighbour) && !isEqual(focusedNeighbour, startnode))
	//		    cheapNeighbours.add(focusedNeighbour);
	//	    }
	//	}
	currentNeighbours.clear();
    }

    /**
     * Initialisiert alle verschiedenen Wege von Start zu Endknoten
     *  - Für jeden bereits gegangenen Weg werden so viele neue Wege angelegt,
     *    wie die jeweils letzte Kreuzung Nachbarkreuzungen hat
     */
    private void initializeRoute() {

    }

    /**
     * Die noch zu bearbeitenden Kreuzungen werden nach ihren Kosten sortiert
     *  - Die "Billigsten" werden zuerst abgearbeitet
     *  - Die erste Kreuzung der PrioQue ist die zu bearbeitende
     */
    private void sortPrioQue() {

	for (Junction cheapNeighbour : cheapNeighbours) {
	    prioQue.add(cheapNeighbour);
	}
	cheapNeighbours.clear();
	prioQueInsertionSort();
	if (!prioQue.isEmpty())
	    nearestNode = prioQue.getFirst();
    }

    /**
     * Sortiert die Kreuzungen in der PrioQue ausgehend von deren aktuellen Kosten
     */
    private void prioQueInsertionSort() {

	for (int i = 0; i < prioQue.size(); i++) {
	    Junction temp = prioQue.get(i);
	    int j;
	    for (j = i - 1; j >= 0 && temp.getDuration() < prioQue.get(j).getDuration(); j--) {
		prioQue.set(j + 1, prioQue.get(j));
	    }
	    prioQue.set(j + 1, temp);
	}
    }

    /**
     * Vergleicht zwei übergebene Kreuzungen
     *  - Wenn die erste Kreuzung eine Id besitzt, die einer Id aus der zweiten Kreuzung gleicht,
     *    liefert Methode true
     *  - false, sonst
     * @param one Erste Kreuzung
     * @param two Zweite Kreuzung
     * @return Wahr oder Falsch
     */
    public boolean isEqual(Junction one, Junction two) {

	for (String s1 : one.getIds()) {
	    for (String s2 : two.getIds())
		if (s1 == s2)
		    return true;
	}
	return false;
    }

    private boolean duration() {
	return calcMethod.equals(Constants.EVALUATION_CALCULATION_DURATION);
    }

    private boolean distance() {
	return calcMethod.equals(Constants.EVALUATION_CALCULATION_DISTANCE);
    }

}
