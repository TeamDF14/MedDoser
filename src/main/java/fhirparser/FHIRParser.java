package fhirparser;

import init.Init;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import persistenceSQL.ControlSQL;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;

import static logging.Logging.logger;

/**
 * The class disassembles the HL7 FHIR document in order to obtain the necessary information that is stored in objects
 *
 * @author Sebastian Buechler <orderwb6@gmail.com>
 * @author Stefan Kuppelwieser <edelblistar@online.de>
 */
public class FHIRParser {

    /**
     * Constant variables are created here for the query from the HL7 FHIR document
     */
	protected static final String ATCADRESS = "http://www.whocc.no/atc";
    protected static final String CODE = "code";
    protected static final String CODING = "coding";
    protected static final String DATEASSERTDD = "dateAsserted"; //only one time in use
    protected static final String DISPLAY = "display";
    protected static final String DOSAGE = "dosage"; //only one time in use
    protected static final String EINHEIT = "Einheit";
    protected static final String IDENTIFIER = "identifier";
	protected static final String LOINCADRESS = "http://loinc.org";
    protected static final String MEDICATIONREFERENCE = "medicationReference";
    protected static final String NOTE = "note"; //only one time in use
	protected static final String UNIT = "unit";
	protected static final String PERIODUNITS = "periodUnits";
    protected static final String REASONFORUSECODEABlECONCEPT = "reasonForUseCodeableConcept"; //only one time in use
    protected static final String REFERENCE = "reference";
    protected static final String SECTION = "section";
    protected static final String STATUS = "status"; //only one time in use
    protected static final String SYSTEM = "system";
    protected static final String TEXT = "text";
    protected static final String TITLE = "title"; //only one time in use
    protected static final String VALUE = "value";
    protected static final String WASNOTTAKEN = "wasNotTaken"; //only one time in use
    protected static final String EMPTYSTRING = "";

    /**
     * Declare object
     */
    protected static ControlSQL mySQL;

    /**
     * Contains the information of the resources in indices
     */
    protected static int indiceMetaInformation ;
    protected static int indicePatientInformation;
    protected static int indiceAutorInformation;
    protected static int indiceCustodian;
    protected static int indiceCompositeObservation;
    protected static int indiceCompositeAllergie;
    protected static int indiceCompositeHealthConcerns;
    protected static int indiceCompositeMedication;
    protected static int indiceAdditionalNotes;

    /**
     * Contains the child elements 'entry' of the HL7 FHIR Document
     * With the child elements, the respective necessary information can be found
     */
    protected static ArrayList<Element> documentsEntrys;

    /**
     * The constructor get the path to the HL7 FHIR document for editing the XML-file
     */
    public FHIRParser() {
        // Initialize object ControlSQL
        mySQL = new ControlSQL(Init.dbFile);

        // Save entries in ArrayList<Document>
        bReadHL7FhirDoc();
    }

    /**
     * Read the HL7 Fhir document and save it seperate in the ArrayList<Document>
     * It also can used to refresh the content of ArrayList<Document>
     *
     * @return true if the action success
     */
    public boolean bReadHL7FhirDoc() {
        // Set default values to static variables
        indiceMetaInformation = -1;
        indicePatientInformation = -1;
        indiceAutorInformation = -1;
        indiceCustodian = -1;
        indiceCompositeObservation = -1;
        indiceCompositeAllergie = -1;
        indiceCompositeHealthConcerns = -1;
        indiceCompositeMedication = -1;
        indiceAdditionalNotes = -1;

        documentsEntrys = new ArrayList<>();

        // Split the HL7 FHIR XML to seperate entry items
        if(Init.FHIRFile == null){
            logger.log(Level.FINEST, ".. The Init.newInputfile '" + Init.FHIRFile + "' is null! \n");
            return false;
        }
        if(Init.FHIRFile.exists() && Init.FHIRFile.length() != 0) {
            // The method decomposes the HL7 FHIR document so that every child element 'entry' is added to the ArrayList documentsEntrys.
            // Decomposes the HL7 FHIR document so that every child element 'entry' is added to the ArrayList documentsEntrys
            try {
                logger.log(Level.FINEST, ".. The FHIR source '" + Init.FHIRFile + "' is parsed right now! \n");
                // DOM parser factory
                DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();

                // DOM parser
                DocumentBuilder builder = factory.newDocumentBuilder();

                // The entire XML file
                Document document = builder.parse(Init.FHIRFile);

                // The root Element
                Element root = document.getDocumentElement();

                // Only get child elements called 'entry'
                if (root.hasChildNodes()) {
                    Node child = root.getFirstChild();
                    while (child != null) {
                        if (child.getNodeName().equals("entry")) {
                            documentsEntrys.add((Element) child);
                        }
                        child = child.getNextSibling();
                    }
                }

                // Save the information of the resources into indices
                determinateResource();

            } catch (ParserConfigurationException e) {
                logger.log(Level.FINEST, "An error has occurred: " + e.toString());
                return false;
            } catch (SAXException e) {
                logger.log(Level.FINEST, "An error has occurred: " + e.toString());
                return false;
            } catch (IOException e) {
                logger.log(Level.FINEST, "An error has occurred: " + e.toString());
                return false;
            } catch (Exception e){
                logger.log(Level.FINEST, "An error has occurred: " + e.toString());
                return false;
            }

        } else {
            documentsEntrys = null;
            logger.log(Level.FINEST, "An error has occurred: The HL7 FHIR file does not exist!");
            return false;
        }

        return true;
    }

