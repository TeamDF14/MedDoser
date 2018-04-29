package medicationplaninterpreter;

/**
 * This class contains the clinical parameters of the patient.
 * Paramter of UKF: 'O'
 */
public class ClinicalParameter {

    /**
     * Contains the weight of patient
     */
    private String weight;
    /**
     * Contains the size of patient
     */
    private String size;
    /**
     * Contains the creatinine unit of patient
     */
    private String creatinine;
    /**
     * Contains further complaints
     */
    private String complaints;

    /**
     * The constructor initializes the variables
     */
    public ClinicalParameter() {

        this.weight = null;
        this.size = null;
        this.creatinine = null;
        this.complaints = null;
    }

    /**
     * Get the weight of patient in kg
     *
     * @return weight of patient in kg
     */
    public String getWeight() {
        return weight;
    }

    /**
     * Set the weight of patient in kg
     *
     * @param weight set weight of patient in kg
     */
    public void setWeight(final String weight) {
        this.weight = weight;
    }

    /**
     * Get the size of patient in cm
     *
     * @return get size of patient in cm
     */
    public String getSize() {
        return size;
    }

    /**
     * Get the size of patient in cm
     *
     * @param size get size of patient in cm
     */
    public void setSize(final String size) {
        this.size = size;
    }

    /**
     * Get creatinin unit of the patient
     *
     * @return cratinin unit of the patient
     */
    public String getCreatinine() {
        return creatinine;
    }

    /**
     * Set the creatinin unit of the patient
     *
     * @param creatinine The creatinin unit of patient as String
     */
    public void setCreatinine(final String creatinine) {
        this.creatinine = creatinine;
    }

    /**
     * Get the complaints.
     *
     * @return get complaints
     */
    public String getComplaints() {
        return complaints;
    }

    /**
     * Set the complaints.
     *
     * @param complaints set complaints as String
     */
    public void setComplaints(final String complaints) {
        this.complaints = complaints;
    }
}
