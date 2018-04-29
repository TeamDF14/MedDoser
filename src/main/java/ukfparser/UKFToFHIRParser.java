package ukfparser;

import ca.uhn.fhir.model.api.TemporalPrecisionEnum;
import ca.uhn.fhir.model.dstu2.composite.*;
import ca.uhn.fhir.model.dstu2.resource.*;
import ca.uhn.fhir.model.dstu2.valueset.*;
import ca.uhn.fhir.model.primitive.*;
import init.Init;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import persistenceSQL.ControlSQL;
import persistenceSQL.PZNInfo;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.logging.Level;

import static logging.Logging.logger;

/**
 * <p>This class builds the FHIR XML file from the ukf string.</p>
 */
public class UKFToFHIRParser {

    /**
     * <p>Declare the utilize variable</p>
     */
    private Util utilize;
    /**
     * <p>Declare the parseAuthor variable</p>
     */
    private ParseAuthor parseAuthor;
    /**
     * <p>Declare the parsePatient variable</p>
     */
    private ParsePatient parsePatient;
    /**
     * <p>Declare the parseObservation variable</p>
     */
    private ParseObservation parseObservation;
    /**
     * <p>Declare the parseMedicationSection variable</p>
     */
    private ParseMedicationSection parseMedicationSection;
    /**
     * <p>Declare the parseFreeText variable</p>
     */
    private ParseFreeText parseFreeText;
    /**
     * <p>Declare the createFHIRContent variable</p>
     */
    private CreateFHIRContent createFHIRContent;
    /**
     * <p>Declare a parseMedication object</p>
     */
    private ParseMedicationPlan parseMedicationPlan;
    /**
     * <p>Declare a array, that show which medication plans already read</p>
     */
    private boolean[] readStatusOfUKFString;
    /**
     * <p>Contains the medication plan ID, to check if the next medication plan ID match</p>
     */
    private String medicationPlanID;
    /**
     * <p>Contains the UKF strings of type Document</p>
     */
    private ArrayList<Document> ukfStringArrayList;
    /**
     * <p>Constant that describe the first position of the ukfStringArrayList</p>
     */
    private final int firstPosition = 0;

    /**
     * <p>Constructor</p>
     * <p>Initialize some variables</p>
     */
    public UKFToFHIRParser(){
        // Create fhir file
        this.createFHIRContent = new CreateFHIRContent(this);
        this.ukfStringArrayList = new ArrayList<>();
        this.ukfStringArrayList = null;
        this.medicationPlanID = null;
    }

    /////////////
    // Public methods
    /////////////
    /**
     * <p>It fills the doc variable, initializes the objects and creates the FHIR document.</p>
     * @param ukfString Contains the ukf string.
     */
    public boolean parsing(String ukfString){

        Document doc = bFillDoc(ukfString);

        // Reads the ukf string and saves it into variable
        if (doc != null){

            // Initialize the objects
            initializeMPObjects(doc); // Initialize CreateFHIRContent, ParseAutor, ParseMedicationPlan, Util, ParsePatient, ParseObservation
            initiliazeUKFStringArrayList(); // Initialize the array, that give the information, about which pages already read
            initiliazeReadStatusOfUKFSTringArray();

            // Return false if the ID of the medication plan does not match
            if(getTotalPages() != 1 && getCurrentPage() != 1){
                if(this.medicationPlanID != null && !this.medicationPlanID.equals(this.parseMedicationPlan.getInstanceID())) {
                    return false;
                }
            } else {
                // Set medication plan ID
                this.medicationPlanID = this.parseMedicationPlan.getInstanceID();
            }

            // Add UKF string to ArrayList<String>
            this.ukfStringArrayList.set(getCurrentPage() - 1, doc);

            // Generate bundle if all UKF strings have been collected
            for(int i = 0; i < this.ukfStringArrayList.size(); i++){
                if(this.ukfStringArrayList.get(i) == null){
                    break;
                } else if((this.ukfStringArrayList.size() - 1) == i){

                    // Initialize the objects
                    if(this.parseMedicationSection == null) {
                        this.parseMedicationSection = new ParseMedicationSection(this.ukfStringArrayList);
                    }

                    if(this.parseFreeText == null) {
                        this.parseFreeText = new ParseFreeText(this.ukfStringArrayList.get(this.ukfStringArrayList.size() - 1));
                    }

                    this.createFHIRContent.generateUKFObjects(this.ukfStringArrayList);
                }
            }

            return true;
        }
        else{
            logger.log(Level.SEVERE, "\nThe doc variable could not be filled!\n");
            return false;
        }
    }

    /**
     * Print the generated FHIR content to a file.
     */
    public void print(){

        String fhirContent = createFHIRContent.start();
        createFHIRContent.print(fhirContent);
    }

    /**
     * <p>Returns true if the medication plan consists of more than one page.</p>
     * @return True if the medication plan consists of more than one page, false if not.
     */
    public boolean severalPages(){

        return this.parseMedicationPlan.getTotalPagesInt() > 1;
    }

    /**
     * Calculates if there are pages that were not scanned already
     * @return True if there is at least one page left, false if not.
     */
    public boolean arePagesLeft(){

        boolean pagesLeft = false;
        boolean[] array = getReadStatusOfUKFString();

        if (array == null){
            return false;
        }

        for(int index = 0; index < array.length; index++){
            if (!array[index]){
                pagesLeft = true;
                break;
            }
        }
        return pagesLeft;
    }

    /**
     * <p>Returns the current page of the scanned UKF string</p>
     * @return The current page number of the scanned UKF string
     */
    public int getCurrentPage(){
        return this.parseMedicationPlan.getCurrentPageInt();
    }

    /**
     * <p>Returns the total pages of the medication plan</p>
     * @return The total number of pages of the medication plan.
     */
    public int getTotalPages(){
        return this.parseMedicationPlan.getTotalPagesInt();
    }

    /**
     * <p>Returns an array by giving as many positions as pages.</p>
     * <p>Each position has the value true or false. If the value is true,</p>
     * <p>the UKF string has already been read from the page where the value is written down.</p>
     * <p>For example array [1] = true; This means that page two has already been read.</p>
     * <p>Otherwise, the values have 0</p>
     * @return a boolean array, with the status, if the UKF-page already read on page. (The page number ist index + 1)
     */
    public boolean[] getReadStatusOfUKFString(){

        try {
            if (this.readStatusOfUKFString == null) {
                for (int i = 0; i < this.readStatusOfUKFString.length; i++) {
                    this.readStatusOfUKFString[i] = false;
                }
            }
        } catch(NullPointerException e){
            logger.log(Level.FINEST, "Can not read the length array length of total pages " + e.toString());
        }

        return readStatusOfUKFString;
    }

