package de.dhbw.horb.routePlanner;

public interface Constants {

	public final static String FXML_ROOT = "RoutePlannerRoot.fxml";
	public final static String FXML_MAIN = "RoutePlannerMain.fxml";
	
	public final static String XML_NODE_HIGHWAY = "xmlFile/node_highway.xml";
	public final static String XML_WAY_HIGHWAY = "xmlFile/way_highway.xml";
	public final static String XML_EDGE = "xmlFile/edge.xml";

	public final static String NODE = "node";
	public final static String NODE_ID = "id";
	public final static String NODE_LATITUDE = "lat";
	public final static String NODE_LONGITUDE = "lon";
	
	public final static String NODE_TAG_NAME = "name";

	public final static String WAY = "way";
	public final static String WAY_ID = "id";
	public final static String WAY_NODE = "nd";
	public final static String WAY_REF = "ref";
	public final static String WAY_TAG = "tag";
	public final static String WAY_HIGHWAY = "highway";
	public final static String WAY_MOTORWAY_JUNCTION = "motorway_junction";
	public final static String WAY_EXIT_TO = "exit_to";
	
	public final static String EDGE = "edge";
	public final static String EDGE_NODE = "node";
	public final static String EDGE_ID = "id";
	public final static String EDGE_LATITUDE = "lat";
	public final static String EDGE_LONGITUDE = "lon";
	

}
