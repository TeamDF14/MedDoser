package medicationplaninterpreter;

import java.util.logging.Level;

import static logging.Logging.logger;

/**
 * Contains the name and type of allergies
 * Parameter of UKF: 'O'
 */
public class AllergyIntolerance {

    /**
     * Contains the length of the allergies
     */
    private int length;
    /**
     * Contains the name of the allergies
     */
    private String[] allergyName;
    /**
     * Contains the type of the allergies
     */
    private String[] allergyType;

    /**
     * Initialize empty variables
     */
    public AllergyIntolerance(){
        this.length = 0;
        this.allergyName = null;
        this.allergyType = null;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
        initializeArrays();
    }

    public String[] getAllergyName() {
        return allergyName;
    }

    public void setAllergyName(final int position, final String allergyName) {
        this.allergyName[position] = allergyName;
    }

    public String[] getAllergyType() {
        return allergyType;
    }

    public void setAllergyType(final int position, final String allergieType) {
        this.allergyType[position] = allergieType;
    }

    private void initializeArrays(){
        try{
            if(this.length != 0) {
                this.allergyName = new String[this.length];
                this.allergyType = new String[this.length];
            }
        } catch(Exception e){
            logger.log(Level.FINEST, "AllergyIntolerance arrays cannot be initialized!");
        }
    }
}
