package fhirparser;

import help.Help;
import org.w3c.dom.NodeList;

import java.util.ArrayList;
import java.util.logging.Level;

import static fhirparser.FHIRParser.*;
import static logging.Logging.logger;

/**
 * Get the information of the observation.
 * It contains body weight, body height and creatinine value
 *
 * @author Sebastian Buechler <orderwb6@gmail.com>
 * @author Stefan Kuppelwieser <edelblistar@online.de>
 */
public class FHIRParserObservation {

    /**
     * It gets the information about the observation of patient.
     * You have to check the loinc code in the object for the kind of observation.
     * For every Loinc code is the ArrayList built up differently.
     * How the entries are defined can be seen below..
     *
     * <li>[0] => object 1</li>
     * <li>[1] => object 2</li>
     * <li>[2] => object 3</li>
     * <li>[3] => object 4</li>
     *
     * The objects contains a ArrayList and is structured as follows:
     * <li>[0] => referenceID</li>
     * <li>[1] => loincCode</li>
     * <li>[2] => observationName</li>
     * <li>[3] => unit</li>
     * <li>[4] => stringValue</li>
     *
     * Her is a overview about the relevant loinc codes
     * <li>loinc code   definition</li>
     * <li>3142-7       body weight</li>
     * <li>8302-2       height of patient</li>
     * <li>2160-0       creatinine</li>
     *
     * @return observationInformation with observation information
     */
    public ArrayList<ArrayList<String>> getObservationInformationen() {
        // If there is no composite observation information
        if (indiceCompositeObservation == -1){
            return null;
        }

        // Declare variables
        ArrayList<String> observationReferences;
        ArrayList<ArrayList<String>> observationInformation = new ArrayList<>();

        // Get Reference and save it in ArrayList
        observationReferences = getCompositionObservation();

        // Iterate Observation, add references, add observation
        int counterReference = 0;
        for(int i = indiceCompositeObservation + 1; i < getMaxCompositeObservationLength(); i++){
            ArrayList<String> observation = new ArrayList<>();
            observation.add(0, observationReferences.get(counterReference));
            observationInformation.add(counterReference, getObservation(observation, i));
            counterReference++;
        }

        return observationInformation;
    }

    /**
     * Return the length of the indice of last observation resource
     *
     * @return the length of the indice of last observation resource
     */
    private int getMaxCompositeObservationLength() {
        if(indiceCompositeAllergie != -1){
            return indiceCompositeAllergie;
        } else if (indiceCompositeHealthConcerns != -1) {
            return indiceCompositeHealthConcerns;
        } else {
            return indiceCompositeMedication;
        }
    }

    /**
     * Essentially contain only the corresponding LOINC code and references to the actual content
     * Source: http://wiki.hl7.de/index.php?title=IG:Ultrakurzformat_Patientenbezogener_Medikationsplan#Abschnitt_.E2.80.9EKlinische_Parameter.E2.80.9C
     *
     * @return a filled array with the references to the resource observation
     */
    private ArrayList<String> getCompositionObservation(){
        // Declare ArrayList
        ArrayList<String> autorInformation = new ArrayList<>();

        // Get information from HL7 FHIR Dokument
        NodeList nList_references = documentsEntrys.get(indiceCompositeObservation).getElementsByTagName("entry");

        // Get references to Observation
        autorInformation = searchInterleavedHierarchy(nList_references,REFERENCE);

        return autorInformation;
    }

