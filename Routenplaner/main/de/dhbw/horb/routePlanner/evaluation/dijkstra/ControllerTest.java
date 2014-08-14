package de.dhbw.horb.routePlanner.evaluation.dijkstra;

public class ControllerTest {

    public static void main(String[] args) {

	String startnode = "Berlin-Marzahn (3)";
	String endnode = "Brandenburg (78)";

	//	String startnode = "Kreuz Stuttgart (51)";
	//	String endnode = "Berlin-Spandau (26)";

	Dijkstra dijkstra = new Dijkstra(startnode, endnode);
	dijkstra.calculateRoute("Dauer");
	dijkstra.printNodes();
    }
}
