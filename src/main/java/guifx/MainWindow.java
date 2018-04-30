package guifx;

import enums.eIngestedStatus;
import enums.eIngestionTime;
import init.Init;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import javafx.scene.Scene;
import medicationplaninterpreter.*;
import persistenceXML.Persistence;
import persistenceXML.PersistenceMedIngObject;
import help.Help;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * This class starts the UI on the primaryStage.
 */
public class MainWindow extends Application {

    ArrayList<Section> sections;
    MetaInformation metaInformation;

    Stage primaryStage;

    // The global column widths
    static final double colNamePart = 0.38;
    static final double colAmountPart = 0.12;
    static final double colReasonPart = 0.346;
    static final double colButtonPart = 0.15;
    static final int numberOfColumns = 4;

    static final String sLblNoData = "Keine Angaben";
    static final String sLblPlaceholder = "Keine Einnahmen ausstehend";
    static final String sTViewPlaceholder = "Noch keine Einnahmen vorhanden";

    static final String sBtnHistory = "Verlauf";
    static final String sBtnRescan = "Neu scannen";
    static final String sBtnShowPatient = "Patient";
    static final String sBtnEditTimes = "Zeiten anpassen";
    static final String sLblEditTimesHeader = "Einnahmezeiten anpassen";

    static final String sBtnAllDone = "Alles erledigt";
    static final String sBtnSubmitAll = "Alle bestätigen";
    static final String sBtnSubmitAgain = "Alle erneut bestätigen";
    static final String sBtnStatusNotAvailable = "Nicht verf.";
    static final String sBtnStatusNotAvailableLong = "Noch nicht verfügbar";

    // These hex colors are derived from the medDoser logo
    static final String sBgColorBtnDefault = " -fx-background-color: #cfd2d6;";
    static final String sBgColorBtnIngested = " -fx-background-color: #09c41f;";
    static final String sBgColorBtnDeclined = " -fx-background-color: lightcoral;";
    static final String sBorderColorBtnDefault = " -fx-border-color: #00568c;";
    static final String sBtnSubmit = "Bestätigen";
    static final String sBtnDecline = "Ablehnen";

    static final String sLblHours = "Stunden";
    static final String sLblMinutes = "Minuten";

    static final String sBtnStatusIngested = "Eingenommen";
    static final String sBtnStatusDeclined = "Nicht eingen.";

    static final String sBtnScanNow = "Hier drücken, um zu scannen";
    static final String sBtnScanInProgress = "Warte auf Eingabe";
    static final String sBgColorBtnScanInProgress = " -fx-background-color: yellow";
    static final String sBtnScanCompleted = "Scan erfolgreich";

    static final String sTabUdef = "Besondere Zeiten";
    static final String sLblUndefined ="Unregelmäßig";
    static final String sTabTime = " Uhr";

    static final String sLblMorning = "Morgens";
    static final String sLblMidday = "Mittags";
    static final String sLblEvening = "Abends";
    static final String sLblNight = "Zur Nacht";

    static final String sLblWeek = "Kalenderwoche";

    static final DecimalFormat decimalFormat = new DecimalFormat("#00");

    /**
     * All the non-static stuff gets initialized here
     */
    @Override public void init() {
        BMPInterpreter medInt = new BMPInterpreter();
        sections = medInt.getSections();
        metaInformation = medInt.getMetaInformation();

        try {
            if (primaryStage != null) {
                start(primaryStage);

            }
        }
        catch (Exception e){
            // sth to log
        }

    }

    /**
     *
     * @param primaryStage
     * @throws IOException
     */
    @Override
    public void start(Stage primaryStage) throws IOException {

        this.primaryStage = primaryStage;

        // Prepare the primary stage (will be shown later).
        preparePrimaryStage();

        // If there is no FHIR file already, prompt the user to scan a medicationPlan.
        if (!util.FileSystem.bCheckFileExists(Init.FHIRFile)){

            Stage rescanStage;
            FXMLLoader fxmlLoaderHistory = new FXMLLoader(getClass().getResource("scan.fxml"));
            Parent scanWindow = fxmlLoaderHistory.load();
            ScanController rc = fxmlLoaderHistory.getController();

            // Initialize the stage and fill its scene with the content of the fxml definition (defined above)
            rescanStage = new Stage();
            rescanStage.setScene(new Scene(scanWindow));

            // Set the title of the stage
            rescanStage.setTitle("Medikationsplan einscannen");

            // Set window size to fullscreen
            rescanStage.setMaximized(true);
            // Comment this in when testing under Windows
            //historyStage.setResizable(false);

            // Specifies the owner Window (parent) for new window
            rescanStage.initOwner(primaryStage);

            // Initialize the stage
            rc.initializeScan( this, primaryStage, rescanStage);

            // Show the stage
            rescanStage.show();
        }
        else{
            primaryStage.show();
        }

    }

