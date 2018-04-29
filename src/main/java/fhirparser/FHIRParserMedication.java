package fhirparser;

import help.Help;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.util.ArrayList;

import static fhirparser.FHIRParser.*;

/**
 * Collect all informations to ...
 * ... sections -> medication; notice to medication -> ingestion time; active substances; properties of medication
 */
public class FHIRParserMedication {

    /**
     * <p>Gets the information about the section titles.</p>
     * <p></p>It is always the third 'entry' child -> documentsEntrys.get(3)</p>
     * <ul>
     * <li>[0] => section title</li>
     * <li>[1] => reference to medicationStatement</li>
     * </ul>
     * @return sections with section informations and references to medication
     */
    public ArrayList<ArrayList<String>> getSectionsInformation() {
        // If there is no composite medication information
        if (indiceCompositeMedication == -1){
            return null;
        }

        // Declare and Initialize variables
        ArrayList<ArrayList<String>> sections = new ArrayList<>();

        // Get information from third Child
        NodeList nList_sectionsFirstLevel = documentsEntrys.get(indiceCompositeMedication).getElementsByTagName(SECTION);

        // First level <section>
        for (int i = 0; i < nList_sectionsFirstLevel.getLength(); i++) {
            Element element_sectionFirstLevel = (Element) nList_sectionsFirstLevel.item(i);

            // Generate NodeList with second level <section><section>
            NodeList nodeList = element_sectionFirstLevel.getElementsByTagName(SECTION);


            // determinate if additional information exist. If it exist the last section will be hilarious
            int additionalInformation = 0;
            //if (indiceAdditionalNotes != -1) {
            //    additionalInformation = 1;
            //}

            // Go through all sections
            for(int j = 0; j < (nodeList.getLength() - additionalInformation); j++) {
                // Initialize variables
                ArrayList<String> reference = new ArrayList<>();

                // Get Tags with 'title'
                Element elementTitle = (Element) nodeList.item(j);
                NodeList nList_title = elementTitle.getElementsByTagName(TITLE);
                if (nList_title.getLength() != 0){
                    Element elementTitleTag = (Element) nList_title.item(0);
                    reference.add(0, elementTitleTag.getAttribute(VALUE));
                } else {
                    reference.add(0, EMPTYSTRING);
                }

                // Get Tags with 'reference'
                Element elementReference = (Element) nodeList.item(j);
                NodeList nList_reference = elementReference.getElementsByTagName(REFERENCE);
                // Go through all references
                for(int k = 0; k < nList_reference.getLength(); k++){
                    Element elementReferenceTag = (Element) nList_reference.item(k);
                    reference.add(k + 1, elementReferenceTag.getAttribute(VALUE));
                }

                // Get free text with tag 'display'
                Element elementText = (Element) nodeList.item(j);
                NodeList nList_text = elementText.getElementsByTagName("div");

                if(nList_text.getLength() != 0) {
                    String freeText = nList_text.item(0).getTextContent();
                    reference.add(nList_reference.getLength() + 1, freeText);
                }

                sections.add(reference);
            }
        }

        return sections;
    }

