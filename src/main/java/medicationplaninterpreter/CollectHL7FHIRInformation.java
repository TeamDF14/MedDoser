package medicationplaninterpreter;

import enums.eIngestionTime;
import enums.eObservationType;
import fhirparser.*;

import java.util.ArrayList;
import java.util.logging.Level;

import static logging.Logging.logger;

/**
 * This class disassembles the HL7 FHIR document and stores all information in suitable objects.
 */
public class CollectHL7FHIRInformation {

    /**
     * Defines an empty string.
     */
    private final String emptyString = null;

    /**
     * Declares an object of FHIRParser.
     */
    private FHIRParser FHIRParser = null;

    /**
     * The constructor creates an object of XYDSAF, which retrieves the information from the HL7 FHIR document.
     */
    protected CollectHL7FHIRInformation() {

        // To load the HL7 FHIR file
        FHIRParser = new FHIRParser();
    }

    /**
     * This method can be used to refresh the content of the HL7 FHIR document, which will be parsed in the ArrayList<Document> within the package 'fhirparser'.
     *
     * @return True if successful, false if not.
     */
    protected boolean bRefreshDocumentContent(){
        return FHIRParser.bReadHL7FhirDoc();
    }

    /**
     * <p>This method retrieves the meta information of the BMP and save it in the MetaInformation object.</p>
     * <b>MP = Metainformation of BMP</b>
     *
     * @return A filled MetaInformation object.
     */
    protected MetaInformation getMetaInformation(){

        // Get the information of HL7 FHIR document
        FHIRParserMetaInformation FHIRParserMetaInformation = new FHIRParserMetaInformation();
        MetaInformation metaInformation = new MetaInformation();
        ArrayList<String> arrayListMetaInformation = FHIRParserMetaInformation.getMetaInformation();

        if(arrayListMetaInformation != null) {
            // Set language. It is a required field
            metaInformation.setLanguage(arrayListMetaInformation.get(0));

            // Set version of BMP. It is a required field
            metaInformation.setVersion(arrayListMetaInformation.get(2));

            // Set instance of BMP. It is a required filed
            metaInformation.setInstanceID(arrayListMetaInformation.get(1));

            // Set creation date of composition (equals medicationplan)
            metaInformation.setCreationDate(arrayListMetaInformation.get(3));
        }

        return metaInformation;
    }

    /**
     * <p>This method retrieves the information from the patient and stores it in the patient object.</p>
     * <b>P = Parameter of UKF</b>
     *
     * @return The patient object.
     */
    protected Patient patientInformation(){

        // Get Information of HL7 FHIR document
        FHIRParserPatient FHIRParserPatient = new FHIRParserPatient();
        Patient patientInformation = new Patient();
        ArrayList<String> arrayListPatientInformation = FHIRParserPatient.getPatientInformation();

        if(arrayListPatientInformation != null) {
            // Name and surname are required fields
            patientInformation.setName(arrayListPatientInformation.get(1));
            patientInformation.setSurname(arrayListPatientInformation.get(2));

            // Set egk
            patientInformation.setEgk(arrayListPatientInformation.get(0));

            // Set gender
            patientInformation.setGender(arrayListPatientInformation.get(4));

            // Set birthday
            patientInformation.setBirthday(arrayListPatientInformation.get(3));
        }

        return patientInformation;
    }

    /**
     * <p>Get the informations about the author.</p>
     * <b>A = Parameter of UKF</b>
     *
     * @return The information about the author/doctor.
     */
    protected Author authorInformation() {

        // Get Information of HL7 FHIR document
        FHIRParserAutor FHIRParserAutor = new FHIRParserAutor();
        Author author = new Author();
        ArrayList<String> arrayListAutor = FHIRParserAutor.getAutorInformation();

        if(arrayListAutor != null) {
            // Set name of the exhibitor is a required field
            author.setNameOfExhibitor(arrayListAutor.get(0));

            // Set lifelong doctor number
            author.setLifelongDoctorNumber(arrayListAutor.get(1));

            // Set street
            author.setStreet(arrayListAutor.get(2));

            // Set post code
            author.setPostCode(arrayListAutor.get(3));

            // Set city
            author.setCity(arrayListAutor.get(4));

            // Set telephone number
            author.setTelephoneNumber(arrayListAutor.get(5));

            // Set eMail
            author.seteMailAdress(arrayListAutor.get(6));

            // Set creation date is a required filed
            author.setCreationDate(arrayListAutor.get(7));

            // Set pharmacy ID
            author.setPharmacyIdentificationNumber(arrayListAutor.get(8));

            // Set hospital ID
            author.setHospitalIdentificationNumber(arrayListAutor.get(9));
        }

        return author;
    }

