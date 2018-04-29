package ukfparser;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

/**
 * The object describes the custodian. It references to @C in the ukf string.
 */
public class ParseCustodian {

    /**
     * Contains the identification system
     */
    private String identificiationsSystem; // @s
    /**
     * Contains the iddentification eg. BSNR
     */
    private String identifikation; // @v
    /**
     * Contains the first medication plan
     */
    private Document doc;

    /**
     * The constructor initialize the variables and fill it with the values
     */
    public ParseCustodian(Document document) {
        // Initialize empty variables
        this.identificiationsSystem = null;
        this.identifikation = null;
        this.doc = document;

        convertUkfToVariable();
    }

    /**
     * It parse the attributes of the ukf string into object variables
     */
    private void convertUkfToVariable() {

        NodeList nList = this.doc.getElementsByTagName("C");

        if(nList.getLength() != 0) {

            Element e = (Element) nList.item(0);

            this.identificiationsSystem = e.getAttribute("s");
            this.identifikation = e.getAttribute("v");
        }
    }

    /**
     * Returns true if custodian exist
     * @return true if custodian exit
     */
    protected boolean bCustodianExist(){

        return !util.String.isEmpty(this.identificiationsSystem) && !util.String.isEmpty(this.identificiationsSystem);
    }

    /**
     * Returns the identification system
     * @return the identification system
     */
    protected String getIdentificiationsSystem() {
        return identificiationsSystem;
    }

    /**
     * Returns the identification
     * @return the identification
     */
    protected String getIdentifikation() {
        return identifikation;
    }
}
