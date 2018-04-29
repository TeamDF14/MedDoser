package medicationplaninterpreter;

import java.util.ArrayList;

/**
 * This class can be considered as a model class.
 * Getters are provided in order to work with the information that you get from the HL7 FHIR XML file.
 */
public class BMPInterpreter {

    /**
     * Declare an object
     */
    private CollectHL7FHIRInformation collectHL7FHIRInformation;

    /**
     * The constructor creates an instance.
     */
    public BMPInterpreter(){

        // Creates an instance
        this.collectHL7FHIRInformation = new CollectHL7FHIRInformation();
    }

    /**
     * Returns a full patient object.
     *
     * @return A filled patient object.
     */
    public Patient getPatient() {
        return collectHL7FHIRInformation.patientInformation();
    }

    /**
     * Returns a filled object with information about the author/doctor.
     *
     * @return A filled Author object.
     */
    public Author getAuthorInformation(){
        return collectHL7FHIRInformation.authorInformation();
    }

    /**
     * Returns a filled object with information about the custodian / company.
     *
     * @return A filled Custodian object.
     */
    public Custodian getCustodian(){
        return collectHL7FHIRInformation.custodianInformation();
    }

    /**
     * Returns a full object with the clinical parameters.
     *
     * @return A filled ClinicalParameter object.
     */
    public ClinicalParameter getClinicalParameter(){
        return collectHL7FHIRInformation.clinicalParameter();
    }

    /**
     * Returns a full object about allergies of patient.
     *
     * @return A filled AllergyIntolerance object.
     */
    public AllergyIntolerance getAllergyIntoleranceInformation() {
        return collectHL7FHIRInformation.allergyIntoleranceInformation();
    }

    /**
     * Returns a full object about health concerns of patient.
     *
     * @return A filled HealthConcerns object.
     */
    public HealthConcerns getHealthConcernsInformation() {
        return collectHL7FHIRInformation.healthConcernsInformation();
    }

    /**
     * Returns the titles and tiles code in a Section object.
     *
     * @return A filled Section object
     */
    public ArrayList<Section> getSections( ) {
        return collectHL7FHIRInformation.sectionTitles();
    }

    /**
     * Returns the meta information about the HL7 FHIR document.
     *
     * @return A filled MetaInformation object
     */
    public MetaInformation getMetaInformation() {
        return collectHL7FHIRInformation.getMetaInformation();
    }

    /**
     * Returns the additional notes and free text under the medication plan.
     *
     * @return A filled AdditionalNote object
     */
    public AdditionalNote getAdditionalNotesUnderMedicationTable() {
        return collectHL7FHIRInformation.additionalNotesUnderMedicationTable();
    }

    /**
     * This method is used to refresh the content of the HL7 FHIR document,
     * which will be parsed in the ArrayList<Document> within the package 'fhirparser'.
     *
     * @return True if successful, false if not.
     */
    public boolean bRefreshHL7DocumentContent(){
        return collectHL7FHIRInformation.bRefreshDocumentContent();
    }
}