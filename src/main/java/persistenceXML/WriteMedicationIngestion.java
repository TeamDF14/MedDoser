package persistenceXML;

import init.Init;
import help.Help;
import org.jdom.Document;
import org.jdom.JDOMException;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

import java.io.FileWriter;
import java.io.IOException;
import java.util.logging.Level;
import org.jdom.input.SAXBuilder;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;
import org.w3c.dom.NodeList;


import static logging.Logging.logger;
import static persistenceXML.Persistence.*;

/**
 * This class contains write and update methods for taking medication.
 */
public class WriteMedicationIngestion {

    /**
     * This method adds a new object to the persistence file.
     * It is also decided, if a PersistenceMedIngObject has ever been added.
     * If not, a new element is created for the given object.
     *
     * @param PersistenceMedIngObject The PersistenceMedIngObject object, that describes the attributes that will be saved.
     */
    public void addMedicationIngestion(PersistenceMedIngObject PersistenceMedIngObject) {

        if(!medicationIngestionExist()){
            addElementAndMedicationIngestionElement(PersistenceMedIngObject);
        } else {
            addMedicationIngestionElement(PersistenceMedIngObject);
        }

    }

    /**
     * This method modifies an existing entry in the persistence file of type PersistenceMedIngObject.
     * It only changes the value 'time', 'scheduledTime' and 'status'.
     *
     * @param medIngObjectToUpdate It expects the medIngObjectToUpdate object, that describes the attributes that will be saved.
     */
    public void modifyMedicationIngestion(PersistenceMedIngObject medIngObjectToUpdate){
        try {
            org.w3c.dom.Document doc = getBuildDocument();

            NodeList nList_medicationIngestion = doc.getElementsByTagName(MEDICATIONINGESTION);
            NodeList nList_ingestion = ((Element) nList_medicationIngestion.item(0)).getElementsByTagName(INGESTION);

            for(int i = 0; i < nList_ingestion.getLength(); i++){

                Node ingestion = nList_ingestion.item(i);

                NamedNodeMap attributes = ingestion.getAttributes();
                Node attributeMedStatementReference = attributes.getNamedItem(MEDSTATEMENTREFERENCE);
                Node attributeScheduledDate = attributes.getNamedItem(SCHEDULEDDATE);
                Node attributeScheduledTimeCode = attributes.getNamedItem(SCHEDULEDTIMECODE);

                // Find the already existing entry in the persistence file
                if(attributeMedStatementReference.getTextContent().equals(medIngObjectToUpdate.getMedStatementReference()) &&
                        attributeScheduledDate.getTextContent().equals(util.Date.convertDateToString(medIngObjectToUpdate.getScheduledDate(), true)) &&
                        attributeScheduledTimeCode.getTextContent().equals(medIngObjectToUpdate.getScheduledTimeCode().toString()))
                {
                    // Set status
                    Node attributeStatus = attributes.getNamedItem(STATUS);
                    attributeStatus.setTextContent(medIngObjectToUpdate.getStatus().toString());

                    // Set time
                    Node attributeTime = attributes.getNamedItem(TIME);
                    if (medIngObjectToUpdate.getTime() != null) {
                        attributeTime.setTextContent(util.Date.convertTimeToString(medIngObjectToUpdate.getTime()));
                    }
                    else {
                        attributeTime.setTextContent(EMPTYSTRING);
                    }

                    // Set scheduled time
                    Node attributeScheduledTime = attributes.getNamedItem(SCHEDULEDTIME);
                    if (medIngObjectToUpdate.getScheduledTime() != null) {
                        attributeScheduledTime.setTextContent(util.Date.convertTimeToString(medIngObjectToUpdate.getScheduledTime()));
                    }
                    else {
                        attributeScheduledTime.setTextContent(EMPTYSTRING);
                    }

                }
            }

            // write the content on console
            prettyPrint(doc);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * This method adds a new object PersistenceMedIngObject to the persistence file.
     * This method adds also an entry to the persistence file to the PersistenceMedIngObject element.
     *
     * @param PersistenceMedIngObject The PersistenceMedIngObject object, that describes the attributes that will be saved.
     */
    private void addMedicationIngestionElement(PersistenceMedIngObject PersistenceMedIngObject) {
        org.w3c.dom.Document doc = getBuildDocument();

        // Get the staff element by tag name directly
        Node medicationPlanInterpreter = doc.getElementsByTagName(MEDICATIONINGESTION).item(0);

        // append a new node to staff
        org.w3c.dom.Element ingestion = doc.createElement(INGESTION);

        if(PersistenceMedIngObject.getMedStatementReference() != null){
            ingestion.setAttribute(MEDSTATEMENTREFERENCE, PersistenceMedIngObject.getMedStatementReference());
        } else {
            ingestion.setAttribute(MEDSTATEMENTREFERENCE, EMPTYSTRING);
        }

        if(PersistenceMedIngObject.getScheduledDate() != null){
            ingestion.setAttribute(SCHEDULEDDATE, util.Date.convertDateToString(PersistenceMedIngObject.getScheduledDate(), true));
        } else {
            ingestion.setAttribute(SCHEDULEDDATE, EMPTYSTRING);
        }

        if(PersistenceMedIngObject.getScheduledTimeCode() != null){
            ingestion.setAttribute(SCHEDULEDTIMECODE, PersistenceMedIngObject.getScheduledTimeCode().toString());
        } else {
            ingestion.setAttribute(SCHEDULEDTIMECODE, EMPTYSTRING);
        }


        if(PersistenceMedIngObject.getScheduledTime() != null){
            ingestion.setAttribute(SCHEDULEDTIME,  util.Date.convertTimeToString(PersistenceMedIngObject.getScheduledTime()));
        } else {
            ingestion.setAttribute(SCHEDULEDTIME, EMPTYSTRING);
        }

        if(PersistenceMedIngObject.getStatus() != null){
            ingestion.setAttribute(STATUS, PersistenceMedIngObject.getStatus().toString());
        } else {
            ingestion.setAttribute(STATUS, EMPTYSTRING);
        }

        if(PersistenceMedIngObject.getTime() != null){
            ingestion.setAttribute(TIME, util.Date.convertTimeToString(PersistenceMedIngObject.getTime()));
        } else {
            ingestion.setAttribute(TIME, EMPTYSTRING);
        }

        medicationPlanInterpreter.appendChild(ingestion);

        // write the content into xml file
        prettyPrint(doc);
    }

    /**
     * This method adds a new object PersistenceMedIngObject to the persistence file.
     * This method adds also a entry to the persistence file for all PersistenceMedIngObject entries.
     * The PersistenceMedIngObject element will also created. It will only create unique entries.
     *
     * @param PersistenceMedIngObject The PersistenceMedIngObject object, that describes the attributes that will be saved.
     */
    private void addElementAndMedicationIngestionElement(PersistenceMedIngObject PersistenceMedIngObject) {
        try {
            Document doc = new SAXBuilder().build(Init.persistenceFile);

            // Create 'head' element
            org.jdom.Element program = doc.getRootElement();
            org.jdom.Element medicationInterpreter = new org.jdom.Element(MEDICATIONINGESTION);

            // Add first ingestion to MedicationInterpreter
            org.jdom.Element ingestion = new org.jdom.Element(INGESTION);

            if(PersistenceMedIngObject.getMedStatementReference() != null){
                ingestion.setAttribute(MEDSTATEMENTREFERENCE, PersistenceMedIngObject.getMedStatementReference());
            } else {
                ingestion.setAttribute(MEDSTATEMENTREFERENCE, EMPTYSTRING);
            }

            if(PersistenceMedIngObject.getScheduledDate() != null){
                ingestion.setAttribute(SCHEDULEDDATE, util.Date.convertDateToString(PersistenceMedIngObject.getScheduledDate(), true));
            } else {
                ingestion.setAttribute(SCHEDULEDDATE, EMPTYSTRING);
            }

            if(PersistenceMedIngObject.getScheduledTimeCode() != null){
                ingestion.setAttribute(SCHEDULEDTIMECODE, PersistenceMedIngObject.getScheduledTimeCode().toString());
            } else {
                ingestion.setAttribute(SCHEDULEDTIMECODE, EMPTYSTRING);
            }

            if(PersistenceMedIngObject.getScheduledTime() != null){
                ingestion.setAttribute(SCHEDULEDTIME, util.Date.convertTimeToString(PersistenceMedIngObject.getScheduledTime()));
            } else {
                ingestion.setAttribute(SCHEDULEDTIME, EMPTYSTRING);
            }

            if(PersistenceMedIngObject.getStatus() != null){
                ingestion.setAttribute(STATUS, PersistenceMedIngObject.getStatus().toString());
            } else {
                ingestion.setAttribute(STATUS, EMPTYSTRING);
            }

            if(PersistenceMedIngObject.getTime() != null){
                ingestion.setAttribute(TIME, util.Date.convertTimeToString(PersistenceMedIngObject.getTime()));
            } else {
                ingestion.setAttribute(TIME, EMPTYSTRING);
            }

            medicationInterpreter.addContent(ingestion);
            program.addContent(medicationInterpreter);

            // Save the ingestion and print the xml pretty
            XMLOutputter out = new XMLOutputter(Format.getPrettyFormat());
            FileWriter writer = new FileWriter(Init.persistenceFile);
            out.output(program, writer);
            writer.close();
        } catch(JDOMException e){
            logger.log(Level.FINEST, "Can not create the element " + ": " + e.toString());
        } catch(IOException e){
            logger.log(Level.FINEST, "Can not create the element " + ": " + e.toString());
        }
    }

    /**
     * This method checks, if the PersistenceMedIngObject element exist.
     *<ul>
     * <li>[0] => The PersistenceMedIngObject element not exist</li>
     * <li>[1] => The PersistenceMedIngObject element not exist</li>
     *</ul>
     *
     * @return True if it exists, false if not.
     */
    private boolean medicationIngestionExist() {
        org.w3c.dom.Document doc = getBuildDocument();

        Node nodeMedicationIngestion = doc.getElementsByTagName(MEDICATIONINGESTION).item(0);

        return nodeMedicationIngestion != null;
    }
}
