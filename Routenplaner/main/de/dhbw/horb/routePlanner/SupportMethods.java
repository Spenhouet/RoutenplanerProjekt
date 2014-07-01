package de.dhbw.horb.routePlanner;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import de.dhbw.horb.routePlanner.graphData.Node;

public class SupportMethods {
	public static Long fromDistanceAndSpeedToSeconds(Double distance, int speed) {
		return minutesToSeconds(hoursToMinutes((distance/speed))).longValue();
	}
	
	public static Double daysToHours(Double days){
		return (days*24.0);		
	}
	
	public static Double hoursToMinutes(Double hours){
		return (hours*60.0);		
	}

	public static Double minutesToSeconds(Double minutes){
		return (minutes*60.0);		
	}
	
	public static Double secondsToMinutes(Double seconds){
		return (seconds/60.0);		
	}
	
	public static Double minutesToHours(Double minutes){
		return (minutes/60.0);		
	}
	
	public static Double hoursToDays(Double hours){
		return (hours/24.0);		
	}
	
	public static double fromLatLonToDistanceInKM(Node start, Node end) {

		if (start == null || end == null)
			return 0.0;

		int R = 6371;
		double phi1 = degreesToRadians(start.getLatitude());
		double phi2 = degreesToRadians(end.getLatitude());
		double deltaPhi = degreesToRadians(end.getLatitude() - start.getLatitude());
		double deltaLambda = degreesToRadians(end.getLongitude() - start.getLongitude());

		double a = Math.sin(deltaPhi / 2) * Math.sin(deltaPhi / 2) + Math.cos(phi1) * Math.cos(phi2)
				* Math.sin(deltaLambda / 2) * Math.sin(deltaLambda / 2);
		double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

		return R * c;

	}

	public static double degreesToRadians(double deg) {
		return deg * (Math.PI / 180);
	}

	public static String strListToCommaStr(List<String> strL) {
		if (strL == null)
			return null;

		String value = "";
		for (String i : strL) {
			value += value.isEmpty() ? i : "," + i;
		}

		return value;
	}

	public static List<String> commaStrToStrList(String commaStr) {
		if (commaStr == null)
			return null;
		return new ArrayList<String>(Arrays.asList(commaStr.split(",")));
	}
}
