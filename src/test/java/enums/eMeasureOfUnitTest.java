package enums;

import org.junit.Test;

import static junit.framework.TestCase.assertEquals;

public class eMeasureOfUnitTest {

    @Test
    public void toStringTest() {
        assertEquals("1", eMeasureOfUnit.STUECK.toString());
        assertEquals("p", eMeasureOfUnit.IE.toString());
        assertEquals("q", eMeasureOfUnit.CM.toString());
        assertEquals("s", eMeasureOfUnit.ML.toString());
        assertEquals("5", eMeasureOfUnit.HUB.toString());
        assertEquals("6", eMeasureOfUnit.TROPFEN.toString());
        assertEquals("4", eMeasureOfUnit.BEUTEL.toString());

    }

    @Test
    public void converToStringTest(){
        assertEquals("Stueck", eMeasureOfUnit.converToString("1"));
        assertEquals("IE", eMeasureOfUnit.converToString("p"));
        assertEquals("cm", eMeasureOfUnit.converToString("q"));
        assertEquals("ml", eMeasureOfUnit.converToString("s"));
        assertEquals("HUB", eMeasureOfUnit.converToString("5"));
        assertEquals("Tropfen", eMeasureOfUnit.converToString("6"));
        assertEquals("Beutel", eMeasureOfUnit.converToString("4"));
    }
}
