package de.dhbw.horb.routePlanner.graphData;

import java.util.Map;

public class GraphMap {

	private Map<Integer, Node> nodes;
	
	private GraphMap(){
//		Map<Integer, Node> pem = new HashMap<Integer, Node>();
//	    pem.put( new Integer( ATTRIBUTE ),              "ATTRIBUTE     " );
//	    pem.put( new Integer( CDATA ),                  "CDATA         " );
//	    map = Collections.unmodifiableMap( pem );
	}
	
	
	private void addNode(Node newNode){
		if(newNode == null) return;
		
		nodes.put(new Integer(newNode.getName()), newNode);
	}
}
