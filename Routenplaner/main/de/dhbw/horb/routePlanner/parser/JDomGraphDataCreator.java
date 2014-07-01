package de.dhbw.horb.routePlanner.parser;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.jdom2.Attribute;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

import de.dhbw.horb.routePlanner.Constants;
import de.dhbw.horb.routePlanner.graphData.Node;
import de.dhbw.horb.routePlanner.graphData.NodeMap;
import de.dhbw.horb.routePlanner.graphData.Route;

public class JDomGraphDataCreator {

	Document xmlDocGraphData;
	Document xmlDocNodes;
	Document xmlDocRoutes;
	Element rootGraphData;
	List<Element> listNodes;
	List<Element> listWay;
	XMLOutputter outp;
	Boolean nodesXMLfileExists = false;
	Boolean routesXMLfileExists = false;

	public JDomGraphDataCreator() {
		try {
			SAXBuilder builder = new SAXBuilder();

			/**
			 * Loading nodes document
			 */
			File f = new File(Constants.XML_NODES);
			if (f.exists() && !f.isDirectory()) {
				nodesXMLfileExists = true;
				// xmlDocNodes = builder.build(f);
			} else {
				Element nodes = new Element(Constants.NEW_NODE_S);
				xmlDocNodes = new Document(nodes);
			}

			/**
			 * Loading routes document
			 */
			f = new File(Constants.XML_ROUTES);
			if (f.exists() && !f.isDirectory()) {
				routesXMLfileExists = true;
				// xmlDocRoutes = builder.build(f);
			} else {
				Element nodes = new Element(Constants.NEW_NODE_S);
				xmlDocRoutes = new Document(nodes);
			}

			if (!nodesXMLfileExists || !routesXMLfileExists) {
				outp = new XMLOutputter();
				outp.setFormat(Format.getPrettyFormat());

				/**
				 * Loading graphData document and a list of nodes and ways.
				 */
				xmlDocGraphData = builder.build(new File(Constants.XML_GRAPHDATA));
				rootGraphData = xmlDocGraphData.getRootElement();
				listNodes = rootGraphData.getChildren(Constants.NODE);
				listWay = rootGraphData.getChildren("way");
			}

		} catch (JDOMException | IOException e) {
			e.printStackTrace();
		}
	}

	public void createNewXMLFiles() {
		if (!nodesXMLfileExists)
			createNodeXML();

		if (!routesXMLfileExists)
			createRouteXML();
	}

	public void createRouteXML() {

		StAXNodeParser np = StAXNodeParser.getStAXNodeParser();

		while (np.hasNext()) {

			List<String> ids = np.getNextNodeIDs();
			if (ids == null)
				continue;
			for (String id : ids) {
				createRoute(id);
			}
		}
	}

	private void createRoute(String startID) {

		Route rm = new Route(removeNode(startID));

		Element elWay = getWayContainsNodeID(startID);

		// Node nd = removeNode(startID);
	}

	private Element getWayContainsNodeID(String id) {

		for (int x = 0; x < listWay.size(); x++) {
			Element elWay = (Element) (listWay.get(x));
			if (elWay == null)
				continue;

			if (isLink(elWay))
				listWay.remove(x);

			List<Element> listNode = elWay.getChildren(Constants.WAY_NODE);
			for (int y = 0; y < listNode.size(); y++)
				if (listNode.get(y).getAttributeValue(Constants.WAY_REF).trim().equals(id))
					return elWay;
		}

		return null;
	}

	private Boolean isLink(Element elWay) {
		List<Element> tags = elWay.getChildren(Constants.WAY_TAG);
		for (int i = 0; i < tags.size(); i++) {
			Element elTag = (Element) (tags.get(i));
			if (elTag == null)
				continue;
			String highway = getAttributeValueForK(elTag, Constants.WAY_HIGHWAY);
			if (highway != null && highway.equals(Constants.WAY_MOTORWAY_LINK))
				return true;
		}
		return false;
	}

	private Node removeNode(String id) {

		for (int x = 0; x < listNodes.size(); x++) {
			Element elNode = (Element) (listNodes.get(x));
			if (elNode == null)
				continue;

			if (elNode.getAttributeValue(Constants.NODE_ID).equals(id)) {
				Node back = new Node(id, Double.valueOf(elNode.getAttributeValue(Constants.NODE_LATITUDE)),
						Double.valueOf(elNode.getAttributeValue(Constants.NODE_LONGITUDE)));

				if (getAttributeValueForK(elNode, Constants.NODE_TAG_NAME) == null)
					listNodes.remove(x);

				return back;
			}
		}
		return null;
	}

	public void createNodeXML() {

		NodeMap nm = new NodeMap();
		Element rootNewNodes = xmlDocNodes.getRootElement();

		for (int i = 0; i < listNodes.size(); i++) {
			Element node = (Element) listNodes.get(i);
			if (node == null)
				continue;
			List<Element> listTags = node.getChildren(Constants.WAY_TAG);
			for (int x = 0; x < listTags.size(); x++) {
				Element tag = (Element) listTags.get(x);
				String name = getAttributeValueForK(tag, Constants.NODE_TAG_NAME);
				if (name != null)
					nm.addNode(name, node.getAttributeValue(Constants.NODE_ID));
			}
		}

		while (nm.hasNode()) {
			Map<String, String> nodeMap = nm.removeNode();
			Element newNode = new Element(Constants.NODE);
			newNode.setAttribute(new Attribute(Constants.NEW_NODE_NAME, nodeMap.get(Constants.NEW_NODE_NAME)));
			newNode.setAttribute(new Attribute(Constants.NEW_NODE_IDS, nodeMap.get(Constants.NEW_NODE_ID)));
			rootNewNodes.addContent(newNode);
		}

		try {
			outp.output(xmlDocNodes, new FileOutputStream(Constants.XML_NODES));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private String getAttributeValueForK(Element el, String k) {
		String v = el.getAttributeValue("k");
		if (v != null && v.trim().equals(k))
			return el.getAttributeValue("v");
		return null;
	}
}
