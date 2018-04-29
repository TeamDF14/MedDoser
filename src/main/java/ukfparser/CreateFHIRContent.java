package ukfparser;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.model.dstu2.resource.*;
import ca.uhn.fhir.model.dstu2.valueset.BundleTypeEnum;
import init.Init;
import org.w3c.dom.Document;

import java.io.*;
import java.util.ArrayList;
import java.util.logging.Level;

import static logging.Logging.logger;

/**
 * The FHIR document is created in the class.
 * All relevant blocks are called and the result is a medication plan in format 'HL7 FHIR'.
 */
public class CreateFHIRContent {

    /**
     * Declare the UKFToFHIRParser variable
     */
    private UKFToFHIRParser UKFToFHIRParser;

    // Composition
    private Composition composition = null;
    // Patient
    private Patient patient = null;
    // Author/Doctor
    private Practitioner practitioner = null;
    // Organization
    private Organization organization = null;
    // Clinical parameter
    private Composition.Section compositionClinicalParameter = null;
    private Composition compositionClinicalPara = null;
    private Observation observationClinicalParaWeight = null;
    private Observation observationClinicalParaHeight = null;
    private Observation observationClinicalParaCreatinine = null;
    // Allergy and intolrance
    private Composition.Section compSecAllergyInterlorance = null;
    private Composition compositionAllergie = null;
    private AllergyIntolerance allergy = null;
    private AllergyIntolerance intolerance = null;
    // Health concerns
    private Composition.Section compSecHealthConcerns = null;
    private Composition compositionHealthConcerns = null;
    private Observation observationPregngancyStatus = null;
    private Observation observationbreastfeedingStatus = null;
    // Sections
    private Composition compositionMedicationSection = null;
    private Composition.Section compSecMedicationSection = null;
    // MedicationStatement and medication
    private ArrayList<MedicationStatement> medicationStatementArrayList = null;
    private ArrayList<Medication> medicationArrayList = null;
    // Free text
    private Composition.Section compSecFreeText = null;

    /**
     * Initializes the variables.
     * @param UKFToFHIRParser An instance of the UKF to FHIR parser.
     */
    public CreateFHIRContent(final UKFToFHIRParser UKFToFHIRParser){
        this.UKFToFHIRParser = UKFToFHIRParser;
    }

    /**
     * <p>In this method, the objects that are filled with the information of the UKF string will be generated.</p>
     *
     * @param ukfStringArrayList An ArrayList<String> with all UKF strings in type Document.
     */
    protected void generateUKFObjects(ArrayList<Document> ukfStringArrayList) {

        this.UKFToFHIRParser.setUKFStringArrayList(ukfStringArrayList);

        // Composition
        if(this.composition == null) {
            this.composition = this.UKFToFHIRParser.getFhirMedicationPlan();
        }

        // Patient
        if(this.patient == null){
            this.patient = this.UKFToFHIRParser.getFhirPatient();
        }

        // Autor
        if(this.practitioner == null){
            this.practitioner = this.UKFToFHIRParser.getFhirAutor();
        }

        // Organization
        if(this.organization == null) {
            this.organization = this.UKFToFHIRParser.getFhirOrganization();
        }

        // Clinical parameter
        if(this.observationClinicalParaWeight == null || this.observationClinicalParaHeight == null || this.observationClinicalParaCreatinine == null) {
            this.compositionClinicalParameter = this.UKFToFHIRParser.getFhirCompositionClinicalParameter();
            this.compositionClinicalPara = new Composition();
            this.observationClinicalParaWeight = this.UKFToFHIRParser.getObservationClinicalParaWeight();
            this.observationClinicalParaHeight = this.UKFToFHIRParser.getObservationClinicalParaHeight();
            this.observationClinicalParaCreatinine = this.UKFToFHIRParser.getObservationClinicalParacCeatinine();
        }

        // Allergy and intolerance
        if(this.allergy == null || this.intolerance == null) {
            this.compSecAllergyInterlorance = this.UKFToFHIRParser.getFhirCompositionAllergyIntolerance();
            this.compositionAllergie = new Composition();
            this.allergy = this.UKFToFHIRParser.getFhirAllergy();
            this.intolerance = this.UKFToFHIRParser.getFhirIntolerance();
        }

        // Health concerns
        if(this.observationPregngancyStatus == null || this.observationbreastfeedingStatus == null) {
            this.compSecHealthConcerns = this.UKFToFHIRParser.getCompositionHealthConcern();
            this.compositionHealthConcerns = new Composition();
            this.observationPregngancyStatus = this.UKFToFHIRParser.getObservationPregnancyStatus();
            this.observationbreastfeedingStatus = this.UKFToFHIRParser.getObservationBreastfeedingStatus();
        }

        // Sections, MedicationStatement and medication
        if(this.compSecMedicationSection == null && this.medicationStatementArrayList == null && this.medicationArrayList == null) {
            // Sections
            this.compositionMedicationSection = new Composition();
            this.compSecMedicationSection = this.UKFToFHIRParser.getCompositionMedication();

            // MedicationStatement and medication
            this.medicationStatementArrayList = this.UKFToFHIRParser.getFhirMedicationStatement();
            this.medicationArrayList = this.UKFToFHIRParser.getFhirMedication();
        }

        // Free text
        this.compSecFreeText = this.UKFToFHIRParser.getFhirFreetext();
    }

