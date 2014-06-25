package de.dhbw.horb.routePlanner.ui;

import de.dhbw.horb.routePlanner.parser.GraphDataThreadExecutor;

public class Controler {

	public final static GraphDataThreadExecutor executor = new GraphDataThreadExecutor(2);

	public static void main(String[] args) {

		final GraphicalUserInterface gui = new GraphicalUserInterface();
		gui.setVisible(true);
		

	}

}
