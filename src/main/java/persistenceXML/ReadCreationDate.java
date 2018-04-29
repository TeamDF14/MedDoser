package persistenceXML;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import help.Help;

import java.util.Date;

import static persistenceXML.Persistence.CREATIONDATE;

/**
 * <p>This class reads the creation date of the persistence.</p>
 */
public class ReadCreationDate {

    /**
     * <p>Gets the date when the persistence file was created</p>
     * @param doc expect the whole document
     * @return the date the program was last opened as type String
     */
    protected String readCreationDateAsString(final Document doc){
        return readLastOpened(doc);
    }

    /**
     * <p>Gets the date when the persistence file was created</p>
     * @param doc expect the whole document
     * @return the date the program was last open as type Date
     */
    protected Date lastOpened(final Document doc){
        return util.Date.convertStringToDateTime(readLastOpened(doc));
    }

    /**
     * <p>Gets the date when the persistence file was created</p>
     * @param doc expect the whole document
     * @return the date the program was last opened as type Date
     */
    private String readLastOpened(Document doc) {
        // read information from XML document and safe into NodeList
        NodeList nList_lastOpened = doc.getElementsByTagName(CREATIONDATE);

        // Get the morning ingestion time
        return nList_lastOpened.item(0).getTextContent();
    }
}
