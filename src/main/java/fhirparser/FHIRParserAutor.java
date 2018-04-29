package fhirparser;

import help.Help;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.util.ArrayList;

import static fhirparser.FHIRParser.*;

/**
 * Collect the information about the autor.
 *
 * @author Sebastian Buechler <orderwb6@gmail.com>
 * @author Stefan Kuppelwieser <edelblistar@online.de>
 */
public class FHIRParserAutor {

    /**
     * Gets the information about the autor
     * It is always the third 'entry' child -> documentsEntrys.get(2)
     *
     * <li>[0] => full name of autor/ exhibitor</li>
     * <li>[1] => life long doctor number</li>
     * <li>[2] => street</li>
     * <li>[3] => post code</li>
     * <li>[4] => city</li>
     * <li>[5] => telephone number</li>
     * <li>[6] => eMail</li>
     * <li>[7] => creation date</li>
     * <li>[8] => pharmacy ID</li>
     * <li>[9] => hosptial ID</li>
     *
     * @return authorInformation with autor information
     */
    public ArrayList<String> getAutorInformation(){
        // If there is no autor information
        if (indiceAutorInformation == -1){
            return null;
        }

        // Declare ArrayList
        ArrayList<String> autorInformation = new ArrayList<>();

        // Get information from third Child
        NodeList nList_name = documentsEntrys.get(indiceAutorInformation).getElementsByTagName("family");
        NodeList nList_IDNumber = documentsEntrys.get(indiceAutorInformation).getElementsByTagName("identifier");
        NodeList nList_personList = documentsEntrys.get(indiceAutorInformation).getElementsByTagName("address");
        NodeList nList_telecom = documentsEntrys.get(indiceAutorInformation).getElementsByTagName("telecom");
        NodeList nList_creationDate = documentsEntrys.get(indiceMetaInformation).getElementsByTagName("date");  // creation date is on the composition, the first <entry>, block

        //The arrayList contains information about residence of the autor
        // [0] => street
        // [1] => post code
        // [2] => city
        ArrayList<String> address = searchInterleavedHierarchy(nList_personList, "line");

        // get full name of author
        if(nList_name.getLength() != 0){
            autorInformation.add(0, ((Element) nList_name.item(0)).getAttribute(VALUE));
        } else {
            autorInformation.add(0, EMPTYSTRING);
            //logger.log(Level.INFO, "The name of the author ist unknown!");
        }

        // get life long doctor number
        String lifelongAutorNumber = util.XML.searchHierarchyByTagAndAttribute(nList_IDNumber, SYSTEM, VALUE, "http://kbv.de/LANR", VALUE, VALUE);
        if(!util.String.isEmpty(lifelongAutorNumber)) {
            autorInformation.add(1, lifelongAutorNumber);
        } else {
            autorInformation.add(1, EMPTYSTRING);
            //logger.log(Level.INFO, "The life long doctor number of the author is unknown!");
        }

        // get street
        if(address.size() != 0) {
            autorInformation.add(2, address.get(0));
        } else {
            autorInformation.add(2, EMPTYSTRING);
            //logger.log(Level.INFO, "The street of author is unknown!");
        }

        // get post code
        if(address.size() >= 1) {
            autorInformation.add(3, address.get(1));
        } else {
            autorInformation.add(3, EMPTYSTRING);
            //logger.log(Level.INFO, "The post code of author is unknown!");
        }

        // get city
        if(address.size() >= 2) {
            autorInformation.add(4, address.get(2));
        } else {
            autorInformation.add(4, EMPTYSTRING);
            //logger.log(Level.INFO, "The city of author is unknown!");
        }

        // Get telephone number
        String telephoneNumber = util.XML.searchHierarchyByTagAndAttribute(nList_telecom, "system", VALUE,"phone", VALUE, VALUE);
        if(!util.String.isEmpty(telephoneNumber)) {
            autorInformation.add(5, telephoneNumber);
        } else {
            autorInformation.add(5, EMPTYSTRING);
            //logger.log(Level.INFO, "The phone number of autor is unknown!");
        }

        // Get eMail
        String eMail = util.XML.searchHierarchyByTagAndAttribute(nList_telecom, "system", VALUE, "email", VALUE, VALUE);
        if(!util.String.isEmpty(eMail)) {
            autorInformation.add(6, eMail);
        } else {
            autorInformation.add(6, EMPTYSTRING);
            //logger.log(Level.INFO, "The eMail of autor is unknown!");
        }

        // Get creation date
        if(nList_creationDate.getLength() != 0){
            autorInformation.add(7, ((Element) nList_creationDate.item(0)).getAttribute(VALUE));
        } else {
            autorInformation.add(7, EMPTYSTRING);
            //logger.log(Level.INFO, "The creation date of bmp is unknown!");
        }

        // get pharmacy ID
        String pharmacyID = util.XML.searchHierarchyByTagAndAttribute(nList_IDNumber, SYSTEM, VALUE, "http://kbv.de/IDF", VALUE, VALUE);
        if(!util.String.isEmpty(pharmacyID)) {
            autorInformation.add(8, pharmacyID);
        } else {
            autorInformation.add(8, EMPTYSTRING);
            //logger.log(Level.INFO, "The pharmacy ID is unknown!");
        }

        // get hospital ID
        String hospitalID = util.XML.searchHierarchyByTagAndAttribute(nList_IDNumber, SYSTEM, VALUE, "http://kbv.de/KIK", VALUE, VALUE);
        if(!util.String.isEmpty(hospitalID)) {
            autorInformation.add(9, hospitalID);
        } else {
            autorInformation.add(9, EMPTYSTRING);
            //logger.log(Level.INFO, "The hospital ID is unknown!");
        }

        return autorInformation;
    }
}
