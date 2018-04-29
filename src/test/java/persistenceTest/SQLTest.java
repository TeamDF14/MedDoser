package persistenceTest;

import init.Init;
import org.junit.*;
import persistenceSQL.ControlSQL;
import persistenceSQL.PZNInfo;
import persistenceSQL.SectionInfo;

import static org.junit.Assert.*;

public class SQLTest {

    private static ControlSQL csql;

    @BeforeClass
    public static void createDB(){
        Init.adjustPaths();
        Init.dbFile.delete();

        csql = new ControlSQL();
    }

    @AfterClass
    public static void clean() {
        Init.dbFile.delete();
    }

    @Test
    public void medicationTest() {

        assertTrue("The connection variable is empty.", csql.connection != null);
        assertTrue("The database file was not created. ", Init.dbFile.exists());

        PZNInfo pznInfo =  csql.getMedication("2394397");

        assertTrue("Empty field 'tradeName'", pznInfo.getTradeName() != null);
        assertTrue("Empty field 'substance'", pznInfo.getSubstance() != null);

        assertEquals("Wrong trade name", "Amoxicillin RAT", pznInfo.getTradeName());
        assertEquals("Wrong substance", "750 MG FTA", pznInfo.getSubstance());
    }

    @Test
    public void sectionTest(){
        assertTrue("The connection variable is empty.", csql.connection != null);
        assertTrue("The database file was not created. ", Init.dbFile.exists());

        SectionInfo sectionInfo;

        // Existing section entry - first possible entry
        sectionInfo =  csql.getSection(411);
        assertEquals("Wrong section title","Bedarfsmedikation", sectionInfo.getSectionTitle());
        assertEquals("Wrong section code",411, sectionInfo.getSectionCode());
        sectionInfo =  csql.getSection("Bedarfsmedikation");
        assertEquals("Wrong section title","Bedarfsmedikation", sectionInfo.getSectionTitle());
        assertEquals("Wrong section code",411, sectionInfo.getSectionCode());

        // Existing section entry - last possible entry
        sectionInfo =  csql.getSection(422);
        assertEquals("Wrong section title","Wichtige Angaben", sectionInfo.getSectionTitle());
        assertEquals("Wrong section code",422, sectionInfo.getSectionCode());
        sectionInfo =  csql.getSection("Wichtige Angaben");
        assertEquals("Wrong section title","Wichtige Angaben", sectionInfo.getSectionTitle());
        assertEquals("Wrong section code",422, sectionInfo.getSectionCode());


        // Non-Existing section entry- not available
        sectionInfo =  csql.getSection(999);
        assertEquals("Element properties are not empty, despite they should be (section Code: 999)", 999,  sectionInfo.getSectionCode());
        assertEquals("Wrong section title","Standard", sectionInfo.getSectionTitle());
        sectionInfo =  csql.getSection("");
        assertEquals("Element properties are not empty", 0,  sectionInfo.getSectionCode());
        assertEquals("Wrong section title","Standard", sectionInfo.getSectionTitle());

    }
}
