package de.dhbw.horb.routePlanner.graphData;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import javax.xml.stream.XMLStreamException;

import de.dhbw.horb.routePlanner.parser.GraphDataConstants;
import de.dhbw.horb.routePlanner.parser.GraphDataParser;
import de.dhbw.horb.routePlanner.ui.Controller;

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

	public void addNode(Long id) {
		addNodeMT(id);
	}

	public void addNode(Node node) {
		if (node == null)
			return;
		nodes.add(node);
	}

	public Node getNode(int index) {
		if (nodes.size()-1 < index)
			return null;
		return nodes.get(index);
	}

	public Edge getFirstEdge() {
		while(nodes.get(0) == null || nodes.get(1) == null){
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

	private void addNodeMT(final Long id) {

		Controller.executor.getExecutor().submit(new Runnable() {

			@Override
			public void run() {
				try {
					addNode(GraphDataParser.getGraphDataParser(GraphDataConstants.CONST_XML_NODE_HIGHWAY).getNode(id));
				} catch (XMLStreamException e) {
					e.printStackTrace();
				}
			}
		});
	}
}