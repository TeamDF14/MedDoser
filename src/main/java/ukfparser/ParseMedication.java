package ukfparser;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.logging.Level;

import static logging.Logging.logger;

/**
 * <p>The object describes the medication sections.</p>
 * <p>It references to @M in the ukf string</p>
 * <p>It contains the medication sections, medication statement and medication</p>
 */
public class ParseMedication {

    /**
     * Contains the pharmacy central number
     */
    private String pharmacentralnumber; // @p
    /**
     * Contains the drug name
     */
    private String drugname; // @a
    /**
     * Contains the dosage form
     */
    private String dosageForm; // @f
    /**
     * Contains the dosage form free text
     */
    private String dosageFormFreeText; // @fd
    /**
     * Contains the dosage scheme morning
     */
    private String dosageSchemeMorning; // @m
    /**
     * Contians the dosage scheme midday
     */
    private String dosageSchemeMidday; // @d
    /**
     * Contains the dosage scheme evening
     */
    private String dosageSchemeEvening; // @v
    /**
     * Contains the dosage scheme night
     */
    private String dosageSchemeNight; // @h
    /**
     * Contains the dosage scheme free text
     */
    private String dosageSchemeFreeText; // @t
    /**
     * Contains the dosage code
     */
    private String dosageCode; // @du
    /**
     * Contains the free text to the dosage
     */
    private String dosageFreeText; // @dud
    /**
     * Contains the note
     */
    private String note; // @i
    /**
     * Contains the reason for use
     */
    private String reasonForUse; // @r
    /**
     * Contains the additional line
     */
    private String additionalLine; // @x
    /**
     * Contains a list of all active substances that belongs to the medication
     */
    private ArrayList<ParseActiveSubstance> activeSubstanceArrayList;

    /**
     * <p>The constructor initialize the variables and fill it with the values</p>
     * <p>It is overloaded with Node</p>
     * <p>The node contains one specific medication</p>
     */
    public ParseMedication(final Node nodeListMedication) {
        initializeVariables();

        convertUkfToVariable(nodeListMedication);
    }

    /**
     * The method initialize all variables
     */
    private void initializeVariables() {
        // Initialize empty variables
        this.pharmacentralnumber = null; // @p
        this.drugname = null; // @a
        this.dosageForm = null; // @f
        this.dosageFormFreeText = null; // @fd
        this.dosageSchemeMorning = null; // @m
        this.dosageSchemeMidday = null; // @d
        this.dosageSchemeEvening = null; // @v
        this.dosageSchemeNight = null; // @h
        this.dosageSchemeFreeText = null; // @t
        this.dosageCode = null; // @du
        this.dosageFreeText = null; // @dud
        this.note = null; // @i
        this.reasonForUse = null; // @r
        this.additionalLine = null; // @x
        this.activeSubstanceArrayList = new ArrayList<>();
    }

    /**
     * It parse the attributes of the ukf string into object variables
     */
    private void convertUkfToVariable(final Node nListMedication ) {

        Element e = (Element) nListMedication;

        this.pharmacentralnumber = e.getAttribute("p");
        this.drugname = e.getAttribute("a");
        this.dosageForm = e.getAttribute("f");
        this.dosageFormFreeText = e.getAttribute("fd");
        this.dosageSchemeMorning = e.getAttribute("m");
        this.dosageSchemeMidday = e.getAttribute("d");
        this.dosageSchemeEvening = e.getAttribute("v");
        this.dosageSchemeNight = e.getAttribute("h");
        this.dosageSchemeFreeText = e.getAttribute("t");
        this.dosageCode = e.getAttribute("du");
        this.dosageFreeText = e.getAttribute("dud");
        this.note = e.getAttribute("i");
        this.reasonForUse = e.getAttribute("r");
        this.additionalLine = e.getAttribute("x");

        fillActiveSubstances(nListMedication);
    }

    /**
     * The method pulls all active substances from the Node
     * @param nListMedication
     */
    private void fillActiveSubstances(final Node nListMedication) {

        // Get first element of nListMedication
        Element e = (Element) nListMedication;

        // Get element W
        NodeList nameList = e.getElementsByTagName("W");

        for(int i = 0; i < nameList.getLength(); i++) {

            // Get active substance
            this.activeSubstanceArrayList.add(new ParseActiveSubstance(nameList.item(i)));

        }
    }

    /**
     * Returns true if a active substance exist
     * @return true if a active substance exist
     */
    protected boolean bExistActiveSubstance(){

        return !this.activeSubstanceArrayList.isEmpty();
    }

    /**
     * Returns the pharmacy central number
     * @return the pharmacy central number
     */
    protected String getPharmacentralnumber() {
        return pharmacentralnumber;
    }

    /**
     * Returns the drug name
     * @return the drug name
     */
    protected String getDrugname() {
        return drugname;
    }

    /**
     * Returns the dosage form
     * @return the dosage form
     */
    protected String getDosageForm() {
        return dosageForm;
    }