    /**
     * <p>Set the position of the medication plans that already read</p>
     * @param position expect the page of the read UKF-String
     */
    public void setReadStatusOfUKFString(final int position){

        try {
            this.readStatusOfUKFString[position - 1] = true;
        } catch(Exception e){
            logger.log(Level.FINEST, "Can not set the the position of the already read medication plan array " + e.toString());
        }
    }

    //////////
    // Private methods
    //////////
    /**
     * Reads the ukf string and fills the doc variable with the ukf content.
     * @param ukfString Contains the ukf string. Pass null if the default string should be used.
     * @return True if the doc variable could be filled successfully, false if not.
     */
    private Document bFillDoc(String ukfString) {

        Document doc = null;

        try {
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();

            Reader reader;
            InputStream is;
            if(!util.String.isEmpty(ukfString)){

                is = new ByteArrayInputStream(ukfString.getBytes(StandardCharsets.UTF_8));
                reader = new InputStreamReader(is, "UTF-8");
            }
            else {
                is = new FileInputStream(Init.newUKFFile);
                reader = new InputStreamReader(is, "UTF-8");
            }

            InputSource input = new InputSource(reader);

            // Fill variable doc with the current UKF-String
            doc = dBuilder.parse(input);
            doc.getDocumentElement().normalize();

        } catch(ParserConfigurationException e){
            logger.log(Level.FINEST, "Can not read ukf string!" + e.toString());
        } catch(FileNotFoundException e){
            logger.log(Level.FINEST, "Can not read ukf string!" + e.toString());
        } catch(IOException e){
            logger.log(Level.FINEST, "Can not read ukf string!" + e.toString());
        } catch(SAXException e){
            logger.log(Level.FINEST, "Can not read ukf string!" + e.toString());
        } catch(Exception e) {
            logger.log(Level.FINEST, "Can not read ukf string!" + e.toString());
        }

        return doc;
    }

    /**
     * Generate a random ID for the IDs
     * @return a random ID
     */
    private String generateRandomID(){

        String preID = "urn:uuid:";
        UUID uuid = UUID.randomUUID();

        return preID + uuid.toString();
    }

    /**
     * The method determines the dosages and returns them as a list
     * @param sectionPosition expect the section position
     * @param medicationPosition expect the medication position
     * @return a list with the dosages
     */
    private List<MedicationStatement.Dosage> getFhirMedicationDosage(final int sectionPosition, final int medicationPosition) {

        List<MedicationStatement.Dosage> listDosage = new ArrayList<>();

        // Dosage morning
        MedicationStatement.Dosage dosageMorning = new MedicationStatement.Dosage();
        TimingDt timingDtMorning = new TimingDt();
        timingDtMorning.getRepeat()
                .setPeriod(this.parseMedicationSection.getMedication(sectionPosition,medicationPosition).isDosageSchemeMorningActive())
                .setPeriodUnits(UnitsOfTimeEnum.D)
                .setWhen(EventTimingEnum.PCM);
        dosageMorning.setTiming(timingDtMorning);
        listDosage.add(dosageMorning);

        // Add dosage form and dosage unit morning
        SimpleQuantityDt simpleQuantityDtMorning = new SimpleQuantityDt();
        simpleQuantityDtMorning
                .setValue(this.parseMedicationSection.getMedication(sectionPosition,medicationPosition).getDosageSchemeMorning())
                .setUnit(this.parseMedicationSection.getMedication(sectionPosition,medicationPosition).getDosageFreeText())
                .setSystem("http://unitsofmeasure.org")
                .setCode(this.parseMedicationSection.getMedication(sectionPosition,medicationPosition).getDosageCode());
        dosageMorning.setQuantity(simpleQuantityDtMorning);

        // Dosage midday
        MedicationStatement.Dosage dosageMidday = new MedicationStatement.Dosage();
        TimingDt timingDtMidday = new TimingDt();
        timingDtMidday.getRepeat()
                .setPeriod(this.parseMedicationSection.getMedication(sectionPosition,medicationPosition).isDosageSchemeMiddayActive())
                .setPeriodUnits(UnitsOfTimeEnum.D)
                .setWhen(EventTimingEnum.PCD);
        dosageMidday.setTiming(timingDtMidday);
        listDosage.add(dosageMidday);

        // Add dosage form and dosage unit morning
        SimpleQuantityDt simpleQuantityDtMidday = new SimpleQuantityDt();
        simpleQuantityDtMidday
                .setValue(this.parseMedicationSection.getMedication(sectionPosition,medicationPosition).getDosageSchemeMidday())
                .setUnit(this.parseMedicationSection.getMedication(sectionPosition,medicationPosition).getDosageFreeText())
                .setSystem("http://unitsofmeasure.org")
                .setCode(this.parseMedicationSection.getMedication(sectionPosition,medicationPosition).getDosageCode());
        dosageMidday.setQuantity(simpleQuantityDtMidday);

        // Dosage evening
        MedicationStatement.Dosage dosageEvening = new MedicationStatement.Dosage();
        TimingDt timingDtEvening = new TimingDt();
        timingDtEvening.getRepeat()
                .setPeriod(this.parseMedicationSection.getMedication(sectionPosition,medicationPosition).isDosageSchemeEveningActive())
                .setPeriodUnits(UnitsOfTimeEnum.D)
                .setWhen(EventTimingEnum.PCV);
        dosageEvening.setTiming(timingDtEvening);
        listDosage.add(dosageEvening);

        // Add dosage form and dosage unit eveneing
        SimpleQuantityDt simpleQuantityDtEvening = new SimpleQuantityDt();
        simpleQuantityDtEvening
                .setValue(this.parseMedicationSection.getMedication(sectionPosition,medicationPosition).getDosageSchemeEvening())
                .setUnit(this.parseMedicationSection.getMedication(sectionPosition,medicationPosition).getDosageFreeText())
                .setSystem("http://unitsofmeasure.org")
                .setCode(this.parseMedicationSection.getMedication(sectionPosition,medicationPosition).getDosageCode());
        dosageEvening.setQuantity(simpleQuantityDtEvening);

        // Dosage night
        MedicationStatement.Dosage dosageNight = new MedicationStatement.Dosage();
        TimingDt timingDtNight = new TimingDt();
        timingDtNight.getRepeat()
                .setPeriod(this.parseMedicationSection.getMedication(sectionPosition,medicationPosition).isDosageSchemeNightActive())
                .setPeriodUnits(UnitsOfTimeEnum.D)
                .setWhen(EventTimingEnum.HS);
        dosageNight.setTiming(timingDtNight);
        listDosage.add(dosageNight);

        // Add dosage form and dosage unit morning
        SimpleQuantityDt simpleQuantityDtNight = new SimpleQuantityDt();
        simpleQuantityDtNight
                .setValue(this.parseMedicationSection.getMedication(sectionPosition,medicationPosition).getDosageSchemeNight())
                .setUnit(this.parseMedicationSection.getMedication(sectionPosition,medicationPosition).getDosageFreeText())
                .setSystem("http://unitsofmeasure.org")
                .setCode(this.parseMedicationSection.getMedication(sectionPosition,medicationPosition).getDosageCode());
        dosageNight.setQuantity(simpleQuantityDtNight);

        return listDosage;
    }

