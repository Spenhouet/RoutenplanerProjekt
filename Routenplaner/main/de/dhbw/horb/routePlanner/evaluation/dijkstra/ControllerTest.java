package de.dhbw.horb.routePlanner.evaluation.dijkstra;

public class ControllerTest {

    public static void main(String[] args) {

	String startnode = "Kreuz Meerbusch (27)";
	String endnode = "Marl-Sinsen (10)";
	String calcMethod = "Dauer";

	Dijkstra dijkstra = new Dijkstra(startnode, endnode);

	dijkstra.calculateRoute(calcMethod);
	dijkstra.printNodes();

    }
}
