package help;

import init.Init;
import org.apache.commons.io.IOUtils;
import org.junit.BeforeClass;
import org.junit.Test;
import ukfparser.UKFToFHIRParser;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import static org.junit.Assert.*;

public class HelpTest {

    Calendar cal1 = new GregorianCalendar(util.Date.timeZone);
    Calendar cal2 = new GregorianCalendar(util.Date.timeZone);


    @Test
    public void bCheckFileExists() {

        Init.adjustPaths();

        Init.newInputFile.delete();
        Init.persistenceFile.delete();

        assertFalse("File must not exist", util.FileSystem.bCheckFileExists(Init.newInputFile));
        assertFalse("File must not exist", util.FileSystem.bCheckFileExists(Init.persistenceFile));

        Init.adjustPaths();
        Init.persistenceFile.delete();

        // Build Path
        Init.adjustPaths();

        // Read the new ukf string from txt file
        FileInputStream fisTargetFile = null;
        String targetFileStr = null;
        try {
            fisTargetFile = new FileInputStream(System.getProperty("user.dir") + "\\src\\test\\java\\medicationplaninterpreter\\UKF_TestfallNr16.txt");
            targetFileStr = IOUtils.toString(fisTargetFile, "UTF-8");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e){
            e.printStackTrace();
        }

        // Convert the ukf string to a HL7 FHIR file
        UKFToFHIRParser ukfToFHIRParser = new UKFToFHIRParser();
        ukfToFHIRParser.parsing(targetFileStr);
        ukfToFHIRParser.print();

        Init.initialize();

        assertTrue("File must exist", util.FileSystem.bCheckFileExists(Init.newInputFile));
        assertTrue("File must exist", util.FileSystem.bCheckFileExists(Init.persistenceFile));

    }

    @Test
    public void bIsDayEqual() {

        cal1.setTime(new Date());
        cal2.setTime(new Date());
        assertTrue("Days must be equal", util.Date.bIsDayEqual(cal1.getTime(), cal2.getTime()));

        cal2.set(Calendar.HOUR_OF_DAY, cal1.get(Calendar.HOUR_OF_DAY) +1 );
        assertTrue("Days must be equal", util.Date.bIsDayEqual(cal1.getTime(), cal2.getTime()));

        cal2.set(Calendar.DAY_OF_YEAR, cal1.get(Calendar.DAY_OF_YEAR) + 1);
        assertFalse("Days must not be equal", util.Date.bIsDayEqual(cal1.getTime(), cal2.getTime()));

        cal2.set(Calendar.MONTH, cal1.get(Calendar.MONTH) + 1);
        assertFalse("Days must not be equal", util.Date.bIsDayEqual(cal1.getTime(), cal2.getTime()));

        cal2.set(Calendar.YEAR, cal1.get(Calendar.YEAR) + 1);
        assertFalse("Days must not be equal", util.Date.bIsDayEqual(cal1.getTime(), cal2.getTime()));

        assertFalse("Days must not be equal", util.Date.bIsDayEqual(null, null));
        assertFalse("Days must not be equal", util.Date.bIsDayEqual(null, new Date()));
        assertFalse("Days must not be equal", util.Date.bIsDayEqual(new Date(), null));

    }
}