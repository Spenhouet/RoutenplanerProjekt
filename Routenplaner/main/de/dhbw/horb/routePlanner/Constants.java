package de.dhbw.horb.routePlanner;

public interface Constants {

    public final static String FXML_ROOT = "RoutePlannerRoot.fxml";
    public final static String FXML_MAIN = "RoutePlannerMain.fxml";

    public final static String LINK_COMPLETELINK = "((";
    public final static String LINK_LINKEND = ");>;);out body;";
    public final static String LINK_LINKTARGET = "&target=ol_fixed";

    public final static String OVERPASS_API = "http://www.overpass-api.de/api/interpreter";

    public final static String USER_HOME = System.getProperty("user.home");
    public final static String PROGRAM_HOME = USER_HOME + System.getProperty("file.separator")
	    + "DHBW-Routenplaner 2014";
    public final static String XML_GRAPHDATA = PROGRAM_HOME + System.getProperty("file.separator") + "graphData.xml";
    public final static String XML_NODES = PROGRAM_HOME + System.getProperty("file.separator") + "nodes.xml";
    public final static String XML_ROUTES = PROGRAM_HOME + System.getProperty("file.separator") + "routes.xml";

    public final static String STARTUP_INITIALIZE = "Initialisiere...";
    public final static String STARTUP_CANCEL = "Abbrechen...";
    public final static String STARTUP_CHECK_PREREQUISITES = "Überprüfe Startvorraussetzungen...";
    public final static String STARTUP_CHECK_XML_GRAPHDATA = "Überprüfe GraphDataXML...";
    public final static String STARTUP_CHECK_XML_NODES = "Überprüfe NodeXML...";
    public final static String STARTUP_CHECK_XML_ROUTES = "Überprüfe RouteXML...";
    public final static String STARTUP_ERROR_PREREQUISITES = "Startbedingungen nicht erfüllt!";
    public final static String STARTUP_ERROR_XML_GRAPHDATA = "GraphDataXML konnte nicht erzeugt werden";
    public final static String STARTUP_ERROR_XML_NODES = "NodeXML/RouteXML konnte nicht erzeugt werden";
    public final static String STARTUP_ERROR_XML_ROUTES = "RouteXML/NodeXML konnte nicht erzeugt werden";
    public final static String STARTUP_CREATE_XML_GRAPHDATA = "GraphDataXML wird heruntergeladen und erzeugt...";
    public final static String STARTUP_CREATE_XML_NODES = "NodeXML/RouteXML wird heruntergeladen und erzeugt...";
    public final static String STARTUP_CREATE_XML_ROUTES = "RouteXML/NodeXML wird heruntergeladen und erzeugt...";

    public final static String EVALUATION_METHOD_ASTAR = "AStern";
    public final static String EVALUATION_METHOD_DIJKSTRA = "Dijkstra";
    public final static String EVALUATION_CALCULATION_DURATION = "Dauer";
    public final static String EVALUATION_CALCULATION_DISTANCE = "Strecke";

    public final static String COUNTRY_VERIFIED = "Deutschland, France";

    public final static String SETTINGS = "settings";
    public final static String SETTINGS_KVSET = "kvSet";
    public final static String SETTINGS_XML = PROGRAM_HOME + System.getProperty("file.separator") + "settings.xml";
    public final static String SETTINGS_COUNTRY = "Country";
    public final static String SETTINGS__DEFAULT_COUNTRY = "Deutschland";
    public final static String SETTINGS_EVALUATION_METHOD = "Evaluation_Method";
    public final static String SETTINGS_CALCULATION_METHOD = "Calculation_Method";

    public final static String NODE = "node";
    public final static String NODE_ID = "id";
    public final static String NODE_ID_EX = "nodeID";
    public final static String NODE_LATITUDE = "lat";
    public final static String NODE_LONGITUDE = "lon";
    public final static String NODE_TAG = "tag";
    public final static String NODE_NAME = "name";
    public final static String NODE_HIGHWAY = "highway";
    public final static String NODE_HIGHWAY_EX = "highwayNode";
    public final static String NODE_MOTORWAY_JUNCTION = "motorway_junction";

    public final static String WAY = "way";
    public final static String WAY_ID = "id";
    public final static String WAY_ID_EX = "wayID";
    public final static String WAY_NODE = "nd";
    public final static String WAY_REF = "ref";
    public final static String WAY_TAG = "tag";
    public final static String WAY_MAXSPEED = "maxspeed";
    public final static String WAY_HIGHWAY = "highway";
    public final static String WAY_HIGHWAY_EX = "highwayWay";
    public final static String WAY_MOTORWAY = "motorway";
    public final static String WAY_MOTORWAY_LINK = "motorway_link";

    public final static String NEW_NODE = "node";
    public final static String NEW_NODE_S = "nodes";
    public final static String NEW_NODE_IDS = "ids";
    public final static String NEW_NODE_ID = "id";
    public final static String NEW_NODE_NAME = "name";
    public final static int NEW_NODE_MAX_IDS = 15;

    public final static String NEW_ROUTE = "route";
    public final static String NEW_ROUTE_S = "routes";
    public final static String NEW_ROUTE_NUMBER = "nr";
    public final static String NEW_ROUTE_WAYIDS = "wayIDs";
    public final static String NEW_ROUTE_DISTANCE = "distance";
    public final static String NEW_ROUTE_DURATION = "duration";
    public final static String NEW_ROUTE_DEPARTURENODEID = "departureNodeID";
    public final static String NEW_ROUTE_DESTINATIONNODEID = "destinationNodeID";
    public final static String NEW_ROUTE_DEPARTURENODENAME = "departureNodeName";
    public final static String NEW_ROUTE_DESTINATIONNODENAME = "destinationNodeName";
}
