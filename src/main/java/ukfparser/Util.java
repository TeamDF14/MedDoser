package ukfparser;

import java.util.ArrayList;

/**
 * The Util class is a helper class for the UKF parser package.
 * The class includes the knowledge of the most diverse IDs that are necessary in a medication plan.
 */
public class Util {

    /**
     * Contains the patient ID
     */
    private String patientID;
    /**
     * Contains the author ID
     */
    private String authorID;
    /**
     * Contains the custodian ID
     */
    private String custodianID;
    /**
     * Contains the body weight ID
     */
    private String bodyWeightID;
    /**
     * Contains the body height ID
     */
    private String bodyHeightID;
    /**
     * Contains the creatinine ID
     */
    private String creatininID;
    /**
     * Contains the allergy ID
     */
    private String allergyID;
    /**
     * Contains the intolerance ID
     */
    private String intoleranceID;
    /**
     * Contains the pregnancy status ID
     */
    private String pregnancyStatusID;
    /**
     * Contains the breast feeding ID
     */
    private String breastfeedingStatusID;
    /**
     * Contains all IDs of medication statement and medication
     */
    private ArrayList<ArrayList<String>> medicationStatementReferenceID;

    /**
     * The constructor initialize all variables
     */
    public Util(){
        this.patientID = null;
        this.authorID = null;
        this.custodianID = null;
        this.bodyWeightID = null;
        this.bodyHeightID = null;
        this.creatininID = null;
        this.allergyID = null;
        this.intoleranceID = null;
        this.medicationStatementReferenceID = new ArrayList<>();
    }

    /**
     * Returns the allergy ID
     * @return the allergy ID
     */
    protected String getAllergyID() {
        return allergyID;
    }

    /**
     * Set the allergy ID
     * @param allergyID expect the allergy ID
     */
    protected void setAllergyID(final String allergyID) {
        this.allergyID = allergyID;
    }

    /**
     * Returns the intolerance ID
     * @return the intolerance ID
     */
    protected String getIntoleranceID() {
        return intoleranceID;
    }

    /**
     * Set the intorlerance ID
     * @param intoleranceID expect the intolerance ID
     */
    protected void setIntoleranceID(final String intoleranceID) {
        this.intoleranceID = intoleranceID;
    }

    /**
     * Returns the patient ID
     * @return the patient ID
     */
    protected String getPatientID() {
        return patientID;
    }

    /**
     * Set the patient ID
     * @param patientID expect the patient ID
     */
    protected void setPatientID(final String patientID) {
        this.patientID = patientID;
    }

    /**
     * Returns the author ID
     * @return the author ID
     */
    protected String getAuthorID() {
        return authorID;
    }

    /**
     * Set the author ID
     * @param authorID expect the author ID
     */
    protected void setAuthorID(final String authorID) {
        this.authorID = authorID;
    }

    /**
     * Returns the custodian ID
     * @return the custodian ID
     */
    protected String getCustodianID() {
        return custodianID;
    }

    /**
     * Set the custodian ID
     * @param custodianID expect the custodian ID
     */
    protected void setCustodianID(final String custodianID) {
        this.custodianID = custodianID;
    }

    /**
     * Returns the body weight ID
     * @return the body weight ID
     */
    protected String getBodyWeightID() {
        return bodyWeightID;
    }

    /**
     * Set the body weight ID
     * @param bodyWeightID expect the body weight ID
     */
    protected void setBodyWeightID(final String bodyWeightID) {
        this.bodyWeightID = bodyWeightID;
    }

    /**
     * Returns the body height ID
     * @return the body height ID
     */
    protected String getBodyHeightID() {
        return bodyHeightID;
    }

    /**
     * Set the body height ID
     * @param bodyHeightID expecht the body height ID
     */
    protected void setBodyHeightID(final String bodyHeightID) {
        this.bodyHeightID = bodyHeightID;
    }

    /**
     * Returns the creatinin ID
     * @return the creatinin ID
     */
    protected String getCreatininID() {
        return creatininID;
    }

