package de.dhbw.horb.routePlanner.graphData;

import java.util.ArrayList;
import java.util.List;

public class Route {
	
	private Node start;
	private Node end;
	private int length = 0;
	private List<Object> nodeAndEdge;
	private Boolean nodeAddAble = true;
	private Boolean edgeAddAble = false;
	
	
	public Route(Node node){
		nodeAndEdge = new ArrayList<Object>();
		start = node;
		addNode(node);
	}
	
		
	public void addNode(Node node){
		if(nodeAddAble){
			nodeAndEdge.add(node);
			end = node;
		} else {
//			TODO: Exception?	
		}
		
		switchBlock();
	}
	
	public void addEdge(Edge edge){
		if(edgeAddAble){
			nodeAndEdge.add(edge);
			length += edge.getLength();
		} else {
//			TODO: Exception?	
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
