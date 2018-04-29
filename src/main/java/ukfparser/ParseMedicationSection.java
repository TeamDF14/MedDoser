package ukfparser;

import logging.Logging;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import persistenceSQL.ControlSQL;

import java.util.ArrayList;
import java.util.logging.Level;

/**
 * <p>The object describes the medication sections.</p>
 * <p>It contains the medication sections, medication statement and medication.</p>
 */
public class ParseMedicationSection {

    /**
     * It contains a list with the section titles
     */
    private ArrayList<String> titleArrayList; // @t
    /**
     * It contains a list wie the section tiles codes
     */
    private ArrayList<String> titleCodeArrayList; // @c
    /**
     * It contains a list with the length of the medication within sections
     */
    private ArrayList<Integer> medicationLengthForTitle;
    /**
     * It contains the free text hat belongs to the section
     */
    private ArrayList<ArrayList<String>> freeTextArrayList; // @X
    /**
     * It contains a list with all medications
     */
    private ArrayList<ArrayList<ParseMedication>> medicationArrayList;
    /**
     * <p>Contains the UKF strings of type Document</p>
     */
    private ArrayList<Document> ukfStringArrayList;
    /**
     * <p>Declare a controlSQL variable to communicate with the SQLite db</p>
     */
    private ControlSQL controlSQL;

    /**
     * The constructor initialize the variables and fill it with the values
     */
    public ParseMedicationSection(ArrayList<Document> ukfStringArrayList) {
        // Initialize empty variables
        this.titleArrayList = new ArrayList<>();
        this.titleCodeArrayList = new ArrayList<>();
        this.medicationLengthForTitle = new ArrayList<>();
        this.medicationArrayList = new ArrayList<>();
        this.freeTextArrayList = new ArrayList<>();
        this.ukfStringArrayList = ukfStringArrayList;
        this.controlSQL = new ControlSQL();

        getMedicationArray();
    }

    /**
     * It parse all medication into the list 'medicationArrayList'
     */
    private void getMedicationArray() {

        for(int i = 0; i < this.ukfStringArrayList.size(); i++) {

            NodeList nList = this.ukfStringArrayList.get(i).getElementsByTagName("S");

            // Determinate length of section
            int length = 0;
            if(bExistFreeText() && (this.ukfStringArrayList.size() - 1 == i)) {
                length = nList.getLength() - 1;
            } else {
                length = nList.getLength();
            }

            // Iterate through each tag with "S"
            for(int j = 0; j < length; j++){

                // Initialize variables
                ArrayList<ParseMedication> tmpMedicationArrayList = new ArrayList<>();
                Element element = (Element) nList.item(j);

                // Add section title and Code to the both ArrayList<>();
                setSectionTitleAndCode(element, i);

                // Get and set ArrayList medicationLengthForTitle
                int medicationLength = getAndSetMedicationLengthForTitle(i, j, element);

                // Get each medication
                for (int k = 0; k < medicationLength; k++) {
                    Node nodeListMedication = element.getElementsByTagName("M").item(k);
                    ParseMedication parseMedication = new ParseMedication(nodeListMedication);
                    tmpMedicationArrayList.add(parseMedication);
                }

                // Set free text to section
                setFreeTextToSection(element);

                // The query checks if there is the same section on the current MP as the last section on the previous medication plan
                addMedicationArrayList(i, j, tmpMedicationArrayList, element);
            }
        }

        String stefan = "test";
    }

