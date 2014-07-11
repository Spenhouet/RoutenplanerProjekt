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

    private Junction startnode;
    private Junction endnode;
    private int price;
    private List<Junction> nodes = new ArrayList<Junction>();

    public Way(Junction startnode, Junction endnode) {
	setStartnode(startnode);
	setEndnode(endnode);
	nodes.add(startnode);
    }

    public Way(Junction startnode, Junction endnode, List<Junction> nodes,
	    int gonePrice, Junction newNode, int newPrice) {
	setStartnode(startnode);
	setEndnode(endnode);
	setPrice(gonePrice + newPrice);
	addNodes(nodes);
	this.nodes.add(newNode);
    }

    /**
     * Zum aktuellen Weg werden alle übergebenen Knoten hinzugefügt
     * @param nodes
     */
    private void addNodes(List<Junction> nodes) {
	for (Junction node : nodes) {
	    this.nodes.add(node);
	}
    }

    public Junction getLastNode() {
	return nodes.get(nodes.size() - 1);
    }

    public int getPrice() {
	return price;
    }

    public void setPrice(int price) {
	this.price = price;
    }

    public List<Junction> getNodes() {
	return nodes;
    }

    public void setStartnode(Junction startnode) {
	this.startnode = startnode;
    }

    public void setEndnode(Junction endnode) {
	this.endnode = endnode;
    }

}