    /**
     * The method decides what kind of observation it is and dissects the properties of the observation in another method
     *
     * @param observation get an empty ArrayList<String> observation
     * @param indiceObservation specifies the length of the observations element
     * @return a filled ArrayList with the properties of the Observation
     */
    private ArrayList<String> getObservation(ArrayList<String> observation, final int indiceObservation){
        // Get information from HL7 FHIR Dokument
        NodeList nList_coding = documentsEntrys.get(indiceObservation).getElementsByTagName(CODING);
        NodeList nList_valueQuantity = documentsEntrys.get(indiceObservation).getElementsByTagName("valueQuantity");

        // Get system value for Observation
        String systemValue = util.XML.searchHierarchyByAttribute(nList_coding, SYSTEM);

        // Case body weight / height / creatinine / pregnancy
        if(systemValue.equals(LOINCADRESS)) {
            // Get LOINC code for type of Observation
            String loincCode = util.XML.searchHierarchyByAttribute(nList_coding, CODE);

            /**
             * loinc code   definition
             * 3142-7       body weight
             * 8302-2       height of patient
             * 2160-0       creatinine
             */
            if(!util.String.isEmpty(loincCode) && (loincCode.equals("3142-7") || loincCode.equals("8302-2") || loincCode.equals("2160-0"))){
                observation = getClinicalParameter(observation, nList_coding, nList_valueQuantity, loincCode);
            } else {
                logger.log(Level.INFO, "The loinc code does not match with body weigt, height of patient or cratinine! Observation can not associated!");
            }

        } else {
            logger.log(Level.INFO, "There is no loinc code! Observation can not associated!");
        }

        return observation;
    }

    /**
     * This method analyzes the observation and stores the information in an array list.
     * It can be worked on body weight, body size and creatinine unit
     *
     * @param clinicalParameter an empty Arraylist<String> for storeing the information
     * @param nList_coding the part of the HL7 FHIR document which should be edited by tagging element coding
     * @param nList_valueQuantity NodeList nList_valueQuantity the part of the HL7 FHIR document which should be edited by tagging element valueQuantity
     * @param loincCode which is being tested
     * @return a filled arraylist of loin code 'loinCode'
     */
    private ArrayList<String> getClinicalParameter(ArrayList<String> clinicalParameter, final NodeList nList_coding, final NodeList nList_valueQuantity, final String loincCode) {
        // Set loinc code
        clinicalParameter.add(1, loincCode);

        // Get clinicalParameter name
        String observationName = util.XML.searchHierarchyByAttribute(nList_coding, DISPLAY);
        if (!util.String.isEmpty(observationName)) {
            clinicalParameter.add(2, observationName);
        } else {
            clinicalParameter.add(2, EMPTYSTRING);
            logger.log(Level.INFO, "The name of the clinicalParameter is unknown!");
        }

        // Get unit
        String unit = null;
        if (!util.String.isEmpty(loincCode)) {
            unit = getUCUM(loincCode);
            clinicalParameter.add(3, unit);
        } else {
            String tmpUnit = util.XML.searchHierarchyByAttribute(nList_valueQuantity, UNIT);
            if (!util.String.isEmpty(tmpUnit)) {
                clinicalParameter.add(3, unit);
            } else {
                clinicalParameter.add(3, EMPTYSTRING);
                logger.log(Level.INFO, "The unit of the clinicalParameter is unknown!");
            }
        }

        // get value for unit
        String value = util.XML.searchHierarchyByAttribute(nList_valueQuantity, VALUE);
        if (!util.String.isEmpty(value)) {
            clinicalParameter.add(4, value);
        } else {
            clinicalParameter.add(4, EMPTYSTRING);
            logger.log(Level.INFO, "The unit of the clinicalParameter is unknown!");
        }

        return clinicalParameter;
    }

    /**
     * Expects a loinc code and return the unit of the loinc code defintion
     *
     * @param loincCode expects the loinc code
     * @return the unit of the loinc code defintion
     */
    private String getUCUM(final String loincCode){
        // Initliaze variables
        String unit;

        /**
         * Loinc code   definition      unit
         * 3142-7       body weight     kg
         * 8302-2       height          cm
         * 2160-0       creatinine      mg/dl
         */
        if (loincCode.equals("3142-7")){
            unit = "kg";
        } else if(loincCode.equals("8302-2")){
            unit = "cm";
        } else if(loincCode.equals("2160-0")){
            unit = "mg/dl";
        } else {
            unit = null;
        }
        return unit;
    }
}
