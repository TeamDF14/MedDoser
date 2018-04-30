package persistenceTest;

import init.Init;
import org.junit.*;
import persistenceXML.DefaultXML;

import static org.junit.Assert.*;

public class DefaultXMLTest {

    @BeforeClass
    public static void initMedInt() {
        // Delete existing persistence file, if exists
        deletePersistenceFile();
    }

    @AfterClass
    public static void cleanMedInt() {
        // Delete existing persistence file, if exists
        deletePersistenceFile();
    }

    @Before
    public void setup() {
        // TODO: Stuff that has to be executed before each test
    }

    @After
    public void close() {
        // TODO: Stuff that has to be executed after each test
    }

    @Test
    public void createXMLFile() {

        // Create xml file
        Init.adjustPaths();
        new DefaultXML(){};

        assertTrue("The persistence file does not exist!", Init.persistenceFile.exists());

    }

    public static void deletePersistenceFile() {

        // create path
        Init.adjustPaths();

        // delete persistence file
        if(Init.persistenceFile.exists() && !Init.persistenceFile.isDirectory()) {
            Init.persistenceFile.delete();
        }
    }



}
