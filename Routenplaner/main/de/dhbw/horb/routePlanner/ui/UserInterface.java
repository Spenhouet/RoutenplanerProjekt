package de.dhbw.horb.routePlanner.ui;

import java.io.IOException;

import javax.xml.stream.XMLStreamException;

import de.dhbw.horb.routePlanner.graphData.GraphMap;
import de.dhbw.horb.routePlanner.parser.GraphDataParser;


public class UserInterface {

	public static void main(String[] args) {
		// TODO UI oder GUI
		
		GraphMap gMap = new GraphMap();
		
		
		
		try {
			GraphDataParser parser = new GraphDataParser();
			parser.everyNode(gMap);
		} catch (XMLStreamException | IOException e) {
			e.printStackTrace();
		}
		
		
		
		
	}

}