    /**
     * <p>Gets the information about the medications.</p>
     * <p>It start after the sections in two steps.</p>
     * <p>Two entrys are one mediation.</p>
     * <p>How the entries are defined can be seen below..</p>
     * <ul>
     * <li>[0] => section/medicationStatement reference</li>
     * <li>[1] => medication note</li>
     * <li>[2] => reason of usage</li>
     * <li>[3] => asserted date</li>
     * <li>[4] => status of medication</li>
     * <li>[5] => was not taken</li>
     * <li>[6] => medicationStatement/Medication reference</li>
     * <li>[7] => full name of medication</li>
     * <li>[8] => pzn number of medication</li>
     * <li>[9] => form name of the medication / product</li>
     * <li>[10] => form code of the medication / product</li>
     * </ul>
     * @return An array list that contains for each position another array list with the medication
     */
    public ArrayList<ArrayList<String>> getMedicationsInformation() {
        // If there is no composite medication information
        if (indiceCompositeMedication == -1) {
            return null;
        }

        // Declare and Initialize variables
        ArrayList<ArrayList<String>> medications = new ArrayList<>();
        int lengthMedications = 0;

        // determinate length of medication
        if(indiceAdditionalNotes != -1){
            lengthMedications = indiceAdditionalNotes - (indiceCompositeMedication + 1); // (indiceCompositeMedication + 1) => exclude the composite part
        } else {
            lengthMedications = documentsEntrys.size() - (indiceCompositeMedication + 1); // (indiceCompositeMedication + 1) => exclude the composite part
        }

        // iterate through medications
        for (int i = 0; i < lengthMedications; i += 2) {
            // Declare and Initialize variables
            ArrayList<String> medication = new ArrayList<>();

            // Get information from HL7 FHIR document
            NodeList nList_medicationStatementNote = documentsEntrys.get((indiceCompositeMedication + 1) + i).getElementsByTagName(NOTE);
            NodeList nList_medicationStatementReasonForUse = documentsEntrys.get((indiceCompositeMedication + 1) + i).getElementsByTagName(REASONFORUSECODEABlECONCEPT);
            NodeList nList_medicationStatementDateAsserted = documentsEntrys.get((indiceCompositeMedication + 1) + i).getElementsByTagName(DATEASSERTDD); // dateAsserted in german => "Dokumentationsdatum"
            NodeList nList_medicationStatementStatus = documentsEntrys.get((indiceCompositeMedication + 1) + i).getElementsByTagName(STATUS);
            NodeList nList_medicationStatementReference = documentsEntrys.get((indiceCompositeMedication + 1) + i).getElementsByTagName(IDENTIFIER);
            NodeList nList_medicationStatementWasNotTaken = documentsEntrys.get((indiceCompositeMedication + 1) + i).getElementsByTagName(WASNOTTAKEN);
            NodeList nList_medicationStatementMedicationReference = documentsEntrys.get((indiceCompositeMedication + 1) + i).getElementsByTagName(MEDICATIONREFERENCE);
            NodeList nList_medicationCoding = documentsEntrys.get((indiceCompositeMedication + 1) + (i + 1)).getElementsByTagName(CODING); // (i + 1) => To go one entry further. So you are not in MedicationStatement rather Medication


            // Add section to medicationStatement reference
            String medicationStatementReference = util.XML.searchHierarchyByAttribute(nList_medicationStatementReference, VALUE);
            if (!util.String.isEmpty(medicationStatementReference)){
                medication.add(0, medicationStatementReference);
            } else {
                medication.add(0, EMPTYSTRING);
                //logger.log(Level.FINEST, "The section to medicationStatement reference of medication is unknown!");
            }

            // Add medication note
            String medicationStatementNote = null;
            if(nList_medicationStatementNote.getLength() != 0){  // check length not null
                medicationStatementNote = ((Element) nList_medicationStatementNote.item(0)).getAttribute(VALUE);
            }

            if (!util.String.isEmpty(medicationStatementNote)){
                medication.add(1, medicationStatementNote);
            } else {
                medication.add(1, EMPTYSTRING);
                //logger.log(Level.FINEST, "The note of medication is unknown!");
            }

            // Add reason for usage
            String medicationStatementReasonForUse = util.XML.searchHierarchyByAttribute(nList_medicationStatementReasonForUse, TEXT);
            if (!util.String.isEmpty(medicationStatementReasonForUse)){
                medication.add(2, medicationStatementReasonForUse);
            } else {
                medication.add(2, EMPTYSTRING);
                //logger.log(Level.FINEST, "The reason of use of medication is unknown!");
            }

            // Add asserted date (into german: "Dokumentationsdatum")
            String medicationStatementDateAsserted = null;
            if(nList_medicationStatementDateAsserted.getLength() != 0){  // check length not null
                medicationStatementDateAsserted = ((Element) nList_medicationStatementDateAsserted.item(0)).getAttribute(VALUE);
            }

            if (!util.String.isEmpty(medicationStatementDateAsserted)){
                medication.add(3, medicationStatementDateAsserted);
            } else {
                medication.add(3, EMPTYSTRING);
                //logger.log(Level.FINEST, "The date of asserted of medication is unknown!");
            }

            // add status
            // The status can be used to indicate whether the drug is taken ("active") or not ("completed").
            String medicationStatementStatus = null;
            if(nList_medicationStatementStatus.getLength() != 0){ // check length not null
                medicationStatementStatus = ((Element) nList_medicationStatementStatus.item(0)).getAttribute(VALUE);
            }

            if (!util.String.isEmpty(medicationStatementStatus)){
                medication.add(4, medicationStatementStatus);
            } else {
                medication.add(4, EMPTYSTRING);
                //logger.log(Level.FINEST, "The status of medication is unknown!");
            }

            // Add was not taken
            /**
             * Also in stages of development an indicator (wasNotTaken) can be given that the drug
             * was not taken (for example because of incompatibilities).
             * Reasons can also be recorded here (reasonNotTaken).
             * However, the current medication plan does not yet provide this,
             * but it is already mentioned here in connection with the AMTS use case.
             *
             * It can be true or false!
             */
            String medicationStatementWasNotTaken = null;
            if (nList_medicationStatementWasNotTaken.getLength() != 0){ // check length not null
                medicationStatementWasNotTaken = ((Element) nList_medicationStatementWasNotTaken.item(0)).getAttribute(VALUE);
            }

            if (!util.String.isEmpty(medicationStatementWasNotTaken)){
                medication.add(5, medicationStatementWasNotTaken);
            } else {
                medication.add(5, EMPTYSTRING);
                //logger.log(Level.FINEST, "The was not taken of medication is unknown!");
            }

            // Add medicationStatement to medication reference
            String medicationStatementMedicationReference = util.XML.searchHierarchyByAttribute(nList_medicationStatementMedicationReference, REFERENCE);
            if (!util.String.isEmpty(medicationStatementMedicationReference)){
                medication.add(6, medicationStatementMedicationReference);
            } else {
                medication.add(6, EMPTYSTRING);
                //logger.log(Level.FINEST, "The medicationStatement to medication reference of medication is unknown!");
            }

            // Add medication name
            String medicationFullName = util.XML.searchHierarchyByTagAndAttribute(nList_medicationCoding, SYSTEM, VALUE, "http://www.ifaffm.de/pzn", DISPLAY, VALUE);
            String medicationCode = util.XML.searchHierarchyByTagAndAttribute(nList_medicationCoding, SYSTEM, VALUE, "http://www.ifaffm.de/pzn", CODE, VALUE);
            if( (!util.String.isEmpty(medicationFullName) && !util.String.isEmpty(medicationCode)) || (util.String.isEmpty(medicationFullName) && !util.String.isEmpty(medicationCode))){
                if(mySQL.getMedication(medicationCode).getTradeName() != null) {
                    medication.add(7, mySQL.getMedication(medicationCode).getTradeName());
                } else {
                    medication.add(7, medicationFullName);
                }
                medication.add(8, medicationCode);
            } else if ((!util.String.isEmpty(medicationFullName) && util.String.isEmpty(medicationCode))){
                medication.add(7, medicationFullName);
                medication.add(8, EMPTYSTRING);
            } else {
                medication.add(7, EMPTYSTRING);
                //logger.log(Level.FINEST, "The name of medication is unknown!");
                medication.add(8, EMPTYSTRING);
                //logger.log(Level.FINEST, "The pzn code of medication is unknown!");
            }

            // Add pzn code
            if (!util.String.isEmpty(medicationCode)){
                medication.add(8, medicationCode);
            } else {
                medication.add(8, EMPTYSTRING);
                //logger.log(Level.FINEST, "The pzn code of medication is unknown!");
            }

            // Add form name of the medication
            String medicationFormName = util.XML.searchHierarchyByTagAndAttribute(nList_medicationCoding, SYSTEM, VALUE, "http://hl7.org/fhir/v3/orderableDrugForm", DISPLAY, VALUE);
            if (!util.String.isEmpty(medicationFormName)){
                medication.add(9, medicationFormName);
            } else {
                medication.add(9, EMPTYSTRING);
                //logger.log(Level.FINEST, "The drug name form of the medication is unknown!");
            }

            // Add form code of the medication
            String medicationFormCode = util.XML.searchHierarchyByTagAndAttribute(nList_medicationCoding, SYSTEM, VALUE, "http://hl7.org/fhir/v3/orderableDrugForm", CODE, VALUE);
            if (!util.String.isEmpty(medicationFormCode)){
                medication.add(10, medicationFormCode);
            } else {
                medication.add(10, EMPTYSTRING);
                //logger.log(Level.FINEST, "The drug name form of the medication is unknown!");
            }

            // add medication to medications and increase medicationsPosition
            if (i == 0){
                medications.add(0, medication);
            } else {
                medications.add(i / 2, medication);
            }
        }

        return medications;
    }

