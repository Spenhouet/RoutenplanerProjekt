package de.dhbw.horb.routePlanner.data;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;

import de.dhbw.horb.routePlanner.Constants;

public class DomStAXMapRouteParser {

    private Map<String, Map<String, String>> routes;

    public DomStAXMapRouteParser() {

	XMLInputFactory factory = XMLInputFactory.newInstance();
	try {
	    GraphDataStreamReader routeSR = new GraphDataStreamReader(
		    factory.createXMLStreamReader(new FileInputStream(Constants.XML_ROUTES)));

	    routes = new HashMap<String, Map<String, String>>();

	    while (routeSR.hasNext()) {
		if (!routeSR.nextStartElement() || !routeSR.isRoute())
		    continue;

		Map<String, String> values = new HashMap<String, String>();

		String departureNodeID = null;

		for (int x = 0; x < routeSR.getAttributeCount(); x++) {

		    String key = routeSR.getAttributeLocalName(x);
		    String value = routeSR.getAttributeValue(x);
		    if (key == null || value == null)
			continue;

		    if (key.equals(Constants.NEW_ROUTE_DEPARTURENODEID))
			departureNodeID = value;

		    values.put(key, value);
		}

		if (departureNodeID == null || values == null)
		    continue;

		routes.put(departureNodeID, values);
	    }

	} catch (FileNotFoundException | XMLStreamException e) {
	    e.printStackTrace();
	}
    }

    public Map<String, String> getRoute(String departureNodeID) {

	return routes.get(departureNodeID);
    }
}