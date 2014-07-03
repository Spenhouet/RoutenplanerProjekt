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
	private boolean targetReached;
	private List<String> nodePrice = new ArrayList<String>();
	private LinkedList<Junction> prioQue = new LinkedList<Junction>();
	private List<Junction> goneNodes = new ArrayList<Junction>();
	private List<Junction> cheapNeighbours = new ArrayList<Junction>();
	private List<Junction> currentNeighbours = new ArrayList<Junction>();
	private Paths paths;

	public Dijkstra(List<String> startnode, List<String> endnode) {
		this.startnode = new Junction(startnode);
		this.endnode = new Junction(endnode);
		this.nearestNode = new Junction();
		paths = new Paths(this.startnode, this.endnode);
		paths.add(new Way(this.startnode, this.endnode));
		calculatingRoute();
	}

	/**
	 * Hauptmethode
	 * Solange Ziel nicht erreicht ist, werden neue Knotenpreise berechnet,
	 * Wege neu initialisiert und die Prioritätswarteschlange neusortiert (welcher Knoten wird als nächstes abgearbeitet)
	 */
	public void calculatingRoute() {
		
		while (!targetReached) {
			calcNewNodePrices(nearestNode);
			initializeRoute();
			sortPrioQue();
		}
		//TODO Vergleichsmethode schreiben
		if (prioQue.getFirst() == endnode) {
			System.out.print("Ziel erreicht");
			targetReached = true;
			//paths.initializeCheapestWay();
		}
		
	}

	private void calcNewNodePrices(Junction initialNode) {

		for (int i = 0; i < initialNode.getIds().size(); i++) {
			Junction focusedNeighbour = new Junction(currentNeighbours.get(i).getIds(), currentNeighbours.get(i).getPrice());
			
			if (/*Abstand initialNode zu focusedNeighbour*/+initialNode.getPrice() < focusedNeighbour.getPrice() || focusedNeighbour.getPrice() == 0) {
				
				if (focusedNeighbour.getPrice() != 0)
//					deleteWay(focusedNeighbour);
					
				/*Setzte Knotenpreis mit Abstand von initialNode zu focusedNeighbour + initialNode.getPrice()*/
				//TODO: Vergleichsmethode schreiben
				if (!prioQue.contains(focusedNeighbour) && focusedNeighbour != startnode)
					cheapNeighbours.add(focusedNeighbour);
			}
		}

	}

	private void initializeRoute() {

	}

	private void sortPrioQue() {

	}

}
