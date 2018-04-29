package persistenceXML;

import init.Init;
import org.w3c.dom.*;
import org.xml.sax.SAXException;
import help.Help;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.logging.Level;

import static logging.Logging.logger;

/**
 * <p>This class is the initialization class of the package.</p>
 */
public class Persistence {

    /**
     * Create protected static variables for the package persistence
     */
    protected static final String TIME = "time";
    protected static final String MORNING = "morning";
    protected static final String MIDDAY = "midday";
    protected static final String EVENING = "evening";
    protected static final String NIGHT = "night";
    protected static final String INGESTION = "ingestion";
    protected static final String INGESTIONTIME = "IngestionTime";
    protected static final String LASTOPENEDDATE = "LastOpenedDate";
    protected static final String ROOTELEMENT = "MedDoser";
    protected static final String SCHEDULEDTIMECODE = "scheduledTimeCode";
    protected static final String SCHEDULEDTIME = "scheduledTime";
    protected static final String SCHEDULEDDATE = "scheduledDate";
    protected static final String MEDSTATEMENTREFERENCE = "medStatementReference";
    protected static final String STATUS = "status";
    protected static final String MEDICATIONINGESTION = "MedicationIngestion";
    protected static final String EMPTYSTRING = "";
    protected static final String CREATIONDATE = "CreationDate"; // Only one time in use
    protected final static String NAMESPACE = "https://www.oth-regensburg.de";

    /**
     * Declare WriteIngestionTime object
     */
    private WriteIngestionTime writeIngestionTime;
    /**
     * Declare ReadIngestionTime object
     */
    private ReadIngestionTime readIngestionTime;
    /**
     * Declare ReadLastOpened object
     */
    private ReadLastOpened readLastOpened;
    /**
     * Declare WriteLastOpened object
     */
    private WriteLastOpened writeLastOpened;


    /**
     * Initializes empty variables
     */
    public Persistence(){

        // Create default document
        if(!util.FileSystem.bCheckFileExists(Init.persistenceFile)) {
            new DefaultXML();
         }

        // Initialize objects
        writeIngestionTime = new WriteIngestionTime();
        readIngestionTime = new ReadIngestionTime();

        writeLastOpened = new WriteLastOpened();
        readLastOpened = new ReadLastOpened();

        // Fill the global temporary variable
        bUpdatePersistenceXMLObject();
    }

    /////////////////////////
    // Load the persistence object
    /////////////////////////

    /**
     * <p>Updates the static PersistenceXMLObject in class 'Init'.</p>
     * <p>This increases the performance, because after each write command, the object will be updated. </p>
     *
     * @return True if the update was successful.
     */
    public boolean bUpdatePersistenceXMLObject(){

        try {
            PersistenceXMLObject persistenceXMLObject = new PersistenceXMLObject();

            // Set creation date
            persistenceXMLObject.setCreationDate(null);

            // Set last opened
            persistenceXMLObject.setLastOpenedDate(readLastOpenedString());

            // Set ingestion times
            String[] ingestionTimes = new String[4];

            ingestionTimes[0] = readIngestionTimeMorningString();
            ingestionTimes[1] = readIngestionTimeMiddayString();
            ingestionTimes[2] = readIngestionTimeEveningString();
            ingestionTimes[3] = readIngestionTimeNightString();

            persistenceXMLObject.setIngestionTimes(ingestionTimes);

            // Set medication ingestion objects
            persistenceXMLObject.setMedicationIngestionArrayList(readMedicationIngestionStatusAll());

            // Fill persistence XML object
            Init.persistenceXMLObject = persistenceXMLObject;
            logger.log(Level.INFO, "The object in the RAM was updated because the data in the XML file changed.");

            return true;
        } catch(Exception e){

            logger.log(Level.FINEST, "Cannot update the XML object in the RAM." + e.toString());
            return false;
        }
    }



    /////////////////////////
    // Block last opened
    /////////////////////////
    /**
     * Returns the latest date the file was opened.
     *
     * @return  A date.
     */
    public Date readLastOpened(){

        Document doc = readDocument();
        return readLastOpened.lastOpened(doc);
    }

    /**
     * Returns the latest date the file was opened.
     *
     * @return  A date, formatted as String.
     */
    public String readLastOpenedString(){

        Document doc = readDocument();
        return readLastOpened.lastOpenedAsString(doc);
    }

    /**
     * Updates the date the file was opened.
     *
     * @param date The new date.
     */
    public void writeLastOpened(final Date date){

        writeLastOpened.saveLastOpened(date);
        bUpdatePersistenceXMLObject();
    }



    /////////////////////////
    // Block ingestion time
    /////////////////////////
    /**
     * Modifies the ingestion time morning (PCM) with the new time of type Date.
     *
     * @param time The time of type Date
     */
    public void writeIngestionTimeMorning(final Date time){
        writeIngestionTime.morning(time);
        bUpdatePersistenceXMLObject();
    }

