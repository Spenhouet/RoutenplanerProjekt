package de.dhbw.horb.routePlanner.mockUp;


import com.google.maps.googleMapsAPI.GoogleMapsProjection2;
import com.google.maps.googleMapsAPI.PointF;

import de.dhbw.horb.routePlanner.parser.GraphDataDom;

public class MockUp {

	public static void main(String[] args) {

//		GoogleMapsProjection2 gmp = new GoogleMapsProjection2();
//
//		PointF start = new PointF(49.4782247, 6.3640853);
//		PointF end = new PointF(49.4783273, 6.3640864);
//
//		System.out.println(gmp.fromLatLonToDistanceInKM(start, end));

//		List<String> names;
//		String input = "";
//
//		names = GraphDataParser.getGraphDataParser(
//				Constants.XML_NODE_HIGHWAY).containsName(input);
//
//		for (String string : names) {
//			System.out.println(string);
//		}
		
		GraphDataDom dom = new GraphDataDom();
		
		dom.addKM();

	}

}
