package fhirparser;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.util.ArrayList;
import java.util.logging.Level;

import static fhirparser.FHIRParser.*;
import static logging.Logging.logger;

/**
 * Get the meta information about the HL7 Fhir document
 *
 * @author Sebastian Buechler <orderwb6@gmail.com>
 * @author Stefan Kuppelwieser <edelblistar@online.de>
 */
public class FHIRParserMetaInformation {

    /**
     * Returns the meta information about the HL7 FHIR document
     * It is always the first 'entry' child -> documentsEntrys.get(0)
     * <p>
     * <li>[0] => language</li>
     * <li>[1] => instance number</li>
     * <li>[2] => versionID</li>
     * <li>[3] => creation date of composite</li>
     *
     * @return metaInformation with meta information
     */
    public ArrayList<String> getMetaInformation() {
        // If there is no meta information
        if (indiceMetaInformation == -1){
            return null;
        }

        // Declare ArrayList
        ArrayList<String> metaInformation = new ArrayList<>();

        // Get information from first Child
        NodeList nList_language = documentsEntrys.get(indiceMetaInformation).getElementsByTagName("language");
        NodeList nList_systemID = documentsEntrys.get(indiceMetaInformation).getElementsByTagName(SYSTEM);
        NodeList nList_instanceID = documentsEntrys.get(indiceMetaInformation).getElementsByTagName(VALUE);
        NodeList nList_version = documentsEntrys.get(indiceMetaInformation).getElementsByTagName("id");
        NodeList nList_creationDate = documentsEntrys.get(indiceMetaInformation).getElementsByTagName("date");

        // Get language
        if(nList_language.getLength() != 0){
            metaInformation.add(0, ((Element) nList_language.item(0)).getAttribute(VALUE));
        } else{
            metaInformation.add(0, EMPTYSTRING);
            logger.log(Level.FINEST, "Language of bmp is unknown! HL7 FHIR Document failure!");
        }

        // Get instance number
        if (((Element) nList_systemID.item(0)).getAttribute(VALUE).equals("http://mein.medikationsplan.de/composition")) {
            Element element = (Element) nList_instanceID.item(0);
            metaInformation.add(1, element.getAttribute(VALUE));
        } else {
            metaInformation.add(0, EMPTYSTRING);
            logger.log(Level.FINEST, "Instance of bmp is unknown! HL7 FHIR Document failure!");
        }

        // Get versionID. It is always the first value. It is a required field
        if (nList_version.getLength() != 0) {
            metaInformation.add(2, ((Element) nList_version.item(0)).getAttribute(VALUE));
        } else {
            metaInformation.add(2, EMPTYSTRING);
            logger.log(Level.FINEST, "Version of bmp is unknown! HL7 FHIR Document failure!");
        }

        // Get date of creation of the composition (equals medicationplan)
        if(nList_creationDate.getLength() != 0){
            metaInformation.add(3, ((Element) nList_creationDate.item(0)).getAttribute(VALUE));
        } else{
            metaInformation.add(3, EMPTYSTRING);
            logger.log(Level.FINEST, "The creation date of composite is unknown!");
        }

        return metaInformation;
    }
}
