package enums;

/**
 * Enums for ingestion times
 * In german: 'Einnahmezeiten'
 *
 * @author Sebastian Buechler <orderwb6@online.de>
 * @author Stefan Kuppelwieser <edelblistar@online.de>
 * @version 1.0
 */
public enum eIngestionTime {
    MORNING ("PCM"),
    MIDDAY ("PCD"),
    EVENING ("PCV"),
    NIGHT ("HS"),
    UNDEFINED ("Udef");

    private String code;

    eIngestionTime(String code) {
        this.code = code;
    }

    /**
     * Checks if the parameter name equals the enum names.
     * @param otherName the name, that should checked
     * @return true, if the name is equal to the given name, false otherwise
     */
    public boolean bIsEqualToName(String otherName) {
        // (otherName == null) check is not needed because name.equals(null) returns false
        return code.equals(otherName);
    }

    /**
     * Convert the enum CODE to string
     *
     * @return the enum CODE as string
     */
    public String toString() {
        return this.code;
    }


    /**
     * Converts the string CODE to its enum expression
     *
     * @return The string CODE as an enum expression
     */
    public static eIngestionTime fromString(String code) {
        switch (code) {
            case "PCM":
                return MORNING;
            case "PCD":
                return MIDDAY;
            case "PCV":
                return EVENING;
            case "HS":
                return NIGHT;
            case "Udef":
                return UNDEFINED;
            default:
                return null;
        }
    }
}
