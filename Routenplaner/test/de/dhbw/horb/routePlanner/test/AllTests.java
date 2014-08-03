package de.dhbw.horb.routePlanner.test;

import junit.framework.Test;
import junit.framework.TestSuite;
import de.dhbw.horb.routePlanner.test.general.UTSupportMethods;
import de.dhbw.horb.routePlanner.test.graphData.UTRoute;

public class AllTests {

    public static Test suite() {
	TestSuite suite = new TestSuite();
	suite.addTestSuite(UTSupportMethods.class);
	suite.addTestSuite(UTRoute.class);
	return suite;
    }
}
