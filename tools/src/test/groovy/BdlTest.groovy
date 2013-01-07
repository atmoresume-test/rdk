import org.junit.Before
import org.junit.Test
import robowiki.rdk.util.RdkUtils

import static org.junit.Assert.assertEquals
import static org.junit.Assert.assertTrue

class BdlTest {

    private static final String BOT_NAME = 'sample.Walls 1.0'
    GroovyShell shell
    Binding binding
    File botsDir

    @Before
    void setUp() {
        binding = new Binding()
        shell = new GroovyShell(binding)
        botsDir = File.createTempFile('bdl-', '-bots')
        botsDir.delete()
        botsDir.mkdir()
        botsDir.deleteOnExit()
    }


    @Test
    def void testDownloadWalls() {
        binding.setVariable('args', ['-d', botsDir.getAbsolutePath(), '-b', BOT_NAME] as String[])
        shell.evaluate(new File('src/main/groovy/bdl.groovy'))
        def botFile = new File(botsDir, RdkUtils.getJarName(BOT_NAME))
        assertTrue(botFile.exists())
        assertTrue(botFile.size() > 0)
    }

    @Test
    def void testDownloadSampleBots() {
        binding.setVariable('args', ['-d', botsDir.getAbsolutePath(), '-b', 'sample.*'] as String[])
        shell.evaluate(new File('src/main/groovy/bdl.groovy'))
        assertEquals(13, botsDir.list().length)
    }

}
