package de.dhbw.horb.routePlanner.graphData;

import java.util.ArrayList;
import java.util.List;

/**
 * Klasse die eine Strecke repr�sentiert.
 * @author Sebastian
 * @param start Der Knoten an dem die Strecke beginnt.
 * @param end Der Knoten an dem die Strecke aufh�rt.
 * @param length Die gesamt l�nge der Strecke.
 * @param nodeAndEdge Liste die immer abwechselnd aus Knoten und Kanten besteht.
 */
public class Route {
	
	private Node start;
	private Node end;
	private int length = 0;
	private List<Object> nodeAndEdge;
	private Boolean nodeAddAble = true;
	private Boolean edgeAddAble = false;
	
	/**
	 * Konstruktor der Strecke.
	 * @param node Der Knoten bei dem die Strecke beginnt.
	 */
	public Route(Node node){
		nodeAndEdge = new ArrayList<Object>();
		start = node;
		addNode(node);
	}
	
	/**
	 * Ein Knoten wird zur Strecke hinzugef�gt.
	 * Wenn als letztes eine Kante hinzugef�gt wurde, l�sst sich nun ein Knoten hinzuf�gen.
	 * @param node Ein neuer Knoten wird an das Ende der Strecke geh�ngt.
	 */
	public void addNode(Node node){
		if(nodeAddAble){
			nodeAndEdge.add(node);
			end = node;
		} else {
//			TODO: Exception wenn versucht wird ein Knoten nach einem Knoten anzuh�ngen?	
		}
		
		switchBlock();
	}
	
	/*
	 * Eine Kante wird zur Strecke hinzugef�gt.
	 * @param length Eine neue Kante verl�ngert die Strecke.
	 */
	public void addEdge(Way edge){
		if(edgeAddAble){
			nodeAndEdge.add(edge);
//			length += edge.getLength();
			//TODO
		} else {
//			TODO: Exception wenn versucht wird eine Kante nach einer Kante anzuh�ngen?	
		}
		
		switchBlock();
	}
	
	private void switchBlock(){
		nodeAddAble = !nodeAddAble;
		edgeAddAble = !edgeAddAble;
	}
	
		
	public Node getSource() {
		return start;
	}
	public Node getTarget() {
		return end;
	}
	public int getLength() {
		return length;
	}
	public int getNodeAndEdgeCount(){
		return nodeAndEdge.size();
	}

	
}
