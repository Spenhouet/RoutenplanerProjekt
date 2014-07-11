package de.dhbw.horb.routePlanner.test;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import de.dhbw.horb.routePlanner.SupportMethods;

public class UTSupportMethods {

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

}
