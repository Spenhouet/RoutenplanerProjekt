package de.dhbw.horb.routePlanner.parser;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.jdom2.Attribute;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

import de.dhbw.horb.routePlanner.Constants;
import de.dhbw.horb.routePlanner.graphData.NodeMap;
import de.dhbw.horb.routePlanner.graphData.Route;

public class JDomGraphDataCreator {

	private Document xmlDocNodes;
	private Document xmlDocRoutes;
	private XMLOutputter outp;
	private Boolean nodesXMLfileExists = false;
	private Boolean routesXMLfileExists = false;
	private Route rm;
	private DomMapNodeParser nodeMapDom;
	private DomMapWayParser wayMapDom;

	public JDomGraphDataCreator() {
		/**
		 * Loading nodes document
		 */
		File f = new File(Constants.XML_NODES);
		if (f.exists() && !f.isDirectory()) {
			nodesXMLfileExists = true;
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
		} else {
			Element nodes = new Element(Constants.NEW_NODE_S);
			xmlDocRoutes = new Document(nodes);
		}

		if (!nodesXMLfileExists || !routesXMLfileExists) {
			outp = new XMLOutputter();
			outp.setFormat(Format.getPrettyFormat());

			nodeMapDom = new DomMapNodeParser();
		}
	}

	public void createNewXMLFiles() {
		if (!nodesXMLfileExists)
			createNodeXML();

		if (!routesXMLfileExists)
			createRouteXML();
	}

	public void createRouteXML() {

		wayMapDom = new DomMapWayParser(nodeMapDom);
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

		if (nmd == null)
			return;

		rm = new Route(startID, nodeMapDom, wayMapDom);
		if (doWay(startID)) {
			Element rootNewRoute = xmlDocRoutes.getRootElement();

			Element newRoute = new Element(Constants.NEW_ROUTE);

			newRoute.setAttribute(new Attribute(Constants.NEW_ROUTE_DEPARTURENODEID, rm.getDepartureNodeID()));
			newRoute.setAttribute(new Attribute(Constants.NEW_ROUTE_DISTANCE, rm.getDistance().toString()));
			newRoute.setAttribute(new Attribute(Constants.NEW_ROUTE_DURATION, rm.getDurationInSeconds().toString()));
			newRoute.setAttribute(new Attribute(Constants.NEW_ROUTE_DESTINATIONNODEID, rm.getDestinationNodeID()));
			newRoute.setAttribute(new Attribute(Constants.NEW_ROUTE_WAYIDS, rm.getWayIDsAsCommaString()));
			String nr = rm.getNumber();
			if (nr != null)
				newRoute.setAttribute(new Attribute(Constants.NEW_ROUTE_NUMBER, nr));

			rootNewRoute.addContent(newRoute);

			try {
				outp.output(xmlDocRoutes, new FileOutputStream(Constants.XML_ROUTES));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		rm = null;
	}

	private Boolean doWay(String id) {

		if (rm == null)
			return false;

		if (rm.hadRun() && nodeMapDom.isMotorwayJunction(id)) {
			return true;
		}

		if (!rm.hadRun())
			rm.firstRun();

		List<String> ways = nodeMapDom.getWayListForNode(id);
		if (ways == null)
			return false;

		String wayID = null;
		if (ways.size() >= 1) {
			for (int i = 0; i < ways.size(); i++) {
				if (wayMapDom.isLink(ways.get(i))) {
					wayID = ways.remove(i);
					break;
				}
			}
			for (int i = -1; i < ways.size(); i++) {

				if (wayID != null) {
					String nextNodeID = wayMapDom.getNextNodeID(wayID, id);
					if (doWay(nextNodeID)) {
						rm.addNode(nextNodeID, wayID);
						return true;
					}
				}

				if (i < (ways.size() - 1))
					wayID = ways.get(i + 1);
			}
		}
		return false;
	}

	public void createNodeXML() {

		NodeMap nm = new NodeMap();
		Element rootNewNodes = xmlDocNodes.getRootElement();

		Map<String, String> nd = nodeMapDom.getMotorwayJunctionsMap();
		for (String nodeID : nd.keySet())
			nm.addNode(nd.get(nodeID), nodeID);

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
}
