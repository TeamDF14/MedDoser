package medicationplaninterpreter;

import java.util.ArrayList;
import enums.eIngestedStatus;

/**
 * <p>This class contains the characteristics of the medications</p>
 * <b>Parameter of UKF: 'M'</b>
 */
public class Medication {

    /**
     * Contains pzn number of medication
     */
    private String pharmacyCentralNumber;
    /**
     * Contains trade name of medication
     */
    private String tradeName;
    /**
     * Contains notes
     */
    private String note;
    /**
     * Contains reason of taking
     */
    private String reason;
    /**
     * Contains ingestion status: DEFAULT, INGESTED or DECLINED.
     */
    private eIngestedStatus ingestionStatus;
    /**
     * The status can be used to indicate whether the drug is taken ("active") or not
     * ("completed"). In later stages, additional values are possible ("entered-in-error", "intended").
     */
    private String status;
    /**
     * Contains the asserted date of medication
     */
    private String assertedDate;
    /**
     * Contains the section title of the medication.
     */
    private String sectionTitle;
    /**
     * Contains the information about, if it was not taken.
     * In the expansion stages, an indicator (what) can not be specified.
     */
    private String wasNotTaken;
    /**
     * Contains the reference id of medicationStatement
     */
    private String referenceMedicationStatement;
    /**
     * Contains the reference id of medication
     */
    private String referenceToMedication;
    /**
     * Contains the active substances of medications
     */
    private ActiveSubstance activeSubstance;
    /**
     * Contains the ingestion time of medication
     */
    private ArrayList<IngestionTime> ingestionTimes;
    /**
     * Contains the ingestion form of the medication
     */
    private String drugFormName;
    /**
     * Contains the form code of the drug
     */
    private String drugFormCode;

    /**
     * Initializes empty variables
     */
    public Medication(){

        // Initialize variables
        this.pharmacyCentralNumber = null;
        this.tradeName = null;
        this.note = null;
        this.reason = null;
        this.ingestionStatus = eIngestedStatus.DEFAULT;
        this.status = null;
        this.assertedDate = null;
        this.sectionTitle = null;
        this.wasNotTaken = null;
        this.referenceMedicationStatement = null;
        this.referenceToMedication = null;
        this.activeSubstance = null;
        this.ingestionTimes = null;
        this.drugFormName = null;
        this.drugFormCode = null;
    }

    public eIngestedStatus getIngestionStatus() {
        return ingestionStatus;
    }

    public void setIngestionStatus(eIngestedStatus ingestionStatus) {
        this.ingestionStatus = ingestionStatus;
    }


    public String getSectionTitle() {
        return sectionTitle;
    }

    public void setSectionTitle(String sectionTitle) {
        this.sectionTitle = sectionTitle;
    }


    /**
     * Return pzn number
     *
     * @return pzn number as String
     */
    public String getPharmacyCentralNumber() {
        return pharmacyCentralNumber;
    }

    /**
     * Set pzn number
     *
     * @param pharmacyCentralNumber set pzn number
     */
    public void setPharmacyCentralNumber(final String pharmacyCentralNumber) {
        this.pharmacyCentralNumber = pharmacyCentralNumber;
    }

    /**
     * Get trade name of medication
     *
     * @return get trade name of medication as String
     */
    public String getTradeName() {
        return tradeName;
    }

    /**
     * Set trade name of medication
     *
     * @param tradeName set trade name of medication as String array
     */
    public void setTradeName(final String tradeName) {
        this.tradeName = tradeName;
    }

    /**
     * Get note of medication as String
     *
     * @return get note of mediation as String
     */
    public String getNote() {
        return note;
    }

    /**
     * Set note of medication as String
     *
     * @param note set not of medication as String
     */
    public void setNote(final String note) {
        this.note = note;
    }

    /**
     * Get reason of taking medication as String
     *
     * @return get reason of taking medication as String
     */
    public String getReason() {
        return reason;
    }

    /**
     * Set reason of taking medication as String
     *
     * @param reason set reason of taking medication as as String
     */
    public void setReason(final String reason) {
        this.reason = reason;
    }

    /**
     * Set the asserted date of medication
     *
     * @param assertedDate exepct the asserted of medication
     */
    public void setAssertedDate(final String assertedDate) {
        this.assertedDate = assertedDate;
    }

    /**
     * Get the asserted date of medication
     *
     * @return the asserted date of medication
     */
    public String getAssertedDate() {
        return assertedDate;
    }

