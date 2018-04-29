package guifx;

import init.Init;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import medicationplaninterpreter.*;
import help.Help;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class PatientController {

    @FXML
    Label lblPatientTitle;
    @FXML
    Label lblAuthorTitle;

    @FXML
    Label lblPatientName;
    @FXML
    Label lblPatientNameValue;
    @FXML
    Label lblPatientBirthday;
    @FXML
    Label lblPatientBirthdayValue;

    @FXML
    Label lblPatientGender;
    @FXML
    Label lblPatientGenderValue;
    @FXML
    Label lblPatientWeightHeight;
    @FXML
    Label lblPatientWeightHeightValue;
    @FXML
    Label lblPatientMore;
    @FXML
    Label lblPatientMoreValue;
    @FXML
    Label lblPatientAllergies;
    @FXML
    Label lblPatientAllergiesValue;

    @FXML
    Label lblAuthorName;
    @FXML
    Label lblAuthorNameValue;
    @FXML
    Label lblAuthorAddress;
    @FXML
    Label lblAuthorAddressValue;
    @FXML
    Label lblAuthorPhone;
    @FXML
    Label lblAuthorPhoneValue;
    @FXML
    Label lblAuthorMail;
    @FXML
    Label lblAuthorMailValue;

    @FXML
    Label lblPatientHeader;
    @FXML
    Label lblPatientDate;

    @FXML
    Button btnClosePatient;

    /**
     * <p>Initializes the patient stage.</p>
     * <p>This stage is only for presentation purposes, no interaction with the user is required.</p>
     * @param s The patient stage where the following elements are bound to.
     */
    public void initializePatient(Stage s) {

        BMPInterpreter BMPInterpreter = new BMPInterpreter();

        // Header text
        lblPatientHeader.setText("Allgemeine Informationen");


        // Headers
        lblPatientTitle.setText("Patient");
        lblAuthorTitle.setText("Arzt/Praxis");


        // Name and surname
        Patient patientInfo = BMPInterpreter.getPatient();
        lblPatientName.setText("Name:");
        lblPatientNameValue.setText((patientInfo.getName()) + (patientInfo.getSurname() != null ? " " + patientInfo.getSurname() : ""));
        lblPatientBirthday.setText("Geburtsdatum:");


        // Birthday
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        sdf.setTimeZone(util.Date.timeZone);
        try {
            Date convertedBirthday;
            convertedBirthday = sdf.parse(patientInfo.getBirthday());
            SimpleDateFormat sdFBirthday = new SimpleDateFormat("dd. MMMM YYYY");
            lblPatientBirthdayValue.setText(sdFBirthday.format(convertedBirthday));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (util.String.isEmpty(lblPatientBirthdayValue.getText())) {
            lblPatientBirthdayValue.setText(MainWindow.sLblNoData);
        }
        lblPatientBirthdayValue.setWrapText(true);


        // Gender
        lblPatientGender.setText("Geschlecht:");
        lblPatientGender.setWrapText(true);
        lblPatientGenderValue.setText((!util.String.isEmpty(patientInfo.getGender()) ? patientInfo.getGender(): ""));
        if (util.String.isEmpty(lblPatientGenderValue.getText())) {
            lblPatientGenderValue.setText(MainWindow.sLblNoData);
        }
        lblPatientGenderValue.setWrapText(true);


        // Height and weight
        ClinicalParameter clinicalInfo = BMPInterpreter.getClinicalParameter();
        lblPatientWeightHeight.setText("Körperwerte:");
        lblPatientWeightHeightValue.setText(
                (!util.String.isEmpty(clinicalInfo.getSize()) ? clinicalInfo.getSize() : "")
                        + ((!util.String.isEmpty(clinicalInfo.getSize()) && !util.String.isEmpty(clinicalInfo.getWeight())) ? ", ": "")
                + (!util.String.isEmpty(clinicalInfo.getWeight()) ?  clinicalInfo.getWeight() : ""));
        if (util.String.isEmpty(lblPatientWeightHeightValue.getText())) {
            lblPatientWeightHeightValue.setText(MainWindow.sLblNoData);
        }
        lblPatientWeightHeightValue.setWrapText(true);


        // Allergies
        AllergyIntolerance allergyInfo = BMPInterpreter.getAllergyIntoleranceInformation();
        lblPatientAllergies.setText("Allergien und Unverträglichkeiten:");
        lblPatientAllergies.setWrapText(true);

        String sAllergies = "";
        if (allergyInfo != null && allergyInfo.getLength() > 0) {
            for (int i = 0; i < allergyInfo.getAllergyName().length; i++) {
                if (!util.String.isEmpty(allergyInfo.getAllergyName()[i])){
                    sAllergies = sAllergies + allergyInfo.getAllergyName()[i] + " [" + allergyInfo.getAllergyType()[i] + "]\n";
                }
            }
        }
        lblPatientAllergiesValue.setText(!util.String.isEmpty(sAllergies) ? sAllergies : "keine");
        lblPatientAllergiesValue.setWrapText(true);


        // Pregnany, brustfeeding
        HealthConcerns furtherInfo = BMPInterpreter.getHealthConcernsInformation();
        lblPatientMore.setText("Weiteres:");

        String sFurtherInfo = "";
        if (furtherInfo.isbBreastfeeding()) {
            sFurtherInfo = "Stillend";
        }
        if (furtherInfo.isbPregnancyStatus()) {
            sFurtherInfo = sFurtherInfo + ", schwanger";
        }
        lblPatientMoreValue.setText(!util.String.isEmpty(sFurtherInfo) ? sFurtherInfo : MainWindow.sLblNoData);
        lblPatientMoreValue.setWrapText(true);


        // Doctor / author
        Author author = BMPInterpreter.getAuthorInformation();
        lblAuthorName.setText("Name + Titel:");
        lblAuthorNameValue.setText(author.getNameOfExhibitor());
        lblAuthorNameValue.setWrapText(true);

        lblAuthorAddress.setText("Adresse:");
        lblAuthorAddressValue.setText(author.getStreet() + "\n" + author.getPostCode() + " " + author.getCity());
        lblAuthorAddressValue.setWrapText(true);

        lblAuthorPhone.setText("Telefon:");
        lblAuthorPhoneValue.setText(author.getTelephoneNumber());
        lblAuthorPhoneValue.setWrapText(true);

        lblAuthorMail.setText("E-Mail:");
        lblAuthorMailValue.setText(author.geteMailAdress());
        lblAuthorMailValue.setWrapText(true);

        // Header label (right)
        Date convDate = util.Date.convertStringToDate(author.getCreationDate());
        lblPatientDate.setText("Ausstelldatum: ");
        if (convDate == null){
            lblPatientDate.setText(lblPatientDate.getText() + author.getCreationDate());
        }
        else{
            lblPatientDate.setText(lblPatientDate.getText() + convDate.toString());
        }

        // Add the date of the last scan
        if (Init.persistenceXMLObject.getCreationDate() != null){
            lblPatientDate.setText(lblPatientDate.getText() + "\nScandatum: " + Init.persistenceXMLObject.getCreationDate());
        }

        btnClosePatient.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                s.close();
            }
        });

        }
    }

