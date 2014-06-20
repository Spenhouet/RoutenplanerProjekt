package de.dhbw.horb.routePlanner.graphData;

import java.util.ArrayList;
import java.util.List;

/**
 * Klasse die eine Kante beschreibt.
 * 
 * @author Sebastian
 */
public class Way {

	private Long id;
	private List<Long> nodeIDs;

	// private List<String, Object> characteristics;

	public Way(Long id) {
		nodeIDs = new ArrayList<Long>();
		setId(id);
	}

	public void addNodeID(Long id) {
		nodeIDs.add(id);
	}

	public Long getNode(int index) {
		if (nodeIDs.size() < index)
			return null;
		return nodeIDs.get(index);
	}

	public Edge getFirstEdge() {
		if (nodeIDs.size() < 2)
			return null;
		return new Edge(nodeIDs.get(0), nodeIDs.get(1));
	}

	public Boolean hasEdge() {
		if (nodeIDs.size() >= 2)
			return true;
		return false;
	}

	public Edge removeFirstEdge() {
		Edge firstEdge = getFirstEdge();
		removeFirstNode();
		return firstEdge;
	}

	public Long removeFirstNode() {
		if (nodeIDs.size() <= 0)
			return null;
		return nodeIDs.remove(0);
	}

	public Long getId() {
		return id;
	}

	private void setId(Long id) {
		this.id = id;
	}

}
