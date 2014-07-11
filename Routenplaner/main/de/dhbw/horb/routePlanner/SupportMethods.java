package de.dhbw.horb.routePlanner;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.StringTokenizer;

/**
 * Diese Klasse stellt Hilfsmethoden zur Verfügung.
 */
public class SupportMethods {

    /**
     * Berechnung der Dauer von einer Fahrt über eine bestimmte Strecke mit einer bestimmten Geschwindigkeit.
     * @param distance Die Länge der Strecke.
     * @param speed Die Geschwindigkeit.
     * @return Die Dauer in Millisekunden.
     */
    public static Long fromDistanceAndSpeedToMilliseconds(Double distance,
	    int speed) {
	return secondsToMilliseconds(
		minutesToSeconds(hoursToMinutes((distance / speed))))
		.longValue();
    }

    /**
     * Umrechnung von Tagen zu Stunden.
     * @param days Die Anzahl der Tage.
     * @return Die Anzahl der Stunden.
     */
    public static Double daysToHours(Double days) {
	return (days * 24.0);
    }

    /**
     * Umrechnung von Stunden zu Minuten.
     * @param hours Die Anzahl der Stunden.
     * @return Die Anzahl der Minuten.
     */
    public static Double hoursToMinutes(Double hours) {
	return (hours * 60.0);
    }

    /**
     * Umrechnung von Minuten in Sekunden.
     * @param minutes Die Anzahl der Minuten.
     * @return Die Anzahl der Sekunden.
     */
    public static Double minutesToSeconds(Double minutes) {
	return (minutes * 60.0);
    }

    /**
     * Umrechnung von Sekunden in Millisekunden.
     * @param seconds Die Anzahl der Sekunden.
     * @return Die Anzahl der Millisekunden.
     */
    public static Double secondsToMilliseconds(Double seconds) {
	return (seconds * 1000.0);
    }

    /**
     * Umrechnung von Sekunden in Minuten.
     * @param seconds Die Anzahl der Sekunden.
     * @return Die Anzahl der Minuten.
     */
    public static Double secondsToMinutes(Double seconds) {
	return (seconds / 60.0);
    }

    /**
     * Umrechnung von Minuten in Stunden.
     * @param minutes Die Anzahl der Minuten.
     * @return Die Anzahl der Stunden.
     */
    public static Double minutesToHours(Double minutes) {
	return (minutes / 60.0);
    }

    /**
     * Umrechnung von Stunden zu Tagen.
     * @param hours Die Anzahl der Stunden.
     * @return Die Anzahl der Tage.
     */
    public static Double hoursToDays(Double hours) {
	return (hours / 24.0);
    }

    /**
     * Umrechnung von Millisekunden in Sekunden.
     * @param milliseconds Die Anzahl der Millisekunden.
     * @return Die Anzahl der Sekunden.
     */
    public static Double millisecondsToSeconds(Double milliseconds) {
	return (milliseconds / 1000.0);
    }

    /**
     * Berechnung der Entfernung zweier Koordinaten.
     * @param lat1 Breitengrad des ersten Punktes.
     * @param lon1 Längengrad des ersten Punktes.
     * @param lat2 Breitengrad des zweiten Punktes.
     * @param lon2 Längengrad des zweiten Punktes.
     * @return Die Entfernung der beiden Punkte zueinander in Kilometer.
     */
    public static double fromLatLonToDistanceInKM(Double lat1, Double lon1,
	    Double lat2, Double lon2) {

	if (lat1 == null || lon1 == null || lat2 == null || lon2 == null)
	    return 0.0;

	int R = 6371;
	double phi1 = degreesToRadians(lat1);
	double phi2 = degreesToRadians(lat2);
	double deltaPhi = degreesToRadians(lat2 - lat1);
	double deltaLambda = degreesToRadians(lon2 - lon1);

	double a = Math.sin(deltaPhi / 2) * Math.sin(deltaPhi / 2)
		+ Math.cos(phi1) * Math.cos(phi2) * Math.sin(deltaLambda / 2)
		* Math.sin(deltaLambda / 2);
	double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

	return R * c;
    }

    /**
     * Umrechnung von Grad zu Radiant.
     * @param deg Der Grad Wert.
     * @return Den Radiant Wert.
     */
    public static double degreesToRadians(double deg) {
	return deg * (Math.PI / 180);
    }

    /**
     * Konvertiert eine String Liste in einen Komma separierten String.
     * Leere Listeneinträge werden ignoriert.
     * @param strL Die String Liste. Bsp.: ["test1","test2","test3","test4"]
     * @return Der Komma separierte String. Bsp.: "test1, test2, test3, test4"
     */
    public static String strListToCommaStr(List<String> strL) {
	if (strL == null)
	    return null;

	String value = "";
	for (String i : strL) {
	    if (i == null || i.trim().isEmpty())
		continue;
	    value += value.isEmpty() ? i : "," + i.trim();
	}

	return value;
    }

    /**
     * Konvertiert einen Komma separierten String in eine String Liste. Die Kommas trennen die Strings.
     * Leerzeichen zwischen Komma und String werden entfernt sowie ignoriert.
     * @param commaStr Der Komma separierte String. Bsp.: "test1, test2, test3, test4"
     * @return Die String Liste mit den Elementen aus dem Komma String. Bsp.: ["test1","test2","test3","test4"]
     */
    public static List<String> commaStrToStrList(String commaStr) {
	if (commaStr == null)
	    return null;

	List<String> list = new ArrayList<String>();
	StringTokenizer st2 = new StringTokenizer(commaStr, ",");

	while (st2.hasMoreElements()) {
	    String nextElement = st2.nextElement().toString().trim();
	    if (nextElement != null && !nextElement.isEmpty())
		list.add(nextElement);
	}

	return list;
    }

    /**
     * Sortiert eine String Liste nach der Ähnlichkeit zu einem String.
     * Nach ganz oben wird ein genau gleiches Element sortiert.
     * Ansonsten gilt: Um so weiter vorn der Vergleichs String im Listen Element zu finden ist, desto höher wird dieses 
     * eingeordnet. 
     * @param list Die zu sortierende String Liste.
     * @param str Der genannte Vergleichs String.
     * @return Die neu sortierte String Liste.
     */
    public static List<String> sortListCompairedToEquality(List<String> list,
	    String str) {

	if (list == null || str == null)
	    return null;

	final String compStr = str.toLowerCase();

	Collections.sort(list, new Comparator<String>() {
	    @Override
	    public int compare(String s1, String s2) {
		s1 = s1.toLowerCase();
		s2 = s2.toLowerCase();

		if (s1.equals(compStr) && !s2.equals(compStr)) {
		    return -1;
		} else if (!s1.equals(compStr) && s2.equals(compStr)) {
		    return 1;
		} else if (s1.indexOf(compStr) < s2.indexOf(compStr)) {
		    return -1;
		} else if (s1.indexOf(compStr) > s2.indexOf(compStr)) {
		    return 1;
		}
		return 0;
	    }
	});

	return list;
    }

    /**
     * Prüft ob ein String nur aus Zahlen besteht bzw. eine Zahl ist.
     * @param str Der zu überprüfende String.
     * @return True: Ist eine Zahl. False: Ist keine Zahl.
     */
    public static boolean isNumeric(String str) {
	try {
	    Double.parseDouble(str);
	} catch (NumberFormatException nfe) {
	    return false;
	}
	return true;
    }
}
