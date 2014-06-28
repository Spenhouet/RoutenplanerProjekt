package de.dhbw.horb.routePlanner;

public interface Constants {

	public final static String FXML_ROOT = "RoutePlannerRoot.fxml";
	public final static String FXML_MAIN = "RoutePlannerMain.fxml";

	public final static String LINK_LINKSTART = "http://overpass-api.de/api/convert?data=";
	public final static String LINL_COMPLETELINK = "((";
	public final static String LINK_LINKEND = ");>;);out;";
	public final static String LINK_LINKTARGET = "&target=ol_fixed";
	
	public final static String XML_GRAPHDATA = "xmlFile/graphData.xml";
	public final static String XML_GRAPHDATA_NEW = "xmlFile/graphDataNew.xml";

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
	public final static String WAY_DISTANCE = "distance";
	public final static String WAY_MAXSPEED = "maxspeed";
	public final static String WAY_HIGHWAY = "highway";
	public final static String WAY_MOTORWAY_JUNCTION = "motorway_junction";
	public final static String WAY_MOTORWAY_LINK = "motorway_link";
	public final static String WAY_EXIT_TO = "exit_to";
	
	public final static String EDGE = "edge";
	public final static String EDGE_NODE = "node";
	public final static String EDGE_ID = "id";
	public final static String EDGE_LATITUDE = "lat";
	public final static String EDGE_LONGITUDE = "lon";
	

}
