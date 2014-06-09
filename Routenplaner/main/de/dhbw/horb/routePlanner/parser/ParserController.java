package de.dhbw.horb.routePlanner.parser;

import java.io.IOException;

import javax.xml.stream.XMLStreamException;

import de.dhbw.horb.routePlanner.ui.GraphicalUserInterface;

public class ParserController {
	
	
	public void fillGUI(GraphicalUserInterface gui){
		try {
			
//			TODO: Threads für je Knoten und Edge erzeugen
			new GraphDataParser().everyNodeToGui(gui);
			new GraphDataParser().everyEdgeToGui(gui);
			
		} catch (XMLStreamException | IOException e) {
			e.printStackTrace();
		}
	}
}
