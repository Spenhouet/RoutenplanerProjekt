package de.dhbw.horb.routePlanner.evaluation;

import java.util.ArrayList;
import java.util.List;

/**
 * Klasse Way
 * Repräsentiert einen Weg von Start zu beliebigem Endknoten
 * @author Simon
 *
 */
public class Way {

	private Crossroads startnode;
	private Crossroads endnode;
	private int price;
	private List<Crossroads> nodes = new ArrayList<Crossroads>();

	public Way(Crossroads startnode, Crossroads endnode) {
		setStartnode(startnode);
		setEndnode(endnode);
		nodes.add(startnode);
	}
	
	public Way(Crossroads startnode, Crossroads endnode, List<Crossroads> nodes, int gonePrice, Crossroads newNode,
			int newPrice) {
		setStartnode(startnode);
		setEndnode(endnode);
		setPrice(gonePrice + newPrice);
		addNodes(nodes);
		this.nodes.add(newNode);
	}

	private void addNodes(List<Crossroads> nodes) {
		for (Crossroads node : nodes) {
			this.nodes.add(node);
		}
	}

	public Crossroads getLastNode() {
		return nodes.get(nodes.size() - 1);
	}

	public int getPrice() {
		return price;
	}
	
	public void setPrice(int price) {
		this.price = price;
	}

	public List<Crossroads> getNodes() {
		return nodes;
	}

	public void setStartnode(Crossroads startnode) {
		this.startnode = startnode;
	}

	public void setEndnode(Crossroads endnode) {
		this.endnode = endnode;
	}

}
