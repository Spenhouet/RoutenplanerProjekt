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
	private String nearestNode;
	private String[] nodePrice;
	private boolean targetReached;
	private List<Crossroads> prioQue = new ArrayList<Crossroads>();
	private List<Crossroads> goneNodes = new ArrayList<Crossroads>();
	private List<Crossroads> cheapNeighbours = new ArrayList<Crossroads>();
	
	
	public Dijkstra(List<String> startnode, List<String> endnode){
		this.startnode = new Crossroads(startnode);
		this.endnode = new Crossroads(endnode);
		
	}
	
}
