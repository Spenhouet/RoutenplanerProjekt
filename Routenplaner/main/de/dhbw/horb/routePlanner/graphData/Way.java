package de.dhbw.horb.routePlanner.graphData;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;

import javax.xml.stream.XMLStreamException;

import de.dhbw.horb.routePlanner.parser.GraphDataConstants;
import de.dhbw.horb.routePlanner.parser.GraphDataParser;

/**
 * Klasse die eine Kante beschreibt.
 * 
 * @author Sebastian
 */
public class Way {

	private Long id;
	private List<Node> nodes;
	ExecutorService executor;

	// private List<String, Object> characteristics;

	public Way(Long id) {
		nodes = new ArrayList<Node>();
		setId(id);
	}

	public void addNode(Long id) throws XMLStreamException {
		addNode(GraphDataParser.getGraphDataParser(GraphDataConstants.CONST_XML_NODE_HIGHWAY).getNode(id));
	}

	public void addNode(Node node) {
		// if (node == null || node.getLatitude() == null || node.getLongitude()
		// == null)
		// return;
		nodes.add(node);
	}

	public Node getNode(int index) {
		if (nodes.size() - 1 < index || id == null)
			return null;
		return nodes.get(index);
	}

	public Edge getFirstEdge() {
		while (nodes.get(0) == null || nodes.get(1) == null) {
		}

		if (nodes.size() < 2)
			return null;
		return new Edge(nodes.get(0), nodes.get(1));
	}

	public Boolean hasEdge() {
		if (nodes.size() >= 2)
			return true;
		return false;
	}

	public Edge removeFirstEdge() {
		Edge firstEdge = getFirstEdge();
		removeFirstNode();
		return firstEdge;
	}

	public Node removeFirstNode() {
		if (nodes.size() <= 0)
			return null;
		return nodes.remove(0);
	}

	public Long getId() {
		return id;
	}

	private void setId(Long id) {
		this.id = id;
	}
}
