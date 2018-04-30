package persistenceTest;

import enums.eIngestedStatus;
import enums.eIngestionTime;
import org.junit.Test;
import persistenceXML.PersistenceMedIngObject;
import help.Help;

import static org.junit.Assert.assertEquals;

public class PersistenceMedIngObjectTest {

    @Test
    public void PersistenceMedIngEmptyObjectTest(){

        PersistenceMedIngObject persistenceMedIngObject = new PersistenceMedIngObject();

        assertEquals(null, persistenceMedIngObject.getMedStatementReference());
        assertEquals(null, persistenceMedIngObject.getScheduledTimeCode());
        assertEquals(null, persistenceMedIngObject.getScheduledTime());
        assertEquals(eIngestedStatus.DEFAULT, persistenceMedIngObject.getStatus());
        assertEquals(null, persistenceMedIngObject.getScheduledDate());
        assertEquals(null, persistenceMedIngObject.getTime());
    }

    @Test
    public void PersistenceMedIngFilledObjectTest(){
        PersistenceMedIngObject persistenceMedIngObject = new PersistenceMedIngObject();

        persistenceMedIngObject.setMedStatementReference("urn:uuid:92e56a57-15fa-48c6-aebf-08a1bc67b9e1");
        persistenceMedIngObject.setScheduledTimeCode(eIngestionTime.EVENING);
        persistenceMedIngObject.setScheduledTime(util.Date.convertStringToTime("08:00"));
        persistenceMedIngObject.setStatus(eIngestedStatus.DECLINED);
        persistenceMedIngObject.setScheduledDate(util.Date.convertStringToDate("04/25/2018"));
        persistenceMedIngObject.setTime(util.Date.convertStringToTime("13:37"));

        assertEquals("urn:uuid:92e56a57-15fa-48c6-aebf-08a1bc67b9e1", persistenceMedIngObject.getMedStatementReference());
        assertEquals("PCV", persistenceMedIngObject.getScheduledTimeCode().toString());
        assertEquals("08:00", util.Date.convertTimeToString(persistenceMedIngObject.getScheduledTime()));
        assertEquals(eIngestedStatus.DECLINED, persistenceMedIngObject.getStatus());
        assertEquals("04/25/2018", util.Date.convertDateToString(persistenceMedIngObject.getScheduledDate(), true));
        assertEquals("13:37", util.Date.convertTimeToString(persistenceMedIngObject.getTime()));
    }
}
