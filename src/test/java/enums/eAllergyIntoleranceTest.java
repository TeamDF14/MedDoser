package enums;
import org.junit.*;

import static junit.framework.TestCase.assertEquals;

public class eAllergyIntoleranceTest {

    @Test
    public void equalsAllergyIntoleranceNameTest() {

        assertEquals("Allergie", eAllergyIntolerance.translateAllergyIntolerance("allergy"));
        assertEquals("Intoleranz", eAllergyIntolerance.translateAllergyIntolerance("intolerance"));
        assertEquals(null, eAllergyIntolerance.translateAllergyIntolerance("unknown"));
    }

}
