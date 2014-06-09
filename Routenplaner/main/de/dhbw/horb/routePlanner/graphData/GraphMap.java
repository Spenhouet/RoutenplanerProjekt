package de.dhbw.horb.routePlanner.graphData;

import java.util.HashMap;
import java.util.Map;

public class GraphMap {

	private Map<String, Node> nodes;
	//TODO Abschaffen
	
	public GraphMap(){
		//TODO Map oder Liste?
		nodes = new HashMap<String, Node>();
//		Map<String, Node> pem = new HashMap<Integer, Node>();
//	    pem.put( new Integer( ATTRIBUTE ),              "ATTRIBUTE     " );
//	    pem.put( new Integer( CDATA ),                  "CDATA         " );
//	    map = Collections.unmodifiableMap( pem );
	}
	
	
	public void addNode(Node newNode){
		if(newNode == null) return;
		
		nodes.put(newNode.getName(), newNode);
	}
}