    /***
     * <p>The query checks if there is the same section on the current MP as the last section on the previous medication plan</p>
     * @param pageNumber contains the current number of the MP page
     * @param medicationNumber contains the current medication number
     * @param tmpMedicationArrayList contains the medication for the section
     * @param element contains the section attribute with the child to check if the section already exist
     */
    private void addMedicationArrayList(int pageNumber, int medicationNumber, ArrayList<ParseMedication> tmpMedicationArrayList, Element element) {
        if (pageNumber != 0 && medicationNumber == 0){
            String sectionTitle = null;
            try{
                sectionTitle = this.controlSQL.getSection(Integer.parseInt(element.getAttribute("c"))).getSectionTitle();
            } catch (Exception e){
                Logging.logger.log(Level.SEVERE, "Can not convert String into int", e);
            }

            // Case if the before and current title matches
            if(this.titleArrayList.get(this.titleArrayList.size() - 1).equals(this.titleArrayList.get(this.titleArrayList.size() - 2))){

                // remove last entry
                this.titleArrayList.remove(this.titleArrayList.size() -1);

                // Add the medication to the old title
                ArrayList<ParseMedication> medicationArrayList = this.medicationArrayList.get(this.medicationArrayList.size() - 1);
                medicationArrayList.addAll(tmpMedicationArrayList);
                this.medicationArrayList.set(this.medicationArrayList.size() - 1, medicationArrayList);
            } else {
                this.medicationArrayList.add(tmpMedicationArrayList);
            }
        } else {
            // Add the new medication as another section
            this.medicationArrayList.add(tmpMedicationArrayList);
        }
    }

    /**
     * <p>Set the free text to the section</p>
     * @param element contains the whole section attribute with child
     */
    private void setFreeTextToSection(Element element) {
        // Set free text to section
        NodeList nodeListFreeText = element.getElementsByTagName("X");
        ArrayList<String> freeText = new ArrayList<>();
        for (int k = 0; k < nodeListFreeText.getLength(); k++) {

            Element e = (Element) nodeListFreeText.item(k);

            String freeTextMessage = null;
            if(!util.String.isEmpty(e.getAttribute("t"))) {
                freeTextMessage = e.getAttribute("t");
            } else {
                freeTextMessage = e.getTextContent();
            }
            freeText.add(freeTextMessage);
        }
        this.freeTextArrayList.add(freeText);
    }

    /**
     * <p>It get and set the medication length for each medication title</p>
     * @param pageNumber contains the current page number of the MP
     * @param medicationNumber contains the current number of the medication of the section
     * @param element contains the whole section attribute with child
     * @return the current length of the medication of the current section of the current page
     */
    private int getAndSetMedicationLengthForTitle(int pageNumber, int medicationNumber, Element element) {
        // Set length of medication for the section
        int medicationLength = (element.getElementsByTagName("M")).getLength();
        if (pageNumber != 0 && medicationNumber == 0){
            String sectionTitle = null;
            try{
                if(!util.String.isEmpty(element.getAttribute("c"))) {

                    sectionTitle = this.controlSQL.getSection(Integer.parseInt(element.getAttribute("c"))).getSectionTitle();
                } else {
                    // Set any int to get the default section name ("Standard")
                    sectionTitle = this.controlSQL.getSection(-99).getSectionTitle();
                }
            } catch (Exception e){
                Logging.logger.log(Level.SEVERE, "Can not convert String into int", e);
            }

            // Case if the before and current title matchs
            if(this.titleArrayList.get(this.titleArrayList.size() - 2).equals(sectionTitle)){
                // Add the medication to the old title
                int counter = this.medicationLengthForTitle.get(this.medicationArrayList.size() - 1);
                this.medicationLengthForTitle.set(this.medicationArrayList.size() - 1, counter + medicationLength);
            } else {
                this.medicationLengthForTitle.add(medicationLength);
            }
        } else {
            // Add the new medication as another section
            this.medicationLengthForTitle.add(medicationLength);
        }
        return medicationLength;
    }

