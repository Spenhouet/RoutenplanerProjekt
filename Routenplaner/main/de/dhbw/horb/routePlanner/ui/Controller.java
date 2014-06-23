package de.dhbw.horb.routePlanner.ui;

import de.dhbw.horb.routePlanner.parser.GraphDataParserMultithread;
import de.dhbw.horb.routePlanner.parser.GraphDataThreadExecutor;

public class Controller {

	public final static GraphDataThreadExecutor executor = new GraphDataThreadExecutor();

	public static void main(String[] args) {

		final GraphicalUserInterface gui = new GraphicalUserInterface();
		gui.getStringName();

	}

}
