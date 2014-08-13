package de.dhbw.horb.routePlanner.evaluation.dijkstra;

public class ControllerTest {

    public static void main(String[] args) {

	String startnode = "Berlin-Marzahn (3)";
	String endnode = "Horb am Neckar (30)";
	String calcMethod = "Dauer";

	Dijkstra dijkstra = new Dijkstra(startnode, endnode);

	dijkstra.calculateRoute(calcMethod);
	dijkstra.printNodes();

    }
}
