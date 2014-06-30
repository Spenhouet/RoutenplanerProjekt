package de.dhbw.horb.routePlanner.mockUp;


import de.dhbw.horb.routePlanner.Constants;
import de.dhbw.horb.routePlanner.parser.JDomGraphDataCreator;
import de.dhbw.horb.routePlanner.parser.GraphDataParser;

public class MockUp {

	public static void main(String[] args) {

		
//		GraphDataDom dom = new GraphDataDom();
//		
//		dom.updateWays();
		
		
		GraphDataParser.getGraphDataParser(Constants.XML_GRAPHDATA).test();

	}

}
