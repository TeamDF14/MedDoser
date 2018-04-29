package medicationplaninterpreter;

import java.util.ArrayList;

/**
 * This class contains the information about the active substances of a medication.
 * Parameter of UKF: 'W'
 */
public class ActiveSubstance {

    /**
     * Contains the intensity of the active substances
     */
    private ArrayList<String> activeSubstanceIntensity;
    /**
     * Contains the power of the active substances
     */
    private ArrayList<String> activeSubstanceName;
    /**
     * Contains the reference id of medicationStatement
     */
    private String referenceMedicationStatement;
    /**
     * Contains the reference id of medication
     */
    private String referenceToMedication;

    /**
     * Initialize empty variables
     */
    public ActiveSubstance(){

        this.activeSubstanceIntensity = new ArrayList<>();
        this.activeSubstanceName = new ArrayList<>();
    }

    /**
     * Get the names of the active substances as String[]
     *
     * @return the name of the active substances as String[]
     */
    public ArrayList<String> getActiveSubstanceIntensity() {
        return activeSubstanceIntensity;
    }

    /**
     * Set the intensity of the active substances as String[]
     *
     * @param activeSubstanceIntensity set the name of the active substances as String[]
     */
    public void setActiveSubstanceIntensity(final ArrayList<String> activeSubstanceIntensity) {
        this.activeSubstanceIntensity = activeSubstanceIntensity;
    }

    /**
     * Set a intensity of the active substances as String[]
     *
     * @param activeSubstanceCode set a name of the active substances as String[]
     */
    public void addActiveSubstanceCode(final String activeSubstanceCode) {
        this.activeSubstanceIntensity.add(activeSubstanceCode);
    }

    /**
     * Get the intensity of the active substances as String[]
     *
     * @return get the power of the active substances as String[]
     */
    public ArrayList<String> getActiveSubstanceName() {
        return activeSubstanceName;
    }

    /**
     * Set the name of the active substances as String[]
     *
     * @param activeSubstanceName set the power of the active substances as String[]
     */
    public void setActiveSubstanceName(final ArrayList<String> activeSubstanceName) {
        this.activeSubstanceName = activeSubstanceName;
    }

    /**
     * Set a name of the active substances as String[]
     *
     * @param activeSubstanceName set a namef the active substances as String[]
     */
    public void addActiveSubstanceName(final String activeSubstanceName) {
        this.activeSubstanceName.add(activeSubstanceName);
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
}
