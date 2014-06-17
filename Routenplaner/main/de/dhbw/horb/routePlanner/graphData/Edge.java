package de.dhbw.horb.routePlanner.graphData;

import de.dhbw.horb.routePlanner.parser.GraphDataParserMultithread;

public class Edge {

	private Node startNode;
	private Node endNode;
	private Long startNodeID;
	private Long endNodeID;

	public Edge(Long startNodeID, Long endNodeID) {

		setStartNode(startNodeID);
		setEndNode(endNodeID);

		setStartNodeID(startNodeID);
		setEndNodeID(endNodeID);
	}

	public Node getStartNode() {
		return startNode;
	}

	private void setStartNode(Long startNodeID) {
		Thread sT = new GraphDataParserMultithread().getNodeThread(startNodeID);
		sT.start();
		// TODO Wert setzen aus Thread
	}

	public Node getEndNode() {
		return endNode;
	}

	private void setEndNode(Long endNodeID) {
		Thread eT = new GraphDataParserMultithread().getNodeThread(endNodeID);
		eT.start();
		// TODO Wert setzen aus Thread
	}

	public Long getStartNodeID() {
		return startNodeID;
	}

	public void setStartNodeID(Long startNodeID) {
		this.startNodeID = startNodeID;
	}

	public Long getEndNodeID() {
		return endNodeID;
	}

	public void setEndNodeID(Long endNodeID) {
		this.endNodeID = endNodeID;
	}

}
