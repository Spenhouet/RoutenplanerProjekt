package de.dhbw.horb.routePlanner.parser;

import javax.xml.stream.XMLStreamException;

import de.dhbw.horb.routePlanner.ui.GraphicalUserInterface;

public class GraphDataParserMultithread {

	public void fillGUI(GraphicalUserInterface gui) {
		final GraphicalUserInterface guiMT = gui;
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				try {
					GraphDataParser.getGraphDataParser(GraphDataConstants.CONST_XML_WAY_HIGHWAY).everyWayToGui(guiMT);
				} catch (XMLStreamException e) {
					e.printStackTrace();
				}
			}
		}).start();
	}
}
