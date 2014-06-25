package de.dhbw.horb.routePlanner.graphData;

import java.awt.Color;

public class Edge {

	private Node startNode;
	private Node endNode;
	private Color color;

	public Edge(Node startNode, Node endNode) {

		setStartNode(startNode);
		setEndNode(endNode);
		setColor(Color.black);
	}

	public Node getStartNode() {
		return startNode;
	}

	private void setStartNode(Node startNode) {
		this.startNode = startNode;
	}

	public Node getEndNode() {
		return endNode;
	}

	private void setEndNode(Node endNode) {
		this.endNode = endNode;
	}

	public Color getColor() {
		return color;
	}

	public void setColor(Color color) {
		this.color = color;
	}

}