    /**
     * <p>Collects information about dosages.</p>
     * <p>It includes the cases quantityQuantity, quantityRange and text.</p>
     * <p>It is assumed that a drug can only accept quantityQuantity, quantityRange and text on one day.</p>
     * <p>How the entries are defined can be seen below..</p>
     *
     * <ul>
     * <li>[0] => section to medicationStatement reference</li>
     * <li>[1] => medicationStatement to medicaiton reference</li>
     * <li>[2] => time of dosage</li>
     * <li>[3] => information of needed</li>
     * <li>[4] => quantityQuantity: value</li>
     * <li>[5] => quantityQuantity: unit</li>
     * <li>[6] => quantityQuantity: unit code</li>
     * <li>[7] => quantityRange: start value</li>
     * <li>[8] => quantityRange: unit</li>
     * <li>[9] => quantityRange: unit code</li>
     * <li>[10] => quantityRange: end value</li>
     * <li>[11] => quantityRange: unit </li>
     * <li>[12] => quantityRange: unit code</li>
     * <li>[13] => text</li>
     * <li>[2] - [13] repeat</li>
     * </ul>
     * @return a filled array of dosages
     */
    public ArrayList<ArrayList<String>> getDosageInformation(){
        // Declare and Initialize variables
        ArrayList<ArrayList<String>> dosages = new ArrayList<>();
        // If there is no composite medication information
        if (indiceCompositeMedication == -1){
            return null;
        }

        int lengthMedications = 0;

        // determinate length of medications
        if(indiceAdditionalNotes != -1){
            lengthMedications = indiceAdditionalNotes - (indiceCompositeMedication + 1); // (indiceCompositeMedication + 1) => exclude the composite part
        } else {
            lengthMedications = documentsEntrys.size() - (indiceCompositeMedication + 1); // (indiceCompositeMedication + 1) => exclude the composite part
        }

        // iterate through all medications
        for (int i = 0; i < lengthMedications; i += 2) {
            // Declare and Initialize variables
            ArrayList<String> dosage = new ArrayList<>();

            // Get information from HL7 FHIR document
            NodeList nList_medicationStatementReference = documentsEntrys.get((indiceCompositeMedication + 1) + i).getElementsByTagName(IDENTIFIER);
            NodeList nList_medicationStatementMedicationReference = documentsEntrys.get((indiceCompositeMedication + 1) + i).getElementsByTagName(MEDICATIONREFERENCE);
            NodeList nList_medicationStatementDosage = documentsEntrys.get((indiceCompositeMedication + 1) + i).getElementsByTagName(DOSAGE);

            // Add section to medicationStatement reference
            String medicationStatementReference = util.XML.searchHierarchyByAttribute(nList_medicationStatementReference, VALUE);
            if (!util.String.isEmpty(medicationStatementReference)){
                dosage.add(0, medicationStatementReference);
            } else {
                dosage.add(0, EMPTYSTRING);
                //logger.log(Level.FINEST, "The section to medicatinStatement reference of medication is unknown!");
            }

            // Add medicationStatement to medicaiton reference
            String medicationStatementMedicationReference = util.XML.searchHierarchyByAttribute(nList_medicationStatementMedicationReference, REFERENCE);
            if (!util.String.isEmpty(medicationStatementMedicationReference)){
                dosage.add(1, medicationStatementMedicationReference);
            } else {
                dosage.add(1, EMPTYSTRING);
                //logger.log(Level.FINEST, "The medicationStatement to medication reference of medication is unknown!");
            }

            String freeText = util.XML.searchHierarchyByAttribute(nList_medicationStatementDosage, TEXT);
            if(!util.String.isEmpty(freeText)){
                dosage.add(2, freeText);
                //logger.log(Level.FINEST, "The dosages have no time, but also a free text to the medication");
            } else {
                // Saves the count of passage. It is needed to get the right position from the value. This is because the information is stored in 12 steps at each time you take it
                int passage = 0;

                // Iterate through each doses
                for (int j = 0; j < nList_medicationStatementDosage.getLength(); j++) {

                    // set position of move position
                    int movePosition;

                    // Get the next position of the variable. Because it is stored in 12 steps
                    movePosition = 12 * passage;

                    // Get information from HL7 FHIR document
                    NodeList nList_dosageWhen = ((Element) nList_medicationStatementDosage.item(j)).getElementsByTagName("when");
                    NodeList nList_asNeeded = ((Element) nList_medicationStatementDosage.item(j)).getElementsByTagName("asNeededBoolean");
                    NodeList nList_quantityQuantity = ((Element) nList_medicationStatementDosage.item(j)).getElementsByTagName("quantityQuantity");
                    NodeList nList_quantityRange = ((Element) nList_medicationStatementDosage.item(j)).getElementsByTagName("quantityRange");
                    NodeList nList_text = ((Element) nList_medicationStatementDosage.item(j)).getElementsByTagName(TEXT);
                    NodeList nList_low = ((Element) nList_medicationStatementDosage.item(j)).getElementsByTagName("low");
                    NodeList nList_high = ((Element) nList_medicationStatementDosage.item(j)).getElementsByTagName("high");

                    // Add time of dosage
                    String dosageWhen;
                    if (nList_dosageWhen.getLength() != 0) {
                        dosageWhen = ((Element) nList_dosageWhen.item(0)).getAttribute(VALUE);
                    } else {
                        dosageWhen = null;
                    }

                    // Add the information when the dosage should take
                    if (!util.String.isEmpty(dosageWhen)) {
                        dosage.add(2 + movePosition, dosageWhen);
                    } else {
                        dosage.add(2 + movePosition, EMPTYSTRING);
                        //logger.log(Level.FINEST, "The time of dosage is unknown!");
                    }

                    // Add information of needed
                    String asNeeded;
                    if (nList_asNeeded.getLength() != 0) {
                        asNeeded = ((Element) nList_asNeeded.item(0)).getAttribute(VALUE);
                    } else {
                        asNeeded = null;
                    }

                    // Add the information about if the dose should be taken as needed or not
                    if (!util.String.isEmpty(asNeeded)) {
                        dosage.add(3 + movePosition, asNeeded);
                    } else {
                        dosage.add(3 + movePosition, EMPTYSTRING);
                        //logger.log(Level.FINEST, "It is unknown if the dose should be taken as needed or not!");
                    }

                    if (nList_quantityQuantity.getLength() != 0) { //case "quantityQuantity"
                        // Set the dosage of type quantityQuantity
                        dosage = setDosageQuantityQuantity(dosage, movePosition, nList_quantityQuantity);
                    } else if (nList_quantityRange.getLength() != 0) { // case "quantityRange"
                        // Set the dosage of type quantityRange
                        dosage = setDosageQuantityRange(dosage, movePosition, nList_low, nList_high);
                    } else if (nList_text.getLength() != 0) { // case TEXT
                        // Set the dosage of type free text
                        dosage = setDosageFreeText(dosage, movePosition, nList_text);
                    } else {
                        // Set no dosage. This case should not be visited normally
                        dosage = setNoDosage(dosage, movePosition);
                    }

                    // increase passage
                    passage += 1;
                }
            }

            // add medication to medications and increase medicationsPosition
            if(i == 0) {
                dosages.add(0, dosage);
            } else {
                dosages.add(i / 2, dosage);
            }
        }

        return dosages;
    }