    /**
     * Gets the information about the custodian and stores it in the object.
     *
     * @return A filled custodian object.
     */
    protected Custodian custodianInformation(){

        // Get Information of HL7 FHIR document
        FHIRParserCustodian FHIRParserCustodian = new FHIRParserCustodian();
        ArrayList<String> arrayListCustodian = FHIRParserCustodian.getCustodianInformation();
        Custodian custodian = new Custodian();

        // Here it is checked if any information is available
        if(arrayListCustodian != null){
            // Set identifier of the system
            custodian.setIdentifierSystem(arrayListCustodian.get(0));

            // Set value of the system identifier
            custodian.setIdentifierValue(arrayListCustodian.get(1));
        }

        return custodian;
    }

    /**
     * <p>Gets the informations about the clinical parameters.</p>
     * <b>O = Parameter of UKF</b>
     *
     * @return The informations about the observation of the patient.
     */
    protected ClinicalParameter clinicalParameter() {

        // Get Information of HL7 FHIR document
        FHIRParserObservation FHIRParserObservation = new FHIRParserObservation();
        ArrayList<ArrayList<String>> arrayListObservations = FHIRParserObservation.getObservationInformationen();
        ClinicalParameter clinicalParameter = new ClinicalParameter();

        // Here it is checked if any information is available
        if(arrayListObservations != null){
            // iterate through all saved observations
            for(int i = 0; i < arrayListObservations.size(); i++){
                // Get observation information
                ArrayList<String> observation = arrayListObservations.get(i);

                if(observation.get(1).equals(eObservationType.BODY_WEIGHT.toString())){
                    // Add body weight
                    clinicalParameter.setWeight(observation.get(4) + " " + observation.get(3));
                } else if(observation.get(1).equals(eObservationType.PATIENT_HEIGHT.toString())) {
                    // Add patient height
                    clinicalParameter.setSize(observation.get(4) + " " + observation.get(3));
                } else if(observation.get(1).equals(eObservationType.CREATININE_VALUE.toString())){
                    // Add creatinine value
                    clinicalParameter.setCreatinine(observation.get(4) + " " + observation.get(3));
                }
            }
        }

        return clinicalParameter;
    }

    /**
     * <p>Gets the information about the observation (allergies) of the patient.</p>
     * <b>O = Parameter of UKF</b>
     *
     * @return A filled object with allergies.
     */
    protected AllergyIntolerance allergyIntoleranceInformation(){

        // Get Information of HL7 FHIR document
        FHIRParserAllergy FHIRParserAllergy = new FHIRParserAllergy();
        ArrayList<ArrayList<String>> arrayListsAllergies = FHIRParserAllergy.getAllergyInformation();
        AllergyIntolerance allergyIntolerance = new AllergyIntolerance();

        // Here it is checked if any information is available
        if(arrayListsAllergies != null) {

            // Set the length of allergyIntolerance
            allergyIntolerance.setLength(arrayListsAllergies.size());

            for(int i = 0; i < allergyIntolerance.getLength(); i++){
                // Get allergy
                ArrayList<String> allergy = arrayListsAllergies.get(i);

                // Set name of allergy
                allergyIntolerance.setAllergyName(i, allergy.get(1));

                // Set type of allergy
                allergyIntolerance.setAllergyType(i, allergy.get(2));
            }
        }

        return allergyIntolerance;
    }

