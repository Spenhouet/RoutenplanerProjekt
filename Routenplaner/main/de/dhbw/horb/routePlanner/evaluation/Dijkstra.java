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
    // TODO wie wird auf Kreuzungskosten zugegriffen?
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
     * Solange Ziel nicht erreicht ist, werden neue Knotenpreise berechnet,
     * Wege neu initialisiert und die Prioritätswarteschlange neusortiert -> welcher Knoten wird als nächstes abgearbeitet
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

    private void calcNewNodePrices(Junction initialNode) {

	for (int i = 0; i < initialNode.getIds().size(); i++) {
	    Junction focusedNeighbour = new Junction(currentNeighbours.get(i)
		    .getIds(), currentNeighbours.get(i).getPrice());

	    if (/*Abstand initialNode zu focusedNeighbour*/+initialNode
		    .getPrice() < focusedNeighbour.getPrice()
		    || focusedNeighbour.getPrice() == 0) {

		if (focusedNeighbour.getPrice() != 0)
		    //					deleteWay(focusedNeighbour);

		    /*Setzte Knotenpreis mit Abstand von initialNode zu focusedNeighbour + initialNode.getPrice()*/
		    if (!prioQue.contains(focusedNeighbour)
			    && !isEqual(focusedNeighbour, startnode))
			cheapNeighbours.add(focusedNeighbour);
	    }
	}
    }

    private void initializeRoute() {

    }

    private void sortPrioQue() {

	for (Junction cheapNeighbour : cheapNeighbours) {
	    prioQue.add(cheapNeighbour);
	}
	cheapNeighbours.clear();
	prioQueInsertionSort();
	nearestNode = prioQue.getFirst();
    }

    // TODO Testen!
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
