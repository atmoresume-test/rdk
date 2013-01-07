package robowiki.rdk.util

import org.junit.Test
import robowiki.rdk.util.RdkUtils

import static org.junit.Assert.assertEquals

class RdkUtilsTest {

    @Test
    def void testGetJarName() {
        assertEquals('pkg.Bot_ver.jar', RdkUtils.getJarName('pkg.Bot ver'));
    }

}