    /**
     * Store the corresponding values from the quantityRange in the ArrayList of the dosage.
     * How the entries are defined can be seen below..
     *
     * <li>[4] => quantityQuantity: value</li>
     * <li>[5] => quantityQuantity: unit</li>
     * <li>[6] => quantityQuantity: unit code</li>
     *
     * @param dosage is the already started arraylist of dosage
     * @param movePosition set position of move position. It ensures that the correct VALUE is always added up in order to save the entries in the ArrayList
     * @param nList_quantityQuantity contains the information about the dosage
     * @return a filled array of quantityQuantity dosage
     */
    private ArrayList<String> setDosageQuantityQuantity(ArrayList<String> dosage, int movePosition, NodeList nList_quantityQuantity) {
        // Add Value
        String value = util.XML.searchHierarchyByAttribute(nList_quantityQuantity, VALUE);
        if (!util.String.isEmpty(value)){
            dosage.add(4 + movePosition, value);
        } else {
            dosage.add(4 + movePosition, EMPTYSTRING);
            //logger.log(Level.FINEST, "The quantityQuantity VALUE of dosage is unknown!");
        }

        // Add unit as String
        String unit  = util.XML.searchHierarchyByAttribute(nList_quantityQuantity, UNIT);
        if (!util.String.isEmpty(unit )){
            dosage.add(5 + movePosition, unit );
        } else {
            dosage.add(5 + movePosition, EINHEIT);
            //logger.log(Level.INFO, "The quantityQuantity unit as string of dosage is unknown!");
        }

        // Add unit code
        String unitCode  = util.XML.searchHierarchyByAttribute(nList_quantityQuantity, CODE);
        if (!util.String.isEmpty(unitCode)){
            dosage.add(6 + movePosition, unitCode );
        } else {
            dosage.add(6 + movePosition, EINHEIT);
            //logger.log(Level.INFO, "The quantityQuantity unit code of dosage is unknown!");
        }

        // the subsequent entries to zero
        dosage.add(7 + movePosition, EMPTYSTRING);
        dosage.add(8 + movePosition, EMPTYSTRING);
        dosage.add(9 + movePosition, EMPTYSTRING);
        dosage.add(10 + movePosition, EMPTYSTRING);
        dosage.add(11 + movePosition, EMPTYSTRING);
        dosage.add(12 + movePosition, EMPTYSTRING);
        dosage.add(13 + movePosition, EMPTYSTRING);

        return dosage;
    }

