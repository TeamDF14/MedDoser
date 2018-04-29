package enums;

import org.junit.Test;

import static junit.framework.TestCase.assertEquals;

public class eGenderTest {

    @Test
    public void convertUKFGenderNameTest() {

        assertEquals("Männlich", eGender.convertUKFGenderName("male"));
        assertEquals("Weiblich", eGender.convertUKFGenderName("female"));
        assertEquals("Unbestimmt", eGender.convertUKFGenderName("unknown"));
        assertEquals("Keine Angabe", eGender.convertUKFGenderName("Wrong input"));

    }
}
