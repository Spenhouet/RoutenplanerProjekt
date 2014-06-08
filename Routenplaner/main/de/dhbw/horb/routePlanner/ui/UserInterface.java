package de.dhbw.horb.routePlanner.ui;

import de.dhbw.horb.routePlanner.parser.GraphDataParser;


public class UserInterface {

	public static void main(String[] args) {
		// TODO UI oder GUI
		GraphDataParser parser = new GraphDataParser();
		parser.search();
	}

}