    /**
     * <p>Gets the information about the observation of the patient.</p>
     * <b>O = Parameter of UKF</b>
     *
     * @return A filled object with health concerns.
     */
    protected HealthConcerns healthConcernsInformation(){

        // Get Information of HL7 FHIR document
        FHIRParserHealthConcerns FHIRParserHealthConcerns = new FHIRParserHealthConcerns();
        ArrayList<ArrayList<String>> arrayListsHealthConcerns = FHIRParserHealthConcerns.getHealthConcernsInformation();
        HealthConcerns healthConcerns = new HealthConcerns();

        // Here it is checked if any information is available
        if(arrayListsHealthConcerns != null){

            for(int i = 0; i < arrayListsHealthConcerns.size(); i++){
                // Get single element of health concerns
                ArrayList<String> arrayListHealthConcern = arrayListsHealthConcerns.get(i);

                // Here is checked which case occurs
                if(arrayListHealthConcern.get(1).equals(eObservationType.PREGNANCY_STATUS.toString())){ // case pregnancy status
                    // Set pregnancy status reference
                    healthConcerns.setPregnancyReference(arrayListHealthConcern.get(0));

                    // Set loinc code
                    healthConcerns.setPregnancyLoincCode(arrayListHealthConcern.get(1));

                    // Set the name of pregnancy
                    healthConcerns.setPregnancyName(arrayListHealthConcern.get(2));

                    // Set status of pregnancy as text
                    healthConcerns.setPregnancyStatus(arrayListHealthConcern.get(3));

                    // Set status of pregnancy as boolean
                    if(arrayListHealthConcern.get(3).equals("1")) {
                        healthConcerns.setbPregnancyStatus(true);
                    } else {
                        healthConcerns.setbPregnancyStatus(false);
                    }
                } else { // case breast feeding
                    // Set breast feeding reference
                    healthConcerns.setBreastfeedingReference(arrayListHealthConcern.get(0));

                    // Set breast feeding name
                    healthConcerns.setBreastfeedingLoincCode(arrayListHealthConcern.get(1));

                    // Set status of breas feeding as text
                    healthConcerns.setBreastfeedingName(arrayListHealthConcern.get(2));

                    // Set status of pregnancy as text
                    healthConcerns.setBreastfeedingStatus(arrayListHealthConcern.get(3));

                    // Set status of breast feeding
                    if(arrayListHealthConcern.get(3).equals("1")) {
                        healthConcerns.setbBreastfeeding(true);
                    } else {
                        healthConcerns.setbBreastfeeding(false);
                    }
                }
            }
        }

        return healthConcerns;
    }

    /**
     * <p>Gets the title and the title code from the HL7 FHIR XML.</p>
     * <b>S = Parameter of UKF</b>
     *
     * @return An array with all titles as free text and code.
     */
    protected ArrayList<Section> sectionTitles() {

        // Get Information of HL7 FHIR document
        FHIRParserMedication FHIRParserMedication = new FHIRParserMedication();
        ArrayList<ArrayList<String>> arrayListsSections = FHIRParserMedication.getSectionsInformation();
        ArrayList<ArrayList<String>> arrayListsMedications = FHIRParserMedication.getMedicationsInformation();
        ArrayList<ArrayList<String>> arrayListsDosage = FHIRParserMedication.getDosageInformation();
        ArrayList<ArrayList<String>> arrayListActiveSubstance = FHIRParserMedication.getActiveSubstanceInformation();

        // Initialize an arrayList for sections objects
        ArrayList<Section> sections = new ArrayList<>();

        // If there is no composite medication information
        if (arrayListsSections == null && arrayListsMedications == null && arrayListsDosage == null && arrayListActiveSubstance == null) {
            return null;
        }

        // Saves the position of the next variable
        int moveposition = 0;

        // Iterate sections and set Section object
        for(int i = 0; i < arrayListsSections.size(); i++){

            // Create variables and objects
            int medicationLenght = 0;
            int remindFreeTextAmount = 0;
            Section sectionObject = new Section();

            // Get section
            ArrayList<String> section = arrayListsSections.get(i);

            // Add section Title
            sectionObject.setTitle(section.get(0));

            // Add section Code
            sectionObject.setUnknownTitleCode(section.get(0));

            // Get free text that belongs to the section and terminate medication length
            medicationLenght = section.size();
            for(int j = 0; j < section.size(); j++){

                if(!section.get(j).contains("urn:uuid:") && j != 0){
                    sectionObject.setFreeText(section.get(j));
                    medicationLenght--;
                    remindFreeTextAmount++;
                }
            }

            // Crate mediation object and arrayList
            ArrayList<Medication> medications = new ArrayList<>();

            for(int j = 1; j < medicationLenght; j++){

                // Initialize medication object to save the information for each medication
                Medication medication = new Medication();

                // Get information about medication statement
                medication = getMedicationStatement(medication, arrayListsMedications, section, j, moveposition);

                // Get information about medication
                medication = getMedication(medication, arrayListsMedications, arrayListsDosage, section, moveposition + j);

                // Get all information about all substances
                medication = getActiveSubstances(medication, arrayListsMedications, arrayListActiveSubstance, section, j, moveposition);

                medications.add(j - 1, medication);
            }

            // movePosition
            // '-1' is the title that is not in the medication ArrayList as the amount of free text 'remindFreeTextAmount'
            moveposition += section.size() - 1 - remindFreeTextAmount;

            // create Medication information
            sectionObject.setMedications(medications);

            // Add object to arraylist
            sections.add(i, sectionObject);
        }

        return sections;
    }

