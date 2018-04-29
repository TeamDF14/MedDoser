package mainTest;

import init.Init;
import org.junit.*;
import help.Help;

import java.io.IOException;
import java.util.logging.FileHandler;

import static junit.framework.TestCase.assertTrue;
import static org.hamcrest.core.Is.is;

public class InitTest {

    @Test
    public void initializeTest() {

        Init.initialize();

        // Check if the files were created properly
        assertTrue(util.FileSystem.bCheckFileExists(Init.dbFile));
        assertTrue(util.FileSystem.bCheckFileExists(Init.persistenceFile));

        FileHandler fh = null;
        try {
            fh = new FileHandler(Init.nameOfLogFile, 5120 * 1024, 3, true);
        } catch (IOException e) {
            e.printStackTrace();
        }
        assertTrue(fh != null);

    }

}
