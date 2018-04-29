package enums;

public enum eGender {

    MALE ("male"),
    FEMELE ("female"),
    UNIDENTFIED ("unknown");

    private String eGenderName;

    eGender(String genderName) {
        this.eGenderName = genderName;
    }

    /**
     * Check if the parameter name equals the enum names
     *
     * @param otherName the name, that should checked
     * @return true, if the name is equals otherwise false
     */
    private boolean equalsGenderName(String otherName) {
        // (otherName == null) check is not needed because name.equals(null) returns false
        return eGenderName.equals(otherName);
    }

    /**
     * Converts the string CODE to its enum expression
     *
     * @return The string CODE as an enum expression
     */
    public static String convertUKFGenderName(String gendername) {

        if (MALE.equalsGenderName(gendername)) {
            return "Männlich";
        } else if (FEMELE.equalsGenderName(gendername)) {
            return "Weiblich";
        } else if (UNIDENTFIED.equalsGenderName(gendername)) {
            return "Unbestimmt";
        } else {
            return "Keine Angabe";
        }
    }

}
