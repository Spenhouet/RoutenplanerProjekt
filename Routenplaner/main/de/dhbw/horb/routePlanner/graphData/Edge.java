package de.dhbw.horb.routePlanner.graphData;

public class Edge {

	private Node startNode;
	private Node endNode;

	public Edge(Node startNode, Node endNode) {

		setStartNode(startNode);
		setEndNode(endNode);

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

}
