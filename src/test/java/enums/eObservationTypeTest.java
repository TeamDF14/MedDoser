package enums;

import org.junit.Test;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertTrue;

public class eObservationTypeTest {

    @Test
    public void equalsNameTest(){
        assertTrue(eObservationType.BODY_WEIGHT.equalsName("3142-7"));
        assertTrue(eObservationType.PATIENT_HEIGHT.equalsName("8302-2"));
        assertTrue(eObservationType.CREATININE_VALUE.equalsName("2160-0"));
        assertTrue(eObservationType.PREGNANCY_STATUS.equalsName("11449-6"));
        assertTrue(eObservationType.BREASTFEEDING_STATUS.equalsName("63895-7"));
    }


    @Test
    public void toStringTest() {
        assertEquals("3142-7", eObservationType.BODY_WEIGHT.toString());
        assertEquals("8302-2", eObservationType.PATIENT_HEIGHT.toString());
        assertEquals("2160-0", eObservationType.CREATININE_VALUE.toString());
        assertEquals("11449-6", eObservationType.PREGNANCY_STATUS.toString());
        assertEquals("63895-7", eObservationType.BREASTFEEDING_STATUS.toString());
    }

}
