package enums;

import java.beans.beancontext.BeanContext;

/**
 * Enums for measure of unit
 */
public enum  eMeasureOfUnit {

    STUECK ("1"),
    IE ("p"),
    CM ("q"),
    ML ("s"),
    HUB ("5"),
    TROPFEN ("6"),
    BEUTEL ("4");

    String code;

    eMeasureOfUnit(String code){
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
    public static String converToString(String code) {

        if(STUECK.bIsEqualToName(code)){
            return "Stueck";
        } else if (IE.bIsEqualToName(code)){
            return "IE";
        } else if (CM.bIsEqualToName(code)){
            return "cm";
        } else if (ML.bIsEqualToName(code)){
            return "ml";
        } else if (HUB.bIsEqualToName(code)){
            return "HUB";
        } else if (TROPFEN.bIsEqualToName(code)){
            return "Tropfen";
        } else if (BEUTEL.bIsEqualToName(code)){
            return "Beutel";
        } else {
            return null;
        }
    }

}