    /**
     * Returns the dosage form as free text
     * @return the dosage form as free text
     */
    protected String getDosageFormFreeText() {
        return dosageFormFreeText;
    }

    /**
     * Returns the value 1 if the dosage scheme is not empty on morning
     * @return the value 1 if the dosage scheme is not empty on morning
     */
    protected int isDosageSchemeMorningActive(){

        if(!util.String.isEmpty(this.dosageSchemeMorning)){
            return 1;
        } else {
            return 0;
        }
    }

    /**
     * Returns the value 1 if the dosage scheme is not empty on midday
     * @return the value 1 if the dosage scheme is not empty on midday
     */
    protected int isDosageSchemeMiddayActive(){

        if(!util.String.isEmpty(this.dosageSchemeMidday)){
            return 1;
        } else {
            return 0;
        }
    }

    /**
     * Returns the value 1 if the dosage scheme is not empty on evening
     * @return the value 1 if the dosage scheme is not empty on evening
     */
    protected int isDosageSchemeEveningActive(){

        if(!util.String.isEmpty(this.dosageSchemeEvening)){
            return 1;
        } else {
            return 0;
        }
    }

    /**
     * Returns the value 1 if the dosage scheme is not empty on night
     * @return the value 1 if the dosage scheme is not empty on night
     */
    protected int isDosageSchemeNightActive(){

        if(!util.String.isEmpty(this.dosageSchemeNight)){
            return 1;
        } else {
            return 0;
        }
    }

    /**
     * Returns true if a dosage scheme as free text is available
     * @return true if a dosage scheme as free text is available
     */
    protected boolean bDosageSchemeFreeTextActive(){

        return !util.String.isEmpty(this.dosageSchemeFreeText);
    }

    /**
     * Returns the value of the dosage form of morning
     * @return the value of the dosage form of morning
     */
    protected double getDosageSchemeMorning() {
        return convertDosageScheme(dosageSchemeMorning);
    }

    /**
     * Returns the value of the dosage form of midday
     * @return the value of the dosage form of midday
     */
    protected double getDosageSchemeMidday() {
        return convertDosageScheme(dosageSchemeMidday);
    }

    /**
     * Returns the value of the dosage form of evening
     * @return the value of the dosage form of evening
     */
    protected double getDosageSchemeEvening() {
        return convertDosageScheme(dosageSchemeEvening);
    }

    /**
     * Returns the value of the dosage form of night
     * @return the value of the dosage form of night
     */
    protected double getDosageSchemeNight() {
        return convertDosageScheme(dosageSchemeNight);
    }

    /**
     * Convert the dosage form in the right format
     * @param dosageValue contains the current dosage value
     * @return the dosage form in the right format
     */
    private double convertDosageScheme(String dosageValue){

        DecimalFormat decimalFormat = new DecimalFormat("####.##");
        double value = 0;

        try {
            if (dosageValue.equals("1/2")) {
                value = 0.5;
            } else if (dosageValue.equals("2/3")) {
                value = 0.66;
            } else if (dosageValue.equals("1/3")) {
                value = 0.33;
            } else if (dosageValue.equals("1/4")) {
                value = 0.25;
            } else if (dosageValue.equals("1/8")) {
                value = 0.13;
            } else {
                value = Double.parseDouble(dosageValue);
            }
        } catch (NullPointerException e){
            logger.log(Level.FINEST, "Can not convert dosage value to double!" + e.toString());
            value = 0;
        } catch (NumberFormatException e){
            logger.log(Level.FINEST, "Can not convert dosage value to double!" + e.toString());
            value = 0;
        }

        // removes 0 after decimal, replace ',' in '.'
        return Double.parseDouble(decimalFormat.format(value).replace(",", "."));
    }

    /**
     * Returns the free text of the dosage
     * @return the free text of the dosage
     */
    protected String getDosageSchemeFreeText() {
        return dosageSchemeFreeText;
    }

    /**
     * Returns the code of the dosage
     * @return the code of the dosage
     */
    protected String getDosageCode() {
        return dosageCode;
    }

    /**
     * Return the free text to the dosage
     * @return the free text to the dosage
     */
    protected String getDosageFreeText() {

        String dosage = enums.eMeasureOfUnit.converToString(this.dosageFreeText);

        if(!util.String.isEmpty(dosage)){
            return dosage;
        } else {
            return this.dosageFreeText;
        }
    }

    /**
     * Returns the note
     * @return the note
     */
    protected String getNote() {
        return note;
    }

    /**
     * Returns the reason for use
     * @return the reason for use
     */
    protected String getReasonForUse() {
        return reasonForUse;
    }

    /**
     * Returns the additional lines
     * @return the additional lines
     */
    protected String getAdditionalLine() {
        return additionalLine;
    }

    /**
     * Returns the active substances as ArrayList<ParseActiveSubstance>
     * @return the active substances as ArrayList<ParseActiveSubstance>
     */
    protected ArrayList<ParseActiveSubstance> getActiveSubstanceArrayList() {
        return activeSubstanceArrayList;
    }
}
