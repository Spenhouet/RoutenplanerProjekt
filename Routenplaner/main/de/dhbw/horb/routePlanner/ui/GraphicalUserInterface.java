package de.dhbw.horb.routePlanner.ui;

import de.dhbw.horb.routePlanner.graphData.Edge;
import de.dhbw.horb.routePlanner.graphData.Node;

public class GraphicalUserInterface {

	public GraphicalUserInterface() {

	}

	public void addEdge(Edge newEdge) {
		
		Node start = newEdge.getStartNode();
		Node end = newEdge.getEndNode();
		System.out.println("Start Node ID: " + newEdge.getStartNodeID() + 
						" mit Breitengrad: " + start.getLatitude() + 
						 " mit Längengrad: " + start.getLongitude() + 
							" End Node ID: " + newEdge.getEndNodeID() + 
						" mit Breitengrad: " + end.getLatitude() +
						 " mit Längengrad: " + end.getLongitude());
	}
}
