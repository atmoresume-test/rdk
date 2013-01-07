package robowiki.rdk.util

import org.junit.Test

import static org.junit.Assert.assertTrue

class BotListTest {

    @Test
    def void testLoadParticipants() {
        def parts = new BotList().loadParticipants(RdkUtils.duelRumbleUrl())
        assertTrue(parts.size() > 0)
    }

}