    private static MedicationOverviewController c;

    /**
     * <p>This function prepares the primary stage, <b>without</b> showing it. It </p>
     * <ul>
     *     <li>loads the associated fxml description file</li>
     *     <li>sets a window title</li>
     *     <li>sets the stage to be maximized</li>
     *     <li>makes it non-resizable.</li>
     * </ul>
     * <p>The stage must be shown after calling this function.</p>
     */
    private void preparePrimaryStage(){
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("meddoser.fxml"));
        Parent root = null;
        try {
            root = fxmlLoader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Interrupt the old task
        if (c != null && c.updateThread != null && c.updateThread.isAlive()){
            c.updateThread.interrupt();
        }

        c = fxmlLoader.getController();
        c.setMainApp(this, primaryStage);

        // Set window size to fullscreen
        primaryStage.setMaximized(true);
        primaryStage.setResizable(false);

        primaryStage.setTitle("MedDoserFX");

        if (root != null){
            primaryStage.setScene(new Scene(root));
        }

    }
    /**
     *
     * @param args
     */
    public static void main(String[] args) {

        Init.initialize();

        launch(args);
    }


    /**
     * Sets the style of the button.
     * @param btn The button object to be styled.
     * @param eStatus The status of the button in order to determine the button style.
     * @param eTime The ingestion time of the table that the button contains.
     * @param ingestedTime The time when the medication was ingested
     */
    public void setButtonStyle(Button btn, eIngestedStatus eStatus, eIngestionTime eTime, Date ingestedTime, Date day){

        Calendar cal = new GregorianCalendar();
        if (ingestedTime != null){
            cal.setTimeZone(util.Date.timeZone);
            cal.setTime(ingestedTime);
        }

        switch (eStatus){
            case DEFAULT:
                btn.setText(sBtnSubmit);

                if ((eTime != eIngestionTime.UNDEFINED)){

                    // Disable the button, if the dayOfYear of the ingestedTime is equal to today and the desired time is in the future
                    boolean hasTimeReleased = Help.bHasIngestionTimeReleased(eTime);
                    if (!hasTimeReleased && util.Date.bIsDayEqual(day, new Date())){
                        btn.setDisable(true);
                        btn.setText(MainWindow.sBtnStatusNotAvailable);
                    }
                    else{
                        btn.setDisable(false);
                        btn.setText(MainWindow.sBtnSubmit);
                    }

                }
                btn.setStyle(null); // reset the button style
                btn.setStyle(sBorderColorBtnDefault); // add border color
                break;

            case INGESTED:
                // Write the ingested time instead of a default text
                btn.setText((ingestedTime != null) ? (MainWindow.decimalFormat.format(cal.get(Calendar.HOUR_OF_DAY)) + ":" + MainWindow.decimalFormat.format(cal.get(Calendar.MINUTE)) + " Uhr") : sBtnStatusIngested);
                btn.setDisable(true);
                btn.setStyle(sBgColorBtnIngested);
                break;

            case DECLINED:
                btn.setText(sBtnStatusDeclined);
                btn.setDisable(true);
                btn.setStyle(sBgColorBtnDeclined);
                break;
        }
    }

    /**
     * Creates a new stage in order to submit the ingestions.
     * @param lMedFX A list containing all the medications to submit
     * @param eTime The time of the ingestions
     * @param selectedDate The date of the ingestion
     * @param p The persistence object
     * @param parentStage The parent stage object
     * @return A new stage object.
     */
    public Stage createIngestionTimeStage(ArrayList<MedicationFX> lMedFX, eIngestionTime eTime, Date selectedDate, Persistence p, Stage parentStage){

        Stage ingestionTimeStage = null;

        try {
            FXMLLoader fxmlLoaderIngestionTime = new FXMLLoader(getClass().getResource("ingestion.fxml"));
            Parent ingestionTimeWindow = fxmlLoaderIngestionTime.load();
            IngestionController itc = fxmlLoaderIngestionTime.getController();

            // Create a new stage and fill it with the content of the fxml definition (defined above)
            ingestionTimeStage = new Stage();
            ingestionTimeStage.setScene(new Scene(ingestionTimeWindow));
            ingestionTimeStage.setTitle("Einnahmezeit angeben");

            // Set window size to fullscreen
            ingestionTimeStage.setMaximized(true);
            //ingestionTimeStage.setResizable(false);

            // Specifies the modality for new window.
            //ingestionTimeStage.initModality(Modality.WINDOW_MODAL);

            // Specifies the owner Window (parent) for new window
            ingestionTimeStage.initOwner(parentStage);

            itc.initializeIngestionTime(p, this, parentStage, ingestionTimeStage, lMedFX, selectedDate, eTime);

            ingestionTimeStage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }

        return ingestionTimeStage;
    }

