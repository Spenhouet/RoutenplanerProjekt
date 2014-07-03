package de.dhbw.horb.routePlanner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SupportMethods {
	public static Long fromDistanceAndSpeedToMilliseconds(Double distance, int speed) {
		return secondsToMilliseconds(minutesToSeconds(hoursToMinutes((distance / speed)))).longValue();
	}

	public static Double daysToHours(Double days) {
		return (days * 24.0);
	}

	public static Double hoursToMinutes(Double hours) {
		return (hours * 60.0);
	}

	public static Double minutesToSeconds(Double minutes) {
		return (minutes * 60.0);
	}

	public static Double secondsToMilliseconds(Double seconds) {
		return (seconds * 1000.0);
	}

	public static Double secondsToMinutes(Double seconds) {
		return (seconds / 60.0);
	}

	public static Double minutesToHours(Double minutes) {
		return (minutes / 60.0);
	}

	public static Double hoursToDays(Double hours) {
		return (hours / 24.0);
	}

	public static Double millisecondsToSeconds(Double milliseconds) {
		return (milliseconds / 1000.0);
	}

	public static double fromLatLonToDistanceInKM(Double lat1, Double lon1, Double lat2, Double lon2) {

		if (lat1 == null || lon1 == null || lat2 == null || lon2 == null)
			return 0.0;

		int R = 6371;
		double phi1 = degreesToRadians(lat1);
		double phi2 = degreesToRadians(lat2);
		double deltaPhi = degreesToRadians(lat2 - lat1);
		double deltaLambda = degreesToRadians(lon2 - lon1);

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

	public static boolean isNumeric(String str) {
		try {
			Double.parseDouble(str);
		} catch (NumberFormatException nfe) {
			return false;
		}
		return true;
	}
}
