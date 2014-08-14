package de.dhbw.horb.routePlanner.test.data;

import junit.framework.TestCase;

import org.junit.Assert;

import de.dhbw.horb.routePlanner.data.SettingsManager;

public class UTSettingsManager extends TestCase {

    @Override
    protected void setUp() {
    }

    @Override
    protected void tearDown() {
    }

    public void testSaveAndGetValue() {
	Assert.assertEquals(SettingsManager.getValue("testKey", "default"), "default");
	Assert.assertTrue(SettingsManager.saveSetting("testKey", "value"));
	Assert.assertEquals(SettingsManager.getValue("testKey", "default"), "value");
	Assert.assertTrue(SettingsManager.deleteSetting("testKey"));
	Assert.assertEquals(SettingsManager.getValue("testKey", "default"), "default");
    }
}
