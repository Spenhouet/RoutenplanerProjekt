package de.dhbw.horb.routePlanner.ui;

import java.util.List;

import de.dhbw.horb.routePlanner.graphData.Edge;
import de.dhbw.horb.routePlanner.graphData.Node;
import de.dhbw.horb.routePlanner.parser.GraphDataConstants;
import de.dhbw.horb.routePlanner.parser.GraphDataParser;

public class GraphicalUserInterface {

	private Long edgeCount;

	public GraphicalUserInterface() {
		edgeCount = 0L;
		
//		new GraphDataParserMultithread().fillGUI(this);
	
	}

	public void addEdge(Edge newEdge) {
		edgeCount++;
		Node start = newEdge.getStartNode();
		Node end = newEdge.getEndNode();
		System.out.println(edgeCount + ". Start Node ID: " + start.getID()
				+ " mit Breitengrad: " + start.getLatitude()
				+ " mit Längengrad: " + start.getLongitude() + " End Node ID: "
				+ end.getID() + " mit Breitengrad: " + end.getLatitude()
				+ " mit Längengrad: " + end.getLongitude());
	}
	
	
	
	public void getStringName(){
		List<String> names;
		String name = "";
		
		names = GraphDataParser.getGraphDataParser(GraphDataConstants.CONST_XML_NODE_HIGHWAY).containsName(name);
		
		for (String string : names) {
			System.out.println(string);
		}
		
	}
}
