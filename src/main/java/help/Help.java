package help;

import enums.eIngestedStatus;
import enums.eIngestionTime;
import init.Init;
import medicationplaninterpreter.IngestionTime;
import medicationplaninterpreter.Medication;
import persistenceXML.PersistenceMedIngObject;
import java.util.*;

/**
 * <p>This class contains static methods which can be used from everywhere in the project. </p>
 */
public class Help {

    /**
     * <p>Checks if the current system time is more actual than the ingestion time defined by the user.</p>
     *
     * Example: Parameter 'iTime' is set to MIDDAY and the corresponding ingestion time is set to 12:00 h, the current system time is 12:18 h.
     * Then the current system time is compared to that time and the function evaluates to TRUE.

     * @param iTime The time of the day to be compared. For explanation of the values, see above.
     * @return True if the time has passed, false if not.
     */
    public static final boolean bHasIngestionTimeReleased(final eIngestionTime iTime){

        // Define two calendars
        Calendar cPersistence = new GregorianCalendar(util.Date.timeZone);
        Calendar cCurrent = new GregorianCalendar(util.Date.timeZone);

        switch (iTime){
            case MORNING:
                cPersistence.setTime(Init.persistenceXMLObject.getIngestionTimeMorning());
                break;
            case MIDDAY:
                cPersistence.setTime(Init.persistenceXMLObject.getIngestionTimeMidday());
                break;
            case EVENING:
                cPersistence.setTime(Init.persistenceXMLObject.getIngestionTimeEvening());
                break;
            case NIGHT:
                cPersistence.setTime(Init.persistenceXMLObject.getIngestionTimeNight());
                break;
            default:
                return false;

        }

        // Get the current system time
        cCurrent.setTime(new Date());

        if (cCurrent.get(Calendar.HOUR_OF_DAY) > cPersistence.get(Calendar.HOUR_OF_DAY)){
            return true;

        }
        else if(cCurrent.get(Calendar.HOUR_OF_DAY) == cPersistence.get(Calendar.HOUR_OF_DAY)){
            if (cCurrent.get(Calendar.MINUTE) >= cPersistence.get(Calendar.MINUTE)){
                return true;
            }
        }
        return false;

    }

    /**
     * <p>Creates a list that contains unique dates. </p>
     * <p>The data comes from the global persistenceXMLObject from class Init and consists of all entries of the persistence file, including today.</p>
     * @return A new list consisting of unique dates.
     */
    public static ArrayList<Date> getDateList() {

        ArrayList<Date> list = new ArrayList<>();
        ArrayList<PersistenceMedIngObject> listMedIng;
        listMedIng = Init.persistenceXMLObject.getMedicationIngestionArrayListAll();

        if ((listMedIng != null) && (!listMedIng.isEmpty())){

            // Set initial date to the lowest possible value: 1970
            Date lastSeenDate = new Date(0);

            for (PersistenceMedIngObject medIng: listMedIng) {

                if (!util.Date.bIsDayEqual(lastSeenDate, medIng.getScheduledDate())){

                    lastSeenDate = medIng.getScheduledDate();

                    // Add entry only if it is not in the list yet
                    if (!util.Date.isDateContained(medIng.getScheduledDate(), list)){
                        list.add(medIng.getScheduledDate());
                    }
                }
            }
        }
        else {
            return list;
        }

        // Sort the list descending by date
        Collections.sort(list);

        return list;
    }


    /**
     * Checks if all fields of <i><b>dosageValue</b></i> within the given list of type <i>IngestionTime</i> are set to 0.
     * Therefore, the string of the field is compared to "0".
     * @param lIngTime A list with entries for each of the four valid ingestion times (PCM, PCD, PCV and HS).
     * @return True if <b>all</b> values are set to 0, false if not.
     */
    public static boolean isAllSetTo0(ArrayList<IngestionTime> lIngTime){

        if (lIngTime == null){
            return true;
        }

        for (IngestionTime iTime : lIngTime){

            if (!iTime.getDosageValue().equals("0")){
                return false;
            }
        }

        return true;
    }

