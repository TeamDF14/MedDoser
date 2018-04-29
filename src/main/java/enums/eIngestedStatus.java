package enums;

/**
 * Enums for ingested status
 * In german: 'Einnahmestatus'
 *
 * @author Sebastian Buechler <orderwb6@online.de>
 * @author Stefan Kuppelwieser <edelblistar@online.de>
 * @version 1.0
 */
public enum eIngestedStatus {

    DECLINED ("0"),
    INGESTED ("1"),
    DEFAULT ("2");

    private String status;

    eIngestedStatus(String code) {
        this.status = code;
    }

    /**
     * Checks if the parameter name equals the enum names.
     * @param otherName the name, that should checked
     * @return true, if the name is equal to the given name, false otherwise
     */
    public boolean bIsEqualToName(String otherName) {
        // (otherName == null) check is not needed because name.equals(null) returns false
        return this.status.equals(otherName);
    }

    /**
     * Convert the enum CODE to string
     *
     * @return the enum CODE as string
     */
    public String toString() {
        return this.status;
    }


    /**
     * Converts the string CODE to its enum expression
     *
     * @return The string CODE as an enum expression
     */
    public static eIngestedStatus fromString(String status) {
        switch (status) {
            case "0":
                return DECLINED;
            case "1":
                return INGESTED;
            case "2":
                return DEFAULT;
            default:
                return null;
        }
    }
}
