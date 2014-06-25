package de.dhbw.horb.routePlanner.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Date;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

import com.google.maps.googleMapsAPI.GoogleMapsProjection2;
import com.google.maps.googleMapsAPI.PointF;

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

	final MapPanel map = new MapPanel();

	public GraphicalUserInterface() {

		initWindow();
		initControls();
		initMap();

		pack();

		setTime();
		edgeCount = 0L;

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
				map.addLine(x1, y1, x2, y2, randomColor);
			}
		});
		clearButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				map.clearLines();
			}
		});
	}

	private void initMap() {
		map.setPreferredSize(new Dimension(500, 500));
		getContentPane().add(map, BorderLayout.CENTER);

		new GraphDataParserMultithread().fillGUI(this);
	}

	public void addEdge(Edge newEdge) {
		// TODO Robin & Julius

		edgeCount++;

		if (edgeCount % 1000 == 0) {

			printTimeDuration();
		}

		

		// System.out.println(edgeCount + ". Start Node ID: " + start.getID() +
		// " mit Breitengrad: " + start.getLatitude()
		// + " mit L�ngengrad: " + start.getLongitude() + " End Node ID: " +
		// end.getID() + " mit Breitengrad: "
		// + end.getLatitude() + " mit L�ngengrad: " + end.getLongitude());
		
		map.addEdge(newEdge);
	}

	public void autofillComboBox() {
		// TODO Robin & Julius

		List<String> names;
		String input = "m�n";

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
}
