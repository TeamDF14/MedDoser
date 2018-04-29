package guifx;

import enums.eIngestedStatus;
import enums.eIngestionTime;
import init.Init;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import persistenceXML.Persistence;
import help.Help;

import java.util.*;

public class IngestionController {

    Stage parentStage;
    Stage ingestionTimeStage;

    Persistence p;
    MainWindow mainApp;
    ArrayList<MedicationFX> lMedFX;
    Date selectedDate;
    eIngestionTime eTime;

    Date dateFromPersistence;

    private final double colIngestionTimeNamePart = 0.746;
    private final double colIngestionTimeAmountPart = 0.25;

    private final String sLblSubmitSingle = "Einnahme bestätigen";
    private final String sLblSubmitMultiple = "Einnahmen bestätigen";

    private final String sBtnDeclineSingle = "Nicht eingenommen";
    private final String sBtnDeclineMultiple = "Alle nicht eingenommen";

    private final String sBtnSubmitSingle = "Speichern";
    private final String sBtnSubmitMultiple = "Alle speichern";

    @FXML
    public TableColumn colNameIngestionTime;
    @FXML
    public TableColumn colAmountIngestionTime;


    @FXML
    public Button btnDeclineIngestion;
    @FXML
    public Button btnCloseIngestionTime;
    @FXML
    public Button btnSaveIngestionTime;



    @FXML
    public Slider sldHours;
    @FXML
    public Slider sldMinutes;

    @FXML
    public Label lblIngestionTimeHeader;
    @FXML
    public Label lblTimeOfUser;

    @FXML
    public TableView tViewIngestionTime;


    /**
     * Closes the ingestion time stage.
     */
    public void closeIngestionTime(){
        ingestionTimeStage.close();
    }


    /**
     * The constructor of the class binds the given MainWindow instance to a local variable.
     * @param mainApp An instance of the class 'MainWindow'.
     */
    public IngestionController(MainWindow mainApp) {
        this.mainApp = mainApp;
    }

    /**
     * This default constructor is necessary in order to create the stage!
     */
    public IngestionController(){

    }


