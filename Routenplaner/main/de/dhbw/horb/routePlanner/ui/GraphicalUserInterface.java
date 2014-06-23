package de.dhbw.horb.routePlanner.ui;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import de.dhbw.horb.routePlanner.graphData.Edge;
import de.dhbw.horb.routePlanner.graphData.Node;
import de.dhbw.horb.routePlanner.parser.GraphDataConstants;
import de.dhbw.horb.routePlanner.parser.GraphDataParser;
import de.dhbw.horb.routePlanner.parser.GraphDataParserMultithread;

public class GraphicalUserInterface {

	private Long edgeCount;
	private Date date;
	private Long duration;

	public GraphicalUserInterface() {
		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		setTime();
		edgeCount = 0L;

		// TODO Robin & Julius

		new GraphDataParserMultithread().fillGUI(this);
		// this.autofillComboBox();

		// System.out.println(dateFormat.format(date));

	}

	public void addEdge(Edge newEdge) {
		// TODO Robin & Julius

		edgeCount++;

		if (edgeCount % 1000 == 0) {

			printTimeDuration();
		}

		// Node start = newEdge.getStartNode();
		// Node end = newEdge.getEndNode();
		// System.out.println(edgeCount + ". Start Node ID: " + start.getID()
		// + " mit Breitengrad: " + start.getLatitude()
		// + " mit Längengrad: " + start.getLongitude() + " End Node ID: "
		// + end.getID() + " mit Breitengrad: " + end.getLatitude()
		// + " mit Längengrad: " + end.getLongitude());
	}

	public void autofillComboBox() {
		// TODO Robin & Julius

		List<String> names;
		String input = "";

		names = GraphDataParser.getGraphDataParser(GraphDataConstants.CONST_XML_NODE_HIGHWAY).containsName(input);

		for (String string : names) {
			System.out.println(string);
		}

		printTimeDuration();
	}

	public void setTime() {
		date = new Date();
		duration = date.getTime();
	}

	public void printTimeDuration() {
		date = new Date();
		duration = date.getTime() - duration;

		Long dur = duration;

		int h = (int) (dur / 3600000);
		dur -= (h * 3600000);
		int min = (int) (dur / 60000);
		dur -= (min * 60000);
		int seconds = (int) (dur / 1000);
		dur -= (seconds * 1000);

		System.out.println(/*edgeCount +". Dauer - */"H: " + h + " Min: " + min + " Sec: " + seconds + " Mills.: " + dur);
		
		setTime();
	}
}
