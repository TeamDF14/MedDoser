package medicationplaninterpreter;

import enums.eIngestionTime;
import help.Help;

/**
 * <p>This class contains the ingestion times of a medication.</p>
 * <b>Parameter of UKF: 'M'</b>
 */
public class IngestionTime {

    /**
     * Contains value of dosage of medication
     */
    private String dosageValue;
    /**
     * Contains unit of dosage of medication
     */
    private String dosageUnit;
    /**
     * Contains unit of dosage as free text
     */
    private String dosageUnitAsText;
    /**
     * Contains dosage as free Text
     * e.g. if all four intake times are given at the same time
     */
    private String dosageFreeText;
    /**
     * Contains the time of dosage.
     * Basically, for the time hint (when) on the code
     * table https://www.hl7.org/fhir/valueset-event-timing.html be used.
     */
    private eIngestionTime timeOfDosage;
    /**
     * If a dose is taken "on demand", this is expressed
     * with the asNeededBoolean element (true / false = yes / no).
     */
    private String informationOfNeeded;
    /**
     * Contains the reference id of medicationStatement
     */
    private String referenceMedicationStatement;
    /**
     * Contains the reference id of medication
     */
    private String referenceToMedication;

    /**
     * Initializes empty variables
     */
    public IngestionTime(){

        // Initialize variables
        this.dosageValue = null;
        this.dosageUnit = null;
        this.dosageUnitAsText = null;
        this.dosageFreeText = null;
        this.timeOfDosage = null;
        this.informationOfNeeded = null;
        this.referenceMedicationStatement = null;
        this.referenceToMedication = null;
    }


    /**
     * Converts the dosage form in the correct format.
     * @param dosageValue The current dosage value.
     * @return The dosage form in the correct format.
     */
    private String convertDosageScheme(String dosageValue){

        // If dosageValue == null
        if(util.String.isEmpty(dosageValue)){
            return "0";
        }

        if (dosageValue.equals("0.5")) {
            return "1/2";
        } else if (dosageValue.equals("0.66")) {
            return "2/3";
        } else if (dosageValue.equals("0.33")) {
            return "1/3";
        } else if (dosageValue.equals("0.25")) {
            return "1/4";
        } else if (dosageValue.equals("0.13")) {
            return "1/8";
        } else {
            return dosageValue.replace(".0", "");
        }
    }

    /**
     * Get the form of dosage as String.
     *
     * @return The form of the dosage.
     */
    public String getDosageValue() {
        return convertDosageScheme(dosageValue);
    }

    /**
     * Set form of dosage as String
     *
     * @param dosageValue set form of dosage as String
     */
    public void setDosageValue(final String dosageValue) {
        this.dosageValue = dosageValue;
    }

    /**
     * Get unit of dosage as String
     *
     * @return get unit of dosage as String
     */
    public String getDosageUnit() {
        return dosageUnit;
    }

    /**
     * Set the unit of the dosage
     *
     * @param dosageUnit filled arry of dosageUnit as Strng[]
     */
    public void setDosageUnit(final String dosageUnit) {
        this.dosageUnit = dosageUnit;
    }

    /**
     * Get unit of dosage as text as String
     *
     * @return unit of dosage as text as String
     */
    public String getDosageUnitAsText() {
        return dosageUnitAsText;
    }

    /**
     * Set unit of dosage as text as String
     *
     * @param dosageUnitAsText get unit of dosage as text as String
     */
    public void setDosageUnitAsText(final String dosageUnitAsText) {
        this.dosageUnitAsText = dosageUnitAsText;
    }

    /**
     * Get dosage as free text as String
     * e.g. if all four intake times are given at the same time
     *
     * @return get dosage as free text as String
     */
    public String getDosageFreeText() {
        return dosageFreeText;
    }

    /**
     * Set dosage as free text as String
     * e.g. if all four intake times are given at the same time
     *
     * @param dosageFreeText set dosage as free text as String
     */
    public void setDosageFreeText(final String dosageFreeText) {
        this.dosageFreeText = dosageFreeText;
    }

    /**
     * Returns the time of dosage.
     * Basically, for the time hint (when) on the
     * code table https://www.hl7.org/fhir/valueset-event-timing.html be used.
     *
     * @return the time of dosage as code
     */
    public eIngestionTime getTimeOfDosage() {
        return timeOfDosage;
    }

    /**
     * Set the time of dosage.
     * Basically, for the time hint (when) on the
     * code table https://www.hl7.org/fhir/valueset-event-timing.html be used.
     *
     * @param timeOfDosage expect the time of dosage as code
     */
    public void setTimeOfDosage(eIngestionTime timeOfDosage) {
        this.timeOfDosage = timeOfDosage;
    }

    /**
     * Get the information of needed.
     * If a dose is taken "on demand", this is expressed
     * with the asNeededBoolean element (true / false = yes / no).
     *
     * @return true oder false, if the medication need to take or not
     */
    public String getInformationOfNeeded() {
        return informationOfNeeded;
    }

    /**
     * Set the information of needed.
     * If a dose is taken "on demand", this is expressed
     * with the asNeededBoolean element (true / false = yes / no).
     *
     * @param informationOfNeeded contains the information, if the medication need to take or not
     */
    public void setInformationOfNeeded(String informationOfNeeded) {
        this.informationOfNeeded = informationOfNeeded;
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