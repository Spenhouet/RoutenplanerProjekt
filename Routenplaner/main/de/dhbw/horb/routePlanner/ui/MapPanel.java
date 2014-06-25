package de.dhbw.horb.routePlanner.ui;

import java.awt.Color;
import java.awt.Graphics;
import java.util.LinkedList;

import javax.swing.JComponent;

import com.google.maps.googleMapsAPI.GoogleMapsProjection2;
import com.google.maps.googleMapsAPI.PointF;

import de.dhbw.horb.routePlanner.graphData.Edge;
import de.dhbw.horb.routePlanner.graphData.Node;

public class MapPanel extends JComponent {


	/**
	 * 
	 */
	private static final long serialVersionUID = 6967910227953409164L;

	private static class Line {
		final int x1;
		final int y1;
		final int x2;
		final int y2;
		final Color color;

		public Line(int x1, int y1, int x2, int y2, Color color) {
			this.x1 = x1;
			this.y1 = y1;
			this.x2 = x2;
			this.y2 = y2;
			this.color = color;
		}
	}

	private final LinkedList<Line> lines = new LinkedList<Line>();

	public void addEdge(Edge e){
		
		Node start = e.getStartNode();
		Node end = e.getEndNode();

		int zoom = 1;

		GoogleMapsProjection2 gmp = new GoogleMapsProjection2();

		PointF startP = gmp.fromLatLngToPoint(start.getLatitude(), start.getLongitude(), zoom);
		PointF endP = gmp.fromLatLngToPoint(end.getLatitude(), end.getLongitude(), zoom);

		int x1 = (int) startP.x;
		int y1 = (int) startP.y;
		int x2 = (int) endP.x;
		int y2 = (int) endP.y;

		int xC = (int) 200;
		int yC = (int) 100;
		
		x1 -= xC;
		y1 -= yC;
		x2 -= xC;
		y2 -= yC;
		
		addLine(x1, y1, x2, y2, e.getColor());
		// FIXME Kordinaten noch falsch
		
	}
	
	public void addLine(int x1, int x2, int x3, int x4) {
		addLine(x1, x2, x3, x4, Color.black);
	}

	public void addLine(int x1, int x2, int x3, int x4, Color color) {
		lines.add(new Line(x1, x2, x3, x4, color));
		repaint();
	}

	public void clearLines() {
		lines.clear();
		repaint();
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		for (Line line : lines) {
			g.setColor(line.color);
			g.drawLine(line.x1, line.y1, line.x2, line.y2);
		}
	}

}
