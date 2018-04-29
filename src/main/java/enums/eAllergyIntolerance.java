package enums;

import com.google.common.annotations.VisibleForTesting;

/**
 * <p>Enums for allergy and intolerance</p>
 *
 * @author Sebastian Buechler <orderwb6@online.de>
 * @author Stefan Kuppelwieser <edelblistar@online.de>
 * @version 1.0
 */
public enum eAllergyIntolerance {

    ALLERGY ("allergy"),
    INTOLERANCE ("intolerance");

    private String allergyIntoleranceName;

    eAllergyIntolerance(String name) {
        this.allergyIntoleranceName = name;
    }

    /**
     * Check if the parameter name equals the enum names
     *
     * @param otherName the name, that should checked
     * @return true, if the name is equals otherwise false
     */
    @VisibleForTesting
    private boolean equalsAllergyIntoleranceName(String otherName) {
        // (otherName == null) check is not needed because name.equals(null) returns false
        return allergyIntoleranceName.equals(otherName);
    }

    /**
     * Converts the string CODE to its enum expression
     *
     * @return The string CODE as an enum expression
     */
    public static String translateAllergyIntolerance(String name) {

        if(ALLERGY.equalsAllergyIntoleranceName(name)){
            return "Allergie";
        } else if(INTOLERANCE.equalsAllergyIntoleranceName(name)){
            return "Intoleranz";
        } else {
            return null;
        }
    }
}
