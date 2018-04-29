package medicationplaninterpreter;

/**
 * <p>Contains the information about the organisation (custodian).</p>
 * <b>Parameter of UKF: 'C'</b>
 */
public class Custodian {

    /**
     * Contains the identification type of the system
     * E.g. BSNR or urn:ooid
     */
    private String identifierSystem;
    /**
     * Contains the value of identification
     */
    private String identifierValue;

    /**
     * Initializes empty variables
     */
    public Custodian(){
        // Initialize variables
        this.identifierSystem = null;
        this.identifierValue = null;
    }

    /**
     * Get the identification type of the system
     *
     * @return the identification type of the system
     */
    public String getIdentifierSystem() {
        return identifierSystem;
    }

    /**
     * Set the identification type of the system
     *
     * @param identifierSystem expect the identification type of the system
     */
    public void setIdentifierSystem(final String identifierSystem) {
        this.identifierSystem = identifierSystem;
    }

    /**
     * Get the value of identification
     *
     * @return the value of identification
     */
    public String getIdentifierValue() {
        return identifierValue;
    }

    /**
     * Set the value of identification
     *
     * @param identifierValue the value of identification
     */
    public void setIdentifierValue(final String identifierValue) {
        this.identifierValue = identifierValue;
    }
}
