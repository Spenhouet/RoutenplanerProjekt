package de.dhbw.horb.routePlanner.parser;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;

import de.dhbw.horb.routePlanner.Constants;

public class StAXNodeParser {

	public static StAXNodeParser getStAXNodeParser() {
		try {
			return new StAXNodeParser(Constants.XML_NODES);
		} catch (XMLStreamException | FileNotFoundException e) {
			e.printStackTrace();
			return null;
		}
	}

	private GraphDataStreamReader graphSR;

	private StAXNodeParser(String xmlFile) throws FileNotFoundException, XMLStreamException {

		XMLInputFactory factory = XMLInputFactory.newInstance();
		graphSR = new GraphDataStreamReader(factory.createXMLStreamReader(new FileInputStream(xmlFile)));
	}

	public List<String> containsName(String name) {

		List<String> names = new ArrayList<String>();

		try {
			while (graphSR.nextStartElement()) {
				if (!graphSR.isNode())
					continue;
				String v = graphSR.getAttributeValue(Constants.NEW_NODE_NAME);

				if (v.toLowerCase().contains(name.toLowerCase()))
					names.add(v);
			}

		} catch (NumberFormatException | XMLStreamException e) {
			e.printStackTrace();
		}

		return names;
	}
}
