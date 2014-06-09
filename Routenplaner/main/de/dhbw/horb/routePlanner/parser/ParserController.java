package de.dhbw.horb.routePlanner.parser;

import javax.xml.stream.XMLStreamException;

import de.dhbw.horb.routePlanner.ui.GraphicalUserInterface;

public class ParserController {
	
	
	public void fillGUI(GraphicalUserInterface gui){
		new Thread(new fillGUI(gui)).start();
	}
			
	public class fillGUI implements Runnable{

		private GraphicalUserInterface gui;
		
		private fillGUI(GraphicalUserInterface gui){
			this.gui = gui;
		}
		
		@Override
		public void run() {
			try {
				GraphDataParser.getGraphDataParser().everyNodeToGui(gui);
				GraphDataParser.getGraphDataParser().everyEdgeToGui(gui);
				
			} catch (XMLStreamException e) {
				e.printStackTrace();
			}
		}
	}
	
	
	
	
}
