package fhirparser;

import enums.eObservationType;
import help.Help;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.util.ArrayList;

import static fhirparser.FHIRParser.*;

/**
 * Contains the information of health concers.
 * It includes breast feeding and status of pregnancy.
 *
 * @author Sebastian Buechler <orderwb6@gmail.com>
 * @author Stefan Kuppelwieser <edelblistar@online.de>
 */
public class FHIRParserHealthConcerns {

    /**
     * Gets the information about the health concerns of patient.
     * It belongs to observation of patient.
     * It exist an ArrayList, that contains n ArrayList with helath concerns.
     * How the entries are defined can be seen below..
     *
     * For pregnancy status
     * <li>[1] => loincCode</li>
     * <li>[2] => name of the object/li>
     * <li>[3] => status of pregnancy as text</li>
     * <li>[4] => status of pregnancy as boolean</li>
     *
     * For breast feeding
     * <li>[1] => name of the object/li>
     * <li>[2] => status of breast feeding as text</li>
     * <li>[3] => status of breast feeding as boolean</li>
     *
     * @return an ArrayList<ArrayList<String>> with health concers
     */
    public ArrayList<ArrayList<String>> getHealthConcernsInformation() {
        // If there is no composite observation information
        if (indiceCompositeHealthConcerns == -1) {
            return null;
        }

        // Initialize ArrayList and variables
        int counter = 0;
        ArrayList<ArrayList<String>> healthConcerns = new ArrayList<>();

        for (int i = indiceCompositeHealthConcerns + 1; i < indiceCompositeMedication; i++) {
            // Get information from HL7 FHIR document
            NodeList nList_fullUrl = documentsEntrys.get(i).getElementsByTagName("fullUrl");
            NodeList nList_code = documentsEntrys.get(i).getElementsByTagName(CODE);

            // Initialize ArrayList<String>
            ArrayList<String> healthConcern = new ArrayList<>();

            // Get reference of health concerns
            if(nList_fullUrl.getLength() != 0){
                healthConcern.add(0, ((Element) nList_fullUrl.item(0)).getAttribute(VALUE));
            }

            // Get system value to find out which branch is fulfilled
            NodeList nList_coding = ((Element) nList_code.item(0)).getElementsByTagName("code");
            String loinc = ((Element) nList_coding.item(0)).getAttribute(VALUE);

            // Here it is decided which branch is committed
            if (eObservationType.BREASTFEEDING_STATUS.equalsName(loinc)) { // case breast feeding

                healthConcern = getBreastFeedingStatus(healthConcern, documentsEntrys.get(i), nList_code);
                //logger.log(Level.INFO, "The patient has a breast feeding!");
            } else if(eObservationType.PREGNANCY_STATUS.equalsName(loinc)){ // case pregnancy status, because health concerns only have two cases

               healthConcern = getPregnancyStatus(healthConcern, documentsEntrys.get(i), nList_code);
                //logger.log(Level.INFO, "The patient has a pregnancy status!");
            }

            // Add single health concer to health concerns ArrayList<String>
            healthConcerns.add(counter, healthConcern);

            //increase counter
            counter++;
        }

        return healthConcerns;
    }

    /**
     * Inform informant about the status of the pregnancy
     *
     * The objects contains a ArrayList and is structured as follows.
     * Go into the Method to see the list:
     *
     * <li>[1] => loincCode</li>
     * <li>[2] => name of the object/li>
     * <li>[3] => status of pregnancy as text</li>
     *
     * @param prgenancyStatus expects an empty ArrayList<String>
     * @param documentPregnancyStatus contains the full entry with all information to pregnancy status
     * @return a filled arraylist of loin code 'loinCode'
     */
     private ArrayList<String> getPregnancyStatus(ArrayList<String> prgenancyStatus, Element documentPregnancyStatus, NodeList nList_code) {
        // Get information from HL7 FHIR document
        NodeList nList_status = documentPregnancyStatus.getElementsByTagName("comments");

        // Set loinc code
        NodeList nList_coding = ((Element) nList_code.item(0)).getElementsByTagName(CODING);
        String loincCode = util.XML.searchHierarchyByTagAndAttribute(nList_coding, SYSTEM, VALUE, LOINCADRESS, CODE, VALUE);
        if(!util.String.isEmpty(loincCode)) {
            prgenancyStatus.add(1, loincCode);
        } else {
            prgenancyStatus.add(1, EMPTYSTRING);
            //logger.log(Level.FINEST, "The loincCode is unknown! But the loinc code must be present!");
        }

        // Get clinicalParameter name
        String observationName = util.XML.searchHierarchyByTagAndAttribute(nList_coding, CODE, VALUE, loincCode, DISPLAY, VALUE);
        if (!util.String.isEmpty(observationName)) {
            prgenancyStatus.add(2, observationName);
        } else {
            prgenancyStatus.add(2, EMPTYSTRING);
            //logger.log(Level.INFO, "The name of the pregnancy status is unknown!");
        }

        // Get status
         String status = ((Element) nList_status.item(0)).getAttribute("value");
         if (!util.String.isEmpty(status) && status.equals("1")) {
             prgenancyStatus.add(3, status);
         } else {
             prgenancyStatus.add(3, EMPTYSTRING);
             //logger.log(Level.INFO, "The name of the pregnancy status is unknown!");
         }

        return prgenancyStatus;
    }

    /**
     * Inform informant about the status of the breast feeding
     * How the entries are defined can be seen below..
     *
     * <li>[1] => name of the object/li>
     * <li>[2] => status of breast feeding as text</li>
     * <li>[3] => status of breast feeding as boolean</li>
     *
     * @param breastFeedingStatus expects an empty ArrayList<String>
     * @param documentBreastfeedingStatus contains the full entry with all information to breast feeding status
     *@param nList_code the part of the HL7 FHIR document which should be edited by tagging element coding  @return a filled arraylist of loin code 'loinCode'
     */
    private ArrayList<String> getBreastFeedingStatus(ArrayList<String> breastFeedingStatus, Element documentBreastfeedingStatus, final NodeList nList_code){
        // Get information from HL7 FHIR document
        NodeList nList_status = documentBreastfeedingStatus.getElementsByTagName("comments");

        // Set loinc code
        NodeList nList_coding = ((Element) nList_code.item(0)).getElementsByTagName(CODING);
        String loincCode = util.XML.searchHierarchyByTagAndAttribute(nList_coding, SYSTEM, VALUE, LOINCADRESS, CODE, VALUE);
        if(!util.String.isEmpty(loincCode)) {
            breastFeedingStatus.add(1, loincCode);
        } else {
            breastFeedingStatus.add(1, EMPTYSTRING);
            //logger.log(Level.FINEST, "The loincCode is unknown! But the loinc code must be present!");
        }

        // Save the information about the status of breast feeding as text
        String value = util.XML.searchHierarchyByAttribute(nList_coding, DISPLAY);
        if (!util.String.isEmpty(value)) {
            breastFeedingStatus.add(2, value);
        } else {
            breastFeedingStatus.add(2, EMPTYSTRING);
            //logger.log(Level.INFO, "The status as text is unknown of breast feeding!");
        }

        // Get status
        String status = ((Element) nList_status.item(0)).getAttribute("value");
        if (!util.String.isEmpty(status) && status.equals("1")) {
            breastFeedingStatus.add(3, status);
        } else {
            breastFeedingStatus.add(3, EMPTYSTRING);
            //logger.log(Level.INFO, "The name of the pregnancy status is unknown!");
        }

        return breastFeedingStatus;
    }
}
