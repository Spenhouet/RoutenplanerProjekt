package de.dhbw.horb.routePlanner.ui;

import java.io.IOException;

import javax.xml.stream.XMLStreamException;

import de.dhbw.horb.routePlanner.parser.GraphDataParser;


public class Controller {

	public static void main(String[] args) {
		try {
			
			GraphicalUserInterface gui = new GraphicalUserInterface();
			  
		
		
			GraphDataParser parser = new GraphDataParser();
			parser.everyNode(gui);
			
			
			
			
			
		} catch (XMLStreamException | IOException e) {
			e.printStackTrace();
		}
	}

}
