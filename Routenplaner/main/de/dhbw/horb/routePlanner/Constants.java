package de.dhbw.horb.routePlanner;

/**
 * Auslagerung der verwendeten festen Werte um diese zentral und leicht änderbar zu halten und den Überblick zu
 * bewahren.
 */
public interface Constants {

    public final static String FXML_ROOT = "RoutePlannerRoot.fxml";
    public final static String FXML_MAIN = "RoutePlannerMain.fxml";
    public final static String FXML_ROOT_STARTUP = "StartupRoot.fxml";
    public final static String FXML_MAIN_STARTUP = "StartupMain.fxml";

    public final static String LINK_COMPLETELINK = "((";
    public final static String LINK_LINKEND = ");>;);out body;";

    public final static String OVERPASS_API = "http://www.overpass-api.de/api/interpreter";
    public final static String OVERPASS_DE = "overpass-api.de";

    public final static String USER_HOME = System.getProperty("user.home");
    public final static String PROGRAM_HOME = USER_HOME + System.getProperty("file.separator")
	    + "DHBW-Routenplaner-2014";
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
    public final static String STARTUP_ERROR_FOLDER = "Programm-Ordner konnte nicht erstellt werden!";
    public final static String STARTUP_ERROR_INTERNET = "Es besteht keine Verbindung zu den Overpass-Servern!";
    public final static String STARTUP_ERROR_XML_GRAPHDATA = "GraphDataXML konnte nicht heruntergeladen werden";
    public final static String STARTUP_ERROR_XML_NODES = "NodeXML/RouteXML konnte nicht erzeugt werden";
    public final static String STARTUP_ERROR_XML_ROUTES = "RouteXML/NodeXML konnte nicht erzeugt werden";
    public final static String STARTUP_CREATE_XML_GRAPHDATA = "GraphDataXML wird heruntergeladen...";
    public final static String STARTUP_CREATE_XML_NODES = "NodeXML/RouteXML wird erzeugt...";
    public final static String STARTUP_CREATE_XML_ROUTES = "RouteXML/NodeXML wird erzeugt...";

    public final static String EVALUATION_METHOD_ASTAR = "AStern";
    public final static String EVALUATION_METHOD_DIJKSTRA = "Dijkstra";
    public final static String EVALUATION_CALCULATION_DURATION = "Dauer";
    public final static String EVALUATION_CALCULATION_DISTANCE = "Strecke";

    public final static String COUNTRY_VERIFIED = "Deutschland, Österreich, Nederland";

    public final static String SETTINGS = "settings";
    public final static String SETTINGS_KVSET = "kvSet";
    public final static String SETTINGS_XML = PROGRAM_HOME + System.getProperty("file.separator") + "settings.xml";
    public final static String SETTINGS_COUNTRY = "Country";
    public final static String SETTINGS__DEFAULT_COUNTRY = "Deutschland";
    public final static String SETTINGS_EVALUATION_METHOD = "Evaluation_Method";
    public final static String SETTINGS_CALCULATION_METHOD = "Calculation_Method";
    public final static String SETTINGS_COLOR_WAYS = "Color_Ways";
    public final static String SETTINGS_COLOR_WAYS_DEFAULT = "0x0000ffff";
    public final static String SETTINGS_COLOR_NODES = "Color_Nodes";
    public final static String SETTINGS_COLOR_NODES_DEFAULT = "0xff0000ff";

    public final static String NODE = "node";
    public final static String NODE_ID = "id";
    public final static String NODE_ID_EX = "nodeID";
    public final static String NODE_REF = "ref";
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

    public final static String TOOLTIP_CLOSE_BUTTON = "Dieser Button beendet das Programm";
    public final static String TOOLTIP_INFO_BUTTON = "Dieser Button zeigt Informationen über das Programm";
    public final static String TOOLTIP_CALCULATE_ROUTE_BUTTON = "Dieser Button löst die Berechnung der Route aus.";
    public final static String TOOLTIP_UPDATE_DATA_BUTTON = "Dieser Button löst ein Update der dem Programm zugrunde "
	    + "liegenden Daten aus. Dieser Vorgang kann sehr lange dauern!";
    public final static String TOOLTIP_START_COMBOBOX = "Geben Sie hier bitte den Startknoten Ihrer Route ein.";
    public final static String TOOLTIP_TARGET_COMBOBOX = "Geben Sie hier bitte den Zielknoten Ihrer Route ein.";
    public final static String TOOLTIP_COUNTRY_COMBOBOX = "Hier können Sie das Land auswählen, in dem Sie eine Route "
	    + "berechnen möchten. Um das Land zu wechseln muss das Programm neu geladen werden.";
    public final static String TOOLTIP_WAYS_COLORPICKER = "Hiermit können Sie die Farbe ändern, in der Wege markiert "
	    + "werden. Nach einer Änderung muss erneut eine Route berechnet werden, um den Effekt zu sehen.";
    public final static String TOOLTIP_NODES_COLORPICKER = "Hiermit können Sie die Farbe ändern, in der Knoten "
	    + "markiert werden. Nach einer Änderung muss erneut eine Route berechnet werden, um den Effekt zu sehen.";
    public final static String TOOLTIP_FASTEST_ROUTE_RADIO = "Setzten Sie den Haken hier, wenn Sie die schnellste "
	    + "Route berechnet haben möchten.";
    public final static String TOOLTIP_SHORTEST_ROUTE_RADIO = "Setzten Sie den Haken hier, wenn Sie die kürzeste "
	    + "Route berechnet haben möchten.";
    public final static String TOOLTIP_DIJKSTRA_ROUTE_RADIO = "Wenn Sie eine Route mittels Dijkstra berechnen "
	    + "möchten, setzten Sie den Haken hier.";
    public final static String TOOLTIP_ASTAR_ROUTE_RADIO = "Wenn Sie eine Route mittels AStern berechnen möchten, "
	    + "setzten Sie den Haken hier.";

    public final static String ROUTEPLANNER_INFO_STRING = "Dieses Java-Programm entstand im Rahmen der Vorlesung "
	    + "\"Programmieren I + II\" an der DHBW Stuttgart Campus Horb. \n\n\u00a9 2014 Julius Mahlenbrey, "
	    + "Simon Stehle, Sebastian Penhouet und Robin Kinting";
    public final static String ROUTEPLANNER_POPUP_COLOR_CHANGED = "Sie müssen erneut eine Route berechnen lassen, "
	    + "damit diese Änderung wirksam wird.";
    public final static String ROUTEPLANNER_POPUP_DATA_UPDATE = "Das Aktualisieren der den Berechnungen zugrunde "
	    + "liegenden Daten kann sehr lange dauern. Trotzdem aktualisieren?";

}
