package enums;

/**
 * Enums of observation tpyes
 *
 * @author Sebastian Buechler <orderwb6@online.de>
 * @author Stefan Kuppelwieser <edelblistar@online.de>
 * @version 1.0
 */
public enum eObservationType {
    BODY_WEIGHT ("3142-7"),
    PATIENT_HEIGHT ("8302-2"),
    CREATININE_VALUE ("2160-0"),
    PREGNANCY_STATUS ("11449-6"),
    BREASTFEEDING_STATUS ("63895-7");

    private final String code;

    eObservationType(String s) {
        code = s;
    }

    /**
     * Check if the parameter name equals the enum names
     *
     * @param otherName the name, that should checked
     * @return true, if the name is equals otherwise false
     */
    public boolean equalsName(String otherName) {
        // (otherName == null) check is not needed because name.equals(null) returns false
        return code.equals(otherName);
    }

    /**
     * Convert the enum code to string
     *
     * @return the enum code as string
     */
    public String toString() {
        return this.code;
    }
}
