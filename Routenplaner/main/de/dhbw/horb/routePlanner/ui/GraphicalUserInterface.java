package de.dhbw.horb.routePlanner.ui;

import de.dhbw.horb.routePlanner.graphData.Edge;
import de.dhbw.horb.routePlanner.graphData.Node;

public class GraphicalUserInterface {

	public GraphicalUserInterface() {

	}

	public void addNode(Node newNode) {
		System.out
				.println(newNode.getID() + " mit lat " + newNode.getLatitude() + " mit lon " + newNode.getLongitude());
	}

	public void addEdge(Edge newEdge) {
		System.out.println("Start Node ID: " + newEdge.getStartNodeID() + " End Node ID: " + newEdge.getEndNodeID());
	}

}
