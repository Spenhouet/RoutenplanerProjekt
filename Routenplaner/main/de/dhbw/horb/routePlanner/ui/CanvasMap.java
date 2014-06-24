package de.dhbw.horb.routePlanner.ui;

import java.awt.Canvas;
import java.awt.Graphics;

public class CanvasMap extends Canvas {

	public void paint(final Graphics g) {
		/* das paint() der super-Klasse muss natürlich aufgerufen werden. */
		super.paint(g);

		/*
		 * Zuerst zeichne ich ganz eifach eine Linie vom Punkt (5/5) zum Punkt
		 * (50/150)
		 */
		g.drawLine(5, 5, 50, 150);
		/* Dann noch ein Rechteck vom Punkt (50/50) zum Punkt (200/200) */
		g.drawRect(50, 50, 200, 200);

	}
}
