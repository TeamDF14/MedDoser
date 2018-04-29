package ukfparser;

import init.Init;
import org.apache.commons.io.IOUtils;
import org.junit.Test;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import static org.junit.Assert.assertTrue;

public class UKFToFHIRParserTest {


    @Test
    public void parseSinglePageBMPTest(){
        // Build Path
        Init.adjustPaths();

        // Read the new ukf string from txt file
        FileInputStream fileInputStream = null;
        String targetFileStr = null;
        try {
            fileInputStream = new FileInputStream(System.getProperty("user.dir") + "\\src\\test\\java\\medicationplaninterpreter\\UKF_TestfallNr16.txt");
            targetFileStr = IOUtils.toString(fileInputStream, "UTF-8");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e){
            e.printStackTrace();
        }

        // Convert the ukf string to a HL7 FHIR file
        UKFToFHIRParser ukfToFHIRParser = new UKFToFHIRParser();
        ukfToFHIRParser.parsing(targetFileStr);
        ukfToFHIRParser.print();

        assertTrue(Init.newInputFile.exists());
    }
}
