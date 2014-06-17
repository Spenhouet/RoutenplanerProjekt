package de.dhbw.horb.routePlanner.mockUp;

import de.dhbw.horb.routePlanner.graphData.Edge;
import de.dhbw.horb.routePlanner.ui.GraphicalUserInterface;

public class MockUp {
	
	public static void main(String[] args) {
		
		fillGui();
		
	}
	
	
	private static void fillGui(){
		GraphicalUserInterface gui = new GraphicalUserInterface();
		
		Edge newEdge = new Edge(56.456345, 6.4352223);
		
		for (int i = 0; i < 10; i++) {
			gui.addEdge(newEdge);
		}
	}
	
}
