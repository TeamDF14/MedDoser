package persistenceTest;

import enums.eIngestedStatus;
import enums.eIngestionTime;
import org.junit.runners.MethodSorters;
import persistenceXML.PersistenceMedIngObject;
import help.Help;
import org.junit.*;
import persistenceXML.Persistence;

import java.util.ArrayList;
import java.util.Date;

import static org.junit.Assert.*;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class PersistenceTest {

    private static Persistence persistence;

    @BeforeClass
    public static void initMedInt() {
        DefaultXMLTest.deletePersistenceFile();
        persistence = new Persistence();
    }

    @AfterClass
    public static void cleanMedInt() {
        DefaultXMLTest.deletePersistenceFile();
    }

    @Test
    public void A_lastOpenedTest() {
        // check creation date
        assertTrue(persistence.readLastOpened() != null);
        assertTrue(persistence.readLastOpenedString() != null && !persistence.readLastOpenedString().isEmpty());
    }

    @Test
    public void B_writeLastOpenedTimeTest(){
        // write date when last opened
        Date date = new Date();
        persistence.writeLastOpened(date);

        // check new last opened date
        assertEquals(util.Date.convertDateTimeToString(date), util.Date.convertDateTimeToString(persistence.readLastOpened()));
        assertEquals(util.Date.convertDateTimeToString(date), persistence.readLastOpenedString());
    }

    @Test
    public void C_writeMedicationIngestionTest(){
        // Create object
        PersistenceMedIngObject persistenceMedIngObject = new PersistenceMedIngObject();

        persistenceMedIngObject.setMedStatementReference("urn:uuid:92e56a57-15fa-48c6-aebf-08a1bc67b9e1");
        persistenceMedIngObject.setScheduledTimeCode(eIngestionTime.EVENING);
        persistenceMedIngObject.setScheduledTime(util.Date.convertStringToTime("08:00"));
        persistenceMedIngObject.setStatus(eIngestedStatus.DECLINED);
        persistenceMedIngObject.setScheduledDate(util.Date.convertStringToDate("04/25/2018"));
        persistenceMedIngObject.setTime(util.Date.convertStringToTime("13:37"));

        // Save object to XML
        assertTrue(persistence.writeMedicationIngestion(persistenceMedIngObject));
    }

    @Test
    public void D_readMedicationIngestionTest(){
        // Read objects from XML
        ArrayList<PersistenceMedIngObject> medIngObjectArrayList = persistence.readMedicationIngestionStatusAll();

        assertEquals(1, medIngObjectArrayList.size());
        assertEquals("urn:uuid:92e56a57-15fa-48c6-aebf-08a1bc67b9e1", medIngObjectArrayList.get(0).getMedStatementReference());
        assertEquals("PCV", medIngObjectArrayList.get(0).getScheduledTimeCode().toString());
        assertEquals("08:00", util.Date.convertTimeToString(medIngObjectArrayList.get(0).getScheduledTime()));
        assertEquals(eIngestedStatus.DECLINED, medIngObjectArrayList.get(0).getStatus());
        assertEquals("04/25/2018", util.Date.convertDateToString(medIngObjectArrayList.get(0).getScheduledDate(), true));
        assertEquals("13:37", util.Date.convertTimeToString(medIngObjectArrayList.get(0).getTime()));
    }

    @Test
    public void E_readDefaultIngestionTimesTest(){
        // Check default values for ingestion times
        assertEquals("08:00", persistence.readIngestionTimeMorningString());
        assertEquals("12:00", persistence.readIngestionTimeMiddayString());
        assertEquals("17:00", persistence.readIngestionTimeEveningString());
        assertEquals("21:00", persistence.readIngestionTimeNightString());

        assertEquals("Thu Jan 01 08:00:00 CET 1970", persistence.readIngestionTimeMorning().toString());
        assertEquals("Thu Jan 01 12:00:00 CET 1970", persistence.readIngestionTimeMidday().toString());
        assertEquals("Thu Jan 01 17:00:00 CET 1970", persistence.readIngestionTimeEvening().toString());
        assertEquals("Thu Jan 01 21:00:00 CET 1970", persistence.readIngestionTimeNight().toString());
    }

    @Test
    public void F_writeIngestionTimesTest(){
        // write Times as String
        persistence.writeIngestionTimeMorningString("21:00");
        persistence.writeIngestionTimeMiddayString("17:00");
        persistence.writeIngestionTimeEveningString("12:00");
        persistence.writeIngestionTimeNightString("08:00");

        // check new Times
        assertEquals("21:00", persistence.readIngestionTimeMorningString());
        assertEquals("17:00", persistence.readIngestionTimeMiddayString());
        assertEquals("12:00", persistence.readIngestionTimeEveningString());
        assertEquals("08:00", persistence.readIngestionTimeNightString());

        assertEquals("Thu Jan 01 21:00:00 CET 1970", persistence.readIngestionTimeMorning().toString());
        assertEquals("Thu Jan 01 17:00:00 CET 1970", persistence.readIngestionTimeMidday().toString());
        assertEquals("Thu Jan 01 12:00:00 CET 1970", persistence.readIngestionTimeEvening().toString());
        assertEquals("Thu Jan 01 08:00:00 CET 1970", persistence.readIngestionTimeNight().toString());

        // write Times as String
        persistence.writeIngestionTimeMorning(util.Date.convertStringToDateTime("12/11/2017 01:00:00"));
        persistence.writeIngestionTimeMidday(util.Date.convertStringToDateTime("12/11/2017 02:00:00"));
        persistence.writeIngestionTimeEvening(util.Date.convertStringToDateTime("12/11/2017 03:00:00"));
        persistence.writeIngestionTimeNight(util.Date.convertStringToDateTime("12/11/2017 04:00:00"));

        // check new Times
        assertEquals("01:00", persistence.readIngestionTimeMorningString());
        assertEquals("02:00", persistence.readIngestionTimeMiddayString());
        assertEquals("03:00", persistence.readIngestionTimeEveningString());
        assertEquals("04:00", persistence.readIngestionTimeNightString());

        assertEquals("Thu Jan 01 01:00:00 CET 1970", persistence.readIngestionTimeMorning().toString());
        assertEquals("Thu Jan 01 02:00:00 CET 1970", persistence.readIngestionTimeMidday().toString());
        assertEquals("Thu Jan 01 03:00:00 CET 1970", persistence.readIngestionTimeEvening().toString());
        assertEquals("Thu Jan 01 04:00:00 CET 1970", persistence.readIngestionTimeNight().toString());
    }
}
