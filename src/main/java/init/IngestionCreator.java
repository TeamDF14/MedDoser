package init;

import enums.eIngestedStatus;
import enums.eIngestionTime;
import medicationplaninterpreter.IngestionTime;
import medicationplaninterpreter.Medication;
import medicationplaninterpreter.BMPInterpreter;
import medicationplaninterpreter.Section;
import persistenceXML.PersistenceMedIngObject;
import persistenceXML.Persistence;

import java.util.*;
import java.util.logging.Level;

import static logging.Logging.logger;


public class IngestionCreator {

    public IngestionCreator (Persistence p){
        bCreateMedicationIngestions(p);
    }

    /**
     * Creates new entries within 'Persistence_MedDoser.xml'.
     * Creates the medication ingestions for the days since the last start of the application (if they were not created already)
     * @return True if the entries were successfully added, false if they were already added before.
     */
    public static boolean bCreateMedicationIngestions(Persistence p) {

        if (!util.FileSystem.bCheckFileExists(Init.FHIRFile)){
            logger.log(Level.INFO, "<-- IngestionCreator--> No entries were inserted into the persistence file because the FHIR file does not exist yet.");
            return false;
        }

        BMPInterpreter mInt = new BMPInterpreter();
        ArrayList<Section> sections = mInt.getSections();
        int counter = 0;

        if ((sections == null) || sections.isEmpty()) {
            logger.log(Level.INFO, "<-- IngestionCreator--> No entries were inserted into the persistence file because the data could not be parsed.");
            return false;
        }
        else {

            //  Get the difference in days between today and the last opened day
            Date dLastOpened = p.readLastOpened();
            ArrayList<Date> lDifferenceInDays = util.Date.getDifference(dLastOpened, new Date());

            // Fix for the case that the system time cannot be read and is set e.g. to 1970
            if (lDifferenceInDays.size() > 60){
                lDifferenceInDays.clear();
            }

            // Make sure that the entries for today are created when the persistence file is empty
            if (Init.persistenceXMLObject.getMedicationIngestion(new Date()).isEmpty()){
                lDifferenceInDays.add(new Date());
            }

            for (Date d : lDifferenceInDays) {

                // Insert new values only if there are no entries for the given current date
                if (p.readMedicationIngestionStatus(d).isEmpty()) {

                    // each section
                    for (int l = 0; l < sections.size(); l++) {
                        ArrayList<Medication> m = sections.get(l).getMedications();

                        // each medicament
                        for (int i = 0; i < m.size(); i++) {
                            // These settings are always valid
                            PersistenceMedIngObject mi = new PersistenceMedIngObject();
                            mi.setMedStatementReference(m.get(i).getReferenceMedicationStatement());
                            mi.setScheduledDate(d);
                            mi.setStatus(eIngestedStatus.DEFAULT);

                            // Create a separate object for each valid ingestion time -> that are all elements but the last (Udef)
                            // If there is a medication that has no ingestion times defined, skip it.
                            for (eIngestionTime eI : eIngestionTime.values()) {

                                ArrayList<IngestionTime> it = m.get(i).getIngestionTimes();

                                int x = eIngestionTime.values().length;

                                if ((eI.ordinal() != (x - 1))
                                        && (it.size() > 3) && (it.get(eI.ordinal()).getDosageValue().compareTo("0") != 0)) {

                                    mi.setScheduledTimeCode(eI);
                                    mi.setTime(null);
                                    p.writeMedicationIngestion(mi);
                                    counter++;
                                }
                            }
                        }
                    }
                }
            }
            if (counter == 0) {
                logger.log(Level.INFO, "<-- IngestionCreator--> No entries were inserted into the persistence file because they already exist");
                return false;
            } else {
                logger.log(Level.INFO, "<-- IngestionCreator --> " + counter + " new entries were inserted into the persistence file for the current day");
                return true;
            }
        }

    }
}
