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
	// List<Element> listNodes;
	List<Element> listWay;
	GoogleMapsProjection2 gmp;
	XMLOutputter outp;

	public GraphDataDom() {

		gmp = new GoogleMapsProjection2();
		SAXBuilder builder = new SAXBuilder();

		outp = new XMLOutputter();
		outp.setFormat(Format.getPrettyFormat());

		try {
			xmlDoc = builder.build(new File(Constants.XML_GRAPHDATA));
			root = xmlDoc.getRootElement();
			// listNodes = root.getChildren("node");
			listWay = root.getChildren("way");

		} catch (JDOMException | IOException e) {
			e.printStackTrace();
		}
	}

	public void updateWays() {

		try {
			for (int i = 0; i < listWay.size(); i++) {
				if ((i % 1000) == 0)
					outp.output(xmlDoc, new FileOutputStream(new File(
							Constants.XML_GRAPHDATA)));

				Element elWay = (Element) (listWay.get(i));

				if (null == elWay)
					continue;

				String distance = elWay
						.getAttributeValue(Constants.WAY_DISTANCE);
				String maxspeed = elWay
						.getAttributeValue(Constants.WAY_MAXSPEED);
				String ref = elWay.getAttributeValue(Constants.WAY_REF);
				Boolean isLink = false;

				if (distance != null && maxspeed != null && ref != null)
					continue;

				List<Element> listTag = elWay.getChildren(Constants.WAY_TAG);

				isLink = isLink(listTag);

				if (!isLink && distance == null)
					elWay.setAttribute(Constants.WAY_DISTANCE,
							getDistanceFromWay(elWay).toString());

				if (!isLink && maxspeed == null)
					elWay.setAttribute(Constants.WAY_MAXSPEED,
							getMaxSpeed(listTag));

				if (!isLink && ref == null)
					elWay.setAttribute(Constants.WAY_REF, getRef(listTag));

				if (isLink) {
					deleteWay(listWay, i);
					i--;
				} else {
					deleteAllTags(listTag);
				}
			}

			outp.output(xmlDoc, new FileOutputStream(new File(
					Constants.XML_GRAPHDATA)));

		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	private void deleteAllTags(List<Element> tags) {
		while (!tags.isEmpty()) {
			tags.remove(0);
		}
	}

	private void deleteWay(List<Element> ways, int index) {
		ways.remove(index);
	}

	private Boolean isLink(List<Element> tags) {

		for (int i = 0; i < tags.size(); i++) {
			Element elTag = (Element) (tags.get(i));
			if (elTag == null)
				continue;

			if (getAttributeValueForK(elTag, Constants.WAY_HIGHWAY).equals(
					Constants.WAY_MOTORWAY_LINK))
				return true;
		}
		return false;
	}

	private String getRef(List<Element> tags) {

		for (int i = 0; i < tags.size(); i++) {
			Element elTag = (Element) (tags.get(i));
			if (elTag == null)
				continue;

			String maxspeed = getAttributeValueForK(elTag,
					Constants.WAY_MAXSPEED);

			if (maxspeed != null)
				return maxspeed;
		}
		return "";
	}

	private String getMaxSpeed(List<Element> tags) {

		for (int i = 0; i < tags.size(); i++) {
			Element elTag = (Element) (tags.get(i));
			if (elTag == null)
				continue;

			String ref = getAttributeValueForK(elTag, Constants.WAY_REF);

			if (ref != null)
				return ref;
		}
		return "";
	}

	private Double getDistanceFromWay(Element way) {

		Double km = 0.0;

		List<Element> listNode = way.getChildren(Constants.WAY_NODE);
		for (int x = 0; x < (listNode.size() - 1); x++) {
			Element elNode = (Element) (listNode.get(x));
			if (elNode == null)
				continue;

			Long id1 = Long
					.valueOf(elNode.getAttributeValue(Constants.WAY_REF));

			elNode = (Element) (listNode.get(x + 1));
			if (elNode == null)
				continue;

			Long id2 = Long
					.valueOf(elNode.getAttributeValue(Constants.WAY_REF));

			km += gmp.fromLatLonToDistanceInKM(getNode(id1), getNode(id2));
		}

		return km;
	}

	public Node getNode(Long id) {

		// for (int x = 0; x < listNodes.size(); x++) {
		// Element elNode = (Element) (listNodes.get(x));
		// if (elNode == null)
		// continue;
		//
		// if
		// (Long.valueOf(elNode.getAttributeValue(Constants.NODE_ID)).equals(id))
		// {
		// Node back = new Node(id,
		// Double.valueOf(elNode.getAttributeValue(Constants.NODE_LATITUDE)),
		// Double.valueOf(elNode.getAttributeValue(Constants.NODE_LONGITUDE)));
		// listNodes.remove(x);
		// return back;
		// }
		//
		// }

		try {
			return GraphDataParser.getGraphDataParser(Constants.XML_GRAPHDATA)
					.getNode(id);
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
