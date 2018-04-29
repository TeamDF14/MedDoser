package medicationplaninterpreter;

/**
 * This class contains the information about the health concerns.
 * <b>Parameter of UKF: Part of 'O'</b>
 */
public class HealthConcerns {

    /**
     * Contains the loinc code of pregnancy
     */
    private String pregnancyLoincCode;
    /**
     * Contains the title of loinc code of pregnancy
     */
    private String pregnancyName;
    /**
     * Contains the status of pregnancy
     */
    private String pregnancyStatus;
    /**
     * Contains the pregnacy status as boolean
     */
    private boolean bPregnancyStatus; // boolean is default null
    /**
     * Contains the title of loinc code of breast feeding
     */
    private String breastfeedingName;
    /**
     * Contains the status of breast feeding
     */
    private String breastfeedingStatus;
    /**
     * Contains the loinc code of breast feeding
     */
    private boolean bBreastfeeding; // boolean is default null
    /**
     * Contains the breast feeding reference
     */
    private String breastfeedingReference;
    /**
     * Contains the pregnancy status reference
     */
    private String pregnancyReference;
    /**
     * Contains the loinc code of breast feeding
     */
    private String breastfeedingLoincCode;

    /**
     * Initializes empty variables
     */
    public HealthConcerns(){

        // For pregnancy
        this.pregnancyLoincCode = null;
        this.pregnancyName = null;
        this.pregnancyStatus = null;
        this.pregnancyReference = null;
        // for breast feeding
        this.breastfeedingLoincCode = null;
        this.breastfeedingName = null;
        this.breastfeedingStatus = null;
        this.breastfeedingReference = null;
    }

    /**
     * Returns the loinc code of pregnancy
     *
     * @return the loinc code of pregnancy
     */
    public String getPregnancyLoincCode() {
        return pregnancyLoincCode;
    }

    /**
     * Set the loinc code of pregnancy
     *
     * @param pregnancyLoincCode contain the loinc code of pregnancy
     */
    public void setPregnancyLoincCode(final String pregnancyLoincCode) {
        this.pregnancyLoincCode = pregnancyLoincCode;
    }

    /**
     * Returns the name of loinc code of pregnancy
     *
     * @return the name of loinc code of pregnancy
     */
    public String getPregnancyName() {
        return pregnancyName;
    }

    /**
     * Set the name of loinc code of pregnancy
     *
     * @param pregnancyName contains the name of loinc code of pregnancy
     */
    public void setPregnancyName(final String pregnancyName) {
        this.pregnancyName = pregnancyName;
    }

    /**
     * Return the status of pregnancy
     *
     * @return the status of pregnancy
     */
    public String getPregnancyStatus() {
        return pregnancyStatus;
    }

    /**
     * Set the status of pregnancy
     *
     * @param pregnancyStatus the status of pregnancy
     */
    public void setPregnancyStatus(final String pregnancyStatus) {
        this.pregnancyStatus = pregnancyStatus;
    }

    /**
     * Returns the status of pregnancy as boolean
     *
     * Explanations:
     * <li>[0] => not pregnant</li>
     * <li>[1] => is pregnant</li>
     *
     * @return the status of pregnancy as boolean
     */
    public boolean isbPregnancyStatus() {
        return bPregnancyStatus;
    }

    /**
     * Set the status of breast feeding
     *
     * @param bPregnancyStatus set the status of breast feeding
     */
    public void setbPregnancyStatus(final boolean bPregnancyStatus) {
        this.bPregnancyStatus = bPregnancyStatus;
    }

    /**
     * Returns the loinc code of breast feeding
     *
     * @return the loinc code of breas feeding
     */
    public String getBreastfeedingLoincCode() {
        return breastfeedingLoincCode;
    }

    /**
     * Set the loinc code of breast feeding
     *
     * @param breastfeedingLoincCode contains the loinc code of breast feeding
     */
    public void setBreastfeedingLoincCode(String breastfeedingLoincCode) {
        this.breastfeedingLoincCode = breastfeedingLoincCode;
    }

    /**
     * Return the name of loinc code of breast feeding
     *
     * @return the name of loinc code of breast feeding
     */
    public String getBreastfeedingName() {
        return breastfeedingName;
    }

    /**
     * Set the name of loinc code of breast feeding
     *
     * @param breastfeedingName contains the name of loinc code of breast feeding
     */
    public void setBreastfeedingName(final String breastfeedingName) {
        this.breastfeedingName = breastfeedingName;
    }

    /**
     * Return the status of breast feeding
     *
     * @return the status of breast feeding
     */
    public String getBreastfeedingStatus() {
        return breastfeedingStatus;
    }

    /**
     * Set the status of breast feeding
     *
     * @param breastfeedingStatus the status of breast feeding
     */
    public void setBreastfeedingStatus(final String breastfeedingStatus) {
        this.breastfeedingStatus = breastfeedingStatus;
    }

    /**
     * Return the status of breast feeding as boolean
     *
     * Explanations:
     * <li>[0] => not breast feeding</li>
     * <li>[1] => is breast feeding</li>
     *
     * @return the status of breast feeding as boolean
     */
    public boolean isbBreastfeeding() {
        return bBreastfeeding;
    }

    /**
     * Set the status of breast feeding
     *
     * @param bBreastfeeding set the status of breast feeding
     */
    public void setbBreastfeeding(boolean bBreastfeeding) {
        this.bBreastfeeding = bBreastfeeding;
    }

    /**
     * Returns the breast feeding reference
     *
     * @return the breast feeding reference
     */
    public String getBreastfeedingReference() {
        return breastfeedingReference;
    }

    /**
     * Set the breast feeding reference
     *
     * @param breastfeedingReference the breast feeding reference
     */
    public void setBreastfeedingReference(final String breastfeedingReference) {
        this.breastfeedingReference = breastfeedingReference;
    }

    /**
     * Returns the pregnancy status reference
     *
     * @return the pregnancy status reference
     */
    public String getPregnancyReference() {
        return pregnancyReference;
    }

    /**
     * Set the pregnancy status reference
     *
     * @param pregnancyReference pregnancy status reference
     */
    public void setPregnancyReference(final String pregnancyReference) {
        this.pregnancyReference = pregnancyReference;
    }
}
