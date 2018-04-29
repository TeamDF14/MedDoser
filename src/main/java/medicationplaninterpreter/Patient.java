package medicationplaninterpreter;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.logging.Level;

import static logging.Logging.logger;

/**
 * <p>This class Contains the characteristics of the patient</p>
 * <b>Parameter of UKF: 'P'</b>
 */
public class Patient {

    /**
     * Contains the name of the patient
     */
    private String name;
    /**
     * Contains the surname of the patient
     */
    private String surname;
    /**
     * Contains the egk number of the patient
     */
    private String egk;
    /**
     * Contains the gender of the patient
     */
    private String gender;
    /**
     * Contains the date of birth of the patient
     */
    private String birthday;

    /**
     * The constructor initializes empty strings
     */
    public Patient(){

        this.name = null;
        this.surname = null;
        this.egk = null;
        this.gender = null;
        this.birthday = null;
    }

    /**
     * Get the name of the patient.
     *
     * @return The name of patient.
     */
    public String getName() {
        return name;
    }

    /**
     * Set the name of the patient.
     *
     * @param name The name of the patient.
     */
    public void setName(final String name) {
        this.name = name;
    }

    /**
     * Get the surname of the patient.
     *
     * @return The surname of the patient.
     */
    public String getSurname() {
        return surname;
    }

    /**
     * Set the surname of patient.
     *
     * @param surname The surname of patient.
     */
    public void setSurname(final String surname) {
        this.surname = surname;
    }

    /**
     * Get the egk number.
     *
     * @return The egk number.
     */
    public String getEGK() {
        return egk;
    }

    /**
     * Set the egk number.
     *
     * @param egk The egk number.
     */
    public void setEgk(final String egk) {
        this.egk = egk;
    }

    /**
     * <p>Get the gender of patient. There are three possibilities:</p>
     * <ul>
     *     <li>f for 'female'</li>
     *     <li>m for 'male'</li>
     *     <li>u for 'undefined'</li>
     * </ul>
     *
     * @return The gender of the patient.
     */
    public String getGender() {
        return gender;
    }

    /**
     * Set the gender of the patient.There are three possibilities:</p>
     * <ul>
     *     <li>f for 'female'</li>
     *     <li>m for 'male'</li>
     *     <li>u for 'undefined'</li>
     * </ul>
     *
     * @param gender The gender of patient. m (male) or f (female) or u (undefined)
     */
    public void setGender(final String gender) {
        this.gender = gender;
    }

    /**
     * Returns the birthday of the patient as 'date'.
     *
     * @return The birthday of the patient as 'date'. If the birthday is not available, return null.
     */
    public Date getBirthdayAsDate() {

        Date date = null;
        if (this.birthday == null){
            return date;
        }
        try{
            DateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.GERMAN);
            date = format.parse(this.birthday);
        }catch(ParseException e){

            logger.log(Level.FINEST, "An error has occurred: " + e.toString());
        }
        return date;
    }

    /**
     * Returns the birthday of the patient as 'string'.
     *
     * @return The birthday of the patient as string.
     */
    public String getBirthday(){
        return birthday;
    }

    /**
     * Set the birthday of the patient.
     *
     * @param birthday  The birthday of the patient as 'string'.
     */
    public void setBirthday(final String birthday) {
        this.birthday = birthday;
    }

}
