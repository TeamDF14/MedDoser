package persistenceXML;

import enums.eIngestedStatus;
import enums.eIngestionTime;
import help.Help;
import org.joda.time.LocalDate;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.ArrayList;
import java.util.Date;

import static persistenceXML.Persistence.*;

/**
 * This class contains read methods for the medication ingestion.
 */
public class ReadMedicationIngestion {

    /**
     * It returns the medication of all ingestion.
     *
     * @param doc The XML file of type Document.
     * @return The medication of all ingestions.
     */
    public ArrayList<PersistenceMedIngObject> getMedicationIngestionAll(Document doc) {
        ArrayList<PersistenceMedIngObject> arrayListPersistenceMedIngObject = new ArrayList<>();
        NodeList nList_medicationIngestion = doc.getElementsByTagName(MEDICATIONINGESTION);

        for (int i = 0; i < nList_medicationIngestion.getLength(); i++) {

            // Get i element of nodeList
            Node node = nList_medicationIngestion.item(i);

            // Check if node is a element node
            if(node.getNodeType()==Node.ELEMENT_NODE){

                // Get child of node
                Element element = (Element) node;
                NodeList nameList = element.getChildNodes();

                // Iterate child elements
                for(int j = 0; j < nameList.getLength(); j++){

                    // Get child element
                    Node n = nameList.item(j);
                    if(n.getNodeType()==Node.ELEMENT_NODE){
                        Element name = (Element) n;

                        // Fill ArrayList
                        if(name.getTagName().equals(INGESTION)) {

                            PersistenceMedIngObject PersistenceMedIngObject = new PersistenceMedIngObject();

                            PersistenceMedIngObject.setMedStatementReference(name.getAttribute(MEDSTATEMENTREFERENCE));
                            PersistenceMedIngObject.setScheduledDate(util.Date.convertStringToDate(name.getAttribute(SCHEDULEDDATE)));
                            PersistenceMedIngObject.setScheduledTimeCode(eIngestionTime.fromString(name.getAttribute(SCHEDULEDTIMECODE)));
                            PersistenceMedIngObject.setScheduledTime(util.Date.convertStringToTime(name.getAttribute(SCHEDULEDTIME)));
                            PersistenceMedIngObject.setStatus(eIngestedStatus.fromString(name.getAttribute(STATUS)));
                            PersistenceMedIngObject.setTime(util.Date.convertStringToTime(name.getAttribute(TIME)));

                            arrayListPersistenceMedIngObject.add(PersistenceMedIngObject);
                        }
                    }
                }
            }
        }
        return arrayListPersistenceMedIngObject;
    }

    /**
     * It returns the medication for the single date.
     *
     * @param doc It expect the XML file of type Document
     * @param date It expect the date for filtering
     * @return the medication for the date
     */
    public ArrayList<PersistenceMedIngObject> getMedicationIngestionDate(Document doc, Date date){

        return getMedicationIngestion(doc, date, false);
    }

    /**
     * It returns the medication before the specified date.
     *
     * @param doc It expect the XML file of type Document
     * @param date It expect the date for filtering
     * @return the medication before the specified date
     */
    public ArrayList<PersistenceMedIngObject> getMedicationIngestionBeforeDate(Document doc, Date date){

        return getMedicationIngestion(doc, date, true);
    }

    /**
     * This method gets the information from the XML document.
     * A distinction is made as to whether the information should
     * be fetched exactly on the date or before the date. This
     * will be indicated with parameterized boolean
     *
     * @param doc The XML file of type Document.
     * @param date The date for filtering.
     * @param before The information, if you want the entries <b>before</b> or <b>for</b> the specified date.
     * @return the medication before the specified date or for the date
     */
    private ArrayList<PersistenceMedIngObject> getMedicationIngestion(Document doc, Date date, boolean before) {
        ArrayList<PersistenceMedIngObject> persistenceMedIngObjectArrayListAll = getMedicationIngestionAll(doc);
        ArrayList<PersistenceMedIngObject> persistenceMedIngObjectArrayList = new ArrayList<>();

        for(int i = 0; i < persistenceMedIngObjectArrayListAll.size(); i++){

            if(before) {
                org.joda.time.DateTime persistenceDate = new org.joda.time.DateTime(persistenceMedIngObjectArrayListAll.get(i).getScheduledDate());
                org.joda.time.DateTime askedDate = new org.joda.time.DateTime(date);

                LocalDate firstDate = persistenceDate.toLocalDate();
                LocalDate secondDate = askedDate.toLocalDate();

                if(firstDate.isBefore(secondDate)){
                    persistenceMedIngObjectArrayList.add(persistenceMedIngObjectArrayListAll.get(i));
                }

            } else if(!before){

                String persistenceDate = util.Date.convertDateToString(persistenceMedIngObjectArrayListAll.get(i).getScheduledDate(), true);
                String askedDate = util.Date.convertDateToString(date, true);

                if (askedDate.equals(persistenceDate)) {
                    persistenceMedIngObjectArrayList.add(persistenceMedIngObjectArrayListAll.get(i));
                }
            }
        }

        return persistenceMedIngObjectArrayList;
    }
}
