package guifx;

import enums.eIngestedStatus;
import enums.eIngestionTime;
import init.Init;
import javafx.collections.ObservableList;
import org.apache.commons.io.IOUtils;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import persistenceXML.PersistenceMedIngObject;
import ukf2fhir.UKFToFHIRParser;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Date;

import static org.junit.Assert.*;

public class MainWindowTest {

    private static MainWindow mw;

    @BeforeClass
    public static void prepareTest(){

        // Build Path
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
        UKFToFHIRParser ukfToFHIRParser = new UKFToFHIRParser(Init.dbFile, Init.FHIRFile);
        ukfToFHIRParser.parsing(targetFileStr);
        ukfToFHIRParser.print();

        Init.initialize();

        mw = new MainWindow();
        mw.init();
    }

    @AfterClass
    public static void cleanUp(){
        Init.FHIRFile.delete();
        Init.persistenceFile.delete();
    }

    @Test
    public void init() {

        MainWindow mainWindow = new MainWindow();
        mainWindow.init();
        assertFalse("Section must not be empty", mainWindow.sections.isEmpty());
        assertFalse("MetaInformation must not be empty", mainWindow.metaInformation == null);
    }


    @Test
    public void getNewPersistenceEntry() {

        MedicationFX medFx = new MedicationFX();
        medFx.setReferenceMedicationStatement("");
        PersistenceMedIngObject po = MainWindow.getNewPersistenceEntry(medFx, new Date(), new Date(), new Date(), eIngestionTime.MORNING, eIngestedStatus.DEFAULT);

        assertTrue("PersistenceMedIngObject must not be null", po != null);
    }


    @Test
    public void getData() {

        ObservableList<MedicationFX> list;

        list = mw.getData(eIngestionTime.UNDEFINED, new Date(), false, false);
        assertTrue("10 ingestions", list.size() == 4);

        list = mw.getData(eIngestionTime.MORNING, new Date(), false, false);
        assertTrue("2 ingestions", list.size() == 2);

        list = mw.getData(eIngestionTime.MIDDAY, new Date(), false, false);
        assertTrue("No ingestion", list.isEmpty());

    }
}