    /**
     * Store the corresponding values from the quantityRange in the ArrayList of the dosage
     * How the entries are defined can be seen below..
     *
     * <li>[7] => quantityRange: start VALUE</li>
     * <li>[8] => quantityRange: unit</li>
     * <li>[9] => quantityRange: unit code</li>
     * <li>[10] => quantityRange: end VALUE</li>
     * <li>[11] => quantityRange: unit </li>
     * <li>[12] => quantityRange: unit code</li>
     *
     * @param dosage is the already started arraylist of dosage
     * @param movePosition set position of move position. It ensures that the correct VALUE is always added up in order to save the entries in the ArrayList
     * @param nList_low contains the start VALUE of dosage
     * @param nList_high contains the end VALUE of dosage
     * @return a filled arraylist with the dosage of on medication
     */
    private ArrayList<String> setDosageQuantityRange(ArrayList<String> dosage, int movePosition, NodeList nList_low, NodeList nList_high) {
        // the previous entries to zero
        dosage.add(2 + movePosition, EMPTYSTRING);
        dosage.add(3 + movePosition, EMPTYSTRING);
        dosage.add(4 + movePosition, EMPTYSTRING);
        dosage.add(5 + movePosition, EMPTYSTRING);
        dosage.add(6 + movePosition, EMPTYSTRING);

        // Add start value
        String startValue = util.XML.searchHierarchyByAttribute(nList_low, VALUE);
        if (!util.String.isEmpty(startValue)){
            dosage.add(7 + movePosition, startValue);
        } else {
            dosage.add(7 + movePosition, EMPTYSTRING);
            //logger.log(Level.FINEST, "The quantityRange VALUE of dosage is unknown!");
        }

        // Add start unit as String
        String startUnit  = util.XML.searchHierarchyByAttribute(nList_low, UNIT);
        if (!util.String.isEmpty(startUnit )){
            dosage.add(8 + movePosition, startUnit );
        } else {
            dosage.add(8 + movePosition, EINHEIT);
            //logger.log(Level.INFO, "The quantityRange unit as string of dosage is unknown!");
        }

        // Add start unit code
        String startUnitCode  = util.XML.searchHierarchyByTagAndAttribute(nList_low, SYSTEM, VALUE,  "http://unitsofmeasure.org", CODE, VALUE);
        if (!util.String.isEmpty(startUnitCode )){
            dosage.add(9 + movePosition, startUnitCode );
        } else {
            dosage.add(9 + movePosition, EINHEIT);
            //logger.log(Level.INFO, "The quantityRange unit code of dosage is unknown!");
        }

        // Add end value
        String endValue = util.XML.searchHierarchyByAttribute(nList_high, VALUE);
        if (!util.String.isEmpty(endValue)){
            dosage.add(10 + movePosition, endValue);
        } else {
            dosage.add(10 + movePosition, EMPTYSTRING);
            //logger.log(Level.FINEST, "The quantityRange VALUE of dosage is unknown!");
        }

        // Add end unit as String
        String endUnit  = util.XML.searchHierarchyByAttribute(nList_high, UNIT);
        if (!util.String.isEmpty(endUnit )){
            dosage.add(11 + movePosition, endUnit );
        } else {
            dosage.add(11 + movePosition, EINHEIT);
            //logger.log(Level.INFO, "The quantityRange unit as string of dosage is unknown!");
        }

        // Add end unit code
        String endUnitCode  = util.XML.searchHierarchyByTagAndAttribute(nList_high, SYSTEM, VALUE,  "http://unitsofmeasure.org", CODE, VALUE);
        if (!util.String.isEmpty(endUnitCode )){
            dosage.add(12 + movePosition, endUnitCode );
        } else {
            dosage.add(12 + movePosition, EINHEIT);
            //logger.log(Level.INFO, "The quantityRange unit code of dosage is unknown!");
        }

        // the subsequent entries to zero
        dosage.add(13 + movePosition, EMPTYSTRING);

        return dosage;
    }

