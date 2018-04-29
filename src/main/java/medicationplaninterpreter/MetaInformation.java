package medicationplaninterpreter;

/**
 * <p>This class contains the meta information of the BMP.</p>
 * <b>Parameter of UKF: MP
 */
public class MetaInformation {

    /**
     * Contains the version of the BMP
     */
    private String version;
    /**
     * Contains the instanceID of the BMP
     */
    private String instanceID;
    /**
     * Contains the language of the BMP
     */
    private String language;
    /**
     * Contains the creation date of composite (equals medicationplan)
     */
    private String creationDate;

    /**
     * Initializes empty variables
     */
    public MetaInformation(){

        this.version = null;
        this.instanceID = null;
        this.language = null;
    }

    /**
     * Get the version of the BMP.
     *
     * @return The version of the BMP
     */
    public String getVersion() {
        return version;
    }

    /**
     * Set the version of the BMP.
     *
     * @param version The version of BMP.
     */
    public void setVersion(final String version) {
        this.version = version;
    }

    /**
     * Get the instance ID of the BMP.
     *
     * @return The instance ID of BMP.
     */
    public String getInstanceID() {
        return instanceID;
    }

    /**
     * Set the instance ID of the BMP.
     *
     * @param instanceID The instance ID of the BMP.
     */
    public void setInstanceID(final String instanceID) {
        this.instanceID = instanceID;
    }

    /**
     * Get the language of the BMP.
     *
     * @return The language of the BMP.
     */
    public String getLanguage() {
        return language;
    }

    /**
     * Set the language of the BMP.
     *
     * @param language The language of the BMP.
     */
    public void setLanguage(final String language) {
        this.language = language;
    }

    /**
     * Get the creation date of composite (equals medicationplan).
     *
     * @return The creation date of composite.
     */
    public String getCreationDate() {
        return creationDate;
    }

    /**
     * Set creation date of composite (equals medicationplan)
     *
     * @param creationDate expect the creation date of composite (equals medicationplan)
     */
    public void setCreationDate(String creationDate) {
        this.creationDate = creationDate;
    }
}
