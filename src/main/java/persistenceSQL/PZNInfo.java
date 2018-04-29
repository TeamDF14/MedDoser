package persistenceSQL;

/**
 * This class contains the content of the PZN SQLite database
 */
public class PZNInfo {

    /**
     * Contains the pzn number of the medication
     */
    private String pzn;
    /**
     * Contains the trade name of the medication
     */
    private String tradeName;
    /**
     * Contains the substance of the medication
     */
    private String substance;


    public PZNInfo(){
        this.pzn = null;
        this.tradeName = null;
        this.substance = null;
    }

    /**
     * Get the pzn number of the medication
     * @return The pzn number of the medication
     */
    public String getPZN() {
        return pzn;
    }

    /**
     * Set the pzn number of the medication
     * @param pzn The pzn number of the medication
     */
    public void setPZN(final String pzn) {
        this.pzn = pzn;
    }

    /**
     * Get the trade name of the medication
     * @return The trade name of the medication
     */
    public String getTradeName() {
        return tradeName;
    }

    /**
     * Set the trade name of the medication
     * @param tradeName The trade name of the medication
     */
    public void setTradeName(final String tradeName) {
        this.tradeName = tradeName;
    }

    /**
     * Get the substance of the medication
     * @return The substance of the medication
     */
    public String getSubstance() {
        return substance;
    }

    /**
     * Set the substance of the medication
     * @param substance The substance of the medication
     */
    public void setSubstance(final String substance) {
        this.substance= substance;
    }

}