    /**
     * Modifies the ingestion time midday (PCD) with the new time of type Date.
     *
     * @param time The time of type Date.
     */
    public void writeIngestionTimeMidday(final Date time){
        writeIngestionTime.midday(time);
        bUpdatePersistenceXMLObject();
    }

    /**
     * Modifies the ingestion time evening (PCV) with the new time of type Date.
     *
     * @param time The time of type Date.
     */
    public void writeIngestionTimeEvening(final Date time){
        writeIngestionTime.evening(time);
        bUpdatePersistenceXMLObject();
    }

    /**
     * Modifies the ingestion time night (HS) with the new time of type Date.
     *
     * @param time The time of type Date.
     */
    public void writeIngestionTimeNight(final Date time){
        writeIngestionTime.night(time);
        bUpdatePersistenceXMLObject();
    }

    /**
     * Modifies the ingestion time morning (PCM) with the new time of type String.
     *
     * @param time The time of type String.
     */
    public void writeIngestionTimeMorningString(final String time){
        writeIngestionTime.morningWithString(time);
        bUpdatePersistenceXMLObject();
    }

    /**
     * Modifies the ingestion time midday (PCD) with the new time of type String.
     *
     * @param time The time of type String.
     */
    public void writeIngestionTimeMiddayString(final String time){
        writeIngestionTime.middayWithString(time);
        bUpdatePersistenceXMLObject();
    }

    /**
     * Modifies the ingestion time evening (PCV) with the new time of type String.
     *
     * @param time The time of type String.
     */
    public void writeIngestionTimeEveningString(final String time){
        writeIngestionTime.eveningWithString(time);
        bUpdatePersistenceXMLObject();
    }

    /**
     * Modifies the ingestion time night (HS) with the new time of type String.
     *
     * @param time The time of type String.
     */
    public void writeIngestionTimeNightString(final String time){
        writeIngestionTime.nightWithString(time);
        bUpdatePersistenceXMLObject();
    }

    /**
     * Returns the ingestion time of the morning (PCM).
     *
     * @return The ingestion time of the morning.
     */
    public Date readIngestionTimeMorning(){

        Document doc = readDocument();
        return readIngestionTime.morning(doc);
    }

    /**
     * Returns the ingestion time of the midday (PCD).
     *
     * @return The ingestion time of the midday.
     */
    public Date readIngestionTimeMidday(){

        Document doc = readDocument();
        return readIngestionTime.midday(doc);
    }

    /**
     * Returns the ingestion time of the evening (PCV).
     *
     * @return The ingestion time of the evening.
     */
    public Date readIngestionTimeEvening(){

        Document doc = readDocument();
        return readIngestionTime.evening(doc);
    }

    /**
     * Returns the ingestion time of the night (HS).
     *
     * @return The ingestion time of the night.
     */
    public Date readIngestionTimeNight(){

        Document doc = readDocument();
        return readIngestionTime.night(doc);
    }

    /**
     * Returns the ingestion time of the morning (PCM) as String.
     *
     * @return The ingestion time of the morning as String.
     */
    public String readIngestionTimeMorningString(){

        Document doc = readDocument();
        return readIngestionTime.morningAsString(doc);
    }

    /**
     * Returns the ingestion time of the midday (PCV) as String.
     *
     * @return The ingestion time of the midday as String.
     */
    public String readIngestionTimeMiddayString(){

        Document doc = readDocument();
        return readIngestionTime.middayAsString(doc);
    }

    /**
     * Returns the ingestion time of the evening (PCV) as String.
     *
     * @return The ingestion time of the evening as String.
     */
    public String readIngestionTimeEveningString(){

        Document doc = readDocument();
        return readIngestionTime.eveningAsString(doc);
    }

    /**
     * Returns the ingestion time of the night (HS) as String.
     *
     * @return The ingestion time of the night as String.
     */
    public String readIngestionTimeNightString(){

        Document doc = readDocument();
        return readIngestionTime.nightAsString(doc);
    }



    /////////////////////////
    // Block medication ingestion
    /////////////////////////
    /**
     * The method returns all medication ingestions from the XML file.
     *
     * @return All medication ingestions from the XML file.
     */
    public ArrayList<PersistenceMedIngObject> readMedicationIngestionStatusAll( ){

        ReadMedicationIngestion readMedicationIngestion = new ReadMedicationIngestion();
        Document doc = readDocument();
        return readMedicationIngestion.getMedicationIngestionAll(doc);
    }

