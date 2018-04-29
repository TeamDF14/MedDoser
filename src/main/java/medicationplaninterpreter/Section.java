package medicationplaninterpreter;

import persistenceSQL.ControlSQL;
import help.Help;
import java.util.ArrayList;

/**
 * <p>This class contains the characteristics of the sections and their titles.</p>
 * <b>Parameter of UKF: 'S'</b>
 */
public class Section {

    /**
     * Contains the titles as free text
     */
    private String title;
    /**
     * Contains the title code
     */
    private String titleCode;
    /**
     * Contains the medication(s).
     */
    private ArrayList<Medication> medications;
    /**
     * Contains the free text that belongs to the section.
     */
    private String freeText;
    /**
     * Declares an instance of class 'ControlSQL'.
     * */
    private ControlSQL controlSQL;

    /**
     * The constructor initializes the global variables.
     */
    public Section(){

        this.title = null;
        this.titleCode = null;
        this.medications = null;
        this.freeText = null;
        this.controlSQL = new ControlSQL();
    }

    /**
     * Returns the section title. It can be empty by default.
     *
     * @return The section title.
     */
    public String getTitle() {
        return title;
    }

    /**
     * Set the title of the section.
     *
     * @param title The title of the section.
     */
    public void setTitle(final String title) {
        this.title = title;
    }

    /**
     * Return the code of the section title.
     *
     * @return The code of the section title.
     */
    public String getTitleCode() {
        return titleCode;
    }

    /**
     * Set the code of the section title.
     *
     * @param titleCode The code of the title section.
     */
    public void setTitleCode(final String titleCode) {
        this.titleCode = titleCode;
    }

    /**
     * Set the code of the section, if the code ist unknown.
     *
     * @param title The title of the section, instead of the section code.
     */
    public void setUnknownTitleCode(final String title) {

        int titleCode = controlSQL.getSection(title).getSectionCode();
        setTitleCode(util.String.convertIntToString(titleCode));
    }

    /**
     * Returns all the medications.
     *
     * @return A list of all medications.
     */
    public ArrayList<Medication> getMedications() {
        return medications;
    }

    /**
     * Set the medication object.
     *
     * @param medications An arrayList with at least one medication object.
     */
    public void setMedications(ArrayList<Medication> medications) {
        this.medications = medications;
    }

    /**
     * Returns the free text that belongs to the section.
     *
     * @return The free text that belongs to the section.
     */
    public String getFreeText() {
        return freeText != null ? util.String.convertFromSpecialCharacters(freeText) : freeText;
    }

    /**
     * Set the free text that belongs to the section.
     *
     * @param freeText The free text of the section.
     */
    public void setFreeText(final String freeText) {
        this.freeText = freeText;
    }
}