    /**
     * Collects all the information from the active substances resource and stores it in the Object Medication.
     *
     * @param medication the medication object, which should be filled
     * @param arrayListsMedications contains the information of medication, that is described in the HL7 Fhir document
     * @param arrayListActiveSubstance contains the information of active substances, that is described in the HL7 Fhir document
     * @param section contains the all sections with all medications
     * @param position contains the position of the current medication
     * @param moveposition contains the position of the next variable
     * @return A filled medication object.
     */
    protected Medication getActiveSubstances(Medication medication, ArrayList<ArrayList<String>> arrayListsMedications, ArrayList<ArrayList<String>> arrayListActiveSubstance, ArrayList<String> section, int position, int moveposition) {

        // Add active substance
        // check if the reference of medication statement equals the reference of medication
        if(arrayListsMedications.get(moveposition + (position - 1)).get(6).equals(arrayListActiveSubstance.get(moveposition + (position - 1)).get(1))){

            ArrayList<String> activeSubstance = arrayListActiveSubstance.get(moveposition + (position - 1));

            ActiveSubstance activeSubstanceObject = new ActiveSubstance();

            // Set reference of medication statement
            activeSubstanceObject.setReferenceMedicationStatement(activeSubstance.get(0));

            // Set reference of medication
            activeSubstanceObject.setReferenceToMedication(activeSubstance.get(1));

            // Iterate through
            int activeSubstanceLength = activeSubstance.size() - 5;

            for (int i = 0; i < activeSubstanceLength; i += 2) {

                // Set active substance name
                activeSubstanceObject.addActiveSubstanceName(activeSubstance.get(i + 5));

                // Set active substance intensity
                activeSubstanceObject.addActiveSubstanceCode(activeSubstance.get(i + 6));
            }

            medication.setActiveSubstance(activeSubstanceObject);
        }

        return medication;
    }

