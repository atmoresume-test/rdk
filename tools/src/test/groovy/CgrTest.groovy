import org.junit.Before
import org.junit.Test

import static org.junit.Assert.assertEquals

class CgrTest {

    GroovyShell shell
    Binding binding
    File challengesDir

    @Before
    void setUp() {
        binding = new Binding()
        shell = new GroovyShell(binding)
        challengesDir = File.createTempFile('cgr-', '-challenge')
        challengesDir.delete()
        challengesDir.mkdir()
        challengesDir.deleteOnExit()
    }


    @Test
    def void testDownloadWalls() {
        binding.setVariable('args', ['-d', challengesDir.getAbsolutePath()] as String[])
        shell.evaluate(new File('src/main/groovy/cgr.groovy'))
        assertEquals(1, challengesDir.list().length)
    }

}
