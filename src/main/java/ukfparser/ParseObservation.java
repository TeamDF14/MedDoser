package ukfparser;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

/**
 * The object describes the observation. It references to @O in the ukf string.
 */
public class ParseObservation {

    /**
     * It contains the allergy
     */
    private String allergy; // @a
    /**
     * It contains the intolerance
     */
    private String intolerance; //ai
    /**
     * It contains the pregnancy status
     */
    private String pregnancyStatus; // @p
    /**
     * It contains the breast feeding status
     */
    private String breastfeedingStatus; // @b
    /**
     * It contans the body weight
     */
    private String weight; // @w
    /**
     * It contains the body height
     */
    private String height; // @h
    /**
     * It contains the creatinine value
     */
    private String creatinine; // @c
    /**
     * It contains the free text
     */
    private String parameterFreeText; // @x
    /**
     * Contains the first medication plan
     */
    private Document doc;

    /**
     * The constructor initialize the variables and fill it with the values
     */
    public ParseObservation(Document document) {
        // Initialize empty variables
        this.allergy = null;
        this.intolerance = null;
        this.pregnancyStatus = null;
        this.breastfeedingStatus = null;
        this.weight = null;
        this.height = null;
        this.creatinine = null;
        this.parameterFreeText = null;
        this.doc = document;

        convertUkfToVariable();
    }

    /**
     * It parse the attributes of the ukf string into object variables
     */
    private void convertUkfToVariable() {

        NodeList nList = this.doc.getElementsByTagName("O");

        if(nList.getLength() != 0) {

            Element e = (Element) nList.item(0);

            this.intolerance = e.getAttribute("i");
            this.allergy = e.getAttribute("a");
            this.pregnancyStatus = e.getAttribute("p");
            this.breastfeedingStatus = e.getAttribute("b");
            this.weight = e.getAttribute("w");
            this.height = e.getAttribute("h");
            this.creatinine = e.getAttribute("c");
            this.parameterFreeText = e.getAttribute("x");
        }
    }

    /**
     * Returns true if a allgery and intolerance exist
     * @return true if a allgery and intolerance exist
     */
    protected boolean bExistAllergieInterlorance(){

        return bExistAllergy() || bExistIntolerance();
    }

    /**
     * Returns true if a allgery exist
     * @return true if a allgery exist
     */
    protected boolean bExistAllergy(){

        return !util.String.isEmpty(this.allergy);
    }

    /**
     * Returns true if a intolerance exist
     * @return true if a intolerance exist
     */
    protected boolean bExistIntolerance(){

        return !util.String.isEmpty(this.intolerance);
    }

    /**
     * Returns true if a clinical parameter exist
     * @return true if a clinical parameter exist
     */
    protected boolean bExistClinicalParameter(){

        return bExistWeight() || bExistHeight() || bExistCreatinine();
    }

    /**
     * Returns true if a body weight exist
     * @return true if a body weight exist
     */
    protected boolean bExistWeight(){

        return !util.String.isEmpty(this.weight);
    }

    /**
     * Returns true if a body height exist
     * @return true if a body height exist
     */
    protected boolean bExistHeight(){

        return !util.String.isEmpty(this.height);
    }

    /**
     * Returns true if a creatinine value exist
     * @return true if a creatinine value exist
     */
    protected boolean bExistCreatinine(){

        return !util.String.isEmpty(this.creatinine);
    }

    /**
     * Returns true if a health concern exist
     * @return true if a health concern exist
     */
    protected boolean bExistHealthConcern(){

        return bExistBreasfeedingStatus() || bExistpregnancyStatus();
    }

    /**
     * Returns true if a breast feeding status exist
     * @return true if a breast feeding status exist
     */
    protected boolean bExistBreasfeedingStatus(){

        return !util.String.isEmpty(this.breastfeedingStatus);
    }

    /**
     * Returns true if a pregnancy status exist
     * @return true if a pregnancy status exist
     */
    protected boolean bExistpregnancyStatus(){

        return !util.String.isEmpty(this.pregnancyStatus);
    }

    /**
     * Returns the allergy
     * @return the allergy
     */
    protected String getAllergy() {
        return allergy;
    }

    /**
     * Returns the pregnancy status
     * @return the pregnancy status
     */
    protected String getPregnancyStatus() {
        return pregnancyStatus;
    }

    /**
     * Returns the breast feeding status
     * @return the breast feeding status
     */
    protected String getBreastfeedingStatus() {
        return breastfeedingStatus;
    }

    /**
     * Returns the body weight
     * @return the body weight
     */
    protected String getWeight() {
        return weight;
    }

    /**
     * Returns the body height
     * @return the body height
     */
    protected String getHeight() {
        return height;
    }

    /**
     * Returns the creatinine value
     * @return the creatinine value
     */
    protected String getCreatinine() {
        return creatinine;
    }

    /**
     * Returns the free text
     * @return the free text
     */
    protected String getParameterFreeText() {
        return parameterFreeText;
    }

    /**
     * Returns the intolerance
     * @return the intolerance
     */
    public String getIntolerance() {
        return intolerance;
    }
}
