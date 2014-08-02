package de.dhbw.horb.routePlanner.evaluation.aStar;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.dhbw.horb.routePlanner.data.DomStAXMapRouteParser;
import de.dhbw.horb.routePlanner.data.StAXNodeParser;

public class AStar {

    public static void main(String[] args) {
	String start = "Offenbacher Kreuz";
	String goal = "Kreuz Hamburg-Ost";
	AStar a = new AStar(start, goal);
	a.addNighboarToList(start);
	a.findGoal();

    }

    private DomStAXMapRouteParser routeParser;
    private List<Map<String, String>> openEdge;
    private List<Map<String, String>> closedEdge;
    private String start;
    private String goal;
    private List<String> goalIDs;

    public AStar(String start, String goal) {
	openEdge = new ArrayList<Map<String, String>>();
	closedEdge = new ArrayList<Map<String, String>>();
	routeParser = new DomStAXMapRouteParser();
	this.start = start;
	this.goal = goal;
	goalIDs = StAXNodeParser.getStAXNodeParser().getIDsForName(goal);
    }

    public void addNighboarToList(String name) {

	List<String> ids = StAXNodeParser.getStAXNodeParser().getIDsForName(name);
	for (String id : ids) {
	    Map<String, String> rm = routeParser.getRoute(id);
	    if (rm == null)
		continue;

	    Map<String, String> edge = new HashMap<String, String>();

	    edge.put("A", id);
	    edge.put("B", rm.get("destinationNodeID"));
	    edge.put("G", rm.get("duration"));

	    if (goalIDs.contains(edge.get("B")))
		System.out.println("Hell year");

	    if (containsID(closedEdge, edge.get("B")))
		continue;

	    openEdge.add(edge);
	}
    }

    public void findGoal() {
	Map<String, String> mp = getSmallest();
	openEdge.remove(mp);
	closedEdge.add(mp);
	addNighboarToList(StAXNodeParser.getStAXNodeParser().getNameForID(mp.get("B")));
	if (!openEdge.isEmpty())
	    findGoal();
    }

    public Boolean containsID(List<Map<String, String>> edge, String id) {
	for (Map<String, String> mp : edge)
	    if (mp.get("B").equals(id))
		return true;
	return false;
    }

    public Map<String, String> getSmallest() {
	Double weight = null;

	Map<String, String> rMp = null;
	for (Map<String, String> mp : openEdge) {
	    Double nw = Double.valueOf(mp.get("G"));
	    if (weight == null || nw < weight) {
		rMp = mp;
		weight = nw;
	    }
	}
	return rMp;
    }
}
