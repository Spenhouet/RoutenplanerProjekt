package de.dhbw.horb.routePlanner.ui;

import de.dhbw.horb.routePlanner.parser.ParserController;


public class Controller {

	public static void main(String[] args) {
		GraphicalUserInterface gui = new GraphicalUserInterface();
		
		new ParserController().fillGUI(gui);
			
			
			
		
	}

}
