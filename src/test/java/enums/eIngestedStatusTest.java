package enums;

import org.junit.Test;

import static junit.framework.TestCase.assertEquals;

public class eIngestedStatusTest {


    @Test
    public void toStringTest() {
        assertEquals("0", eIngestedStatus.DECLINED.toString());
        assertEquals("1", eIngestedStatus.INGESTED.toString());
        assertEquals("2", eIngestedStatus.DEFAULT.toString());
        assertEquals("Keine Angabe", eGender.convertUKFGenderName("Wrong input"));
    }

    @Test
    public void fromStringTest(){
        assertEquals(eIngestedStatus.DECLINED, eIngestedStatus.fromString("0"));
        assertEquals(eIngestedStatus.INGESTED, eIngestedStatus.fromString("1"));
        assertEquals(eIngestedStatus.DEFAULT, eIngestedStatus.fromString("2"));
    }

}
