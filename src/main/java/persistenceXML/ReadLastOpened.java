package persistenceXML;

import help.Help;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import java.util.Date;

import static persistenceXML.Persistence.LASTOPENEDDATE;

/**
 * This class writes the default body of the XML so that the entries can be edited.
 */
public class ReadLastOpened {

    /**
     * Gets the date the program was last opened
     *
     * @param doc expect the whole document
     * @return the date the program was last opened as type String
     */
    protected String lastOpenedAsString(final Document doc){
        return readLastOpened(doc);
    }

    /**
     * Gets the date the program was last opened
     *
     * @param doc expect the whole document
     * @return the date the program was last open as type Date
     */
    protected Date lastOpened(final Document doc){
        return util.Date.convertStringToDateTime(readLastOpened(doc));
    }

    /**
     * Gets the date the program was last opened
     *
     * @param doc expect the whole document
     * @return the date the program was last opened as type Date
     */
    private String readLastOpened(Document doc) {
        // read information from XML document and safe into NodeList
        NodeList nList_lastOpened = doc.getElementsByTagName(LASTOPENEDDATE);

        // Get the morning ingestion time
        return nList_lastOpened.item(0).getTextContent();
    }
}
