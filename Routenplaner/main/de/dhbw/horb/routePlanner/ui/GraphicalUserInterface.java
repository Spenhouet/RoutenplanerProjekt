package de.dhbw.horb.routePlanner.ui;

import de.dhbw.horb.routePlanner.graphData.Edge;
import de.dhbw.horb.routePlanner.graphData.Node;

public class GraphicalUserInterface {

	private Long edgeCount;
	
	public GraphicalUserInterface() {
		edgeCount = 0L;
	}

	public void addEdge(Edge newEdge) {
		edgeCount++;
		Node start = newEdge.getStartNode();
		Node end = newEdge.getEndNode();
		System.out.println(edgeCount + ". Start Node ID: " + newEdge.getStartNodeID() + 
						" mit Breitengrad: " + start.getLatitude() + 
						 " mit Lšngengrad: " + start.getLongitude() + 
							" End Node ID: " + newEdge.getEndNodeID() + 
						" mit Breitengrad: " + end.getLatitude() +
						 " mit Lšngengrad: " + end.getLongitude());
	}
}
