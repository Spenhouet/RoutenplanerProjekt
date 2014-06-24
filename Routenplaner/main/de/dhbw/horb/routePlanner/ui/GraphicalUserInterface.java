package de.dhbw.horb.routePlanner.ui;

import java.awt.BorderLayout;
import java.util.Date;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JPanel;

import de.dhbw.horb.routePlanner.graphData.Edge;
import de.dhbw.horb.routePlanner.parser.GraphDataConstants;
import de.dhbw.horb.routePlanner.parser.GraphDataParser;

public class GraphicalUserInterface {

	private Long edgeCount;
	private Date date;
	private Long duration;

	JFrame mainFrame = new JFrame("Java Phone");

	JPanel keyDisplayPanel = new JPanel();

	CanvasMap map = new CanvasMap();

	public GraphicalUserInterface() {

		/* Setzt das Layout vom Frame und vom Panel */
		mainFrame.setLayout(new BorderLayout(20, 20));
		keyDisplayPanel.setLayout(new BorderLayout(20, 20));

		/* Hier können wir dan unser Canvas auf das Panel platzieren */
		keyDisplayPanel.add(map, BorderLayout.CENTER);
		/* und natürlich noch das Panel auf das Frame */
		mainFrame.add(keyDisplayPanel, BorderLayout.CENTER);

		/* Nur noch anzeigen und das ganze läuft */
		mainFrame.setSize(300, 300);
		mainFrame.setVisible(true);

		// DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		setTime();
		edgeCount = 0L;

		// TODO Robin & Julius

		// new GraphDataParserMultithread().fillGUI(this);
		this.autofillComboBox();

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
		String input = "mün";

		names = GraphDataParser.getGraphDataParser(
				GraphDataConstants.CONST_XML_NODE_HIGHWAY).containsName(input);

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

		System.out.println(/* edgeCount +". Dauer - */"H: " + h + " Min: "
				+ min + " Sec: " + seconds + " Mills.: " + dur);

		setTime();
	}

	private void init() {

	}
}
