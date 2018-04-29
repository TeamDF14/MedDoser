package persistenceXML;

import help.Help;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.Date;

import static persistenceXML.Persistence.*;

/**
 * This class reads the time of the ingestion.
 */
public class ReadIngestionTime {

    /**
     * The morning time of type Date
     *
     * @return the morning time of type Date
     */
    protected Date morning(final Document doc){
        return util.Date.convertStringToTime(readTime(doc, MORNING));
    }

    /**
     * The midday time of type Date
     *
     * @return the midday time of type Date
     */
    protected Date midday(final Document doc){
        return util.Date.convertStringToTime(readTime(doc, MIDDAY));
    }

    /**
     * The evening time of type Date
     *
     * @return the evening time of type Date
     */
    protected Date evening(final Document doc){
        return util.Date.convertStringToTime(readTime(doc, EVENING));
    }

    /**
     * The night time of type Date
     *
     * @return the night time of type Date
     */
    protected Date night(final Document doc){
        return util.Date.convertStringToTime(readTime(doc, NIGHT));
    }

    /**
     * The morning time of type String
     *
     * @return the morning time of type String
     */
    protected String morningAsString(final Document doc){
        return readTime(doc, MORNING);
    }

    /**
     * The midday time of type String
     *
     * @return the midday time of type String
     */
    protected String middayAsString(final Document doc){
        return readTime(doc,MIDDAY);
    }

    /**
     * The evening time of type String
     *
     * @return the evening time of type String
     */
    protected String eveningAsString(final Document doc){
        return readTime(doc, EVENING);
    }

    /**
     * The night time of type String
     *
     * @return the night time of type String
     */
    protected String nightAsString(final Document doc){
        return readTime(doc, NIGHT);
    }

    /**
     * Returns the length of ingestion time.
     *
     * @param doc The whole persistence document.
     * @return The length of ingestion time.
     */
    protected int getLength(final Document doc){
        // Declare and initialize variables
        int counter = 0;
        NodeList nodeList_ingestionTime = doc.getElementsByTagName(INGESTIONTIME);
        NodeList nodeList_ingestionTimeElements =  nodeList_ingestionTime.item(0).getChildNodes();

        // Set counter
        for(int i = 0; i < nodeList_ingestionTime.item(0).getChildNodes().getLength(); i++){
            Node node = nodeList_ingestionTimeElements.item(i);
            if(node.getNodeType() == node.ELEMENT_NODE) {
                counter++;
            }
        }

        return counter;
    }

    /**
     * Returns the time as String by ingestion time.
     *
     * @param doc expect the document
     * @param ingestionTime expect the time
     * @return the time as String by ingestion time of type String
     */
    private String readTime(final Document doc, final String ingestionTime) {

        // read information from XML document and safe into NodeList
        NodeList nList_ingestionTime = doc.getElementsByTagName(INGESTIONTIME);

        return util.XML.searchHierarchyByAttributeAndValueName(nList_ingestionTime, ingestionTime, TIME);
    }
}
