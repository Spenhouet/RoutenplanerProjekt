package de.dhbw.horb.routePlanner.evaluation;

import java.util.ArrayList;
import java.util.List;

/**
 * Klasse Dijkstra
 * Berechnet kürzeste Route von Start zu  Zielknoten
 * @author Simon
 *
 */
public class Dijkstra {

	private Crossroads startnode;
	private Crossroads endnode;
	private Crossroads nearestNode;
	private boolean targetReached;
	private List<String> nodePrice = new ArrayList<String>();
	private List<Crossroads> prioQue = new ArrayList<Crossroads>();
	private List<Crossroads> goneNodes = new ArrayList<Crossroads>();
	private List<Crossroads> cheapNeighbours = new ArrayList<Crossroads>();
	private Paths paths;

	public Dijkstra(List<String> startnode, List<String> endnode) {
		this.startnode = new Crossroads(startnode);
		this.endnode = new Crossroads(endnode);
		this.nearestNode = new Crossroads();
		paths = new Paths(this.startnode, this.endnode);
		paths.add(new Way(this.startnode, this.endnode));
	}

	public void calculatingRoute() {
		while (!targetReached){
			calcNewNodePrices(nearestNode);
		}
	}
	
	private void calcNewNodePrices(Crossroads initialNode){
		
	}

}