    /**
     * Collects all the information from the medication resource and stores it in the Object Medication.
     *
     * @param medication the medication object, which should be filled
     * @param arrayListsMedications contains the information of medication, that is described in the HL7 Fhir document
     * @param arrayListsDosage contains the information of dosage, that is described in the HL7 Fhir document
     * @param section contains the all sections with all medications
     * @param position contains the position of the current medication
     * @return The medication object with all information from MedicationStatement.
     */
    protected Medication getMedication(Medication medication, ArrayList<ArrayList<String>> arrayListsMedications, ArrayList<ArrayList<String>> arrayListsDosage, ArrayList<String> section, int position) {

        // check if the reference of medication statement equals the reference of medication
        if(arrayListsMedications.get((position - 1)).get(6).equals(arrayListsDosage.get((position - 1)).get(1))){

            // Create variables and objects
            ArrayList<String> medicationDosage = arrayListsDosage.get((position - 1));

            // Saves the count of passage. It is needed to get the right position from the value. This is because the information is stored in 12 steps at each time you take it
            int passage = 0;

            // Get the next position of the variable. Because it is stored in 12 steps
            int moveposition = 12;

            // Case if only a free text dosage exist
            if(medicationDosage.size() == 3){

                IngestionTime ingestionTime = new IngestionTime();

                // Set medications statement reference
                ingestionTime.setReferenceMedicationStatement(medicationDosage.get(0));

                // Set medication reference
                ingestionTime.setReferenceToMedication(medicationDosage.get(1));

                ingestionTime.setDosageFreeText(medicationDosage.get(2));
                medication.addIngestionTime(ingestionTime);

            // Case if regular dosages exists
            } else {

                // Iterate through each ingestion time
                for (int l = 2; l < medicationDosage.size(); l = l + 12) {

                    IngestionTime ingestionTime = new IngestionTime();

                    // Set medications statement reference
                    ingestionTime.setReferenceMedicationStatement(medicationDosage.get(0));

                    // Set medication reference
                    ingestionTime.setReferenceToMedication(medicationDosage.get(1));

                    // Set time of dosage
                    ingestionTime.setTimeOfDosage(eIngestionTime.fromString(medicationDosage.get(2 + (moveposition * passage))));

                    // Set information of needed
                    ingestionTime.setInformationOfNeeded(medicationDosage.get(3 + (moveposition * passage)));

                    // Set value of dosage
                    ingestionTime.setDosageValue(medicationDosage.get(4 + (moveposition * passage)));

                    // Set unit of dosage as code
                    ingestionTime.setDosageUnit(medicationDosage.get(5 + (moveposition * passage)));

                    // Set unit of dosage as text
                    ingestionTime.setDosageUnitAsText(medicationDosage.get(6 + (moveposition * passage)));

                    // Set further information to dosage as text
                    ingestionTime.setDosageFreeText(medicationDosage.get(13 + (moveposition * passage)));

                    // increase passage
                    passage += 1;
                    medication.addIngestionTime(ingestionTime);
                }
            }
        }

        return medication;
    }

    /**
     * Collects all the information from the medication statement resource and stores it in the Object Medication.
     *
     * @param medication the medication object, which should be filled
     * @param arrayListsMedications
     * @param section contains the all sections with all medications
     * @param position contains the position of the current medication
     * @param moveposition contains the position of the next variable
     * @return The medication object with all information from MedicationStatement
     */
    protected Medication getMedicationStatement(Medication medication, ArrayList<ArrayList<String>> arrayListsMedications, ArrayList<String> section, int position, int moveposition) {

        // Add medication statement
        // check if the reference of medication equals the reference of medication statement
        if(arrayListsMedications.get(moveposition + (position - 1)).get(0).equals(section.get(position))){

            ArrayList<String> arrayListMedication = arrayListsMedications.get(moveposition + (position - 1));

            medication.setReferenceMedicationStatement(arrayListMedication.get(0));

            // Set note to medication
            medication.setNote(arrayListMedication.get(1));

            // Set reason to medication
            medication.setReason(arrayListMedication.get(2));

            // Set asserted date to medication
            medication.setAssertedDate(arrayListMedication.get(3));

            // Set status of medication
            medication.setStatus(arrayListMedication.get(4));

            // Set was not taken
            medication.setWasNotTaken(arrayListMedication.get(5));

            // Set reference to medication
            medication.setReferenceToMedication(arrayListMedication.get(6));

            // Set name to medication
            medication.setTradeName(arrayListMedication.get(7));

            // Set pzn number to medication
            medication.setPharmacyCentralNumber(arrayListMedication.get(8));

            // Set drug form to medication
            medication.setDrugFormName(arrayListMedication.get(9));

            // Set drug form code to medication
            medication.setDrugFormCode(arrayListMedication.get(10));
        }

        return medication;
    }

    /**
     * <p>The method collects all additional notes and free text under the medication plan.</p>
     * <b>R = Parameter of UKF</b>
     *
     * @return A filled AdditionalNote object.
     */
    protected AdditionalNote additionalNotesUnderMedicationTable(){

        // Get Information of HL7 FHIR document
        FHIRParserAdditonalNote FHIRParserAdditonalNote = new FHIRParserAdditonalNote();
        AdditionalNote additionalNote = new AdditionalNote();

        // Get note information
        String note = FHIRParserAdditonalNote.getAdditionalNoteInformationen();

        // Here it is checked if any information is available
        if(note != null && !note.isEmpty()){
            additionalNote.setNote(note);
        } else {
            additionalNote.setNote(this.emptyString);
            logger.log(Level.INFO, "Nothing was stored in the AdditionalNote object because apparently no HL7 FHIR document information was present!");
        }

        return additionalNote;
    }
}
