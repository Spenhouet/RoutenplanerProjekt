package de.dhbw.horb.routePlanner.evaluation;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

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
    // TODO Kreuzungskosten als Eigenschaft von Junction -> nodePrice-List nicht gebraucht
    private List<Junction> nodePrice = new ArrayList<Junction>();
    private LinkedList<Junction> prioQue = new LinkedList<Junction>();
    private List<Junction> goneNodes = new ArrayList<Junction>();
    private List<Junction> cheapNeighbours = new ArrayList<Junction>();
    private List<Junction> currentNeighbours = new ArrayList<Junction>();
    private Paths paths;
    private boolean targetReached;

    public Dijkstra(List<String> startnode, List<String> endnode) {

	this.startnode = new Junction(startnode);
	this.endnode = new Junction(endnode);
	this.nearestNode = new Junction();
	paths = new Paths(this.startnode, this.endnode);
	paths.add(new Way(this.startnode, this.endnode));
    }

    /**
     * Hauptmethode
     *  - Solange Ziel nicht erreicht ist, werden neue Knotenpreise berechnet,
     *    Wege neu initialisiert und die Prioritätswarteschlange neusortiert
     */
    public void calculatingRoute() {

	while (!targetReached) {
	    calcNewNodePrices(nearestNode);
	    initializeRoute();
	    sortPrioQue();
	}
	if (isEqual(prioQue.getFirst(), endnode)) {
	    targetReached = true;
	    System.out.print("Ziel erreicht");
	    //paths.initializeCheapestWay();
	}
    }

    /**
     * Berechnet ausgehend vom zu bearbeitenden Knoten die Kosten der Nachbarn
     *  - Wenn es schon einen kürzeren Weg zu einem dieser Nachbarn gibt, wird Kostenwert nicht überschrieben
     *  - Wenn der Kostenwert kleiner als der Aktuelle ist, wird Wert überschrieben 
     *    und alle Wege gelöscht, die als letzte Kreuzung diese Kreuzung haben
     * @param initialNode
     */
    private void calcNewNodePrices(Junction initialNode) {

	for (int i = 0; i < initialNode.getIds().size(); i++) {
	    Junction focusedNeighbour = new Junction(currentNeighbours.get(i)
		    .getIds(), currentNeighbours.get(i).getPrice());

	    if (/*Abstand initialNode zu focusedNeighbour*/+initialNode
		    .getPrice() < focusedNeighbour.getPrice()
		    || focusedNeighbour.getPrice() == 0) {

		if (focusedNeighbour.getPrice() != 0) {
		    //		    deleteWay(focusedNeighbour);		    
		}

		/*Setze Knotenpreis mit Abstand von initialNode zu focusedNeighbour + initialNode.getPrice()*/
		if (!prioQue.contains(focusedNeighbour)
			&& !isEqual(focusedNeighbour, startnode))
		    cheapNeighbours.add(focusedNeighbour);
	    }
	}
	currentNeighbours.clear();
    }

    /**
     * Initialisiert alle verschiedenen Wege von Start zu Endknoten
     *  - Für jeden bereits gegangenen Weg werden so viele neue Wege angelegt,
     *    wie die jeweils letzte Kreuzung Nachbarn hat
     */
    private void initializeRoute() {

    }

    /**
     * Die noch zu bearbeitenden Knoten werden nach ihren Kosten sortiert
     *  - Die billigsten werden zuerst abgearbeitet
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
     * Sortiert die Elemente in der PrioQue ausgehend von deren Kosten
     */
    private void prioQueInsertionSort() {

	for (int i = 0; i < prioQue.size(); i++) {
	    Junction temp = prioQue.get(i);
	    int j;
	    for (j = i - 1; j >= 0
		    && temp.getPrice() < prioQue.get(j).getPrice(); j--) {
		prioQue.set(j + 1, prioQue.get(j));
	    }
	    prioQue.set(j + 1, temp);
	}
    }

    /**
     * Vergleicht zwei übergebene Kreuzungen
     *  - Wenn die erste Kreuzung eine id in der Liste ids besitzt, die einer id aus der zweiten Kreuzung gleicht,
     *    liefert Methode true
     *  - false, sonst
     * @param one
     * @param two
     * @return
     */
    public boolean isEqual(Junction one, Junction two) {

	for (String s1 : one.getIds()) {
	    for (String s2 : two.getIds())
		if (s1 == s2)
		    return true;
	}
	return false;
    }

    public Junction getStartnode() {
	return startnode;
    }

    public Junction getEndnode() {
	return endnode;
    }
}
