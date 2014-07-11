package de.dhbw.horb.routePlanner.test;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import de.dhbw.horb.routePlanner.SupportMethods;

public class UTSupportMethods {

    @Test
    public void fromDistanceAndSpeedToMilliseconds() {

	double delta = 0;
	Assert.assertEquals(36000.0,
		SupportMethods.fromDistanceAndSpeedToMilliseconds(1.0, 100),
		delta);
    }

    @Test
    public void fromLatLonToDistanceInKM() {

	double delta = 0;
	Assert.assertEquals(680.6580311269971, SupportMethods
		.fromLatLonToDistanceInKM(49.4782247, 6.3640853, 54.0737704,
			12.9119103), delta);
    }

    @Test
    public void degreesToRadians() {
	double delta = 0;
	Assert.assertEquals(0.017453292519943295,
		SupportMethods.degreesToRadians(1.0), delta);
    }

    @Test
    public void sortListCompairedToEquality() {

	String compStr = "NAME";
	String str1 = "Bad sorted, NAME";
	String str2 = "NAME better sorted";
	List<String> strList = new ArrayList<String>();
	strList.add(str1);
	strList.add(str2);
	strList.add(compStr);

	Assert.assertEquals(str1, strList.get(0));
	Assert.assertEquals(str2, strList.get(1));
	Assert.assertEquals(compStr, strList.get(2));

	strList = SupportMethods.sortListCompairedToEquality(strList, compStr);

	Assert.assertEquals(compStr, strList.get(0));
	Assert.assertEquals(str2, strList.get(1));
	Assert.assertEquals(str1, strList.get(2));
    }

    @Test
    public void isNumeric() {

	Assert.assertTrue(SupportMethods.isNumeric("1"));
	Assert.assertTrue(SupportMethods.isNumeric("0.123456789"));
	Assert.assertTrue(SupportMethods.isNumeric("123456789.0"));
	Assert.assertFalse(SupportMethods.isNumeric("A"));
	Assert.assertFalse(SupportMethods.isNumeric("No number"));
	Assert.assertFalse(SupportMethods.isNumeric("134643a"));
    }

    @Test
    public void commaStrToStrList() {

	String commaStr = "1, test, t e s t ,,";
	List<String> strList = new ArrayList<String>();
	strList.add("1");
	strList.add("test");
	strList.add("t e s t");

	Assert.assertEquals(strList, SupportMethods.commaStrToStrList(commaStr));
    }

    @Test
    public void strListToCommaStr() {

	String commaStr = "1,test,t e s t";
	List<String> strList = new ArrayList<String>();
	strList.add("1");
	strList.add(" test");
	strList.add(" t e s t ");
	strList.add("");
	strList.add(" ");

	Assert.assertEquals(commaStr, SupportMethods.strListToCommaStr(strList));
    }

    @Test
    public void daysToHours() {
	double delta = 0;
	Assert.assertEquals(48.7776, SupportMethods.daysToHours(2.0324), delta);
    }

    @Test
    public void hoursToMinutes() {
	double delta = 0;
	Assert.assertEquals(3320.736, SupportMethods.hoursToMinutes(55.3456),
		delta);
    }

    @Test
    public void minutesToSeconds() {
	double delta = 0;
	Assert.assertEquals(612.8661,
		SupportMethods.minutesToSeconds(10.214435), delta);
    }

    @Test
    public void secondsToMilliseconds() {
	double delta = 0;
	Assert.assertEquals(32453.23345,
		SupportMethods.secondsToMilliseconds(32.45323345), delta);
    }

    @Test
    public void secondsToMinutes() {
	double delta = 0;
	Assert.assertEquals(72.79088666666667,
		SupportMethods.secondsToMinutes(4367.4532), delta);
    }

    @Test
    public void minutesToHours() {
	double delta = 0;
	Assert.assertEquals(9.4520576,
		SupportMethods.minutesToHours(567.123456), delta);
    }

    @Test
    public void hoursToDays() {
	double delta = 0;
	Assert.assertEquals(2.2968104166666667,
		SupportMethods.hoursToDays(55.12345), delta);
    }

    @Test
    public void millisecondsToSeconds() {
	double delta = 0;
	Assert.assertEquals(1.00123456789,
		SupportMethods.millisecondsToSeconds(1001.23456789), delta);
    }
}
