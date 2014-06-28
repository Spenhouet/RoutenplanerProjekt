package de.dhbw.horb.routePlanner.parser;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import javax.xml.stream.XMLStreamException;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

import com.google.maps.googleMapsAPI.GoogleMapsProjection2;

import de.dhbw.horb.routePlanner.Constants;
import de.dhbw.horb.routePlanner.graphData.Node;

public class GraphDataDom {

	Document xmlDoc;
	Element root;
	List<Element> listNodes;
	List<Element> listWay;
	GoogleMapsProjection2 gmp;
	XMLOutputter outp;

	Document xmlNodeDoc;

	public GraphDataDom() {

		gmp = new GoogleMapsProjection2();
		SAXBuilder builder = new SAXBuilder();

		outp = new XMLOutputter();
		outp.setFormat(Format.getPrettyFormat());

		try {
			xmlDoc = builder.build(new File(Constants.XML_GRAPHDATA));
			root = xmlDoc.getRootElement();
			listNodes = root.getChildren("node");
			listWay = root.getChildren("way");

		} catch (JDOMException | IOException e) {
			e.printStackTrace();
		}
	}

	public void addKM() {

		try {

			for (int i = 0; i < listWay.size(); i++) {
				if ((i % 1000) == 0)
					outp.output(xmlDoc, new FileOutputStream(new File(Constants.XML_GRAPHDATA)));

				Element elWay = (Element) (listWay.get(i));
				if (null == elWay)
					continue;

				if (elWay.getAttributeValue(Constants.WAY_DISTANCE) != null
						&& elWay.getAttributeValue(Constants.WAY_MAXSPEED) != null
						&& elWay.getAttributeValue(Constants.WAY_REF) != null)
					continue;

				Double km = 0.0;

				List<Element> listNode = elWay.getChildren(Constants.WAY_NODE);
				for (int x = 0; x < (listNode.size() - 1); x++) {
					Element elNode = (Element) (listNode.get(x));
					if (elNode == null)
						continue;

					Long id1 = Long.valueOf(elNode.getAttributeValue(Constants.WAY_REF));

					elNode = (Element) (listNode.get(x + 1));
					if (elNode == null)
						continue;

					Long id2 = Long.valueOf(elNode.getAttributeValue(Constants.WAY_REF));

					km += gmp.fromLatLonToDistanceInKM(getNode(id1), getNode(id2));

				}

				String maxspeed = "";
				String ref = "";
				Boolean nextWay = false;

				List<Element> listTag = elWay.getChildren(Constants.WAY_TAG);
				while (!listTag.isEmpty()) {
					Element elTag = (Element) (listTag.remove(0));
					if (elTag == null)
						continue;

					if (getAttributeValueForK(elTag, Constants.WAY_HIGHWAY).trim().equals(Constants.WAY_MOTORWAY_LINK)) {
						listWay.remove(i);
						nextWay = true;
						break;
					}

					if (maxspeed == null || maxspeed == "")
						maxspeed = getAttributeValueForK(elTag, Constants.WAY_MAXSPEED);
					if (ref == null || ref == "")
						ref = getAttributeValueForK(elTag, Constants.WAY_REF);
				}

				if (nextWay)
					continue;

				if (elWay.getAttributeValue(Constants.WAY_DISTANCE) == null)
					elWay.setAttribute(Constants.WAY_DISTANCE, km.toString());

				if (elWay.getAttributeValue(Constants.WAY_MAXSPEED) == null)
					elWay.setAttribute(Constants.WAY_MAXSPEED, maxspeed);

				if (elWay.getAttributeValue(Constants.WAY_REF) == null)
					elWay.setAttribute(Constants.WAY_REF, ref);

			}

			outp.output(xmlDoc, new FileOutputStream(new File(Constants.XML_GRAPHDATA)));

		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public Node getNode(Long id) {

//		for (int x = 0; x < listNodes.size(); x++) {
//			Element elNode = (Element) (listNodes.get(x));
//			if (elNode == null)
//				continue;
//
//			if (Long.valueOf(elNode.getAttributeValue(Constants.NODE_ID)).equals(id)) {
//				Node back = new Node(id, Double.valueOf(elNode.getAttributeValue(Constants.NODE_LATITUDE)),
//						Double.valueOf(elNode.getAttributeValue(Constants.NODE_LONGITUDE)));
//				listNodes.remove(x);
//				return back;
//			}
//
//		}
		
		try {
			return GraphDataParser.getGraphDataParser(Constants.XML_NODE_HIGHWAY).getNode(id);
		} catch (XMLStreamException e) {
			e.printStackTrace();
		}
		
		return null;
	}

	private String getAttributeValueForK(Element el, String k) {
		if (el.getAttributeValue("k").trim().equals(k))
			return el.getAttributeValue("v");
		return "";
	}
}
