package de.dhbw.horb.routePlanner.mockUp;


import de.dhbw.horb.routePlanner.parser.JDomGraphDataCreator;

public class MockUp {

	public static void main(String[] args) {
		
		
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				JDomGraphDataCreator dom = new JDomGraphDataCreator();
				dom.createNewXMLFiles();
				
				
			}
		}).start();
		
		
	}

}
