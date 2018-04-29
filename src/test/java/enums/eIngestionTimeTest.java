package enums;

import org.junit.Test;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertTrue;

public class eIngestionTimeTest {


    @Test
    public void bIsEqualToNameTest() {
        assertTrue(eIngestionTime.MORNING.bIsEqualToName("PCM"));
        assertTrue(eIngestionTime.MIDDAY.bIsEqualToName("PCD"));
        assertTrue(eIngestionTime.EVENING.bIsEqualToName("PCV"));
        assertTrue(eIngestionTime.NIGHT.bIsEqualToName("HS"));
    }

    @Test
    public void toStringTest() {
        assertEquals("PCM", eIngestionTime.MORNING.toString());
        assertEquals("PCD", eIngestionTime.MIDDAY.toString());
        assertEquals("PCV", eIngestionTime.EVENING.toString());
        assertEquals("HS", eIngestionTime.NIGHT.toString());
    }

    @Test
    public void fromStringTest(){
        assertEquals(eIngestionTime.MORNING, eIngestionTime.fromString("PCM"));
        assertEquals(eIngestionTime.MIDDAY, eIngestionTime.fromString("PCD"));
        assertEquals(eIngestionTime.EVENING, eIngestionTime.fromString("PCV"));
        assertEquals(eIngestionTime.NIGHT, eIngestionTime.fromString("HS"));
    }
}
