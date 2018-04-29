package persistenceXML;

import help.Help;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

import java.util.Date;

import static persistenceXML.Persistence.LASTOPENEDDATE;
import static persistenceXML.Persistence.getBuildDocument;
import static persistenceXML.Persistence.prettyPrint;

/**
 * The class writes the last date when the application was opened.
 */
public class WriteLastOpened {

    /**
     * Save the date when the program was last opened
     *
     * @param date expect the date when the program was last opened
     */
    protected void saveLastOpened(final Date date){
        modifyLastOpenedTime(util.Date.convertDateTimeToString(date));
    }

    /**
     * Save (modify) the date when the program was last opened
     *
     * @param date expect the date when the program was last opened
     */
    private void modifyLastOpenedTime(final String date) {
        Document doc = getBuildDocument();

        if(doc != null){
            // Get the staff element by tag name directly
            Node node = doc.getElementsByTagName(LASTOPENEDDATE).item(0);

            // update last opened date
            node.setTextContent(date);

            // write the content into xml file
            prettyPrint(doc);
        }
    }
}