    /**
     * The method returns all medication ingestions from the XML file of the given date.
     *
     * @return All medication ingestions from the XML file of the given date.
     */
    public ArrayList<PersistenceMedIngObject> readMedicationIngestionStatus(final Date date) {

        ReadMedicationIngestion readMedicationIngestion = new ReadMedicationIngestion();
        Document doc = readDocument();
        ArrayList<PersistenceMedIngObject> persistenceMedIngObjectArrayList = readMedicationIngestion.getMedicationIngestionDate(doc, date);

        if(persistenceMedIngObjectArrayList != null){
            return persistenceMedIngObjectArrayList;
        } else {
            return null;
        }
    }

    /**
     * The method returns all medication ingestions from the XML file <b>before</b> the given date.
     *
     * @return all medication ingestions from the XML file before the given date.
     */
    public ArrayList<PersistenceMedIngObject> readMedicationIngestionStatusBefore(final Date date) {

        ReadMedicationIngestion readMedicationIngestion = new ReadMedicationIngestion();
        Document doc = readDocument();
        ArrayList<PersistenceMedIngObject> persistenceMedIngObjectArrayList = readMedicationIngestion.getMedicationIngestionBeforeDate(doc, date);

        if(persistenceMedIngObjectArrayList != null){
            return persistenceMedIngObjectArrayList;
        } else {
            return null;
        }
    }

    /**
     * The method allows you to add any number of PersistenceMedIngObject objects to the XML file.
     *
     * @param arrayListPersistenceMedIngObject An ArrayList with items of type PersistenceMedIngObject.
     */
    public void writeMedicationIngestionStatus(final ArrayList<PersistenceMedIngObject> arrayListPersistenceMedIngObject){

        for( int i = 0; i < arrayListPersistenceMedIngObject.size(); i++){

            WriteMedicationIngestion writeMedicationIngestion = new WriteMedicationIngestion();
            writeMedicationIngestion.addMedicationIngestion(arrayListPersistenceMedIngObject.get(0));
        }
        bUpdatePersistenceXMLObject();
    }

    /**
     * The method allows you to add a single PersistenceMedIngObject object to the XML file.
     *
     * @param PersistenceMedIngObject An object of type PersistenceMedIngObject.
     */
    public boolean writeMedicationIngestion(final PersistenceMedIngObject PersistenceMedIngObject){

        if(PersistenceMedIngObject != null) {
            WriteMedicationIngestion writeMedicationIngestion = new WriteMedicationIngestion();
            writeMedicationIngestion.addMedicationIngestion(PersistenceMedIngObject);
            bUpdatePersistenceXMLObject();
            return true;
        }
        return false;
    }

    /**
     * With this method one can modify an existing entry of the medication ingestion from the XML file.
     *
     * @param medIngObjectToUpdate An object of type PersistenceMedIngObject.
     */
    public void updateMedicationIngestionStatus(final PersistenceMedIngObject medIngObjectToUpdate){

        WriteMedicationIngestion writeMedicationIngestion = new WriteMedicationIngestion();
        writeMedicationIngestion.modifyMedicationIngestion(medIngObjectToUpdate);

        // Update the memory variable
        bUpdatePersistenceXMLObject();
    }



    /////////////////////////
    // Static methods for the package
    /////////////////////////
    /**
     * Formats the xml file prettily.
     *
     * @param doc The doc of type Document.
     */
    protected static void prettyPrint(final Document doc) {

        try{

            // Write the content into xml file
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "no");
            transformer.setOutputProperty(OutputKeys.METHOD, "xml");
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");
            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(Init.persistenceFile);

            transformer.transform(source, result);

        } catch (TransformerConfigurationException e) {
            logger.log(Level.FINEST, "Cannot create default XML file" + ": " + e.toString());
        } catch (TransformerException e) {
            logger.log(Level.FINEST, "Cannot create default XML file" + ": " + e.toString());
        }
    }

    /**
     * This method reads the persistence file and returns it as document doc type.
     *
     * @return The Document of type doc.
     */
    protected static Document getBuildDocument(){
        Document doc = null;

        try{
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dbBuilder = dbFactory.newDocumentBuilder();
            doc = dbBuilder.parse(Init.persistenceFile);

        } catch (SAXException e) {
            logger.log(Level.FINEST, "Can not read Persistence_MedDoser.xml file: " + e.toString());
        } catch (IOException e) {
            logger.log(Level.FINEST, "Can not read Persistence_MedDoser.xml file: " + e.toString());
        } catch (ParserConfigurationException e){
            logger.log(Level.FINEST, "Can not read Persistence_MedDoser.xml file: " + e.toString());
        }

        return doc;
    }

    /////////////////////////
    // private methods for the class
    /////////////////////////
    /**
     * Reads the whole XML document.
     *
     * @return The variable doc of type Document, that includes the document.
     */
    private Document readDocument(){
        Document doc = getBuildDocument();

        if(doc != null) {

            //optional, but recommended
            //read this - http://stackoverflow.com/questions/13786607/normalization-in-dom-parsing-with-java-how-does-it-work

            doc.getDocumentElement().normalize();
        }

        return doc;
    }
}
