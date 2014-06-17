package de.dhbw.horb.routePlanner.parser;

import javax.xml.stream.XMLStreamException;

import de.dhbw.horb.routePlanner.ui.GraphicalUserInterface;

public class GraphDataParserMultithread {

	public void fillGUI(GraphicalUserInterface gui) {
		new Thread(new fillGUI(gui)).start();
	}

	public Thread getNodeThread(Long id) {
		return (new Thread(new getNode(id)));
	}

	public class fillGUI implements Runnable {

		private GraphicalUserInterface gui;

		private fillGUI(GraphicalUserInterface gui) {
			this.gui = gui;
		}

		@Override
		public void run() {
			try {
				GraphDataParser.getGraphDataParser(GraphDataConstants.CONST_XML_NODE_HIGHWAY).everyNodeToGui(gui);
				GraphDataParser.getGraphDataParser(GraphDataConstants.CONST_XML_WAY_HIGHWAY).everyWayToGui(gui);

			} catch (XMLStreamException e) {
				e.printStackTrace();
			}
		}
	}

	public class getNode implements Runnable {

		private Long id;

		private getNode(Long id) {
			this.id = id;
		}

		@Override
		public void run() {
			try {
				GraphDataParser.getGraphDataParser(GraphDataConstants.CONST_XML_NODE_HIGHWAY).getNode(id);

			} catch (XMLStreamException e) {
				e.printStackTrace();
			}
		}
	}

}
