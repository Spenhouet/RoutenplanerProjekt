package de.dhbw.horb.routePlanner.ui;

import de.dhbw.horb.routePlanner.graphData.Edge;
import de.dhbw.horb.routePlanner.parser.GraphDataParserMultithread;

public class Controller {

	public static void main(String[] args) {

		GraphicalUserInterface gui = new GraphicalUserInterface();
		new GraphDataParserMultithread().fillGUI(gui);
//TODO wieder weg
//		---------------------------------------------------------------------------------------
		Edge newEdge = new Edge(56.456345, 6.4352223);
		
		for (int i = 0; i < 10; i++) {
			gui.addEdge(newEdge);
		}
//		---------------------------------------------------------------------------------------
	}

}
