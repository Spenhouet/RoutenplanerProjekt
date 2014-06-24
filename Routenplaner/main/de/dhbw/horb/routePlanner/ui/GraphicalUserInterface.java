package de.dhbw.horb.routePlanner.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Date;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

import de.dhbw.horb.routePlanner.graphData.Edge;
import de.dhbw.horb.routePlanner.graphData.Node;
import de.dhbw.horb.routePlanner.parser.GraphDataConstants;
import de.dhbw.horb.routePlanner.parser.GraphDataParser;
import de.dhbw.horb.routePlanner.parser.GraphDataParserMultithread;

public class GraphicalUserInterface extends JFrame {

	private static final long serialVersionUID = 1L;
	private Long edgeCount;
	private Date date;
	private Long duration;

	final MapPanel comp = new MapPanel();

	public GraphicalUserInterface() {

		initUI();

		setTime();
		edgeCount = 0L;

		// TODO Robin & Julius

		new GraphDataParserMultithread().fillGUI(this);
		// this.autofillComboBox();

		// System.out.println(dateFormat.format(date));
		comp.addLine(4, 10, 60, 70, Color.black);

	}

	public void addEdge(Edge newEdge) {
		// TODO Robin & Julius

		edgeCount++;

		if (edgeCount % 1000 == 0) {

			printTimeDuration();
		}

		Node start = newEdge.getStartNode();
		Node end = newEdge.getEndNode();

		int factor = 1000;
		int cut = 10;

		int x1 = (int) (Math.round((start.getLatitude() * cut) % 1 * factor));
		int y1 = (int) (Math.round((start.getLongitude() * cut) % 1 * factor));
		int x2 = (int) (Math.round((end.getLatitude() * cut) % 1 * factor));
		int y2 = (int) (Math.round((end.getLongitude() * cut) % 1 * factor));

		comp.addLine(x1, y1, x2, y2, Color.black);
		//FIXME Kordinaten noch falsch
		
//		System.out.println(edgeCount + ". Start Node ID: " + start.getID() + " mit Breitengrad: " + start.getLatitude()
//				+ " mit Längengrad: " + start.getLongitude() + " End Node ID: " + end.getID() + " mit Breitengrad: "
//				+ end.getLatitude() + " mit Längengrad: " + end.getLongitude());
	}

	public void autofillComboBox() {
		// TODO Robin & Julius

		List<String> names;
		String input = "mün";

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

		System.out.println(/* edgeCount +". Dauer - */"H: " + h + " Min: " + min + " Sec: " + seconds + " Mills.: "
				+ dur);

		setTime();
	}

	private void initUI() {

		// setTitle("Route Planner");
		// setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		//
		// add(mapD);
		//
		// setSize(500, 500);
		// setLocationRelativeTo(null);

		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		comp.setPreferredSize(new Dimension(500, 500));
		getContentPane().add(comp, BorderLayout.CENTER);
		JPanel buttonsPanel = new JPanel();
		JButton newLineButton = new JButton("New Line");
		JButton clearButton = new JButton("Clear");
		buttonsPanel.add(newLineButton);
		buttonsPanel.add(clearButton);
		getContentPane().add(buttonsPanel, BorderLayout.SOUTH);
		newLineButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				int x1 = (int) (Math.random() * 500);
				int x2 = (int) (Math.random() * 500);
				int y1 = (int) (Math.random() * 500);
				int y2 = (int) (Math.random() * 500);
				Color randomColor = new Color((float) Math.random(), (float) Math.random(), (float) Math.random());
				comp.addLine(x1, y1, x2, y2, randomColor);
			}
		});
		clearButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				comp.clearLines();
			}
		});
		pack();
		setVisible(true);
	}
}