    /**
     * Set the value, if was not taken.
     * Also in stages of development an indicator (wasNotTaken)
     * can be given that the drug was not taken (for example because of incompatibilities).
     *
     * @param wasNotTaken the value, if was not taken
     */
    public void setWasNotTaken(final String wasNotTaken) {
        this.wasNotTaken = wasNotTaken;
    }

    /**
     * Get the value, if was not taken.
     * Also in stages of development an indicator (wasNotTaken)
     * can be given that the drug was not taken (for example because of incompatibilities).
     *
     * @return the value, if was not taken
     */
    public String getWasNotTaken() {
        return wasNotTaken;
    }

    /**
     * The status can be used to indicate whether the drug is taken ("active") or not
     * ("completed"). In later stages, additional values are possible ("entered-in-error", "intended").
     *
     * @param status expect the status of medication
     */
    public void setStatus(String status) {
        this.status = status;
    }

    /**
     * The status can be used to indicate whether the drug is taken ("active") or not
     * ("completed"). In later stages, additional values are possible ("entered-in-error", "intended").
     *
     * @return the status of medication
     */
    public String getStatus() {
        return status;
    }

    /**
     * Get the reference id of MedicationStatement
     *
     * @return the reference id of MedicationStatement
     */
    public String getReferenceMedicationStatement() {
        return referenceMedicationStatement;
    }

    /**
     * Set the reference id of MedicationStatement
     *
     * @param referenceMedicationStatement expect the reference id of MedicationStatement
     */
    public void setReferenceMedicationStatement(String referenceMedicationStatement) {
        this.referenceMedicationStatement = referenceMedicationStatement;
    }

    /**
     * Get the reference id of Medication
     *
     * @return the reference id of Medication
     */
    public String getReferenceToMedication() {
        return referenceToMedication;
    }

    /**
     * Set the reference id of Mediation
     *
     * @param referenceToMedication excpect the reference id of medication
     */
    public void setReferenceToMedication(String referenceToMedication) {
        this.referenceToMedication = referenceToMedication;
    }

    /**
     * Set the active substances.
     * It contains all properties of all active substances to the medication
     *
     * @param activeSubstance expect an object of ActiveSubstance
     */
    public void setActiveSubstance(ActiveSubstance activeSubstance) {
        this.activeSubstance = activeSubstance;
    }

    /**
     * Get the active substances.
     * It contains all properties of all active substances to the medication
     *
     * @return activeSubstance expect an object of ActiveSubstance
     */
    public ActiveSubstance getActiveSubstance() {
        return activeSubstance;
    }

    /**
     * Set the ingestion time.
     * The ingestion time contains all information about the ingestion and how much should take.
     *
     * @param ingestionTimes expect an ArrayList<IngestionTime> of all ingestion times
     */
    public void setIngestionTimes(ArrayList<IngestionTime> ingestionTimes) {
        this.ingestionTimes = ingestionTimes;
    }

    /**
     * Set the ingestion time.
     * The ingestion time contains all information about the ingestion and how much should take.
     *
     * @param ingestionTimes the ingestion time as object from on one ingestion. The ArrayList will be counted up automatically
     */
    public void addIngestionTime(IngestionTime ingestionTimes){
        // initialize the ArrayList
        initializeArrayListIngestionTimes();
        this.ingestionTimes.add(ingestionTimes);
    }

    /**
     * Get the ingestion time.
     * The ingestion time contains all information about the ingestion and how much should take.
     *
     * @return an ArrayList of all ingestion times
     */
    public ArrayList<IngestionTime> getIngestionTimes() {
        return ingestionTimes;
    }

    /**
     * Initialize the ArrayList<IngestionTime>
     */
    private void initializeArrayListIngestionTimes() {
        if(this.ingestionTimes == null){
            this.ingestionTimes = new ArrayList<>();
        }
    }

    /**
     * Return the drug form of ingestion
     *
     * @return the drug form of ingestion
     */
    public String getDrugFormName() {
        return drugFormName;
    }

    /**
     * Set the drug form of ingestion
     *
     * @param drugFormName expect drug form of ingestion
     */
    public void setDrugFormName(String drugFormName) {
        this.drugFormName = drugFormName;
    }

    /**
     * Return the unit of the drug
     *
     * @return the unit of the drug
     */
    public String getDrugFormCode() {
        return drugFormCode;
    }

    /**
     * Set the unit of the drug
     *
     * @param drugFormCode expect the unit of the drug
     */
    public void setDrugFormCode(String drugFormCode) {
        this.drugFormCode = drugFormCode;
    }
}
