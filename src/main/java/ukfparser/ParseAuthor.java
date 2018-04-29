package ukfparser;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import help.Help;

import java.util.logging.Level;

import static logging.Logging.logger;

/**
 * The object describes the author. It references to @A in the ukf string.

 */
public class ParseAuthor {

    /**
     * Contains the life long doctor number
     */
    private String lifelongDoctorNumber; // @lanr
    /**
     * Contains the pharmacy ID number
     */
    private String pharmacyIDNumber; // @idf
    /**
     * Contains the hospital number
     */
    private String hospitalNumber; // @kik
    /**
     * Constains the doctor name
     */
    private String name; // @n
    /**
     * Contains the street name
     */
    private String street; // @s
    /**
     * Contains the zip code
     */
    private String zipCode; // @z
    /**
     * Contians the city
     */
    private String city; // @c
    /**
     * Contains the telephone
     */
    private String telephone; // @p
    /**
     * Contains the eMail
     */
    private String eMail; // @e
    /**
     * Contains the printed date
     */
    private String printedDate; // @t
    /**
     * Contains the first medication plan
     */
    private Document doc;

    /**
     * The constructor initialize the variables and fill it with the values
     */
    public ParseAuthor(Document document) {
        // Initialize empty variables
        this.lifelongDoctorNumber = null;
        this.pharmacyIDNumber = null;
        this.hospitalNumber = null;
        this.name = null;
        this.street = null;
        this.zipCode = null;
        this.city = null;
        this.telephone = null;
        this.eMail = null;
        this.printedDate = null;
        this.doc = document;

        convertUkfToVariable();
    }

    /**
     * Parses the attributes of the ukf string into object variables.
     */
    private void convertUkfToVariable() {

        NodeList nList_a = this.doc.getElementsByTagName("A");

        Element e = (Element) nList_a.item(0);

        this.name = e.getAttribute("n");
        this.street = e.getAttribute("s");
        this.zipCode = e.getAttribute("z");
        this.city = e.getAttribute("c");
        this.telephone = e.getAttribute("p");
        this.eMail = e.getAttribute("e");
        this.printedDate = e.getAttribute("t");
        this.lifelongDoctorNumber = e.getAttribute("lanr");
        this.pharmacyIDNumber = e.getAttribute("idf");
        this.hospitalNumber = e.getAttribute("kik");

        // Check if the required fields have been filled otherwise there is an error message
        if(!util.String.isEmpty(this.name)){
            logger.log(Level.FINEST, "Metaparameter: Required field name ist empty!");
        } else if(!util.String.isEmpty(this.printedDate)){
            logger.log(Level.FINEST, "Metaparameter: Required field printedDate ist empty!");
        }
    }

    /**
     * Determines if an address exists.
     * @return True if an address exists, false if not.
     */
    protected boolean bAddressExists(){

        return !util.String.isEmpty(this.street) || !util.String.isEmpty(this.city) || !util.String.isEmpty(this.zipCode);
    }

    /**
     * Returns the life long doctor number.
     * @return The life long doctor number.
     */
    protected String getLifelongDoctorNumber() {
        return lifelongDoctorNumber;
    }

    /**
     * Returns the pharmacy id number.
     * @return The pharmacy id number.
     */
    protected String getPharmacyIDNumber() {
        return pharmacyIDNumber;
    }

    /**
     * Returns the hospital number.
     * @return The hospital number.
     */
    protected String getHospitalNumber() {
        return hospitalNumber;
    }

    /**
     * Returns the name of the doctor.
     * @return The name of the doctor.
     */
    protected String getName() {
        return name;
    }

    /**
     * Returns the street of the doctor.
     * @return The street of the doctor.
     */
    protected String getStreet() {
        return street;
    }

    /**
     * Returns the zip code of the doctor.
     * @return The zip code of the doctor.
     */
    protected String getZipCode() {
        return zipCode;
    }

    /**
     * Returns the city of the doctor.
     * @return The city.
     */
    protected String getCity() {
        return city;
    }

    /**
     * Returns the telephone number.
     * @return The telephone number.
     */
    protected String getTelephone() {
        return telephone;
    }

    /**
     * Returns the email.
     * @return The email.
     */
    protected String geteMail() {
        return eMail;
    }

    /**
     * Returns the printed date of the medication plan.
     * @return The printed date of the medication plan.
     */
    protected String getPrintedDate() {
        return printedDate;
    }
}
