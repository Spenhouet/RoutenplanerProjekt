package de.dhbw.horb.routePlanner.mockUp;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import de.dhbw.horb.routePlanner.parser.JDomGraphDataCreator;

public class MockUp {

	public static void main(String[] args) {

		new Thread(new Runnable() {

			@Override
			public void run() {
				DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss:ms");
				Calendar cal = Calendar.getInstance();
				System.out.println(dateFormat.format(cal.getTime()));

				JDomGraphDataCreator dom = new JDomGraphDataCreator();
				dom.createNewXMLFiles();

				cal = Calendar.getInstance();
				System.out.println(dateFormat.format(cal.getTime()));

			}
		}).start();

	}
}
