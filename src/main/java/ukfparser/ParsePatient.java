package ukfparser;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.util.logging.Level;

import static logging.Logging.logger;


/**
 * The object describes the patient. It references to @P in the ukf string.
 */
public class ParsePatient {

    /**
     * It contians the name of the patient
     */
    private String name; // @g
    /**
     * It contains the surname of the patient
     */
    private String surname; // @f
    /**
     * It contians the egk of the patient
     */
    private String egk; // @egk
    /**
     * It contians the birthday of the patient
     */
    private String birthday; // @b
    /**
     * It contains the gender of the patient
     */
    private String gender; // @s
    /**
     * It contains the title of the patient
     */
    private String title; // @t
    /**
     * It contains the intent of the patient
     */
    private String intent; // @v
    /**
     * It contians the name suffix of the patient
     */
    private String nameSuffix; // @z
    /**
     * Contains the first medication plan
     */
    private Document doc;

    /**
     * The constructor initialize the variables and fill it with the values
     */
    public ParsePatient(Document document) {
        // Initialize empty variables
        this.name = null;
        this.surname = null;
        this.egk = null;
        this.birthday = null;
        this.gender = null;
        this.title = null;
        this.intent = null;
        this.nameSuffix = null;
        this.doc = document;

        convertUkfToVariable();
    }

    /**
     * It parse the attributes of the ukf string into object variables
     */
    private void convertUkfToVariable() {

        NodeList nListPaitent = this.doc.getElementsByTagName("P");
        Element e = (Element) nListPaitent.item(0);

        this.name = e.getAttribute("g");
        this.surname = e.getAttribute("f");
        this.egk = e.getAttribute("egk");
        this.gender = e.getAttribute("s");
        this.title = e.getAttribute("t");
        this.intent = e.getAttribute("v");
        this.nameSuffix = e.getAttribute("z");
        this.birthday = e.getAttribute("b");

        // Check if the required fields have been filled otherwise there is an error message
        if(!util.String.isEmpty(this.name)){
            logger.log(Level.FINEST, "Metaparameter: Required field name ist empty!");
        } else if(!util.String.isEmpty(this.surname)){
            logger.log(Level.FINEST, "Metaparameter: Required field surname ist empty!");
        }
    }


    /**
     * Returns the name
     * @return the name
     */
    protected String getName() {
        return name;
    }

    /**
     * Returns the surname
     * @return the surname
     */
    protected String getSurname() {
        return surname;
    }

    /**
     * Returns the egk number
     * @return the egk number
     */
    protected String getEgk() {
        return egk;
    }

    /**
     * Returns the birthday
     * @return the birthday
     */
    protected String getBirthday() {
        return birthday;
    }

    /**
     * Returns the gender
     * @return the gender
     */
    protected String getGender() {
        return gender;
    }

    /**
     * Returns the title
     * @return the title
     */
    protected String getTitle() {
        return title;
    }

    /**
     * Returns the intent
     * @return the intent
     */
    protected String getIntent() {
        return intent;
    }

    /**
     * Returns the name suffix
     * @return the name suffix
     */
    protected String getNameSuffix() {
        return nameSuffix;
    }
}
