package ukfparser;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.NodeList;
/**
 * The object describes the free text. The free text does not have a reference to medication. It references to @X in the ukf string.
 */
public class ParseFreeText {

    /**
     * Contains the free text
     */
    private String freeText; // @X
    /**
     * Contains the free text title
     */
    private String freeTextTitle; // @t
    /**
     * Contains the first medication plan
     */
    private Document doc;

    /**
     * The constructor initialize the variables and fill it with the values
     */
    public ParseFreeText(Document document) {
        // Initialize empty variables
        this.freeText = null;
        this.freeTextTitle = null;
        this.doc = document;

        convertUkfToVariable();
    }

    /**
     * It parse the attributes of the ukf string into object variables
     */
    private void convertUkfToVariable() {

        NodeList nList = this.doc.getElementsByTagName("S");
        NodeList nameListFreeText = ((Element) nList.item(nList.getLength() - 1)).getElementsByTagName("X");

        // Get free text
        String freeText = null;
        if(nameListFreeText.getLength() != 0 && nList.getLength() > 1) {

            if (nameListFreeText.item(0).hasAttributes()) {

                // Set title
                String title = ((Element) ((Element) nList.item(nList.getLength() - 1))).getAttribute("t");
                this.freeTextTitle = title;

                // Get free text
                NamedNodeMap namedNodeMapFreeText = nameListFreeText.item(0).getAttributes();
                freeText = namedNodeMapFreeText.getNamedItem("t").getNodeValue();
            } else {

                // Call elements from the UKF string
                Element e = (Element) nList.item(nList.getLength() - 1);
                NodeList nameListFreeTextContent = e.getElementsByTagName("X");
                NodeList nameListMedication = e.getElementsByTagName("M");

                // Get title of free text
                this.freeTextTitle = e.getAttribute("t");

                if (nameListFreeTextContent.getLength() != 0 && nameListMedication.getLength() == 0) {

                    // Get content of attribute t
                    freeText = nameListFreeTextContent.item(0).getTextContent();
                }
            }
        }

        if(!util.String.isEmpty(freeText)){
            this.freeText = freeText;
        } else {
            this.freeText = null;
        }
    }

    /**
     * Returns the free text title
     * @return the free text title
     */
    protected String getFreeTextTitle() {
        return freeTextTitle;
    }

    protected boolean isFreeTextEmpty(){
        return util.String.isEmpty(freeText);
    }

    /**
     * Returns the free text
     * @return the free text
     */
    protected String getFreeText() {
        return freeText;
    }
}
