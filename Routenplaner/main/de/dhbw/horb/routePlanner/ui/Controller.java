package de.dhbw.horb.routePlanner.ui;

import de.dhbw.horb.routePlanner.parser.GraphDataParserMultithread;

public class Controller {

	public static void main(String[] args) {

		GraphicalUserInterface gui = new GraphicalUserInterface();
		new GraphDataParserMultithread().fillGUI(gui);

	}

}
