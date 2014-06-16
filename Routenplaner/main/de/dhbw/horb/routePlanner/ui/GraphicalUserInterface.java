package de.dhbw.horb.routePlanner.ui;

import de.dhbw.horb.routePlanner.graphData.Way;
import de.dhbw.horb.routePlanner.graphData.Node;

public class GraphicalUserInterface {
	
	public GraphicalUserInterface(){
		
	}
	
	
	public void addNode(Node newNode){
		System.out.println(newNode.getID() + " mit lat " + newNode.getLatitude() + " mit lon " + newNode.getLongitude());
	}
	
	public void addWay(Way newEdge){
//		System.out.println("Source: "+newEdge.getSource()+ " Target: "+ newEdge.getTarget());
	}
	
	
}