    /**
     * <p>Initialize CreateFHIRContent, ParseAutor, ParseMedicationPlan, Util, ParsePatient, ParseObservation</p>
     * @param doc contains the current UKF string
     */
    private void initializeMPObjects(Document doc) {
        if(this.createFHIRContent == null) {
            this.createFHIRContent = new CreateFHIRContent(this);
        }

        if(this.parseAuthor == null) {
            this.parseAuthor = new ParseAuthor(doc);
        }

        this.parseMedicationPlan = new ParseMedicationPlan(doc);

        if(this.utilize == null) {
            this.utilize = new Util();
        }

        if(this.parsePatient == null) {
            this.parsePatient = new ParsePatient(doc);
        }

        if(this.parseObservation == null) {
            this.parseObservation = new ParseObservation(doc);
        }
    }

    /**
     * <p>Initialize Array readStatusOfUKFString</p>
     */
    private void initiliazeReadStatusOfUKFSTringArray() {
        if(this.readStatusOfUKFString == null) {
            this.readStatusOfUKFString = new boolean[this.parseMedicationPlan.getTotalPagesInt()];
        }
    }

    /**
     * <p>Initialize ukfStringArrayList ArrayList and fill it each entry with 'false'</p>
     */
    private void initiliazeUKFStringArrayList() {
        if(this.ukfStringArrayList == null){
            this.ukfStringArrayList = new ArrayList<>();

            for(int i = 0; i < getTotalPages(); i++){
                this.ukfStringArrayList.add(i, null);
            }
        }
    }

    ////////////////////
    // Begin to create the blocks for the FHIR xml file
    ///////////////////
    /**
     * Create the meta block of medicationplan
     * @return the meta block as Composition
     */
    protected Composition getFhirMedicationPlan(){

        Composition composition = new Composition();

        // Get version of BMP
        String version = this.parseMedicationPlan.getVersion().replaceAll("020", "2.0");
        composition.setId(version);

        // Set instance ID
        IdentifierDt identifierDt = new IdentifierDt();
        identifierDt.setSystem("http://mein.medikationsplan.de/composition");
        identifierDt.setValue(this.parseMedicationPlan.getInstanceID());
        composition.setIdentifier(identifierDt);

        // Set printed date
        try {
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.GERMAN);
            Date date = dateFormat.parse(this.parseAuthor.getPrintedDate());
            DateTimeDt dateTimeDt = new DateTimeDt();
            dateTimeDt.setValue(date);
            composition.setDate(dateTimeDt);
        } catch (ParseException e) {
            logger.log(Level.FINEST, "Can convert the creation date of ukf file into date!" + e.toString());
        }


        // Set Title of medication plan
        StringDt title_BMP = new StringDt();
        title_BMP.setValue("Patientenbezogener Medikationsplan");
        composition.setTitle(title_BMP);
        composition.setStatus(CompositionStatusEnum.FINAL);
        composition.setConfidentiality("N");

        // Set language
        CodeDt code_BMP = new CodeDt();
        code_BMP.setValue(this.parseMedicationPlan.getLanguage());
        composition.setLanguage(code_BMP);

        // Set patient ID
        ResourceReferenceDt patient_subject = new ResourceReferenceDt();
        utilize.setPatientID(generateRandomID());
        patient_subject.setReference(utilize.getPatientID());
        patient_subject.setDisplay(this.parsePatient.getName() + " " + this.parsePatient.getSurname());
        composition.setSubject(patient_subject);

        // Set authorID
        List<ResourceReferenceDt> entry_author = new ArrayList<ResourceReferenceDt>();
        ResourceReferenceDt author_subject = new ResourceReferenceDt();
        this.utilize.setAuthorID(generateRandomID());
        author_subject.setReference(this.utilize.getAuthorID());
        author_subject.setDisplay(this.parseAuthor.getName());
        entry_author.add(author_subject);
        composition.setAuthor(entry_author);

        // Set custodianID
        ResourceReferenceDt custodian_subject = new ResourceReferenceDt();
        this.utilize.setCustodianID(generateRandomID());
        custodian_subject.setReference(this.utilize.getCustodianID());
        composition.setCustodian(custodian_subject);