    /**
     * Determines the indices of the respective resources.
     * In particular for meta information, patient, author information,
     * composition observation, composition medication
     * and other notes under the medication plan
     */
    private void determinateResource(){
        // It is always in the first resource
        indiceMetaInformation = 0;

        // It is always in the first resource
        indicePatientInformation = 1;

        // It is always in the third resource
        indiceAutorInformation = 2;

        // Check if exist a observation Composition by the loinc code of clinical information
        for(int i = 3; i < documentsEntrys.size(); i++) {

            // Get all resources with 'Composition'
            NodeList nList_custodian = documentsEntrys.get(i).getElementsByTagName("Organization");
            NodeList nList_composition = documentsEntrys.get(i).getElementsByTagName("Composition");

            if(nList_custodian.getLength() != 0){
                indiceCustodian = i;
            }

            // Get all the relevant compositions
            if(nList_composition.getLength() != 0) {
                String value = ((Element) nList_composition.item(0)).getAttribute("xmlns");

                // Enter only composite that attributes equals 'http://hl7.org/fhir'
                if (!util.String.isEmpty(value) && value.equals("http://hl7.org/fhir")){

                    // Get loinc codes to check if it is a observation or medication
                    NodeList nList_coding = ((Element) nList_composition.item(0)).getElementsByTagName(CODING);
                    String code = util.XML.searchHierarchyByTagAndAttribute(nList_coding, SYSTEM, VALUE, LOINCADRESS, CODE, VALUE);

                    // loinc code   definition
                    // 55752-0      Composite of clinical parameter aka observation information
                    // 69730-0      Composite of additional information. It is the information under all medications
                    // 48765-2      Composite of allergies
                    // 75310-3      Health concerns
                    // 19009-0      Composite of medication
                    if(!util.String.isEmpty(code) && code.equals("55752-0")){
                        indiceCompositeObservation = i;
                    } else if(!util.String.isEmpty(code) && code.equals("48765-2")) {
                        indiceCompositeAllergie = i;
                    } else if(!util.String.isEmpty(code) && code.equals("75310-3")){
                        indiceCompositeHealthConcerns = i;
                    } else if(!util.String.isEmpty(code) && code.equals("19009-0")){
                        indiceCompositeMedication = i;
                    } else if(!util.String.isEmpty(code) && code.equals("69730-0")) {
                        indiceAdditionalNotes = i;
                    }
                }
            }
        }
    }

    /**
     * Searches in a nested herachy for a tag name. For example, according to line.
     * For example, here the parameters would be as follows: (nodeList, "line")
     *
     * Example (Skip to method to see example)
     * <li><address></li>
     * <li>     <line value="Schloßstr. 22"/></li>
     * <li>     <line value="10555"/></li>
     * <li>     <line value="Berlin"/></li>
     * <li></address></li>
     *
     * @param nodeList full list with the affected region
     * @param tagName the name of the field tag that condition should be fulfilled
     * @return an Arraylist with the results
     */
    protected static ArrayList<String> searchInterleavedHierarchy(final NodeList nodeList, final String tagName){
        // Initialize variable
        int iterator = 0;
        ArrayList<String> arrayList = new ArrayList<>();

        // Iterate to first level
        for(int i = 0; i < nodeList.getLength(); i++){

            // Get i element of nodeList
            Node node = nodeList.item(i);

            // Check if node is a element node
            if(node.getNodeType()==Node.ELEMENT_NODE){

                // Get childs of node
                Element element = (Element) node;
                NodeList nameList = element.getChildNodes();

                // Iterate child elements
                for(int j = 0; j < nameList.getLength(); j++){

                    // Get child element
                    Node n = nameList.item(j);
                    if(n.getNodeType()==Node.ELEMENT_NODE){
                        Element name = (Element) n;

                        // Fill ArrayList
                        if(name.getTagName().equals(tagName)) {
                            arrayList.add(iterator, name.getAttribute(VALUE));
                            iterator++;
                        }
                    }
                }
            }
        }

        return arrayList;
    }
}