package de.dhbw.horb.routePlanner.evaluation.dijkstra;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Klasse Way
 * Repräsentiert einen Weg von Start zu beliebigem Endknoten
 * @author Simon
 *
 */
public class Way {

    private Double price = (double) 0;
    private List<String> nodes = new ArrayList<String>();
    private List<Map<String, String>> edges = new ArrayList<Map<String, String>>();

    public Way(String startnode, String endnode) {
	nodes.add(startnode);
    }

    public Way(List<String> nodes, Double gonePrice, String newNode, Double newPrice, List<Map<String, String>> edges,
	    Map<String, String> newEdge) {
	setPrice(gonePrice + newPrice);
	addNodes(nodes);
	this.nodes.add(newNode);
	addEdges(edges);
	this.edges.add(newEdge);
    }

    /**
     * Zum aktuellen Weg werden alle übergebenen Knoten hinzugefügt
     * @param nodes
     */
    private void addNodes(List<String> nodes) {
	for (String node : nodes)
	    this.nodes.add(node);
    }

    private void addEdges(List<Map<String, String>> edges) {
	for (Map<String, String> edge : edges)
	    this.edges.add(edge);
    }

    public String getLastNode() {
	return nodes.get(nodes.size() - 1);
    }

    public Double getPrice() {
	return price;
    }

    public void setPrice(Double price) {
	this.price = price;
    }

    public List<String> getNodes() {
	return nodes;
    }

    public List<Map<String, String>> getEdges() {
	return edges;
    }

}