    /**
     * Store the corresponding values from the free text in the ArrayList of the dosage
     *
     * @param dosage is the already started arraylist of dosage
     * @param movePosition set position of move position. It ensures that the correct value is always added up in order to save the entries in the ArrayList
     * @param nList_text contains the free text of dosage
     * @return a filled arraylist with the dosage of on medication
     */
    private ArrayList<String> setDosageFreeText(ArrayList<String> dosage, int movePosition, NodeList nList_text) {
        // the previous entries to zero
        dosage.add(2 + movePosition, EMPTYSTRING);
        dosage.add(3 + movePosition, EMPTYSTRING);
        dosage.add(4 + movePosition, EMPTYSTRING);
        dosage.add(5 + movePosition, EMPTYSTRING);
        dosage.add(6 + movePosition, EMPTYSTRING);
        dosage.add(7 + movePosition, EMPTYSTRING);
        dosage.add(8 + movePosition, EMPTYSTRING);
        dosage.add(9 + movePosition, EMPTYSTRING);
        dosage.add(10 + movePosition, EMPTYSTRING);
        dosage.add(11 + movePosition, EMPTYSTRING);
        dosage.add(12 + movePosition, EMPTYSTRING);

        // Get information from HL7 FHIR document
        String text = ((Element) nList_text.item(0)).getAttribute(VALUE);
        if(!util.String.isEmpty(text)){
            dosage.add(13, text);
        } else {
            dosage.add(13 + movePosition, EMPTYSTRING);
            //logger.log(Level.FINEST, "There is no free text about dosage!");
        }

        return dosage;
    }

