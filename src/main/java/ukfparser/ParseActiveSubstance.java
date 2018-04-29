package ukfparser;

import org.w3c.dom.Element;
import org.w3c.dom.Node;

/**
 * The object describes the active substances of a medicament. It references to @W in the ukf string.
 */
public class ParseActiveSubstance {

    /**
     * It contains the drug name
     */
    private String drugName; // @w
    /**
     * It contains the drug intensity
     */
    private String drugIntensity; // @s
    /**
     * It contains the drug unit
     */
    private String drugUnit;

    /**
     * The constructor initializes the variables and fills it with the values.
     * @param nListMedication The medication with the active substances.
     */
    public ParseActiveSubstance(final Node nListMedication) {

        this.drugName = null; // @w
        this.drugIntensity = null; // @s
        this.drugUnit = null;

        convertUkfToVariable(nListMedication);
    }

    /**
     * It parses the attributes of the ukf string into object variables.
     *
     * @param nListMedication A node list with the medications.
     */
    private void convertUkfToVariable(Node nListMedication) {

        Element e = (Element) nListMedication;
        this.drugName = e.getAttribute("w");
        this.drugIntensity = e.getAttribute("s");
        splitIntensity();
    }

    /**
     * It splits the intensity into value and unit.
     */
    private void splitIntensity() {

        // Split drug intensity and unit value
        String[] part = this.drugIntensity.split("[0-9]*");

        int position;
        for(position = 0; position < this.drugIntensity.length(); position++){
            char c = this.drugIntensity.charAt(position);
            if( c > '9' )
                break;
        }

        // Set drug unit
        this.drugUnit = this.drugIntensity.substring(position);

        // Set intensity
        this.drugIntensity = this.drugIntensity.substring(0, position);
    }

    /**
     * Returns the drug name.
     * @return The drug name.
     */
    public String getDrugName() {
        return drugName;
    }

    /**
     *  Returns the drug unit.
     * @return The drug unit.
     */
    public String getDrugUnit() {
        return drugUnit;
    }

    /**
     * Return the drug intensity.
     * @return The drug intensity.
     */
    public String getDrugIntensity() {
        return drugIntensity;
    }
}
