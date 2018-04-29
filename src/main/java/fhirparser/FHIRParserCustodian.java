package fhirparser;

import org.w3c.dom.NodeList;

import java.util.ArrayList;
import java.util.logging.Level;

import static fhirparser.FHIRParser.*;
import static logging.Logging.logger;

/**
 * Contains the information of the custodian that express the identification of system
 *
 * @author Sebastian Buechler <orderwb6@gmail.com>
 * @author Stefan Kuppelwieser <edelblistar@online.de>
 */
public class FHIRParserCustodian {

    /**
     * The organization entrusted with the administration of the document (custodian) is referenced in the corresponding element of the composition.
     * So the method save the identifier of organization
     * How the entries are defined can be seen below..
     *
     * <li>[0] => identification system</li>
     * <li>[1] => identification</li>
     *
     * @return a filled ArrayList<String> with the information about the identifier of organization
     */
    public ArrayList<String> getCustodianInformation(){
        // If there is no autor information
        if (indiceCustodian == -1){
            return null;
        }

        // Declare ArrayList
        ArrayList<String> autorCustodian = new ArrayList<>();

        // Get information from HL7 Fhir document
        NodeList nList_identifier = documentsEntrys.get(indiceCustodian).getElementsByTagName(IDENTIFIER);

        // Save identification system
        String identificationSystem = util.XML.searchHierarchyByAttribute(nList_identifier, SYSTEM);
        if(!util.String.isEmpty(identificationSystem)){
            autorCustodian.add(0, identificationSystem);
        } else {
            autorCustodian.add(0, EMPTYSTRING);
            logger.log(Level.INFO, "The identification system is unknown!");
        }

        // Save identification
        String identification = util.XML.searchHierarchyByAttribute(nList_identifier, VALUE);
        if(!util.String.isEmpty(identification)) {
            autorCustodian.add(1, identification);
        } else {
            autorCustodian.add(1, EMPTYSTRING);
            logger.log(Level.INFO, "The identification is unknown!");
        }

        return autorCustodian;
    }
}
