package de.dhbw.horb.routePlanner.test.general;

import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;

import org.junit.Assert;

import de.dhbw.horb.routePlanner.SupportMethods;

public class UTSupportMethods extends TestCase {

    @Override
    protected void setUp() {
    }

    @Override
    protected void tearDown() {
    }

    public void testFromDistanceAndSpeedToMilliseconds() {

	double delta = 0;
	Assert.assertEquals(36000.0, SupportMethods.fromDistanceAndSpeedToMilliseconds(1.0, 100), delta);
    }

    public void testFromLatLonToDistanceInKM() {

	double delta = 0;
	Assert.assertEquals(680.6580311269971, SupportMethods.fromLatLonToDistanceInKM(49.4782247, 6.3640853,
	        54.0737704, 12.9119103), delta);
    }

    public void testDegreesToRadians() {
	double delta = 0;
	Assert.assertEquals(0.017453292519943295, SupportMethods.degreesToRadians(1.0), delta);
    }

    public void testSortListCompairedToEquality() {

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

    public void testIsNumeric() {

	Assert.assertTrue(SupportMethods.isNumeric("1"));
	Assert.assertTrue(SupportMethods.isNumeric("0.123456789"));
	Assert.assertTrue(SupportMethods.isNumeric("123456789.0"));
	Assert.assertFalse(SupportMethods.isNumeric("A"));
	Assert.assertFalse(SupportMethods.isNumeric("No number"));
	Assert.assertFalse(SupportMethods.isNumeric("134643a"));
    }

    public void testCommaStrToStrList() {

	String commaStr = "1, test, t e s t ,,";
	List<String> strList = new ArrayList<String>();
	strList.add("1");
	strList.add("test");
	strList.add("t e s t");

	Assert.assertEquals(strList, SupportMethods.commaStrToStrList(commaStr));
    }

    public void testStrListToCommaStr() {

	String commaStr = "1,test,t e s t";
	List<String> strList = new ArrayList<String>();
	strList.add("1");
	strList.add(" test");
	strList.add(" t e s t ");
	strList.add("");
	strList.add(" ");

	Assert.assertEquals(commaStr, SupportMethods.strListToCommaStr(strList));
    }

    public void testDaysToHours() {
	double delta = 0;
	Assert.assertEquals(48.7776, SupportMethods.daysToHours(2.0324), delta);
    }

    public void testHoursToMinutes() {
	double delta = 0;
	Assert.assertEquals(3320.736, SupportMethods.hoursToMinutes(55.3456), delta);
    }

    public void testMinutesToSeconds() {
	double delta = 0;
	Assert.assertEquals(612.8661, SupportMethods.minutesToSeconds(10.214435), delta);
    }

    public void testSecondsToMilliseconds() {
	double delta = 0;
	Assert.assertEquals(32453.23345, SupportMethods.secondsToMilliseconds(32.45323345), delta);
    }

    public void testSecondsToMinutes() {
	double delta = 0;
	Assert.assertEquals(72.79088666666667, SupportMethods.secondsToMinutes(4367.4532), delta);
    }

    public void testMinutesToHours() {
	double delta = 0;
	Assert.assertEquals(9.4520576, SupportMethods.minutesToHours(567.123456), delta);
    }

    public void testHoursToDays() {
	double delta = 0;
	Assert.assertEquals(2.2968104166666667, SupportMethods.hoursToDays(55.12345), delta);
    }

    public void testMillisecondsToSeconds() {
	double delta = 0;
	Assert.assertEquals(1.00123456789, SupportMethods.millisecondsToSeconds(1001.23456789), delta);
    }
}
