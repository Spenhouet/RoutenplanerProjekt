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
import de.dhbw.horb.routePlanner.graphData.NodeMap;
import de.dhbw.horb.routePlanner.graphData.Route;

public class JDomGraphDataCreator {

	private Document xmlDocGraphData;
	private Document xmlDocNodes;
	private Document xmlDocRoutes;
	private Element rootGraphData;
	private List<Element> listNodes;
	private List<Element> listWay;
	private XMLOutputter outp;
	private Boolean nodesXMLfileExists = false;
	private Boolean routesXMLfileExists = false;
	private Route rm;
	private DomMapNodeParser nodeMapDom;

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

		nodeMapDom = new DomMapNodeParser();
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

		Map<String, String> nmd = nodeMapDom.getNode(startID);

		if(nmd == null) return;

		rm = new Route(startID, Double.valueOf(nmd.get(Constants.NODE_LATITUDE)),
				Double.valueOf(nmd.get(Constants.NODE_LONGITUDE)));
		doWay(startID);

		Element rootNewRoute = xmlDocRoutes.getRootElement();

		Element newRoute = new Element(Constants.NEW_ROUTE);

		newRoute.setAttribute(new Attribute(Constants.NEW_ROUTE_DEPARTURENODEID, rm.getDepartureNodeID()));
		newRoute.setAttribute(new Attribute(Constants.NEW_ROUTE_DISTANCE, rm.getDistance().toString()));
		newRoute.setAttribute(new Attribute(Constants.NEW_ROUTE_DURATION, rm.getDurationInSeconds().toString()));
		newRoute.setAttribute(new Attribute(Constants.NEW_ROUTE_DESTINATIONNODEID, rm.getDestinationNodeID()));
		newRoute.setAttribute(new Attribute(Constants.NEW_ROUTE_WAYIDS, rm.getWayIDsAsCommaString()));
		// newRoute.setAttribute(new Attribute(Constants.NEW_ROUTE_NUMBER, rm
		// .getNumber()));

		rootNewRoute.addContent(newRoute);

		try {
			outp.output(xmlDocRoutes, new FileOutputStream(Constants.XML_ROUTES));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void doWay(String id) {

		if (rm == null)
			return;

		String lastID = null;
		Boolean rightWay = false;
		Element elWay = null;
		for (int x = 0; x < listWay.size(); x++) {
			elWay = (Element) (listWay.get(x));
			if (elWay == null)
				continue;

			List<Element> listNode = elWay.getChildren(Constants.WAY_NODE);
			for (int y = 0; y < listNode.size(); y++) {
				
				String ndID = listNode.get(y).getAttributeValue(Constants.WAY_REF);
				if (ndID != null && ndID.trim().equals(id)){
					rightWay = true;
					
					if (y == 0) {
						listWay.remove(x);
						x--;
					}
					
					continue;
				}

				if (!rightWay)
					continue;

				Map<String, String> nmd = nodeMapDom.getNode(ndID);

				if(nmd == null) continue;
				
				lastID = ndID;
				rm.addNode(ndID, Double.valueOf(nmd.get(Constants.NODE_LATITUDE)),
						Double.valueOf(nmd.get(Constants.NODE_LONGITUDE)));

			}
			if (rightWay)
				break;
		}

		if (rightWay && lastID != null && elWay != null) {

			String wayID = elWay.getAttributeValue(Constants.WAY_ID);
			String nr = getAttributeValueForK(elWay, Constants.WAY_REF);
			String maxspeed = getAttributeValueForK(elWay, Constants.WAY_MAXSPEED);
			if (maxspeed == null || maxspeed == "" || maxspeed == "none")
				maxspeed = "120";

			int speed = Integer.valueOf(maxspeed);

			rm.finalizeWay(wayID, nr, speed);
			doWay(lastID);

		}

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
