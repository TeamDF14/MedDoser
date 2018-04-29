package persistenceXML;

import help.Help;
import org.w3c.dom.*;

import java.util.Date;

import static persistenceXML.Persistence.*;

/**
 * This class writes the default body of the XML.
 */
public class WriteIngestionTime {

    /**
     * Writes the ingestion time of morning in the XML document.
     *
     * @param time the ingestion time of morning in the XML document
     */
    protected void morning(final Date time){
        modifyIngestionTime(MORNING, util.Date.convertTimeToString(time));
    }

     /**
     * Write the ingestion time of midday in the XML document
     *
     * @param time the ingestion time of midday in the XML document
     */
     protected void midday(final Date time){
        modifyIngestionTime(MIDDAY, util.Date.convertTimeToString(time));
    }

    /**
     * Write the ingestion time of evening in the XML document
     *
     * @param time the ingestion time of evening in the XML document
     */
    protected void evening(final Date time){
        modifyIngestionTime(EVENING, util.Date.convertTimeToString(time));
    }

    /**
     * Write the ingestion time of night in the XML document
     *
     * @param time the ingestion time of night in the XML document
     */
    protected void night(final Date time){
        modifyIngestionTime(NIGHT, util.Date.convertTimeToString(time));
    }

    /**
     * Write the ingestion time of morning in the XML document
     *
     * @param time the ingestion time of morning in the XML document
     */
    protected void morningWithString(final String time){
        modifyIngestionTime(MORNING, time);
    }

    /**
     * Write the ingestion time of midday in the XML document
     *
     * @param time the ingestion time of midday in the XML document
     */
    protected void middayWithString(final String time){
        modifyIngestionTime(MIDDAY, time);
    }

    /**
     * Write the ingestion time of evening in the XML document
     *
     * @param time the ingestion time of evening in the XML document
     */
    protected void eveningWithString(final String time){
        modifyIngestionTime(EVENING, time);
    }

    /**
     * Write the ingestion time of night in the XML document
     *
     * @param time the ingestion time of night in the XML document
     */
    protected void nightWithString(final String time){
         modifyIngestionTime(NIGHT, time);
    }

    /**
     * Modifies the ingestion time.
     *
     * @param ingestionTime which ingestion should be modified
     * @param time what is the new time
     */
    private void modifyIngestionTime(final String ingestionTime, String time) {
        Document doc = getBuildDocument();

        if(doc != null){
            // Get the element by tag name directly
            Node node = doc.getElementsByTagName(ingestionTime).item(0);

            // update
            NamedNodeMap attr = node.getAttributes();
            Node nodeAttr = attr.getNamedItem(TIME);
            nodeAttr.setTextContent(time);

            // write the content into xml file
            prettyPrint(doc);
        }
    }
}