    /**
     * Checks if there are ingestions of the given date and given ingestion time that got a status assigned that is set to DEFAULT.
     * When the date is at least one day in the past, all ingestions with status DEFAULT do return TRUE.
     * When the date is equal to today, it is checked if the ingestion time has already released. If yes, TRUE is returned, if not, FALSE is returned.
     * @param eTime The ingestion time of the day, e.g. <i>PCM</i>
     * @param date The date of the ingestions to look for
     * @return True if yes, false if there are no ingestions left of the given date.
     */
    public static boolean bIsRemainingIngestion(eIngestionTime eTime, Date date){

        ArrayList<PersistenceMedIngObject> medicationStati = Init.persistenceXMLObject.getMedicationIngestion(date);

        for (PersistenceMedIngObject mo: medicationStati){

            if ((mo.getScheduledTimeCode() == eTime) && (mo.getStatus() == eIngestedStatus.DEFAULT)){

                // At least one day in the past - it does not matter if the time has already released
                if (util.Date.getDifference(new Date(), date).size() > 0){
                    return true;
                }
                // Today + the time has already released
                if (bHasIngestionTimeReleased(eTime)){
                    return true;
                }

            }
        }
        return false;
    }

    /**
     * Compares the current system time to the ingestion times defined by the user
     *
     * @return The current system time as an enum.
     */
    public static final eIngestionTime getCurrentIngestionTime() {
        // ToDo
        eIngestionTime iTime = eIngestionTime.NIGHT;

        Calendar calCurrent = new GregorianCalendar(util.Date.timeZone);
        calCurrent.setTimeZone(util.Date.timeZone);
        calCurrent.setTime(new Date());

        Calendar calStored = new GregorianCalendar(util.Date.timeZone);
        calStored.setTimeZone(util.Date.timeZone);

        // Check NIGHT
        calStored.setTime(Init.persistenceXMLObject.getIngestionTimeNight());
        if (util.Date.bIsEarlier(calCurrent, calStored)) {
            iTime = eIngestionTime.EVENING;
        }

        // Check EVENING
        calStored.setTime(Init.persistenceXMLObject.getIngestionTimeEvening());
        if (util.Date.bIsEarlier(calCurrent, calStored)) {
            iTime = eIngestionTime.MIDDAY;
        }

        // Check MIDDAY
        calStored.setTime(Init.persistenceXMLObject.getIngestionTimeMidday());
        if (util.Date.bIsEarlier(calCurrent, calStored)) {
            iTime = eIngestionTime.MORNING;
        }

        // Check MORNING
        calStored.setTime(Init.persistenceXMLObject.getIngestionTimeMorning());
        if (util.Date.bIsEarlier(calCurrent, calStored)) {
            iTime = eIngestionTime.NIGHT;
        }

        return iTime;
    }

    /**
     * Detect the object that holds the given parameters within the given array list.
     * The data within this list comes from the persistence file 'Persistence_MedDoser.xml'.
     * @param m An array list containing at least one medication ingestion.
     * @param medStatementReference A string containing the medication reference ID.
     * @param scheduledDate The date of the ingestion
     * @param iTime The time of the ingestion
     * @param status The status of the ingestion
     * @return The medicationIngestion object if found, null if not.
     */
    public static PersistenceMedIngObject getMedicationIngestion(ArrayList<PersistenceMedIngObject> m, String medStatementReference, Date scheduledDate, eIngestionTime iTime, eIngestedStatus status){
        for (int i = 0; i < m.size(); i++){
            String persistenceDate = util.Date.convertDateToString(m.get(i).getScheduledDate(), true);
            String askedDate = util.Date.convertDateToString(scheduledDate, true);

            if (m.get(i).getMedStatementReference().compareTo(medStatementReference) == 0
                    && m.get(i).getStatus() == status
                    && m.get(i).getScheduledTimeCode() == iTime
                    && askedDate.equals(persistenceDate)){

                return m.get(i);
            }
        }
        return null;
    }

    /**
     * Detect the medication with the given medication reference ID within the given array list.
     * @param m An array list containing at least one medication.
     * @param medStatementReference A string containing the medication reference ID.
     * @return The medication reference ID if found, null if nothing was found.
     */
    public static Medication getMedication(ArrayList<Medication> m, String medStatementReference){
        for (int i = 0; i < m.size(); i++){
            if (m.get(i).getReferenceMedicationStatement().compareTo(medStatementReference) == 0){
                return m.get(i);
            }
        }
        return null;
    }

}