    /**
     * Set the creatinin ID
     * @param creatininID expect the creatinin ID
     */
    protected void setCreatininID(final String creatininID) {
        this.creatininID = creatininID;
    }

    /**
     * Returns the pregnancy status ID
     * @return the pregnandy status ID
     */
    protected String getPregnancyStatusID() {
        return pregnancyStatusID;
    }

    /**
     * Set the pregnancy status ID
     * @param pregnancyStatusID expect the pregnancy status ID
     */
    protected void setPregnancyStatusID(final String pregnancyStatusID) {
        this.pregnancyStatusID = pregnancyStatusID;
    }

    /**
     * Returns the breast feeding ID
     * @return tbe breast feeding ID
     */
    protected String getBreastfeedingStatusID() {
        return breastfeedingStatusID;
    }

    /**
     * Set the breast feeding ID
     * @param breastfeedingStatusID expect the breast feeding ID
     */
    protected void setBreastfeedingStatusID(final String breastfeedingStatusID) {
        this.breastfeedingStatusID = breastfeedingStatusID;
    }

    /**
     * Returns the medication ID != medication statement ID
     * @param sectionNumber expect the section position
     * @param medication expect the medication position
     * @return the medication ID
     */
    protected String getMedicationStatementReferenceID(int sectionNumber, final int medication) {

        if(sectionNumber != 0){
            sectionNumber = sectionNumber * 2;
        }

        ArrayList<String> medicationArrayList = this.medicationStatementReferenceID.get(sectionNumber);

        return medicationArrayList.get(medication);
    }

    /**
     * Returns the all medication statement and medication IDs
     * @return all medication statement and medication IDs
     */
    protected ArrayList<ArrayList<String>> getMedicationStatementReferenceID() {
        return medicationStatementReferenceID;
    }

    /**
     * Add a list of medication IDs to the section position
     * @param medicationStatementArrayList contains a list of medication IDs to the section
     */
    protected void addMedicationStatementReferenceID(final ArrayList<String> medicationStatementArrayList) {

        this.medicationStatementReferenceID.add(medicationStatementArrayList);
        this.medicationStatementReferenceID.add(new ArrayList<>());
    }

    /**
     * Add the freetext ID to MediStatementReference
     * It is usually the last entry and we are marked with the X in the UKF != equals with the X to each medication
     * @param freeTextArrayList contains a list with the freetext ID
     */
    protected void addFreeTextIDToMedStatementReference(final ArrayList<String> freeTextArrayList) {
        this.medicationStatementReferenceID.add(freeTextArrayList);
    }

    /**
     * Edit one medication
     * @param sectionPosition expect the section position
     * @param medicationReferencePosition expect the mediction position
     * @param medicationReferenceID expect the new medication ID
     */
    protected void setMedicationReferenceID(int sectionPosition, final int medicationReferencePosition, final String medicationReferenceID){

        // calculate medication reference position
        if(sectionPosition == 0){
            sectionPosition = 1;
        } else {
            sectionPosition = sectionPosition * 2 + 1;
        }

        // Get array with medication reference
        ArrayList<String> medicationReferenceArrayList = this.medicationStatementReferenceID.get(sectionPosition);

        // Save key to medication reference
        medicationReferenceArrayList.add(medicationReferencePosition, medicationReferenceID);

        // Save arraylist with key to medicationStatementReferenceID
        this.medicationStatementReferenceID.set(sectionPosition, medicationReferenceArrayList);
    }

    /**
     * Get one medication ID
     * @param sectionPosition expect the section position
     * @param medicationReferencePosition expect the medication position
     * @return one medication ID
     */
    protected String getMedicationReferenceID(int sectionPosition, final int medicationReferencePosition) {

        if(sectionPosition == 0){
            sectionPosition = 1;
        } else {
            sectionPosition = sectionPosition * 2 + 1;
        }

        // Get array with medication reference
        ArrayList<String> medicationReferenceArrayList = this.medicationStatementReferenceID.get(sectionPosition);

        return medicationReferenceArrayList.get(medicationReferencePosition);
    }
}
