package de.dhbw.horb.routePlanner.parser;

import javax.xml.stream.XMLStreamException;

public class GraphDataParserMultithread {

	public void writeEdgeXML() {
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				try {
					GraphDataParser.getGraphDataParser(GraphDataConstants.CONST_XML_WAY_HIGHWAY).writeEdgeXML();
				} catch (XMLStreamException e) {
					e.printStackTrace();
				}
			}
		}).start();
	}
}
