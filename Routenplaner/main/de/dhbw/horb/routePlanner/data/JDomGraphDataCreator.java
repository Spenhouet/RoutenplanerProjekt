package de.dhbw.horb.routePlanner.data;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
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
	private Route rm;
	private DomStAXMapGraphDataNodesParser nodeMapDom;
	private DomStAXMapGraphDataWaysParser wayMapDom;

	public JDomGraphDataCreator() {

		Element nodes = new Element(Constants.NEW_NODE_S);
		xmlDocNodes = new Document(nodes);

		Element routes = new Element(Constants.NEW_ROUTE_S);
		xmlDocRoutes = new Document(routes);

		outp = new XMLOutputter();
		outp.setFormat(Format.getPrettyFormat());

		nodeMapDom = new DomStAXMapGraphDataNodesParser();

	}

	public void createNewXMLFiles() {
		createNodeXML();
		createRouteXML();
	}

	public void createRouteXML() {

		wayMapDom = new DomStAXMapGraphDataWaysParser(nodeMapDom);
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

		routeIDs = new HashMap<String, String>();

		rm = new Route(startID, nodeMapDom, wayMapDom);
		if (doNextNode(startID)) {
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

	private Map<String, String> routeIDs;

	private Boolean doNextNode(String id) {

		if (rm == null || id == null)
			return false;

		if (routeIDs != null && routeIDs.containsKey(id))
			return false;

		if (rm.hadRun() && nodeMapDom.isMotorwayJunction(id)) {
			if (id.equals(rm.getDepartureNodeID())) {
				return false;
			}
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
					routeIDs.put(id, null);
					if (doNextNode(nextNodeID)) {
						rm.addNode(nextNodeID, wayID);
						return true;
					} else {
						routeIDs.remove(id);
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