    /**
     * Prepares a new medication dataset in order to update the persistence file.
     * @param medFX
     * @param scheduledDate
     * @param eStatusFromUser
     * @return
     */
    public static PersistenceMedIngObject getNewPersistenceEntry(MedicationFX medFX, Date scheduledDate, Date dateFromPersistence, Date timeDefinedByUser, eIngestionTime eTime, eIngestedStatus eStatusFromUser){

        PersistenceMedIngObject po = new PersistenceMedIngObject();
        po.setMedStatementReference(medFX.getReferenceMedicationStatement());
        po.setScheduledDate(scheduledDate);
        po.setStatus(eStatusFromUser);
        po.setScheduledTimeCode(eTime);
        po.setScheduledTime(dateFromPersistence);
        po.setTime(timeDefinedByUser);

        return po;
    }


    /**
     * Creates a new MedicationFX object, based on the given data.
     *
     * @param m A medication object that contains information.
     * @param sectionFreeText The free text of the section of the medication, Leave this field empty if there is no free text available.
     * @param eStatus The status of the ingestion out of the persistence file.
     * @param ingestedTime The time when the medication was ingested. Pass null if the medication wasn't ingested already.
     * @param fromDate The date the ingestion was scheduled for. This is specially relevant for medications
     * @param eTime The ingestion time. Depending on it, the information of this time is fetched from the medication object 'm'.
     * @return A new object, filled with the given data.
     */
    public MedicationFX getNewMedicationFX(Medication m, String sectionFreeText, eIngestedStatus eStatus, Date ingestedTime, Date fromDate, eIngestionTime eTime){

        MedicationFX medFX = new MedicationFX();

        medFX.setIngestedStatus(eStatus);
        medFX.setIngestedTime(ingestedTime);

        medFX.setTradeName(m.getTradeName());
        medFX.setReason(m.getReason());
        medFX.setNote(m.getNote());
        medFX.setStatus(m.getStatus());
        medFX.setPharmacyCentralNumber(m.getPharmacyCentralNumber());
        medFX.setActiveSubstance(m.getActiveSubstance());
        medFX.setAssertedDate(m.getAssertedDate());
        medFX.setDrugFormCode(m.getDrugFormCode());
        medFX.setDrugFormName(m.getDrugFormName());

        medFX.setSectionTitle(m.getSectionTitle());
        medFX.setSectionFreeText(sectionFreeText);

        ArrayList<IngestionTime> medTimes = m.getIngestionTimes();

        int eTimeIndex;
        switch (eTime){
            case MORNING:
                eTimeIndex = 0;
                break;
            case MIDDAY:
                eTimeIndex = 1;
                break;
            case EVENING:
                eTimeIndex = 2;
                break;
            case NIGHT:
                eTimeIndex = 3;
                break;
            case UNDEFINED:
                eTimeIndex = 0;
                break;
            default:
                eTimeIndex = 0;
                break;
        }

        medFX.setReferenceMedicationStatement(medTimes.get(eTimeIndex).getReferenceMedicationStatement());
        medFX.setReferenceToMedication(medTimes.get(eTimeIndex).getReferenceToMedication());
        medFX.setDosageValue(medTimes.get(eTimeIndex).getDosageValue());
        medFX.setDosageUnit(medTimes.get(eTimeIndex).getDosageUnit());

        medFX.setDosageInformationFX(medTimes.get(eTimeIndex).getDosageValue() + " " + medTimes.get(eTimeIndex).getDosageUnit());
        medFX.setDosageFreeText(medTimes.get(eTimeIndex).getDosageFreeText());
        medFX.setTimeOfDosage(medTimes.get(eTimeIndex).getTimeOfDosage());
        medFX.setIngestionTimes(medTimes);


        // Add the ingested times of a UNDEFINED medication to the object
        if (eTime == eIngestionTime.UNDEFINED){
            ArrayList<Date> lIngestedTimes = new ArrayList<>();
            ArrayList<PersistenceMedIngObject> l = Init.persistenceXMLObject.getMedicationIngestion(fromDate);

            for (PersistenceMedIngObject o : l){
                if (o.getScheduledTimeCode() == eIngestionTime.UNDEFINED
                        && o.getMedStatementReference().equals(medFX.getReferenceMedicationStatement())){
                    lIngestedTimes.add(o.getTime());
                }
            }
            medFX.setIngestedTimesUdef(lIngestedTimes);
        }

        return medFX;
    }


