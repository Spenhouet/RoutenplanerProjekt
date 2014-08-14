package de.dhbw.horb.routePlanner.test.data;

import junit.framework.TestCase;

import org.junit.Assert;

import de.dhbw.horb.routePlanner.Constants;
import de.dhbw.horb.routePlanner.data.SettingsManager;
import de.dhbw.horb.routePlanner.data.XMLFileManager;

public class UTXMLFileManager extends TestCase {

    private String filePath;

    @Override
    protected void setUp() {
	filePath = "test/de/dhbw/horb/routePlanner/test/testFiles/testGraphDataStreamReader.xml";
    }

    @Override
    protected void tearDown() {
    }

    public void testFileExists() {
	Assert.assertFalse(XMLFileManager.fileExists("noExisting/File.xml"));
	Assert.assertTrue(XMLFileManager.fileExists(filePath));
    }

    public void testGetExtendedXMLFileName() {
	String save_country = SettingsManager.getValue(Constants.SETTINGS_COUNTRY, "empty");
	String test_country = "Deutschland";
	SettingsManager.saveSetting(Constants.SETTINGS_COUNTRY, test_country);
	Assert.assertFalse(filePath.contains(test_country));
	String ex_filePath = XMLFileManager.getExtendedXMLFileName(filePath);
	Assert.assertNotNull(ex_filePath);
	Assert.assertTrue(ex_filePath.contains(test_country));

	SettingsManager.deleteSetting(Constants.SETTINGS_COUNTRY);
	if (save_country == null || !save_country.contains("empty"))
	    return;
	SettingsManager.saveSetting(Constants.SETTINGS_COUNTRY, save_country);
    }

    //    public void testLockXML() throws IOException {
    //
    //    }
    //
    //    public void testReleaseXML() {
    //
    //    }
}
