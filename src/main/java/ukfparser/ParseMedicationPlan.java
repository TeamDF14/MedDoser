package ukfparser;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.util.logging.Level;

import static logging.Logging.logger;

/**
 * The object describes the meta data. It references to @MP in the ukf string.
 */
public class ParseMedicationPlan {

    /**
     * Contains the version
     */
    private String version; // @v
    /**
     * Contains the instance ID
     */
    private String instanceID; // @U
    /**
     * Contains the current page
     */
    private String currentPage; // @a
    /**
     * Contains the total page
     */
    private String totalPages; // @z
    /**
     * Contains the language
     */
    private String language; // @l
    /**
     * Contains the patch number
     */
    private String patchnumber; // @p
    /**
     * Contains the first medication plan
     */
    private Document doc;

    /**
     * The constructor initialize the variables and fill it with the values
     */
    public ParseMedicationPlan(Document document){
        // Initialize empty variables
        this.version = null;
        this.instanceID = null;
        this.currentPage = null;
        this.totalPages = null;
        this.language = null;
        this.patchnumber = null;
        this.doc = document;

        convertUkfToVariable();
    }

    /**
     * It parse the attributes of the ukf string into object variables
     */
    private void convertUkfToVariable() {

        NodeList nList_MedicationPlan = this.doc.getElementsByTagName("MP");

        Element e = (Element) nList_MedicationPlan.item(0);

        this.version = e.getAttribute("v");
        this.instanceID = e.getAttribute("U");
        this.language = e.getAttribute("l");
        this.currentPage = e.getAttribute("a");
        this.totalPages = e.getAttribute("z");
        this.patchnumber = e.getAttribute("p");

        // Check if the required fields have been filled otherwise there is an error message
        if(!util.String.isEmpty(this.version)){
            logger.log(Level.FINEST, "Metaparameter: Required field version ist empty!");
        } else if(!util.String.isEmpty(this.instanceID)){
            logger.log(Level.FINEST, "Metaparameter: Required field instanceID ist empty!");
        } else if(!util.String.isEmpty(this.language)){
            logger.log(Level.FINEST, "Metaparameter: Required field language ist empty!");
        }

        // Check if the page counter are correctly. If no pages saved -> It only exist one page
        if(util.String.isEmpty(this.currentPage) && util.String.isEmpty(this.totalPages)){
            this.currentPage = "1";
            this.totalPages = "1";
        }
    }

    /**
     * Returns the version
     * @return the version
     */
    protected String getVersion() {
        return version;
    }

    /**
     * Returns the instance ID
     * @return the instance ID
     */
    protected String getInstanceID() {
        return instanceID;
    }

    /**
     * Returns the current page
     * @return the current page
     */
    protected String getCurrentPage() {
        return currentPage;
    }

    /**
     * <p>Returns the current page as int</p>
     * @return the current page as int
     */
    protected int getCurrentPageInt(){
        return Integer.parseInt(this.currentPage);
    }

    /**
     * Returns the total pages
     * @return the total pages
     */
    protected String getTotalPages() {
        return totalPages;
    }

    /**
     * <p>Return the total pages as int</p>
     * @return the total pages as int
     */
    protected int getTotalPagesInt(){
        return Integer.parseInt(this.totalPages);
    }


    /**
     * Returns the language
     * @return the language
     */
    protected String getLanguage() {
        return language;
    }

    /**
     * Returns the patch number
     * @return the patch number
     */
    protected String getPatchnumber() {
        return patchnumber;
    }
}