    /**
     * <p>Collects the desired MedicationFX data, which is consisting of medication ingestions.</p>
     * <p>Therefore, the following data is merged:</p>
     * <ol>
     *     <li>The ingestionTimes from the FHIR file</li>
     *     <li>The status of the ingestions from the persistence file</li>
     * </ol>
     *
     * @param eTime The ingestion time that defines which data should be loaded, e.g. 'PCM' for the PCM tableView.
     * @param fromDate The date that specifies from which date the ingestions should be.
     * @param forHistoryView True if the data is requested for the history view (All stati but Default), false if not (Status = Default).
     * @param onlyForToday True if the data is only requested for the current day, false if not.
     *
     * @return A List containing the requested data.
     */
    public ObservableList<MedicationFX> getData(final eIngestionTime eTime, final Date fromDate, final boolean forHistoryView, final boolean onlyForToday) {

        ObservableList<MedicationFX> list = FXCollections.observableArrayList();
        ArrayList<PersistenceMedIngObject> medicationStati;
        medicationStati = Init.persistenceXMLObject.getMedicationIngestion(fromDate);

        // Get the skipped elements which are not stored in the persistence file
        // (medications to apply on special time)
        if (eTime == eIngestionTime.UNDEFINED){
            for (int s = 0; s < sections.size(); s++){

                ArrayList<Medication> lMedication = sections.get(s).getMedications();

                for (Medication m : lMedication){

                    ArrayList<IngestionTime> lIngTimes = m.getIngestionTimes();

                    if ((lIngTimes.size() < 2) || Help.isAllSetTo0(lIngTimes)){

                        // Add sectionTitle to medication
                        m.setSectionTitle(sections.get(s).getTitle());

                        // Add default String to reason
                        if (m.getReason() == null || m.getReason().isEmpty()){
                            m.setReason("-");
                        }

                        String freeText = "";
                        if (!util.String.isEmpty(sections.get(s).getFreeText())){
                            freeText = sections.get(s).getFreeText();
                        }

                        list.add(getNewMedicationFX(m, freeText, eIngestedStatus.DEFAULT, null, fromDate, eTime));
                    }
                }
            }

        }
        else {

            for (int y = 0; y < medicationStati.size(); y++) {

                // The ones for the current tab (e.g. tab id = 1, eTime = MORNING, 08:00 h)
                if (medicationStati.get(y).getScheduledTimeCode() == eTime) {

                    // Only add medications to the list if the condition is fulfilled.
                    if ((!forHistoryView && (medicationStati.get(y).getStatus() == eIngestedStatus.DEFAULT))
                            || (forHistoryView && onlyForToday && (medicationStati.get(y).getStatus() != eIngestedStatus.DEFAULT))
                            || (forHistoryView && !onlyForToday)) {

                        Medication m = null;
                        int sectionID = -1;
                        for (int i = 0; i < sections.size(); i++) {

                            m = Help.getMedication(sections.get(i).getMedications(), medicationStati.get(y).getMedStatementReference());
                            if (m != null) {

                                // Add sectionTitle to medication
                                m.setSectionTitle(sections.get(i).getTitle());

                                // Add default String to reason
                                if (m.getReason() == null || m.getReason().isEmpty()){
                                    m.setReason("-");
                                }

                                sectionID = i;
                                break;
                            }
                        }

                        String freeText = "";
                        if (sectionID > -1 && !util.String.isEmpty(sections.get(sectionID).getFreeText())){
                            freeText = sections.get(sectionID).getFreeText();
                        }
                        list.add(getNewMedicationFX(m, freeText, medicationStati.get(y).getStatus(), medicationStati.get(y).getTime(),fromDate, eTime));
                    }
                }
            }
        }

        return list;
    }

}
