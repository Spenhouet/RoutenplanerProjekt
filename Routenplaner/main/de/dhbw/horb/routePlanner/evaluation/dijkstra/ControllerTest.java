package de.dhbw.horb.routePlanner.evaluation.dijkstra;

public class ControllerTest {

    public static void main(String[] args) {

	String startnode = "Möckmühl (7)";
	String endnode = "Möckmühl (7)";
	String calcMethod = "Dauer";

	Dijkstra dijkstra = new Dijkstra(startnode, endnode);

	dijkstra.calculateRoute(calcMethod);
	dijkstra.printNodes();

    }
}