    /**
     * Creates a bundle from each single block which is afterwards saved in a string.
     * @return The HL7 FHIR XML as string.
     */
    protected String start(){

        // Create bundle to save the information as HL7 Fhir documentation
        Bundle bundle = new Bundle();
        bundle.setType(BundleTypeEnum.TRANSACTION);

        ///////////
        // START to bundle the blocks
        ///////////

        // Add metadata
        //Composition composition = this.UKFToFHIRParser.getFhirMedicationPlan();
        bundle.addEntry()
                .setFullUrl(composition.getId().getValue())
                .setResource(composition).getRequest();

        // Add patient
        //Patient patient = this.UKFToFHIRParser.getFhirPatient();
        bundle.addEntry()
                .setFullUrl(patient.getId().getValue())
                .setResource(patient).getRequest();

        // Add author
        //Practitioner practitioner = this.UKFToFHIRParser.getFhirAutor();
        bundle.addEntry()
                .setFullUrl(practitioner.getId().getValue())
                .setResource(practitioner).getRequest();

        // Add organization
        //Organization organization = this.UKFToFHIRParser.getFhirOrganization();
        if(organization != null) {
            bundle.addEntry()
                    .setFullUrl(organization.getId().getValue())
                    .setResource(organization).getRequest();
        }

        // Add clinical composition
        //Composition.Section compositionClinicalParameter = this.UKFToFHIRParser.getFhirCompositionClinicalParameter();
        if(compositionClinicalParameter != null) {
            //Composition compositionClinicalPara = new Composition();
            compositionClinicalPara.addSection(compositionClinicalParameter);
            bundle.addEntry()
                    .setFullUrl(compositionClinicalPara.getId().getValue())
                    .setResource(compositionClinicalPara).getRequest();

            // Add observation - clinical parameter - body weight
            //Observation observationClinicalParaWeight = this.UKFToFHIRParser.getObservationClinicalParaWeight();
            bundle.addEntry()
                    .setFullUrl(observationClinicalParaWeight.getId().getValue())
                    .setResource(observationClinicalParaWeight).getRequest();

            // Add observation - clinical parameter - body height
            //Observation observationClinicalParaHeight = this.UKFToFHIRParser.getObservationClinicalParaHeight();
            bundle.addEntry()
                    .setFullUrl(observationClinicalParaHeight.getId().getValue())
                    .setResource(observationClinicalParaHeight).getRequest();

            // Add observation - clinical parameter - creatinin value
            //Observation observationClinicalParaCreatinine = this.UKFToFHIRParser.getObservationClinicalParacCeatinine();
            bundle.addEntry()
                    .setFullUrl(observationClinicalParaCreatinine.getId().getValue())
                    .setResource(observationClinicalParaCreatinine).getRequest();
        }

        // Add allergy composition
        //Composition.Section compSecAllergyInterlorance = this.UKFToFHIRParser.getFhirCompositionAllergyIntolerance();
        if(compSecAllergyInterlorance != null) {
            //Composition compositionAllergie = new Composition();
            compositionAllergie.addSection(compSecAllergyInterlorance);
            bundle.addEntry()
                    .setFullUrl(compositionAllergie.getId().getValue())
                    .setResource(compositionAllergie).getRequest();

            // Add observation - allergy - allergy
            //AllergyIntolerance allergy = this.UKFToFHIRParser.getFhirAllergy();
            bundle.addEntry()
                    .setFullUrl(allergy.getId().getValue())
                    .setResource(allergy).getRequest();

            // Add observation - allergy - intolerance
            //AllergyIntolerance intolerance = this.UKFToFHIRParser.getFhirIntolerance();
            bundle.addEntry()
                    .setFullUrl(intolerance.getId().getValue())
                    .setResource(intolerance).getRequest();
        }

        // Add composition health concerns
        //Composition.Section compSecHealthConcerns = this.UKFToFHIRParser.getCompositionHealthConcern();
        if(compSecHealthConcerns != null) {
            //Composition compositionHealthConcerns = new Composition();
            compositionHealthConcerns.addSection(compSecHealthConcerns);
            bundle.addEntry()
                    .setFullUrl(compositionHealthConcerns.getId().getValue())
                    .setResource(compositionHealthConcerns).getRequest();

            // Add observation - health concern - pregnancy status
            //Observation observationPregngancyStatus = this.UKFToFHIRParser.getObservationPregnancyStatus();
            bundle.addEntry()
                    .setFullUrl(observationPregngancyStatus.getId().getValue())
                    .setResource(observationPregngancyStatus).getRequest();

            // Add observation - health concern - breast feeding status
            //Observation observationbreastfeedingStatus = this.UKFToFHIRParser.getObservationBreastfeedingStatus();
            bundle.addEntry()
                    .setFullUrl(observationbreastfeedingStatus.getId().getValue())
                    .setResource(observationbreastfeedingStatus).getRequest();
        }

        // Add composition of medication sections
        //Composition compositionMedicationSection = new Composition();
        //Composition.Section compSecMedicationSection = this.UKFToFHIRParser.getCompositionMedication();
        compositionMedicationSection.addSection(compSecMedicationSection);
        bundle.addEntry()
                .setFullUrl(compositionMedicationSection.getId().getValue())
                .setResource(compositionMedicationSection).getRequest();

        // Add medication statement and medication
        //ArrayList<MedicationStatement> medicationStatementArrayList = this.UKFToFHIRParser.getFhirMedicationStatement();
        //ArrayList<Medication> medicationArrayList = this.UKFToFHIRParser.getFhirMedication();
        for(int i = 0; i < medicationStatementArrayList.size(); i++) {
            // Add medication statement
            bundle.addEntry().setFullUrl(medicationStatementArrayList.get(i).getId().getValue()).setResource(medicationStatementArrayList.get(i)).getRequest();

            // Add medication
            bundle.addEntry().setFullUrl(medicationArrayList.get(i).getId().getValue()).setResource(medicationArrayList.get(i)).getRequest();
        }

        // Add free text without relation to the medication
        Composition.Section compSecFreeText = this.UKFToFHIRParser.getFhirFreetext();
        if(compSecFreeText != null) {
            Composition compositionFreeText = new Composition();
            compositionFreeText.addSection(compSecFreeText);
            bundle.addEntry()
                    .setFullUrl(compositionFreeText.getId().getValue())
                    .setResource(compositionFreeText).getRequest();
        }

        ///////////
        // END
        ///////////

        // Print bundle as String
        FhirContext ctxfirst = FhirContext.forDstu2();
        ctxfirst.newXmlParser().setPrettyPrint(true).encodeResourceToString(bundle);
        String ctx = ctxfirst.newXmlParser().setPrettyPrint(true).encodeResourceToString(bundle);

        return ctx;
    }

    /**
     * Prints the FHIR content to a xml file.
     *
     * @param FHIRContent The HL7 FHIR XML document.
     * @return True if the write task was successful, false if not.
     */
    protected boolean print(final String FHIRContent) {

        BufferedWriter output = null;
        try {

            // Define the target file where the FHIRContent should be passed to
            File file = Init.newInputFile;

            // Set up a buffered writer including the encoding (important!)
            output = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), Init.encoding));

            // Start the write task
            output.write(FHIRContent);

        } catch ( IOException e ) {
            e.printStackTrace();
            logger.log(Level.FINEST, "Cannot print HL7 FHIR XML file (" + e + ")\n");
            return false;

        } finally {

            if ( output != null ) {
                try {
                    output.close();
                    logger.log(Level.INFO, "Passing the FHIR content to the XML file was successful. \n");
                } catch (IOException e) {
                    e.printStackTrace();
                    return false;
                }
            }
        }
        return true;
    }
}