        return composition;
    }

    /**
     * Create Fhir paitent block
     * @return the fhir patient block as Patient
     */
    protected Patient getFhirPatient(){

        Patient patient = new Patient();

        // Set meta information
        patient.getMeta()
                .addProfile("http://fhir.hl7.de/medikationsplan/patient");

        // Set patient id
        patient.setId(this.utilize.getPatientID());

        // Set health insurance number
        if(!util.String.isEmpty(this.parsePatient.getEgk())) {
            patient.addIdentifier()
                    .setSystem("http://kvnummer.gkvnet.de")
                    .setValue(this.parsePatient.getEgk());
        }

        // Set name of patient
        patient.addName().addFamily(this.parsePatient.getSurname()).addGiven(this.parsePatient.getName());

        // Set gender of patient
        if (this.parsePatient.getGender().equals("W")) {
            patient.setGender(AdministrativeGenderEnum.FEMALE);
        } else if (this.parsePatient.getGender().equals("M")) {
            patient.setGender(AdministrativeGenderEnum.MALE);
        } else if(this.parsePatient.getGender().equals("X")){
            patient.setGender(AdministrativeGenderEnum.UNKNOWN);
        }

        // Set birthday of patient
        if(!util.String.isEmpty(this.parsePatient.getBirthday())) {
            try {
                DateFormat birth = new SimpleDateFormat("yyyy-MM-dd", Locale.GERMAN);
                Date date = null;
                date = birth.parse(this.parsePatient.getBirthday());
                DateDt geb = new DateDt(date, TemporalPrecisionEnum.DAY);
                patient.setBirthDate(geb);
            } catch (ParseException e) {
                logger.log(Level.FINEST, "Can convert the birthday date of ukf file into date!" + e.toString());
            }
        }

        return patient;
    }

    /**
     * Create autor block
     * @return the autor block as Practitioner
     */
    protected Practitioner getFhirAutor(){

        Practitioner practitioner = new Practitioner();

        // Set profile
        practitioner.getMeta().
                addProfile("http://fhir.hl7.de/medikationsplan/parctitioner");

        // Set longlife doctor number
        if(!util.String.isEmpty(parseAuthor.getLifelongDoctorNumber())) {
            practitioner.addIdentifier()
                    .setSystem("http://kbv.de/LANR")
                    .setValue(parseAuthor.getLifelongDoctorNumber());
        }

        // Set pharmacy id number
        if(!util.String.isEmpty(parseAuthor.getPharmacyIDNumber())) {
            practitioner.addIdentifier()
                    .setSystem("http://kbv.de/IDF")
                    .setValue(parseAuthor.getPharmacyIDNumber());
        }

        // Set hospital id number
        if(!util.String.isEmpty(parseAuthor.getHospitalNumber())) {
            practitioner.addIdentifier()
                    .setSystem("http://kbv.de/KIK")
                    .setValue(parseAuthor.getHospitalNumber());
        }

        // Set name of doctor/autor
        HumanNameDt name_austeller = practitioner.getName();
        name_austeller.addFamily(parseAuthor.getName());
        practitioner.setName(name_austeller);

        // Set address of doctor/autor
        if(this.parseAuthor.bAddressExists()) {
            AddressDt addressDt = practitioner.addAddress();
            addressDt.setUse(AddressUseEnum.WORK);
            addressDt.addLine(parseAuthor.getStreet());
            addressDt.addLine(parseAuthor.getZipCode());
            addressDt.addLine(parseAuthor.getCity());
        }

        // Set telephone of doctor/autor
        if(!util.String.isEmpty(this.parseAuthor.getTelephone())) {
            ContactPointDt contactPointDt = practitioner.addTelecom();
            BoundCodeDt phonenummer = contactPointDt.getSystemElement();
            phonenummer.setValue("phone");
            contactPointDt.setSystem(phonenummer);
            contactPointDt.setValue(parseAuthor.getTelephone());
            contactPointDt.setUse(ContactPointUseEnum.WORK);
        }

        // Set eMail of doctor/autor
        if(!util.String.isEmpty(this.parseAuthor.geteMail())){
            ContactPointDt email = practitioner.addTelecom();
            BoundCodeDt emailadresse = email.getSystemElement();
            emailadresse.setValue("email");
            email.setSystem(emailadresse);
            email.setValue(parseAuthor.geteMail());
            email.setUse(ContactPointUseEnum.WORK);
        }

        // Set ID of doctor/autor
        practitioner.setId(this.utilize.getAuthorID());

        return practitioner;
    }

    /**
     * Create organization Block
     * @return organization as Organization
     */
    protected Organization getFhirOrganization(){

        Organization organization = null;
        ParseCustodian parseCustodian = new ParseCustodian(this.ukfStringArrayList.get(firstPosition));

        if(parseCustodian.bCustodianExist()) {

            organization = new Organization();

            // Set ID of custodian
            organization.setId(this.utilize.getCustodianID());

            // system and value of organization
            List<IdentifierDt> entryIdentifierDt = new ArrayList<IdentifierDt>();
            IdentifierDt identifierDt = new IdentifierDt();
            identifierDt.setSystem(parseCustodian.getIdentificiationsSystem());
            identifierDt.setValue(parseCustodian.getIdentifikation());
            entryIdentifierDt.add(identifierDt);
            organization.setIdentifier(entryIdentifierDt);
        }

        return organization;
    }

    /**
     * Create the composition of clinical parameter
     * @return the composition of clinical parameter as Composition.Section
     */
    protected Composition.Section getFhirCompositionClinicalParameter(){

        Composition.Section composition = null;

        if(this.parseObservation.bExistClinicalParameter()){

            composition = new Composition.Section();

            // Set title of clinical parameter
            StringDt title = new StringDt();
            title.setValue("Klinische Parameter");
            composition.setTitle(title);

            // Set loinc code of clinical parameter
            CodingDt loincCode = new CodingDt();
            loincCode
                    .setSystem("http://loinc.org")
                    .setCode("55752-0")
                    .setDisplay("Clinical information");

            // Set loinc code to codeableConceptDt of type CodeableConceptDt
            CodeableConceptDt codeableConceptDt = new CodeableConceptDt();
            codeableConceptDt.addCoding(loincCode);
            composition.setCode(codeableConceptDt);

            // Generate keys for each entry
            List<ResourceReferenceDt> resourceReferenceDtList = new ArrayList<ResourceReferenceDt>();

            if(this.parseObservation.bExistWeight()){

                ResourceReferenceDt resourceReferenceDt = new ResourceReferenceDt();
                this.utilize.setBodyWeightID(generateRandomID());
                resourceReferenceDt.setReference(this.utilize.getBodyWeightID());
                resourceReferenceDtList.add(resourceReferenceDt);
            }

            if(this.parseObservation.bExistHeight()){

                ResourceReferenceDt resourceReferenceDt = new ResourceReferenceDt();
                this.utilize.setBodyHeightID(generateRandomID());
                resourceReferenceDt.setReference(this.utilize.getBodyHeightID());
                resourceReferenceDtList.add(resourceReferenceDt);
            }

            if(this.parseObservation.bExistCreatinine()){

                ResourceReferenceDt resourceReferenceDt = new ResourceReferenceDt();
                this.utilize.setCreatininID(generateRandomID());
                resourceReferenceDt.setReference(this.utilize.getCreatininID());
                resourceReferenceDtList.add(resourceReferenceDt);
            }

            composition.setEntry(resourceReferenceDtList);
        }

        return composition;
    }

    /**
     * Create body weight block for clinical parameter
     * @return the body weight block as Observation
     */
    protected Observation getObservationClinicalParaWeight(){

        Observation observation = new Observation();

        if(this.parseObservation.bExistWeight()){

            // Add profile
            observation.getMeta().
                    addProfile("http://fhir.hl7.de/medikationsplan/observation");

            observation.setId(this.utilize.getBodyWeightID());
            observation.setStatus(ObservationStatusEnum.FINAL);

            observation
                    .getCode()
                    .addCoding()
                    .setSystem("http://loinc.org")
                    .setCode("3142-7")
                    .setDisplay("Body weight");

            observation.setValue(new QuantityDt()
                    .setValue(Integer.parseInt(parseObservation.getWeight()))
                    .setUnit("kg")
                    .setSystem("http://unitsofmeasure.org")
                    .setCode("kg"));

            // Set ID of body weight
            ResourceReferenceDt resourceReferenceDt = new ResourceReferenceDt();
            resourceReferenceDt.setReference(this.utilize.getPatientID());
            observation.setSubject(resourceReferenceDt);
        }

        return observation;
    }

    /**
     * Craate the block body height
     * @return body height as Observation
     */
    protected Observation getObservationClinicalParaHeight(){

        Observation observation = new Observation();

        if(this.parseObservation.bExistHeight()){

            // Set profile
            observation.getMeta().
                    addProfile("http://fhir.hl7.de/medikationsplan/observation");

            observation.setId(this.utilize.getBodyHeightID());
            observation.setStatus(ObservationStatusEnum.FINAL);
            observation
                    .getCode()
                    .addCoding()
                    .setSystem("http://loinc.org")
                    .setCode("8302-2")
                    .setDisplay("Body height");

            observation.setValue(new QuantityDt()
                    .setValue(Integer.parseInt(parseObservation.getHeight()))
                    .setUnit("cm")
                    .setSystem("http://unitsofmeasure.org")
                    .setCode("cm"));

            // Set body height reference
            ResourceReferenceDt resourceReferenceDt = new ResourceReferenceDt();
            resourceReferenceDt.setReference(this.utilize.getPatientID());
            observation.setSubject(resourceReferenceDt);
        }

        return observation;
    }

    /**
     * Create creatinin block
     * @return creatinin block as Observation
     */
    protected Observation getObservationClinicalParacCeatinine(){

        Observation observation = new Observation();

        if(this.parseObservation.bExistCreatinine()) {

            // Set profile
            observation.getMeta().
                    addProfile("http://fhir.hl7.de/medikationsplan/observation");

            observation.setId(this.utilize.getCreatininID());
            observation.setStatus(ObservationStatusEnum.FINAL);
            observation
                    .getCode()
                    .addCoding()
                    .setSystem("http://loinc.org")
                    .setCode("2160-0")
                    .setDisplay("Creatine");

            observation.setValue(new QuantityDt()
                    .setValue(Double.parseDouble(parseObservation.getCreatinine()))
                    .setUnit("mg/dl")
                    .setSystem("http://unitsofmeasure.org")
                    .setCode("mg/dl"));

            // Set creatinin ID
            ResourceReferenceDt resourceReferenceDt = new ResourceReferenceDt();
            resourceReferenceDt.setReference(this.utilize.getPatientID());
            observation.setSubject(resourceReferenceDt);
        }

        return observation;
    }

    /**
     * Create composition of allergy and intolerance
     * @return composition of allergy and intolerance as Composition.Section
     */
    protected Composition.Section getFhirCompositionAllergyIntolerance(){

        Composition.Section composition = null;

        if(this.parseObservation.bExistAllergieInterlorance()){

            composition = new Composition.Section();

            // Set title of clinical parameter
            StringDt title = new StringDt();
            title.setValue("Allergien und Unverträglichkeiten");
            composition.setTitle(title);

            // Set loinc code of clinical parameter
            CodingDt loincCode = new CodingDt();
            loincCode
                    .setSystem("http://loinc.org")
                    .setCode("48765-2")
                    .setDisplay("AllergyIntolerance, adverse reactions, alerts");

            // Set loinc code to codeableConceptDt of type CodeableConceptDt
            CodeableConceptDt codeableConceptDt = new CodeableConceptDt();
            codeableConceptDt.addCoding(loincCode);
            composition.setCode(codeableConceptDt);

            // Generate keys for each entry
            List<ResourceReferenceDt> resourceReferenceDtList = new ArrayList<ResourceReferenceDt>();

            if(this.parseObservation.bExistAllergy()){

                ResourceReferenceDt resourceReferenceDt = new ResourceReferenceDt();
                this.utilize.setAllergyID(generateRandomID());
                resourceReferenceDt.setReference(this.utilize.getAllergyID());
                resourceReferenceDtList.add(resourceReferenceDt);
            }

            if(this.parseObservation.bExistIntolerance()){

                ResourceReferenceDt resourceReferenceDt = new ResourceReferenceDt();
                this.utilize.setIntoleranceID(generateRandomID());
                resourceReferenceDt.setReference(this.utilize.getIntoleranceID());
                resourceReferenceDtList.add(resourceReferenceDt);
            }

            composition.setEntry(resourceReferenceDtList);
        }

        return composition;
    }

    /**
     * Create allergy block
     * @return allergy block as AllergyIntolrance
     */
    protected AllergyIntolerance getFhirAllergy(){

        AllergyIntolerance allergyIntolerance = new AllergyIntolerance();

        // Set id of patient
        ResourceReferenceDt resourceReferenceDt = new ResourceReferenceDt();
        resourceReferenceDt.setReference(this.utilize.getPatientID());
        allergyIntolerance.setPatient(resourceReferenceDt);

        // Set value of intolerance
        CodeableConceptDt substance = new CodeableConceptDt();
        substance.setText(this.parseObservation.getAllergy());
        allergyIntolerance.setSubstance(substance);

        // Set id of allergy
        allergyIntolerance.setId(this.utilize.getAllergyID());

        // Set type of AllergyIntolerance
        allergyIntolerance.setType(AllergyIntoleranceTypeEnum.ALLERGY);

        return allergyIntolerance;
    }

    /**
     * Create intolerance block
     * @return intolerance block as AllergyIntolerance
     */
    protected AllergyIntolerance getFhirIntolerance(){

        AllergyIntolerance allergyIntolerance = new AllergyIntolerance();

        // Set id of patient
        ResourceReferenceDt resourceReferenceDt = new ResourceReferenceDt();
        resourceReferenceDt.setReference(this.utilize.getPatientID());
        allergyIntolerance.setPatient(resourceReferenceDt);


        // Set value of intolerance
        CodeableConceptDt substance = new CodeableConceptDt();
        substance.setText(this.parseObservation.getIntolerance());
        allergyIntolerance.setSubstance(substance);

        // Set id of intolerance
        allergyIntolerance.setId(this.utilize.getIntoleranceID());

        // Set type of AllergyIntolerance
        allergyIntolerance.setType(AllergyIntoleranceTypeEnum.INTOLERANCE);

        return allergyIntolerance;
    }

    /**
     * Create composition for health concern
     * @return health concern block as Composition.Section
     */
    protected Composition.Section getCompositionHealthConcern(){

        Composition.Section composition = null;

        if(this.parseObservation.bExistHealthConcern()){

            composition = new Composition.Section();

            // Set title of clinical parameter
            StringDt title = new StringDt();
            title.setValue("Gesundheitsbelange");
            composition.setTitle(title);

            // Set loinc code
            CodingDt loincCode = new CodingDt();
            loincCode
                    .setSystem("http://loinc.org")
                    .setCode("75310-3")
                    .setDisplay("Health concerns");

            // Set loinc code to codeableConceptDt of type CodeableConceptDt
            CodeableConceptDt codeableConceptDt = new CodeableConceptDt();
            codeableConceptDt.addCoding(loincCode);
            composition.setCode(codeableConceptDt);

            // Generate key for each entry
            List<ResourceReferenceDt> resourceReferenceDtList = new ArrayList<ResourceReferenceDt>();

            if(this.parseObservation.bExistpregnancyStatus()){

                ResourceReferenceDt resourceReferenceDt = new ResourceReferenceDt();
                this.utilize.setPregnancyStatusID(generateRandomID());
                resourceReferenceDt.setReference(this.utilize.getPregnancyStatusID());
                resourceReferenceDtList.add(resourceReferenceDt);
            }

            if(this.parseObservation.bExistBreasfeedingStatus()){

                ResourceReferenceDt resourceReferenceDt = new ResourceReferenceDt();
                this.utilize.setBreastfeedingStatusID(generateRandomID());
                resourceReferenceDt.setReference(this.utilize.getBreastfeedingStatusID());
                resourceReferenceDtList.add(resourceReferenceDt);
            }

            composition.setEntry(resourceReferenceDtList);
        }

        return composition;
    }

    /**
     * Create pregnancy status block for health concern
     * @return the pregnancy status as Observation
     */
    protected Observation getObservationPregnancyStatus(){

        Observation observation = new Observation();

        if(this.parseObservation.bExistpregnancyStatus() && this.parseObservation.getPregnancyStatus().equals("1")){

            // Add profile
            observation.getMeta().
                    addProfile("http://fhir.hl7.de/medikationsplan/allergyintolerance");

            // Set id of pregnancy status
            observation.setId(this.utilize.getPregnancyStatusID());
            observation.setStatus(ObservationStatusEnum.FINAL);

            // Set loinc of breastfeeding
            CodingDt loincCode = new CodingDt();
            loincCode
                    .setSystem("http://loinc.org")
                    .setCode("11449-6")
                    .setDisplay("Pregnancy status");
            CodeableConceptDt codeableConceptDtBreastFeeding = new CodeableConceptDt();
            codeableConceptDtBreastFeeding.addCoding(loincCode);
            observation.setCode(codeableConceptDtBreastFeeding);

            // Set status to true (1)
            observation.setComments("1");

            // Set ID of pregnancy status
            ResourceReferenceDt resourceReferenceDt = new ResourceReferenceDt();
            resourceReferenceDt.setReference(this.utilize.getPatientID());
            observation.setSubject(resourceReferenceDt);
        }

        return observation;
    }

    /**
     * Create breast feeding status
     * @return breast feeding status as Observation
     */
    protected Observation getObservationBreastfeedingStatus() {

        Observation observation = new Observation();

        if(this.parseObservation.bExistBreasfeedingStatus() && this.parseObservation.getBreastfeedingStatus().equals("1")) {

            // Set profile
            observation.getMeta().
                    addProfile("http://fhir.hl7.de/medikationsplan/allergyintolerance");

            // Set status of observation
            observation.setStatus(ObservationStatusEnum.FINAL);

            // Set status of breastfeeding
            observation.setId(this.utilize.getBreastfeedingStatusID());

            // Set loinc of breastfeeding
            CodingDt loincCode = new CodingDt();
            loincCode
                    .setSystem("http://loinc.org")
                    .setCode("63895-7")
                    .setDisplay("Breastfeeding status");
            CodeableConceptDt codeableConceptDtBreastFeeding = new CodeableConceptDt();
            codeableConceptDtBreastFeeding.addCoding(loincCode);
            observation.setCode(codeableConceptDtBreastFeeding);

            // Set status to true (1)
            observation.setComments("1");

            // Set ID of pregnancy status
            ResourceReferenceDt resourceReferenceDt = new ResourceReferenceDt();
            resourceReferenceDt.setReference(this.utilize.getPatientID());
            observation.setSubject(resourceReferenceDt);
        }

        return observation;
    }

    /**
     * Create composition for medication
     * @return medication composition as Composition.Section
     */
    protected Composition.Section getCompositionMedication(){

        Composition.Section comSection = new Composition.Section();

        // Add title to composition
        StringDt title_aktMed = new StringDt();
        title_aktMed.setValue("Aktuelle Medikation");
        comSection.setTitle(title_aktMed);

        // Add loinc code to composition
        CodingDt loinc = new CodingDt();
        loinc
                .setSystem("http://loinc.org")
                .setCode("19009-0")
                .setDisplay("Medication.current");
        CodeableConceptDt codeableConceptDt = new CodeableConceptDt();
        codeableConceptDt.addCoding(loinc);
        comSection.setCode(codeableConceptDt);

        // Initialize a list for the amount of the medication per section
        List<Composition.Section> titleList = new ArrayList<Composition.Section>();

        // Add medication sections and mediation IDs
        for(int i = 0; i < this.parseMedicationSection.getSectionLength(); i++){

            // Create object to save the medication to sections
            Composition.Section sectionTitle = new Composition.Section();

            // Set title
            sectionTitle.setTitle(this.parseMedicationSection.getTitleArrayList(i));

            // Add reference to title
            List<ResourceReferenceDt> resoruce_medication = new ArrayList<ResourceReferenceDt>();

            // Create a temp ArrayList<String> to safe the medication keys in the object
            ArrayList<String> medicationStatementReferenceIDArrayList = new ArrayList<>();

            // Add keys for each medication
            for (int j = 0; j < this.parseMedicationSection.getMedicationLength(i); j++) {

                // Create key for the medication
                medicationStatementReferenceIDArrayList.add(generateRandomID());

                // Add key to the section title
                ResourceReferenceDt resourceReferenceDt = new ResourceReferenceDt();
                resourceReferenceDt.setReference(medicationStatementReferenceIDArrayList.get(j));
                resoruce_medication.add(resourceReferenceDt);
                sectionTitle.setEntry(resoruce_medication);
            }

            // Set free text to section
            String freeText = "";
            for (int j = 0; j < this.parseMedicationSection.getFreeTextLength(i); j++) {
                freeText += util.String.convertToSpecialCharacters(this.parseMedicationSection.getFreeTextArrayList(i,j)) + " ";
            }
            if(!util.String.isEmpty(freeText)) {
                NarrativeDt narrativeDt = new NarrativeDt();
                narrativeDt.setDiv(freeText);
                sectionTitle.setText(narrativeDt);
            }

            // Save the keys to the objects
            this.utilize.addMedicationStatementReferenceID(medicationStatementReferenceIDArrayList);

            // Add section with the keys to the block
            titleList.add(sectionTitle);
            comSection.setSection(titleList);
        }

        // Add free text if exist
        if(!this.parseFreeText.isFreeTextEmpty()){

            // Create object to save the medication to sections
            Composition.Section sectionTitle = new Composition.Section();

            // Set title
            sectionTitle.setTitle(this.parseFreeText.getFreeTextTitle());

            /*
            // Add reference to title
            List<ResourceReferenceDt> resourceReferenceDts = new ArrayList<ResourceReferenceDt>();

            // Create a temp ArrayList<String> to safe the free text and generate random key
            ArrayList<String> freeTextArrayList = new ArrayList<>();
            freeTextArrayList.add(generateRandomID());

            // Add key to the section title
            ResourceReferenceDt resourceReferenceDt = new ResourceReferenceDt();
            resourceReferenceDt.setReference(freeTextArrayList.get(0));
            resourceReferenceDts.add(resourceReferenceDt);
            sectionTitle.setEntry(resourceReferenceDts);

            // Save the keys to the objects
            this.utilize.addFreeTextIDToMedStatementReference(freeTextArrayList);
            */

            // Add section with the keys to the block
            titleList.add(sectionTitle);
            comSection.setSection(titleList);
        }

        return comSection;
    }

    /**
     * <p>Create all medication statement block</p>
     * <p>It contains the basic information about the medication and the dosage</p>
     * @return medication statement block in a ArrayList<MedicationStatement>
     */
    protected ArrayList<MedicationStatement> getFhirMedicationStatement(){

        ArrayList<MedicationStatement> medicationStatementArrayList = new ArrayList<>();

        for (int i = 0; i < this.parseMedicationSection.getSectionLength(); i++) {
            for (int j = 0; j < this.parseMedicationSection.getMedicationLength(i); j++) {

                MedicationStatement medicationStatement = new MedicationStatement();

                // Add profile
                medicationStatement.getMeta()
                        .addProfile("http://fhir.hl7.de/medikationsplan/medicationstatement");

                // Add identifier of medication
                IdentifierDt identifier_medication = new IdentifierDt();
                identifier_medication.setValue(this.utilize.getMedicationStatementReferenceID(i, j));
                medicationStatement.addIdentifier(identifier_medication);

                // Add id of patient
                ResourceReferenceDt patient_resource = new ResourceReferenceDt();
                patient_resource.setReference(this.utilize.getPatientID());
                medicationStatement.setPatient(patient_resource);

                // Add asserted date
                try {
                    DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.GERMAN);
                    Date date = dateFormat.parse(this.parseAuthor.getPrintedDate());
                    DateTimeDt dateTimeDt = new DateTimeDt();
                    dateTimeDt.setValue(date);
                    medicationStatement.setDateAsserted(dateTimeDt);
                } catch (ParseException e) {
                    logger.log(Level.FINEST, "Can convert the printed date of ukf file into date!" + e.toString());
                }

                // Add status to active
                medicationStatement.setStatus(MedicationStatementStatusEnum.ACTIVE);

                // Add was not taken default to yes
                medicationStatement.setWasNotTaken(true);

                // Add reason for use
                CodeableConceptDt reasonForUse = new CodeableConceptDt();
                reasonForUse.setText(this.parseMedicationSection.getMedication(i, j).getReasonForUse());
                medicationStatement.setReasonForUse(reasonForUse);

                // Add note
                medicationStatement.setNote(this.parseMedicationSection.getMedication(i, j).getNote());

                // Generate and add medication reference
                this.utilize.setMedicationReferenceID(i, j, generateRandomID());
                ResourceReferenceDt medRefDt = new ResourceReferenceDt(this.utilize.getMedicationReferenceID(i, j));
                medicationStatement.setMedication(medRefDt);

                // Get dosage medication and add dosage to medication
                List<MedicationStatement.Dosage> listDosage;
                if(this.parseMedicationSection.getMedication(i, j).bDosageSchemeFreeTextActive()){
                    listDosage = new ArrayList<>();
                    MedicationStatement.Dosage dosageFreeText = new MedicationStatement.Dosage();
                    dosageFreeText.setText(this.parseMedicationSection.getMedication(i, j).getDosageSchemeFreeText());
                    listDosage.add(dosageFreeText);
                } else {
                    listDosage = getFhirMedicationDosage(i, j);
                }
                medicationStatement.setDosage(listDosage);

                // Add medication to ArrayList medication
                medicationStatementArrayList.add(medicationStatement);
            }
        }

        return medicationStatementArrayList;
    }

    /**
     * <p>Create all medication block.</p>
     * <p>It contains pzn and the active substances of the medication</p>
     * @return the medication block as ArrayList<Medication>
     */
    protected ArrayList<Medication> getFhirMedication(){

        ArrayList<Medication> medicationArrayList = new ArrayList<>();

        for (int i = 0; i < this.parseMedicationSection.getSectionLength(); i++) {
            for (int j = 0; j < this.parseMedicationSection.getMedicationLength(i); j++) {

                Medication medication = new Medication();
                CodeableConceptDt medicationConceptDt = new CodeableConceptDt();

                // Set Referenz ID
                medication.setId(this.utilize.getMedicationReferenceID(i, j));

                // Add pzn code to medicament
                String displayName = null;
                if(!util.String.isEmpty(this.parseMedicationSection.getMedication(i, j).getPharmacentralnumber())){
                    // Get pzn info
                    ControlSQL controlSQL = new ControlSQL();
                    PZNInfo pznInfo = controlSQL.getMedication(this.parseMedicationSection.getMedication(i, j).getPharmacentralnumber());
                    displayName = pznInfo.getTradeName();
                } else {

                    if(!util.String.isEmpty(this.parseMedicationSection.getMedication(i, j).getDrugname())){
                        displayName = this.parseMedicationSection.getMedication(i, j).getDrugname();
                    } else {
                        displayName = "Handelsname ist unbekannt!";
                    }
                }
                CodingDt pznCode = new CodingDt();
                pznCode
                        .setSystem("http://www.ifaffm.de/pzn")
                        .setCode(this.parseMedicationSection.getMedication(i, j).getPharmacentralnumber())
                        .setDisplay(displayName);
                medicationConceptDt.addCoding(pznCode);

                // Add pzn and atc to medication
                medication.setCode(medicationConceptDt);

                // Add product properties
                CodingDt productCodingDt = new CodingDt();
                Medication.Product product = new Medication.Product();

                // Add product drug form
                CodingDt productNameCodingDt = new CodingDt();
                productNameCodingDt
                        .setSystem("http://hl7.org/fhir/v3/orderableDrugForm")
                        .setCode(this.parseMedicationSection.getMedication(i, j).getDosageCode())
                        .setDisplay(this.parseMedicationSection.getMedication(i, j).getDosageFreeText());

                product.getForm()
                        .addCoding(productNameCodingDt);

                // Add all product ingredients
                ArrayList<ParseActiveSubstance> parseActiveSubstances =  this.parseMedicationSection.getMedication(i, j).getActiveSubstanceArrayList();

                for(int k = 0; k < this.parseMedicationSection.getMedication(i,j).getActiveSubstanceArrayList().size(); k++ ){

                    Medication.ProductIngredient productIngredient = new Medication.ProductIngredient();
                    RatioDt ratioDt = new RatioDt();

                    // Set name of active substance
                    productIngredient
                            .getItem()
                            .setDisplay(this.parseMedicationSection.getMedication(i,j).getActiveSubstanceArrayList().get(k).getDrugName());

                    // Exception handling if the drug intensity ist not a number
                    int numeratorValue;
                    try{
                        numeratorValue = Integer.parseInt(this.parseMedicationSection.getMedication(i,j).getActiveSubstanceArrayList().get(k).getDrugIntensity());
                    } catch (NumberFormatException e) {
                        logger.log(Level.FINEST, "Can convert active substance value to integer!" + e.toString());
                        numeratorValue = -1;
                    }

                    // Set numerator
                    QuantityDt numerator = new QuantityDt();
                    numerator
                            .setValue(numeratorValue) //hier stärke
                            .setSystem("http://unitsofmeasure.org")
                            .setCode(this.parseMedicationSection.getMedication(i,j).getActiveSubstanceArrayList().get(k).getDrugUnit());

                    // Set denominator
                    QuantityDt denominator = new QuantityDt();
                    denominator
                            .setValue(1)
                            .setSystem("http://unitsofmeasure.org")
                            .setCode("1");

                    // Add numerator and dominator to the block amount
                    ratioDt
                            .setNumerator(numerator)
                            .setDenominator(denominator);

                    // Add amount block to the medication
                    productIngredient.setAmount(ratioDt);

                    // Add whole ingredient block to medication
                    product.addIngredient(productIngredient);
                    medication.setProduct(product);
                }

                medicationArrayList.add(medication);
            }
        }

        return medicationArrayList;
    }

    /**
     * Create the free text block. It  has no reference to mediation
     * @return free text block as Composition.Section
     */
    protected Composition.Section getFhirFreetext(){

        Composition.Section comSection = null;

        if(!this.parseFreeText.isFreeTextEmpty()) {

            comSection = new Composition.Section();

            /** It is not necessary in the freetext block
             // Set id of free text reference
             // comSection.setId(this.utilize.getMedicationStatementReferenceID(this.utilize.getMedicationStatementReferenceID().size() - 1, 0));
             */

            // Set title of free text
            StringDt stringDt = new StringDt();
            stringDt.setValue(this.parseFreeText.getFreeTextTitle());
            comSection.setTitle(stringDt);

            // Set loinc code of free text
            CodingDt codingDt = new CodingDt();
            codingDt
                    .setSystem("http://loinc.org")
                    .setCode("69730-0")
                    .setDisplay("Instructions");

            CodeableConceptDt codeableConceptDt = new CodeableConceptDt();
            codeableConceptDt.addCoding(codingDt);
            comSection.setCode(codeableConceptDt);

            // Set status of composition
            NarrativeDt narrativeDt = new NarrativeDt();
            narrativeDt.setStatus(NarrativeStatusEnum.ADDITIONAL);

            // Set text of message
            XhtmlDt xhtmlDt = new XhtmlDt();
            xhtmlDt.setValueAsString(this.parseFreeText.getFreeText());
            narrativeDt.setDiv(xhtmlDt);

            comSection.setText(narrativeDt);
        }

        return comSection;
    }

    /**
     * <p>Set the member variable with the ArrayList, that contains all UKF strings</p>
     * @param UKFStringArrayList expect an ArrayList, that contains alle UKF strings
     */
    protected void setUKFStringArrayList(ArrayList<Document> UKFStringArrayList) {
        this.ukfStringArrayList = UKFStringArrayList;
    }
}
