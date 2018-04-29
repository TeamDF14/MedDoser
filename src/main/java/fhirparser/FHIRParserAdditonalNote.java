package fhirparser;

import help.Help;
import org.w3c.dom.NodeList;

import java.util.logging.Level;

import static fhirparser.FHIRParser.*;
import static logging.Logging.logger;

/**
 * Collects the further information, which is note below the medication plan.
 */
public class FHIRParserAdditonalNote {

    /**
     * Get the information from the additional notes found under the Medication Plan
     *
     * @return the information from the additional notes found under the Medication Plan as ArrayList<String>
     */
    public String getAdditionalNoteInformationen() {
        // If there is no composite observation information
        if (indiceAdditionalNotes == -1){
            logger.log(Level.INFO, "There is no additional note described under the medication plan !");
            return null;
        }

        // Declare variables
        String additonalNotes = null;

        // Get information from HL7 FHIR document
        NodeList nList_additonalNotesCoding = documentsEntrys.get(indiceAdditionalNotes).getElementsByTagName(CODING);

        String loincCode = util.XML.searchHierarchyByTagAndAttribute(nList_additonalNotesCoding, SYSTEM, VALUE,  LOINCADRESS, CODE, VALUE);
        if(loincCode.equals("69730-0")){
            // Get information from HL7 FHIR document
            NodeList nList_additonalNotes = documentsEntrys.get(indiceAdditionalNotes).getElementsByTagName("div");

            // Save additional notes
            String notes = nList_additonalNotes.item(0).getTextContent();
            if(!util.String.isEmpty(notes)){
                additonalNotes = notes;
            }

        } else {
            logger.log(Level.INFO, "There is no additional notes because the loinc code is unknown!");
        }

        return additonalNotes;
    }
}
