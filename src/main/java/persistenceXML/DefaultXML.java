package persistenceXML;

import help.Help;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.util.Date;
import java.util.logging.Level;

import static logging.Logging.logger;
import static persistenceXML.Persistence.*;

/**
 * This class generates a default xml file if it not exists already.
 */
public class DefaultXML {


    /**
     * The constructor calls the method to create the default xml file.
     */
    protected DefaultXML(){

        // create default xml filed
        try {
            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

            // root elements
            Document doc = docBuilder.newDocument();
            Element rootElement = doc.createElement(ROOTELEMENT);
            doc.appendChild(rootElement);

            // Create creation und last opened date
            createDefaultTimes(doc, rootElement);

            // Create ingestion times
            createIngestionTimes(doc, rootElement);

            prettyPrint(doc);

            logger.log(Level.INFO, "A new and empty persistence XML file was created!");

        } catch (ParserConfigurationException e) {
            logger.log(Level.FINEST, "Could not create default XML file" + ": " + e.toString());
        }
    }

    /**
     * Creates the <b>ingestion times</b> part in the xml file.
     *
     * @param doc The doc of type Document.
     * @param rootElement The root element of doc.
     */
    private void createIngestionTimes(Document doc, Element rootElement) {

        // Element ingestion time
        Element elementIngestionTime = doc.createElement(INGESTIONTIME);
        rootElement.appendChild(elementIngestionTime);

        // Ingestion time for morning
        Element elementIngestionTimeMorning = doc.createElement(MORNING);
        elementIngestionTime.appendChild(elementIngestionTimeMorning);
        elementIngestionTimeMorning.setAttribute(TIME, "08:00");

        // Ingestion time for midday
        Element elementIngestionTimeMidday = doc.createElement(MIDDAY);
        elementIngestionTime.appendChild(elementIngestionTimeMidday);
        elementIngestionTimeMidday.setAttribute(TIME, "12:00");

        // Ingestion time for evening
        Element elementIngestionTimeEvent = doc.createElement(EVENING);
        elementIngestionTime.appendChild(elementIngestionTimeEvent);
        elementIngestionTimeEvent.setAttribute(TIME, "17:00");

        // Ingestion time for night
        Element elementIngestionTimeNight = doc.createElement(NIGHT);
        elementIngestionTime.appendChild(elementIngestionTimeNight);
        elementIngestionTimeNight.setAttribute(TIME, "21:00");
    }

    /**
     * Creates the <b>default times</b> part in the xml file.
     *
     * @param doc The doc of type Document.
     * @param rootElement The root element of doc.
     */
    private void createDefaultTimes(Document doc, Element rootElement) {

        // Add creation date
        Node creationDate = doc.createElement(CREATIONDATE);
        rootElement.appendChild(creationDate);
        creationDate.setTextContent(util.Date.convertDateTimeToString(new Date()));

        // Add last opend date
        Node lastedOpenedDate = doc.createElement(LASTOPENEDDATE);
        rootElement.appendChild(lastedOpenedDate);
        lastedOpenedDate.setTextContent(util.Date.convertDateTimeToString(new Date()));
    }
}