    /**
     * <p>Initializes the ingestion time stage. On this stage, the user can submit or decline ingestions.</p>
     * <p>The number of ingestions shown in the tableView depends on the kind of button the user has pressed before on the primary stage.</p>
     * <p>When the user changes the state of an ingestion, the changes ware written to the persistence file.</p>
     * @param p An instance of the persistence controller.
     * @param mainApp An instance of the class 'MainWindow'.
     * @param parentStage The parent stage (where the user comes from), hence either the primary or the history stage.
     * @param ingestionTimeStage The stage where the following elements are bound to.
     * @param lMedFX A list of the medications that will be shown in the tableView.
     * @param selectedDate The date of the planned ingestions, independent from the ingestion time.
     * @param eTime The ingestion time of the medications, e.g. PCM.
     */
    public void initializeIngestionTime(Persistence p, MainWindow mainApp, Stage parentStage, Stage ingestionTimeStage, ArrayList<MedicationFX> lMedFX, Date selectedDate, eIngestionTime eTime){
        this.mainApp = mainApp;
        this.parentStage = parentStage;
        this.ingestionTimeStage = ingestionTimeStage;
        this.lMedFX = lMedFX;
        this.p = p;
        this.selectedDate = selectedDate;
        this.eTime = eTime;

        // Change text of heading label and Submit + Decline button, depending on the amount of chosen medications.
        if (lMedFX.size() < 2){
            lblIngestionTimeHeader.setText("1 " + sLblSubmitSingle);
            btnDeclineIngestion.setText(sBtnDeclineSingle);
            btnSaveIngestionTime.setText(sBtnSubmitSingle);
        }
        else{
            lblIngestionTimeHeader.setText(lMedFX.size() + " " +  sLblSubmitMultiple);
            btnDeclineIngestion.setText(sBtnDeclineMultiple);
            btnSaveIngestionTime.setText(sBtnSubmitMultiple);
        }


        // Bind stored ingestion times to both sliders and to the label above them
        Calendar cal1 = new GregorianCalendar();
        cal1.setTimeZone(util.Date.timeZone);

        switch (eTime){
            case MORNING:
                dateFromPersistence = Init.persistenceXMLObject.getIngestionTimeMorning();
                lblIngestionTimeHeader.setText(lblIngestionTimeHeader.getText() + " : " + MainWindow.sLblMorning);
                break;
            case MIDDAY:
                dateFromPersistence = Init.persistenceXMLObject.getIngestionTimeMidday();
                lblIngestionTimeHeader.setText(lblIngestionTimeHeader.getText() + " : " + MainWindow.sLblMidday);
                break;
            case EVENING:
                dateFromPersistence = Init.persistenceXMLObject.getIngestionTimeEvening();
                lblIngestionTimeHeader.setText(lblIngestionTimeHeader.getText() + " : " + MainWindow.sLblEvening);
                break;
            case NIGHT:
                dateFromPersistence = Init.persistenceXMLObject.getIngestionTimeNight();
                lblIngestionTimeHeader.setText(lblIngestionTimeHeader.getText() + " : " + MainWindow.sLblNight);
                break;
            case UNDEFINED:
                dateFromPersistence = new Date();
                lblIngestionTimeHeader.setText(lblIngestionTimeHeader.getText() + " : " + MainWindow.sLblUndefined);
                break;
            default:
                dateFromPersistence = new Date();
                break;
        }

        cal1.setTime(dateFromPersistence);
        sldHours.setValue(cal1.get(Calendar.HOUR_OF_DAY));
        sldMinutes.setValue(cal1.get(Calendar.MINUTE));
        lblTimeOfUser.setText((MainWindow.decimalFormat.format(cal1.get(Calendar.HOUR_OF_DAY)) + ":" + (MainWindow.decimalFormat.format(cal1.get(Calendar.MINUTE)) +  " Uhr")));

        // Set the min and max values of the hour slider, depending on the ingestion time.
        switch (eTime){

            case MORNING:
                sldHours.setMin(0.0);
                sldHours.setMax(10.0);
                break;
            case MIDDAY:
                sldHours.setMin(11.0);
                sldHours.setMax(14.0);
                break;
            case EVENING:
                sldHours.setMin(15.0);
                sldHours.setMax(19.0);
                break;
            case NIGHT:
                sldHours.setMin(20.0);
                sldHours.setMax(23.0);
                break;
            case UNDEFINED:
                break;
        }

        // Set focus on hour slider
        sldHours.requestFocus();

        // Setup tViewIngestionTime
        setCellValueFactory(eTime);
        fillTableView();

        btnDeclineIngestion.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                updateIngestion(eIngestedStatus.DECLINED, new Date());

                // We need an EXTERNAL close request in order to get the onCloseListener in the calling class activated
                ingestionTimeStage.fireEvent(
                        new WindowEvent(
                                ingestionTimeStage,
                                WindowEvent.WINDOW_CLOSE_REQUEST
                        )
                );

            }
        });

        btnCloseIngestionTime.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {

                // Do nothing but closing the stage. We need no external close listener here because no data is updated.
                closeIngestionTime();
            }
        });

        btnSaveIngestionTime.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {

                // Get the hours and minutes from the sliders
                Calendar cal = new GregorianCalendar();
                cal.setTimeZone(util.Date.timeZone);

                cal.set(Calendar.HOUR_OF_DAY, (int) sldHours.getValue());
                cal.set(Calendar.MINUTE, (int) sldMinutes.getValue());
                Date timeDefinedByUser = cal.getTime();

                updateIngestion(eIngestedStatus.INGESTED, timeDefinedByUser);

                // We need an EXTERNAL close request in order to get the onCloseListener in the calling class activated
                ingestionTimeStage.fireEvent(
                        new WindowEvent(
                                ingestionTimeStage,
                                WindowEvent.WINDOW_CLOSE_REQUEST
                        )
                );

            }
        });

        // Update label text when slider values changes
        sldHours.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {

                lblTimeOfUser.setText(MainWindow.decimalFormat.format(newValue) + ":" + MainWindow.decimalFormat.format(sldMinutes.getValue()) + " Uhr");
            }
        });
        // Update label text when slider values changes
        sldMinutes.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {

                lblTimeOfUser.setText(MainWindow.decimalFormat.format(sldHours.getValue()) + ":" + MainWindow.decimalFormat.format(newValue) + " Uhr");
            }
        });

        // Round double value to an integer value on value changing
        sldHours.valueChangingProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                sldHours.setValue((int) sldHours.getValue());
            }
        });
        // Round double value to an integer value on value changing
        sldMinutes.valueChangingProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                sldMinutes.setValue((int) sldMinutes.getValue());
            }
        });

    }

    /**
     * Set a cellValueFactory to all columns of the ingestionTime table.
     */
    private void setCellValueFactory(eIngestionTime eTime){

        // Name column
        colNameIngestionTime.setCellValueFactory(new PropertyValueFactory<MedicationFX,String>("tradeName"));
        colNameIngestionTime.prefWidthProperty().bind(tViewIngestionTime.widthProperty().multiply(colIngestionTimeNamePart));
        // Set wrapping property
        colNameIngestionTime.setCellFactory(randomID -> {return MedicationOverviewController.getWrappedCell(colNameIngestionTime);});


        // Amount column
        if (eTime == eIngestionTime.UNDEFINED){
            colAmountIngestionTime.setCellValueFactory(new PropertyValueFactory<MedicationFX,String>("dosageFreeText"));
        }
        else{
            colAmountIngestionTime.setCellValueFactory(new PropertyValueFactory<MedicationFX,String>("dosageInformationFX"));
        }
        colAmountIngestionTime.prefWidthProperty().bind(tViewIngestionTime.widthProperty().multiply(colIngestionTimeAmountPart));
        // Set wrapping property
        colAmountIngestionTime.setCellFactory(randomID -> {return MedicationOverviewController.getWrappedCell(colAmountIngestionTime);});
    }

    /**
     * <p>This method fills the table view as the medications (chosen from the user) are bound to the tableView.</p>
     */
    public void fillTableView() {

        Task<List<MedicationFX>> task = new Task<List<MedicationFX>>() {
            @Override
            protected List<MedicationFX> call()  {
                return lMedFX;
            }

            @Override
            protected void succeeded() {
                tViewIngestionTime.getItems().addAll(getValue());
            }
        };

        // Now do the work
        new Thread(task).start();
    }


    /**
     * <p>Updates the state of an ingestion, depending on the status that the user has chosen.</p>
     * <p>If the ingestion time of the medication(s) is set to UNDEFINED in the variable passed to the 'initialize' method, a new entry is created. Otherwise, the existing entry is updated.</p>
     * <p>Therefore, the data within the persistence file is updated. Implicitly, the global xml persistence object in class 'Init' is updated because it already exists.</p>
     * @param eStatusFromUser The decision of the user whether to submit or decline an ingestion.
     * @param timeDefinedByUser The time when the ingestion was done, provided by the user.
     */
    private void updateIngestion(eIngestedStatus eStatusFromUser, Date timeDefinedByUser){

        // Create a new entry in the persistence file
        if (eTime == eIngestionTime.UNDEFINED) {

            for (MedicationFX medFX : lMedFX){

                p.writeMedicationIngestion(MainWindow.getNewPersistenceEntry(medFX, selectedDate, dateFromPersistence, timeDefinedByUser, eTime, eStatusFromUser));
            }
        }

        // Write changes to persistence file
        else{

            // Prepare the ingestion that will be updated
            for (MedicationFX medFX : lMedFX){

                p.updateMedicationIngestionStatus(MainWindow.getNewPersistenceEntry(medFX, selectedDate, dateFromPersistence, timeDefinedByUser, eTime, eStatusFromUser));
            }
        }
    }


}
