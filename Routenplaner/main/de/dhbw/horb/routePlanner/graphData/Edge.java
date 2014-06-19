package de.dhbw.horb.routePlanner.graphData;

import javax.xml.stream.XMLStreamException;

import de.dhbw.horb.routePlanner.parser.GraphDataConstants;
import de.dhbw.horb.routePlanner.parser.GraphDataParser;

public class Edge {

	private Node startNode;
	private Node endNode;
	private Long startNodeID;
	private Long endNodeID;

	public Edge(Long startNodeID, Long endNodeID) {

		setStartNodeID(startNodeID);
		setEndNodeID(endNodeID);

		setNodesMT();

	}

	private void setNodesMT() {

		Thread startNodeThread = new Thread(new Runnable() {

			@Override
			public void run() {
				try {
					setStartNode(GraphDataParser.getGraphDataParser(GraphDataConstants.CONST_XML_NODE_HIGHWAY).getNode(
							startNodeID));
				} catch (XMLStreamException e) {
					e.printStackTrace();
				}
			}
		});

		Thread endNodeThread = new Thread(new Runnable() {

			@Override
			public void run() {
				try {
					setEndNode(GraphDataParser.getGraphDataParser(GraphDataConstants.CONST_XML_NODE_HIGHWAY).getNode(
							endNodeID));
				} catch (XMLStreamException e) {
					e.printStackTrace();
				}
			}
		});
		
		startNodeThread.start();
		endNodeThread.start();
		
		try {
			startNodeThread.join();
			endNodeThread.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
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
