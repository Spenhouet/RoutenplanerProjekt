package de.dhbw.horb.routePlanner.ui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

import de.dhbw.horb.routePlanner.graphData.Edge;
import de.dhbw.horb.routePlanner.parser.GraphDataConstants;
import de.dhbw.horb.routePlanner.parser.GraphDataParser;
import de.dhbw.horb.routePlanner.parser.GraphDataParserMultithread;

public class GraphicalUserInterface extends JFrame {

	private static final long serialVersionUID = 1L;
	private Long edgeCount;
	private Date date;
	private Long duration;
	private GraphicalUserInterface gui;

	private ExecutorService executor;

	final MapPanel map = new MapPanel();

	public GraphicalUserInterface() {
		executor = Executors.newSingleThreadExecutor();

		initWindow();
		initControls();
		initMap();

		pack();

		setTime();
		edgeCount = 0L;
		this.gui = this;
		

		// TODO Robin & Julius
		// this.autofillComboBox();
		

	}

	private void initWindow() {
		this.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});

	}

	private void initControls() {
		JPanel buttonsPanel = new JPanel();
		JButton writeEdgeXML = new JButton("Write Edge XML");
		JButton printMap = new JButton("Print Map");
		JButton clearButton = new JButton("Clear");
		buttonsPanel.add(writeEdgeXML);
		buttonsPanel.add(clearButton);
		buttonsPanel.add(printMap);
		getContentPane().add(buttonsPanel, BorderLayout.SOUTH);
		writeEdgeXML.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				new GraphDataParserMultithread().writeEdgeXML();
			}
		});
		clearButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				map.clearLines();
			}
		});
		printMap.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				GraphDataParser.getGraphDataParser(GraphDataConstants.CONST_XML_EDGE).everyWayToGui(gui);
			}
		});
	}

	private void initMap() {
		map.setPreferredSize(new Dimension(500, 500));
		getContentPane().add(map, BorderLayout.CENTER);
	}

	public void addEdge(final Edge newEdge) {
		// TODO Robin & Julius

		edgeCount++;

		if (edgeCount % 1000 == 0) {

			printTimeDuration();
		}

		// System.out.println(edgeCount + ". Start Node ID: " + start.getID() +
		// " mit Breitengrad: " + start.getLatitude()
		// + " mit Längengrad: " + start.getLongitude() + " End Node ID: " +
		// end.getID() + " mit Breitengrad: "
		// + end.getLatitude() + " mit Längengrad: " + end.getLongitude());

		executor.submit(new Runnable() {

			@Override
			public void run() {
				map.addEdge(newEdge, 5);
			}
		});

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
}
