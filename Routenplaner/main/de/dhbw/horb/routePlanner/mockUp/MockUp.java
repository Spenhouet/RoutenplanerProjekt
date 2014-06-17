package de.dhbw.horb.routePlanner.mockUp;

import de.dhbw.horb.routePlanner.graphData.Edge;
import de.dhbw.horb.routePlanner.ui.GraphicalUserInterface;

public class MockUp {

	public static void main(String[] args) {

		fillGui();

	}

	private static void fillGui() {
		GraphicalUserInterface gui = new GraphicalUserInterface();

		Edge newEdge = new Edge(1057258534L, 254705894L);

		for (int i = 0; i < 10; i++) {
			gui.addEdge(newEdge);
		}
	}

}