    /**
     * <p>Adds the title and title code to the section. Here we pay attention to a multilateral MP,</p>
     * <p>if the title goes on to the next sheet. If so, the existing title will continue to be used and no new one will be added</p>
     * @param element contains the section attribute as Element
     * @param position contains the position of the current page
     */
    private void setSectionTitleAndCode(Element element, int position) {

        // Set section title and section title code
        if (!util.String.isEmpty(element.getAttribute("c"))) {
            String sectionTitle = null;
            try{
                sectionTitle = this.controlSQL.getSection(Integer.parseInt(element.getAttribute("c"))).getSectionTitle();
            } catch (Exception e){
                Logging.logger.log(Level.SEVERE, "Can not convert String into int", e);
            }

            // The query checks if there is the same section on the current MP as the last section on the previous medication plan
            //     if(position == 0) {
                this.titleArrayList.add(sectionTitle);
                this.titleCodeArrayList.add(element.getAttribute("c"));
                //      } else {
                //           if(!this.titleCodeArrayList.get(this.titleCodeArrayList.size() - 1).equals(element.getAttribute("c"))){
                    //              this.titleArrayList.add(sectionTitle);
                    //              this.titleCodeArrayList.add(element.getAttribute("c"));
                    //        }
                //    }

        } else if (!util.String.isEmpty(element.getAttribute("t"))){

            // The query checks if there is the same section on the current MP as the last section on the previous medication plan
            //    if(position == 0) {
                this.titleArrayList.add(element.getAttribute("t"));
                this.titleCodeArrayList.add(element.getAttribute("c"));
                //   } else {

                //      if(!this.titleArrayList.get(this.titleArrayList.size() - 1).equals(element.getAttribute("t"))){
                    //          this.titleArrayList.add(element.getAttribute("t"));
                    //          this.titleCodeArrayList.add(element.getAttribute("c"));
          //      }
         //   }
        } else {

            // The query checks if there is the same section on the current MP as the last section on the previous medication plan
       //     if(position == 0) {
                this.titleArrayList.add(this.controlSQL.getSection(null).getSectionTitle());
                this.titleCodeArrayList.add(this.controlSQL.getSection(null).getSectionTitle());
         //   } else {
          //      if(!this.titleCodeArrayList.get(this.titleCodeArrayList.size() - 1).equals(this.controlSQL.getSection(null).getSectionTitle())){
          //          this.titleArrayList.add(this.controlSQL.getSection(null).getSectionTitle());
           //         this.titleCodeArrayList.add(this.controlSQL.getSection(null).getSectionTitle());
            //    }
           // }
        }
    }

    /**
     * Returns true if a free text exist in the last section that not belongs to any medication
     * @return true if a free text exist in the last section that not belongs to any medication
     */
    protected boolean bExistFreeText(){

        NodeList nList = this.ukfStringArrayList.get(this.ukfStringArrayList.size() - 1).getElementsByTagName("S");

        // Iterator through each 'S'

        Element e = (Element) nList.item(nList.getLength() - 1);

        // Get element X
        NodeList nameListFreeText = e.getElementsByTagName("X");
        NodeList nameListMedication = e.getElementsByTagName("M");

        return nameListFreeText.getLength() != 0 && nameListMedication.getLength() == 0;
    }


    /**
     * Returns the length of all sections
     * @return the lenght of all sections
     */
    protected int getSectionLength(){
        return this.titleArrayList.size();
    }

    /**
     * Returns the title of the section on a specific position
     *
     * @param position expect the position of the section
     * @return the title of the section on a specific position
     */
    protected String getTitleArrayList(int position) {
        return this.titleArrayList.get(position);
    }

    /**
     * Returns the title code of the section on a specific position
     * @param position expect the position of the section
     */
    protected String getTitleCodeArrayList(int position) {
        return this.titleCodeArrayList.get(position);
    }

    /**
     * Returns the medication length of a specific section title
     * @param sectionPosition expect the position of the seciton
     * @return the medication length of a specific section title
     */
    protected int getMedicationLength(int sectionPosition){
        return this.medicationLengthForTitle.get(sectionPosition);
    }

    /**
     * Returns a medication on a specific position of section and medication
     * @param sectionPosition expect the position section
     * @param medicationPosition expect the position of medication
     * @return a medication on a specific position of section and medication
     */
    protected ParseMedication getMedication(int sectionPosition, int medicationPosition) {

        ArrayList<ParseMedication> medArrayList = this.medicationArrayList.get(sectionPosition);

        return medArrayList.get(medicationPosition);
    }

    /**
     * Returns the free text to the section
     * @param sectionPosition expect the position section
     * @param medicationPosition expect the position of medication
     * @return the free text to the section
     */
    public String getFreeTextArrayList(int sectionPosition, int medicationPosition) {

        ArrayList<String> freeText = this.freeTextArrayList.get(sectionPosition);

        return freeText.get(medicationPosition);
    }

    /**
     * Returns the free text length to the section
     * @param sectionPosition expect the position section
     * @return the free text to the section
     */
    public int getFreeTextLength(int sectionPosition){
        return this.freeTextArrayList.get(sectionPosition).size();
    }
}
