package persistenceXML;

import org.joda.time.LocalDate;
import help.Help;

import java.util.ArrayList;
import java.util.Date;

/**
 * This class contains all the information about the persistence XML file.
 */
public class PersistenceXMLObject {

    /**
     * Contains the creation date
     */
    private String creationDate;
    /**
     * Contains the last opened date
     */
    private String lastOpenedDate;
    /**
     * Contains the ingestion times
     */
    private String[] ingestionTimes;
    /**
     * Contains an ArrayList with all persistenceMedIngObject
     */
    private ArrayList<PersistenceMedIngObject> persistenceMedIngObjectArrayList;

    /**
     * The Constructor initialize the variables
     */
    public PersistenceXMLObject(){
        this.creationDate = null;
        this.lastOpenedDate = null;
        this.ingestionTimes = new String[4];
        this.persistenceMedIngObjectArrayList = new ArrayList<>();
    }

    /////////////////////
    // Block meta data
    /////////////////////
    /**
     * Returns the creation date
     * @return the creation date
     */
    public String getCreationDate() {
        return creationDate;
    }

    /**
     * Set the creation date
     * @param creationDate expect the creation date
     */
    public void setCreationDate(final String creationDate) {
        this.creationDate = creationDate;
    }

    /**
     * Returns the last opened date
     * @return the last opened date
     */
    public String getLastOpenedDate() {
        return lastOpenedDate;
    }

    /**
     * Set the last opened date
     * @param lastOpenedDate expect the last opened date
     */
    public void setLastOpenedDate(final String lastOpenedDate) {
        this.lastOpenedDate = lastOpenedDate;
    }

    /////////////////////
    // Block ingestion times
    /////////////////////
    /**
     * Returns the full ingestion times
     * @return a String array with all ingestion times
     */
    public String[] getIngestionTimes() {
        return ingestionTimes;
    }

    /**
     * Set the ingestion times
     * @param ingestionTimes expect an array with all ingestionTimes
     */
    public void setIngestionTimes(final String[] ingestionTimes) {
        this.ingestionTimes = ingestionTimes;
    }

    /**
     * Returns the ingestion time morning as date
     * @return the ingestion time morning as date
     */
    public Date getIngestionTimeMorning(){
        return util.Date.convertStringToTime(this.ingestionTimes[0]);
    }

    /**
     * Returns the ingestion time midday as date
     * @return the ingestion time midday as date
     */
    public Date getIngestionTimeMidday(){
        return util.Date.convertStringToTime(this.ingestionTimes[1]);
    }

    /**
     * Returns the ingestion time evening as date
     * @return the ingestion time evening as date
     */
    public Date getIngestionTimeEvening(){
        return util.Date.convertStringToTime(this.ingestionTimes[2]);
    }

    /**
     * Returns the ingestion time night as date
     * @return the ingestion time night as date
     */
    public Date getIngestionTimeNight(){
        return util.Date.convertStringToTime(this.ingestionTimes[3]);
    }

    /**
     * Returns the ingestion time morning as String
     * @return the ingestion time morning as String
     */
    public String getIngestionTimeStringMorning(){
        return this.ingestionTimes[0];
    }

    /**
     * Returns the ingestion time midday as String
     * @return the ingestion time midday as String
     */
    public String getIngestionTimeStringMidday(){
        return this.ingestionTimes[1];
    }

    /**
     * Returns the ingestion time evening as String
     * @return the ingestion time evening as String
     */
    public String getIngestionTimeStringEvening(){
        return this.ingestionTimes[2];
    }

    /**
     * Returns the ingestion time night as String
     * @return the ingestion time night as String
     */
    public String getIngestionTimeStringNight(){
        return this.ingestionTimes[3];
    }

    /////////////////////
    // Block ingestion times
    /////////////////////
    /**
     * Set the persistenceMedIngObjectArrayList
     * @param persistenceMedIngObjectArrayList expect an ArrayList of type PersistenceMedIngObject with all medication ingestion
     */
    public void setMedicationIngestionArrayList(final ArrayList<PersistenceMedIngObject> persistenceMedIngObjectArrayList) {
        this.persistenceMedIngObjectArrayList = persistenceMedIngObjectArrayList;
    }

    /**
     * Add a single ingestion medication PersistenceMedIngObject to the ArrayList
     * @param PersistenceMedIngObject expect a single ingestion medication of Type PersistenceMedIngObject
     */
    public void addMedicationIngestionArrayList(final PersistenceMedIngObject PersistenceMedIngObject) {
        this.persistenceMedIngObjectArrayList.add(PersistenceMedIngObject);
    }

    /**
     * Returns the ArrayList of type PersistenceMedIngObject with all entries
     * @return the ArrayList of type PersistenceMedIngObject with all entrie
     */
    public ArrayList<PersistenceMedIngObject> getMedicationIngestionArrayListAll() {
        return persistenceMedIngObjectArrayList;
    }

    /**
     * Returns a single PersistenceMedIngObject of the Arraylist with a specified position
     * @param position the specified position
     * @return a single PersistenceMedIngObject of the Arraylist with a specified position
     */
    public PersistenceMedIngObject getMedicationIngestion(final int position){
        return this.persistenceMedIngObjectArrayList.get(position);
    }

    /**
     * Returns all medication with the specified date
     * @param date contains the specified date
     * @return all medication with the specified date
     */
    public ArrayList<PersistenceMedIngObject> getMedicationIngestion(final Date date) {

        ArrayList<PersistenceMedIngObject> pMedIngObjectArrayList = new ArrayList<>();

        for(int i = 0; i < this.persistenceMedIngObjectArrayList.size(); i++){

            String persistenceDate = util.Date.convertDateToString(this.persistenceMedIngObjectArrayList.get(i).getScheduledDate(), true);
            String askedDate = util.Date.convertDateToString(date, true);

            if (askedDate.equals(persistenceDate)) {
                pMedIngObjectArrayList.add(this.persistenceMedIngObjectArrayList.get(i));
            }
        }

        return pMedIngObjectArrayList;
    }

    /**
     * Returns all medication before the specified date
     * @param date contains the specified date
     * @return all medication before the specified date
     */
    public ArrayList<PersistenceMedIngObject> getMedicationIngestionBefore(final Date date) {

        ArrayList<PersistenceMedIngObject> persistenceMedIngObjectArrayList = new ArrayList<>();

        for(int i = 0; i < this.persistenceMedIngObjectArrayList.size(); i++){

            org.joda.time.DateTime persistenceDate = new org.joda.time.DateTime(this.persistenceMedIngObjectArrayList.get(i).getScheduledDate());
            org.joda.time.DateTime askedDate = new org.joda.time.DateTime(date);

            LocalDate firstDate = persistenceDate.toLocalDate();
            LocalDate secondDate = askedDate.toLocalDate();

            if(firstDate.isBefore(secondDate)){
                persistenceMedIngObjectArrayList.add(this.persistenceMedIngObjectArrayList.get(i));
            }
        }

        return persistenceMedIngObjectArrayList;
    }
}
