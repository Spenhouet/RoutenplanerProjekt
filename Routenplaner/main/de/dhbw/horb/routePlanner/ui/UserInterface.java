package de.dhbw.horb.routePlanner.ui;

import de.dhbw.horb.routePlanner.parser.SAXGraphParser;

public class UserInterface {

	public static void main(String[] args) {
		// TODO Automatisch generierter Methodenstub
		
		SAXGraphParser parser = new SAXGraphParser();
		
		String[] args1 = new String[5];
		
		args1[0] = "file/graph_bab.xml";
		args1[1] = "Node";
		args1[2] = "Title";
		args1[3] = "Mein zweiter Button";
		args1[4] = "Comment";
		
		parser.main(args1);
		
	}

}
