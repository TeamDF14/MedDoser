package fhirparser;

import help.Help;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.util.ArrayList;
import java.util.logging.Level;

import static fhirparser.FHIRParser.*;
import static logging.Logging.logger;

/**
 * Get the information about the patient
 *
 * @author Sebastian Buechler <orderwb6@gmail.com>
 * @author Stefan Kuppelwieser <edelblistar@online.de>
 */
public class FHIRParserPatient {

    /**
     * Gets the information of the egk number, surname, sex and birthday of the patient
     * It is always the second 'entry' child -> documentsEntrys.get(1)
     *
     * <li>[0] => egk number</li>
     * <li>[1] => name of patient</li>
     * <li>[2] => surname of patient</li>
     * <li>[3] => birthday of patient</li>
     * <li>[4] => gender of patient</li>
     *
     * @return patientInformation with patient information
     */
    public ArrayList<String>  getPatientInformation(){
        // If there is no patient information
        if (indicePatientInformation == -1){
            return null;
        }

        // Declare ArrayList
        ArrayList<String> patientInformation = new ArrayList<>();

        // Get information from HL7 FHIR document
        NodeList nList_nameTest = documentsEntrys.get(indicePatientInformation).getElementsByTagName("name");
        NodeList nList_gender = documentsEntrys.get(indicePatientInformation).getElementsByTagName("gender");
        NodeList nList_birthTime = documentsEntrys.get(indicePatientInformation).getElementsByTagName("birthDate");
        NodeList nList_systemEGK = documentsEntrys.get(indicePatientInformation).getElementsByTagName(IDENTIFIER);

        //Get kv-nummer
        String kvNumber = util.XML.searchHierarchyByTagAndAttribute(nList_systemEGK, SYSTEM, VALUE, "http://kvnummer.gkvnet.de", VALUE, VALUE);
        if(!util.String.isEmpty(kvNumber)) {
            patientInformation.add(0, kvNumber);
        } else {
            patientInformation.add(0, EMPTYSTRING);
            logger.log(Level.FINEST, "KV-number of patient is unknown! HL7 FHIR Document failure!");
        }

        // Get name
        String name = util.XML.searchHierarchyByAttribute(nList_nameTest, "given");
        if(!util.String.isEmpty(name)) {
            patientInformation.add(1, name);
        }else{
            patientInformation.add(1, EMPTYSTRING);
            logger.log(Level.FINEST, "Name of patient ist unknown! HL7 FHIR Document failure!");
        }

        // Get surname
        String surname = util.XML.searchHierarchyByAttribute(nList_nameTest, "family");
        if(!util.String.isEmpty(surname)) {
            patientInformation.add(2, surname);
        }else{
            patientInformation.add(2, EMPTYSTRING);
            logger.log(Level.FINEST, "Surname of patient ist unknown! HL7 FHIR Document failure!");
        }

        // Get birthday
        if(nList_birthTime.getLength() != 0) {
            patientInformation.add(3, ((Element) nList_birthTime.item(0)).getAttribute(VALUE));
        }else {
            patientInformation.add(3, EMPTYSTRING);
            logger.log(Level.INFO, "The birthday of the patient is unknown!");
        }

        // Get gender
        if(nList_gender.getLength() != 0){
            patientInformation.add(4, enums.eGender.convertUKFGenderName(((Element) nList_gender.item(0)).getAttribute(VALUE)));
        } else{
            patientInformation.add(4, enums.eGender.convertUKFGenderName(EMPTYSTRING));
            logger.log(Level.INFO, "The gender of the patient is unknown!");
        }

        return patientInformation;
    }
}
