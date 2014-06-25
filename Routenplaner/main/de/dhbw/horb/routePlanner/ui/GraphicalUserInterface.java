package de.dhbw.horb.routePlanner.ui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.FileNotFoundException;
import java.util.Date;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.xml.stream.XMLStreamException;

import de.dhbw.horb.routePlanner.graphData.Edge;
import de.dhbw.horb.routePlanner.parser.GraphDataConstants;
import de.dhbw.horb.routePlanner.parser.GraphDataParser;

public class GraphicalUserInterface extends JFrame {

	private static final long serialVersionUID = 1L;
	private Long edgeCount;
	private Date date;
	private Long duration;
	private GraphicalUserInterface gui;
	
	final MapPanel map = new MapPanel();

	public GraphicalUserInterface() {
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
		final JButton writeEdgeXML = new JButton("Write Edge XML");
		JButton printMap = new JButton("Print Map");
		JButton clearButton = new JButton("Clear Map");
		buttonsPanel.add(writeEdgeXML);
		buttonsPanel.add(printMap);
		buttonsPanel.add(clearButton);
		getContentPane().add(buttonsPanel, BorderLayout.EAST);
		writeEdgeXML.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				writeEdgeXML.setEnabled(false);
				Controler.executor.getExecutor().submit(new Runnable() {
					public void run() {
						try {
							GraphDataParser.getGraphDataParser(GraphDataConstants.CONST_XML_WAY_HIGHWAY).writeEdgeXML();
						} catch (XMLStreamException | FileNotFoundException exc) {
							exc.printStackTrace();
						}
					}
				});
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

		Controler.executor.getExecutor().submit(new Runnable() {

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
