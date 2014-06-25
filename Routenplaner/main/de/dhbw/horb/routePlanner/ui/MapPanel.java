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

	public void addEdge(Edge e, int zoom){
		
		Node start = e.getStartNode();
		Node end = e.getEndNode();

		GoogleMapsProjection2 gmp = new GoogleMapsProjection2();

		PointF startP = gmp.fromLatLngToPoint(start.getLatitude(), start.getLongitude(), zoom);
		PointF endP = gmp.fromLatLngToPoint(end.getLatitude(), end.getLongitude(), zoom);

		startP = toScreenCenter(startP, zoom);
		endP = toScreenCenter(endP, zoom);

		int x1 = (int) startP.x;
		int y1 = (int) startP.y;
		int x2 = (int) endP.x;
		int y2 = (int) endP.y;
		
//		System.out.println("x: " + x1 + " y: " + y1);

//		int xC = (int) 4150;
//		int yC = (int) 2700;
		
		//1 x 200 	y 100			x 200	y 100
		//2 x 500 	y 300			x 300	y 200
		//3 x 1000 	y 600			x 500	y 300
		//4 x 2050 	y 1300			x 1050 	y 700
		//5 x 4150 	y 2700			x 2100	y 1400
		
//		x: 265 y: 174
//		x: 530 y: 349
//		x: 1060 y: 699
//		x: 2120 y: 1398
//		x: 4240 y: 2796
//		
		
//		x1 -= xC;
//		y1 -= yC;
//		x2 -= xC;
//		y2 -= yC;
		
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
	
	private PointF toScreenCenter(PointF xy, int zoom){
		GoogleMapsProjection2 gmp = new GoogleMapsProjection2();

		PointF center = gmp.fromLatLngToPoint(51.0, 10.0, zoom);
		
		int xC = (getWidth()/2) - (int)center.x;
		int yC = (getHeight()/2) - (int)center.y;
		 
		return new PointF((xy.x+xC), (xy.y+yC));
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