    /**
     * Write to the indices put an empty string, because the dosage is not defined
     *
     * @param dosage is the already started arraylist of dosage
     * @param movePosition set position of move position. It ensures that the correct value is always added up in order to save the entries in the ArrayList
     * @return a filled arraylist with the dosage of on medication
     */
    private ArrayList<String> setNoDosage(ArrayList<String> dosage, int movePosition) {
        // Set all to null
        dosage.add(2 + movePosition, EMPTYSTRING);
        dosage.add(3 + movePosition, EMPTYSTRING);
        dosage.add(4 + movePosition, EMPTYSTRING);
        dosage.add(5 + movePosition, EMPTYSTRING);
        dosage.add(6 + movePosition, EMPTYSTRING);
        dosage.add(7 + movePosition, EMPTYSTRING);
        dosage.add(8 + movePosition, EMPTYSTRING);
        dosage.add(9 + movePosition, EMPTYSTRING);
        dosage.add(10 + movePosition, EMPTYSTRING);
        dosage.add(11 + movePosition, EMPTYSTRING);
        dosage.add(12 + movePosition, EMPTYSTRING);
        dosage.add(13 + movePosition, EMPTYSTRING);
        //logger.log(Level.FINEST, "There is no information about dosage!");

        return dosage;
    }

    /**
     * Stores the active substance(s) from a drug into an ArrayList
     * How the entries are defined can be seen below..
     *
     * <li>[0] => section to medicationStatement reference</li>
     * <li>[1] => medicationStatement to medicaiton reference</li>
     * <li>[2] => system code</li>
     * <li>[3] => pzn code</li>
     * <li>[4] => pzn name</li>
     * <li>[5] => active substance name</li>
     * <li>[6] => active substance intensity</li>
     * <li>[5] - [6] => repeat</li>
     *
     * @return a filled array with the active substances
     */
    public ArrayList<ArrayList<String>> getActiveSubstanceInformation(){
        // Declare and Initialize variables
        ArrayList<ArrayList<String>> activeSubstances = new ArrayList<>();
        // If there is no composite medication information
        if (indiceCompositeMedication == -1){
            return null;
        }
        // Initialize variable
        int lengthMedications = 0;

        // determinate length of medications
        if(indiceAdditionalNotes != -1){
            lengthMedications = indiceAdditionalNotes - (indiceCompositeMedication + 1); // (indiceCompositeMedication + 1) => exclude the composite part
        } else {
            lengthMedications = documentsEntrys.size() - (indiceCompositeMedication + 1); // (indiceCompositeMedication + 1) => exclude the composite part
        }

        // iterate through all medications
        for (int i = 0; i < lengthMedications; i += 2) {
            // Declare and Initialize variables
            ArrayList<String> activeSubstance = new ArrayList<>();

            // Get information from HL7 FHIR document
            NodeList nList_medicationStatementReference = documentsEntrys.get((indiceCompositeMedication + 1) + i).getElementsByTagName(IDENTIFIER);
            NodeList nList_medicationStatementMedicationReference = documentsEntrys.get((indiceCompositeMedication + 1) + i).getElementsByTagName(MEDICATIONREFERENCE);
            NodeList nList_medicationCoding = documentsEntrys.get((indiceCompositeMedication + 1) + (i + 1)).getElementsByTagName(CODING);
            NodeList nList_ingredients = documentsEntrys.get((indiceCompositeMedication + 1) + (i + 1)).getElementsByTagName("ingredient");

            // Add section to medicationStatement reference
            String medicationStatementReference = util.XML.searchHierarchyByAttribute(nList_medicationStatementReference, VALUE);
            if (!util.String.isEmpty(medicationStatementReference)){
                activeSubstance.add(0, medicationStatementReference);
            } else {
                activeSubstance.add(0, EMPTYSTRING);
                //logger.log(Level.FINEST, "The section to medicationStatement reference of medication is unknown!");
            }

            // Add medicationStatement to medication reference
            String medicationStatementMedicationReference = util.XML.searchHierarchyByAttribute(nList_medicationStatementMedicationReference, REFERENCE);
            if (util.String.isEmpty(medicationStatementMedicationReference)){
                activeSubstance.add(1, medicationStatementMedicationReference);
            } else {
                activeSubstance.add(1, EMPTYSTRING);
                //logger.log(Level.FINEST, "The medicationStatement to medication reference of medication is unknown!");
            }

            // ADD system code
            NodeList nList_systemCode = ((Element) nList_medicationCoding.item(0)).getElementsByTagName(SYSTEM);
            String systemCode = ((Element) nList_systemCode.item(0)).getAttribute("value");
            if (!util.String.isEmpty(systemCode)) {
                activeSubstance.add(2, systemCode);
            } else {
                activeSubstance.add(2, EMPTYSTRING);
                //logger.log(Level.INFO, "The system code of medication is unknown!");
            }

            String pznCode = util.XML.searchHierarchyByNodeAndTagAndAttribute(nList_medicationCoding.item(0), SYSTEM, VALUE, systemCode, CODE, VALUE);
            if (!util.String.isEmpty(pznCode)) {
                activeSubstance.add(3, pznCode);
            } else {
                activeSubstance.add(3, EMPTYSTRING);
                //logger.log(Level.INFO, "The pzn code of the active substance of medication is unknown!");
            }

            // ADD atc name
            String pznName = util.XML.searchHierarchyByNodeAndTagAndAttribute(nList_medicationCoding.item(0), SYSTEM, VALUE, systemCode, DISPLAY, VALUE);
            if (!util.String.isEmpty(pznName)) {
                activeSubstance.add(4, pznName);
            } else {
                activeSubstance.add(4, EMPTYSTRING);
                //logger.log(Level.INFO, "The pzn name of the active substance of medication is unknown!");
            }

            for(int j = 0; j < nList_ingredients.getLength(); j++){

                int movepositon = 0;
                if(j != 0){
                    movepositon =+ 2;
                }

                String displayName = util.XML.searchHierarchyByAttribute((NodeList) nList_ingredients.item(j), DISPLAY);
                if(!util.String.isEmpty(displayName)){
                    activeSubstance.add(5 + movepositon, displayName);
                } else {
                    activeSubstance.add(5 + movepositon, EMPTYSTRING);
                    //logger.log(Level.INFO, "The name of the active substance of medication is unknown!");
                }

                NodeList numerator = ((Element) nList_ingredients.item(j)).getElementsByTagName("numerator");
                String intensity = util.XML.searchHierarchyByAttribute(numerator, VALUE);
                intensity += " " + util.XML.searchHierarchyByAttribute(numerator, CODE);
                if(!util.String.isEmpty(intensity)){
                    activeSubstance.add(6 + movepositon, intensity);
                } else {
                    activeSubstance.add(6 + movepositon, EMPTYSTRING);
                    //logger.log(Level.INFO, "The name of the active substance of medication is unknown!");
                }
            }

            // add medication to medications and increase medicationsPosition
            if(i == 0) {
                activeSubstances.add(0, activeSubstance);
            } else {
                activeSubstances.add(i / 2, activeSubstance);
            }
        }

        return activeSubstances;
    }
}
