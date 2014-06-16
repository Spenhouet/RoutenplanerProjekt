package de.dhbw.horb.routePlanner.graphData;

import java.util.ArrayList;
import java.util.List;

/**
 * Klasse die eine Strecke repräsentiert.
 * @author Sebastian
 * @param start Der Knoten an dem die Strecke beginnt.
 * @param end Der Knoten an dem die Strecke aufhört.
 * @param length Die gesamt länge der Strecke.
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
	 * Ein Knoten wird zur Strecke hinzugefügt.
	 * Wenn als letztes eine Kante hinzugefügt wurde, lässt sich nun ein Knoten hinzufügen.
	 * @param node Ein neuer Knoten wird an das Ende der Strecke gehängt.
	 */
	public void addNode(Node node){
		if(nodeAddAble){
			nodeAndEdge.add(node);
			end = node;
		} else {
//			TODO: Exception wenn versucht wird ein Knoten nach einem Knoten anzuhängen?	
		}
		
		switchBlock();
	}
	
	/*
	 * Eine Kante wird zur Strecke hinzugefügt.
	 * @param length Eine neue Kante verlängert die Strecke.
	 */
	public void addEdge(Way edge){
		if(edgeAddAble){
			nodeAndEdge.add(edge);
//			length += edge.getLength();
			//TODO
		} else {
//			TODO: Exception wenn versucht wird eine Kante nach einer Kante anzuhängen?	
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
