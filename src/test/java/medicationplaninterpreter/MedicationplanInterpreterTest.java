package medicationplaninterpreter;

import init.Init;
import org.apache.commons.io.IOUtils;
import org.junit.*;
import org.junit.runners.MethodSorters;
import ukfparser.UKFToFHIRParser;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertTrue;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class MedicationplanInterpreterTest {

    @BeforeClass
    public static void initMedicationplanInterpreter() {
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
    }

    @AfterClass
    public static void cleanMedicationplanInterpreter() {
        // Delete created files
        Init.newInputFile.delete();
    }

    @Test
    public void authorInformationTest() {
        BMPInterpreter BMPInterpreter = new BMPInterpreter();
        Author author = BMPInterpreter.getAuthorInformation();

        assertEquals("165630445", author.getLifelongDoctorNumber());
        assertEquals("", author.getPharmacyIdentificationNumber());
        assertEquals("", author.getHospitalIdentificationNumber());
        assertEquals("Dr. Xra Überall", author.getNameOfExhibitor());
        assertEquals("Hauptstraße 55", author.getStreet());
        assertEquals("01234", author.getPostCode());
        assertEquals("Am Ort", author.getCity());
        assertEquals("04562-12345", author.getTelephoneNumber());
        assertEquals("m.ueberall@mein-netz.de", author.geteMailAdress());
    }

    @Test
    public void patientInformationTest(){
        BMPInterpreter BMPInterpreter = new BMPInterpreter();
        Patient patient = BMPInterpreter.getPatient();

        assertEquals("Testfall", patient.getName());
        assertEquals("Nr. 16", patient.getSurname());
        assertEquals("E998982310", patient.getEGK());
        assertEquals("Männlich", patient.getGender());
        assertEquals("1974-04-20", patient.getBirthday());
    }


    // Creation date and instance ID has not been tested because it is always regenerated
    @Test
    public void metaInformationTest(){
        BMPInterpreter BMPInterpreter = new BMPInterpreter();
        MetaInformation metaInformation = BMPInterpreter.getMetaInformation();

        assertEquals("023", metaInformation.getVersion());
        assertEquals("64C313FEEE974DAF8EEA4609E155C2A8", metaInformation.getInstanceID());
        assertEquals("de-DE", metaInformation.getLanguage());
    }

    @Test
    public void getCustodianTest(){
        BMPInterpreter BMPInterpreter = new BMPInterpreter();
        Custodian custodian = BMPInterpreter.getCustodian();

        assertEquals(null, custodian.getIdentifierSystem());
        assertEquals(null, custodian.getIdentifierValue());
    }

    @Test
    public void getObservationOfPatientTest(){
        BMPInterpreter BMPInterpreter = new BMPInterpreter();
        ClinicalParameter clinicalParameter = BMPInterpreter.getClinicalParameter();

        assertEquals("82 kg", clinicalParameter.getWeight());
        assertEquals(null, clinicalParameter.getSize());
        assertEquals(null, clinicalParameter.getCreatinine());
        assertEquals(null, clinicalParameter.getComplaints());
    }

    @Test
    public void getHealthConcernsTest(){
        BMPInterpreter BMPInterpreter = new BMPInterpreter();
        HealthConcerns healthConcerns = BMPInterpreter.getHealthConcernsInformation();

        // Check pregnancy
        assertEquals(null, healthConcerns.getPregnancyLoincCode());
        assertEquals(null, healthConcerns.getPregnancyName());
        assertEquals(null, healthConcerns.getPregnancyStatus());
        assertTrue(!healthConcerns.isbPregnancyStatus());

        // Check breast feeding
        assertEquals(null, healthConcerns.getBreastfeedingName());
        assertEquals(null, healthConcerns.getBreastfeedingStatus());
        assertTrue(!healthConcerns.isbBreastfeeding());
    }

    @Test
    public void getAllergiesTest(){
        BMPInterpreter BMPInterpreter = new BMPInterpreter();
        AllergyIntolerance allergyIntolerance = BMPInterpreter.getAllergyIntoleranceInformation();

        String[] allergyName = allergyIntolerance.getAllergyName();
        String[] allergyType = allergyIntolerance.getAllergyType();

        assertEquals(null, allergyName);
        assertEquals(null, allergyType);
    }

    @Test
    public void getSectionsTest(){
        BMPInterpreter BMPInterpreter = new BMPInterpreter();
        ArrayList<Section> arrayListSection = BMPInterpreter.getSections();

        // Check length of title
        assertTrue(arrayListSection.size() == 2);

        // Check first section title
        assertEquals("Dauermedikation", arrayListSection.get(0).getTitle());
        assertEquals("412", arrayListSection.get(0).getTitleCode());

        // Get length of medication of first section
        assertTrue("Anzahl der Medikamente stimmt nicht",arrayListSection.get(0).getMedications().size() == 5);

        // Check second section title
        assertEquals("Selbstmedikation", arrayListSection.get(1).getTitle());
        assertEquals("418", arrayListSection.get(1).getTitleCode());

        // Get length of medication of second section
        assertTrue(arrayListSection.get(1).getMedications().size() == 1);
    }

    @Test
    public void getAdditionalNoteTest(){
        BMPInterpreter BMPInterpreter = new BMPInterpreter();
        AdditionalNote additionalNote = BMPInterpreter.getAdditionalNotesUnderMedicationTable();

        assertEquals(null, additionalNote.getNote());
    }

    @Test
    public void refreshHL7DocumentContent(){
        BMPInterpreter BMPInterpreter = new BMPInterpreter();

        assertTrue(BMPInterpreter.bRefreshHL7DocumentContent());
    }

    // The method begins with z, because it should be the last test
    @Test
    public void z_outputFileNotExists(){

        boolean caseOneDone = false;
        File oldPath = Init.newInputFile;

        for(int i = 0; i < 2; i++) {

            if(caseOneDone){
                // manipulate path
                Init.newInputFile = null;
                assertEquals(null, Init.newInputFile);
            } else{
                // manipulate path
                Init.newInputFile = new File(Init.newInputFile + "FileNotExist");
                assertTrue(!Init.newInputFile.exists());
                caseOneDone = true;
            }


            // Get object of all objects
            BMPInterpreter BMPInterpreter = new BMPInterpreter();

            // Get meta information
            MetaInformation metaInformation = BMPInterpreter.getMetaInformation();
            assertEquals(null, metaInformation.getVersion());
            assertEquals(null, metaInformation.getInstanceID());
            assertEquals(null, metaInformation.getLanguage());
            assertEquals(null, metaInformation.getCreationDate());

            // Get patient information
            Patient patient = BMPInterpreter.getPatient();
            assertEquals(null, patient.getName());
            assertEquals(null, patient.getSurname());
            assertEquals(null, patient.getBirthday());
            assertEquals(null, patient.getBirthdayAsDate());
            assertEquals(null, patient.getEGK());
            assertEquals(null, patient.getGender());

            // Get autor information
            Author author = BMPInterpreter.getAuthorInformation();
            assertEquals(null, author.getLifelongDoctorNumber());
            assertEquals(null, author.getPharmacyIdentificationNumber());
            assertEquals(null, author.getNameOfExhibitor());
            assertEquals(null, author.getStreet());
            assertEquals(null, author.getPostCode());
            assertEquals(null, author.getCity());
            assertEquals(null, author.getTelephoneNumber());
            assertEquals(null, author.geteMailAdress());
            assertEquals(null, author.getCreationDate());

            // Get custodian information
            Custodian custodian = BMPInterpreter.getCustodian();
            assertEquals(null, custodian.getIdentifierValue());
            assertEquals(null, custodian.getIdentifierSystem());

            // Get observation information
            ClinicalParameter clinicalParameter = BMPInterpreter.getClinicalParameter();
            assertEquals(null, clinicalParameter.getWeight());
            assertEquals(null, clinicalParameter.getSize());
            assertEquals(null, clinicalParameter.getCreatinine());
            assertEquals(null, clinicalParameter.getComplaints());

            // Get allergyIntolerance
            AllergyIntolerance allergyIntolerance = BMPInterpreter.getAllergyIntoleranceInformation();
            assertEquals(null, allergyIntolerance.getAllergyName());
            assertEquals(null, allergyIntolerance.getAllergyType());
            assertEquals(0, allergyIntolerance.getLength());

            // Get health concerns
            HealthConcerns healthConcerns = BMPInterpreter.getHealthConcernsInformation();
            assertEquals(null, healthConcerns.getPregnancyLoincCode());
            assertEquals(null, healthConcerns.getPregnancyName());
            assertEquals(null, healthConcerns.getPregnancyStatus());
            assertTrue(!healthConcerns.isbPregnancyStatus());
            assertEquals(null, healthConcerns.getPregnancyReference());
            assertEquals(null, healthConcerns.getBreastfeedingName());
            assertEquals(null, healthConcerns.getPregnancyStatus());
            assertTrue(!healthConcerns.isbBreastfeeding());
            assertEquals(null, healthConcerns.getBreastfeedingReference());

            // Get medication
            ArrayList<Section> sectionArrayList = BMPInterpreter.getSections();
            assertEquals(null, sectionArrayList);

            // Get additional notes
            AdditionalNote additionalNote = BMPInterpreter.getAdditionalNotesUnderMedicationTable();
            assertEquals(null, additionalNote.getNote());
        }

        // restore manipulated path
        Init.newInputFile = oldPath;

        // Check if the path was manipulated correctly
        assertTrue(Init.newInputFile.exists());
        assertTrue(oldPath.exists());
    }
}
