package de.dhbw.horb.routePlanner.evaluation.dijkstra;

import java.util.ArrayList;
import java.util.List;

/**
 * Klasse Way
 * Repräsentiert einen Weg von Start zu beliebigem Endknoten
 * @author Simon
 *
 */
public class Way {

    private Long price = (long) 0;
    private List<String> nodes = new ArrayList<String>();

    public Way(String startnode, String endnode) {
	nodes.add(startnode);
    }

    public Way(List<String> nodes, Long gonePrice, String newNode, Long newPrice) {
	setPrice(gonePrice + newPrice);
	addNodes(nodes);
	this.nodes.add(newNode);
    }

    /**
     * Zum aktuellen Weg werden alle übergebenen Knoten hinzugefügt
     * @param nodes
     */
    private void addNodes(List<String> nodes) {
	for (String node : nodes) {
	    this.nodes.add(node);
	}
    }

    public String getLastNode() {
	return nodes.get(nodes.size() - 1);
    }

    public Long getPrice() {
	return price;
    }

    public void setPrice(Long price) {
	this.price = price;
    }

    public List<String> getNodes() {
	return nodes;
    }

}
