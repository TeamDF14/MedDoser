package fhirparser;

import help.Help;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.util.ArrayList;
import java.util.logging.Level;

import static fhirparser.FHIRParser.*;
import static logging.Logging.logger;

/**
 * Collect the information about all allergies
 *
 * @author Sebastian Buechler <orderwb6@gmail.com>
 * @author Stefan Kuppelwieser <edelblistar@online.de>
 */
public class FHIRParserAllergy {

    /**
     * Gets the information about the allergies of patient.
     * It belongs to observation of patient.
     * It exist an ArrayList, that contains n ArrayList with AllergyIntolerance.
     * How the entries are defined can be seen below..
     * <ul>
     * <li>[0] => reference of allergie</li>
     * <li>[1] => name of allergy</li>
     * <li>[2] => type of allergy</li>
     * </ul>
     * @return an ArrayList<ArrayList<String>> with allergies
     */
    public ArrayList<ArrayList<String>> getAllergyInformation() {
        // If there is no composite observation information
        if (indiceCompositeAllergie == -1){
            return null;
        }

        // Declare ArrayList
        ArrayList<ArrayList<String>> allergiesInformation = new ArrayList<>();

        // determinate lenght of allergies resources
        int allergiesLength = 0;

        if (indiceCompositeHealthConcerns != -1){
            // 'indiceCompositeAllergie + 1' => Remove composite part of allergies
            allergiesLength = indiceCompositeHealthConcerns - (indiceCompositeAllergie + 1);
        } else {
            // 'indiceCompositeAllergie + 1' => Remove composite part of allergies
            allergiesLength = indiceCompositeMedication - (indiceCompositeAllergie + 1);
        }

        // Iterate through allergies entries
        for(int i = 0; i < allergiesLength; i++){
            // Declare ArrayList
            ArrayList<String> allergie = new ArrayList<>();

            // Get information from HL7 FHIR document
            NodeList nList_reference = documentsEntrys.get((indiceCompositeAllergie + 1) + i).getElementsByTagName("patient");
            NodeList nList_substance = documentsEntrys.get((indiceCompositeAllergie + 1) + i).getElementsByTagName("substance");
            NodeList nList_type = documentsEntrys.get((indiceCompositeAllergie + 1) + i).getElementsByTagName("type");

            // get allergy reference
            String reference = util.XML.searchHierarchyByAttribute(nList_reference, REFERENCE);
            if(!util.String.isEmpty(reference)){
                allergie.add(0, reference);
            } else {
                allergie.add(0, EMPTYSTRING);
                logger.log(Level.FINEST, "The reference of the allergie is unknown!");
            }

            // get allergy name
            String allergieSubstance = util.XML.searchHierarchyByAttribute(nList_substance, TEXT);
            if(!util.String.isEmpty(reference)){
                allergie.add(1, allergieSubstance);
            } else {
                allergie.add(1, EMPTYSTRING);
                logger.log(Level.FINEST, "The substance/name of the allergy is unknown!");
            }

            // get kind of allergy
            String type = ((Element) nList_type.item(0)).getAttribute(VALUE);
            if(!util.String.isEmpty(type)){
                allergie.add(2, enums.eAllergyIntolerance.translateAllergyIntolerance(type));
            } else {
                allergie.add(2, EMPTYSTRING);
                logger.log(Level.FINEST, "The type of the allergy is unknown!");
            }

            allergiesInformation.add(i, allergie);
        }

        return allergiesInformation;
    }
}
