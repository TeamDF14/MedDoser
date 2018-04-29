package guifx;

import enums.eIngestionTime;
import init.Init;
import javafx.application.Platform;
import javafx.beans.property.ObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.util.Callback;
import persistenceXML.Persistence;
import threads.SoundTask;
import help.Help;

import java.io.IOException;
import java.util.*;
import java.util.List;

public class MedicationOverviewController {

    Stage primaryStage;
    Persistence p;

    @FXML // Reference to the main application.
    private MainWindow mainApp;

    @FXML
    public TableView tviewPCM;
    @FXML
    public TableView tviewPCD;
    @FXML
    public TableView tviewPCV;
    @FXML
    public TableView tviewHS;
    @FXML
    public TableView tviewUdef;


    @FXML
    public TabPane tabPane;


    @FXML
    public TableColumn<MedicationFX, String> colNamePCM;
    @FXML
    public TableColumn<MedicationFX, String> colNamePCD;
    @FXML
    public TableColumn<MedicationFX, String> colNamePCV;
    @FXML
    public TableColumn<MedicationFX, String> colNameHS;
    @FXML
    public TableColumn<MedicationFX, String> colNameUdef;

    @FXML
    public TableColumn<MedicationFX, String> colAmountPCM;
    @FXML
    public TableColumn<MedicationFX, String> colAmountPCD;
    @FXML
    public TableColumn<MedicationFX, String> colAmountPCV;
    @FXML
    public TableColumn<MedicationFX, String> colAmountHS;
    @FXML
    public TableColumn<MedicationFX, String> colAmountUdef;


    @FXML
    public TableColumn<MedicationFX, String> colReasonPCM;
    @FXML
    public TableColumn<MedicationFX, String> colReasonPCD;
    @FXML
    public TableColumn<MedicationFX, String> colReasonPCV;
    @FXML
    public TableColumn<MedicationFX, String> colReasonHS;
    @FXML
    public TableColumn<MedicationFX, String> colReasonUdef;

    @FXML
    public TableColumn colButtonPCM;
    @FXML
    public TableColumn colButtonPCD;
    @FXML
    public TableColumn colButtonPCV;
    @FXML
    public TableColumn colButtonHS;
    @FXML
    public TableColumn colButtonUdef;

    @FXML
    public Button btnHistory;
    @FXML
    public Button btnRescan;
    @FXML
    public Button btnShowPatient;
    @FXML
    public Button btnEditTimes;


    @FXML
    public Button btnSubmitAllPCM;
    @FXML
    public Button btnSubmitAllPCD;
    @FXML
    public Button btnSubmitAllPCV;
    @FXML
    public Button btnSubmitAllHS;
    @FXML
    public Button btnSubmitAllUdef;


    @FXML
    public Label lblCurrentDate;


    /**
     * The constructor is called before the initialize() method.
     */
    public MedicationOverviewController() {
    }

    /**
     * Global variable in order to interrupt the thread from outside.
     */
    Thread updateThread;

    /**
     * Initializes the controller class. This method is automatically called
     * after the fxml file has been loaded.
     */
    public void setMainApp(MainWindow mainApp, Stage primaryStage) {

        this.mainApp = mainApp;
        p = new Persistence();
        this.primaryStage = primaryStage;

        // Continue the thread: The thread is looking for changes on the data (the stati of the ingestions), depending on the reminderInterval
        updateThread = new Thread(getUpdateTask(Init.reminderInterval));
        updateThread.setDaemon(true);
        updateThread.start();

        refreshAllTables();

        // Set an onShown listener in order to refresh all tables instantly after opening the primary stage
        if (primaryStage != null){
            primaryStage.setOnShown(new EventHandler<WindowEvent>() {
                @Override
                public void handle(WindowEvent event) {

                    refreshAllTables();
                }
            });
        }
    }


    /**
     * Initializes the controller class. Because of the annotation, this method is automatically called after the fxml file has been loaded.
     */
    @FXML
    private void initialize() {

        // Define the columns of the tableViews and their content
        setCellValueFactory();

        // Set tab texts
        setTitledPaneText();

        // Set an onShown listener in order to refresh all tables instantly after opening the primary stage
        if (primaryStage != null){
            primaryStage.setOnShown(new EventHandler<WindowEvent>() {
                @Override
                public void handle(WindowEvent event) {

                    refreshAllTables();
                }
            });
        }

        // Thread: Set label text to current date and time every fice seconds
        Task taskDate = new Task<Void>() {
            @Override
            public Void call() throws Exception {
                while (true) {
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {

                            lblCurrentDate.setText(util.Date.convertDateToString(new Date(), false) + "h");
                        }
                    });
                    Thread.sleep(5000);
                }
            }
        };
        Thread threadDate = new Thread(taskDate);
        threadDate.setDaemon(true);
        threadDate.start();

        // Define a custom row factory for the medication note(s)
        setRowFactoryForNotes();

        // Highlight the currently selected tab
        tabPane.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Tab>() {
            @Override
            public void changed(ObservableValue<? extends Tab> observable, Tab oldValue, Tab newValue) {

                for (Tab t : tabPane.getTabs()){
                    if (t == tabPane.getSelectionModel().getSelectedItem()){
                        t.getStyleClass().add("activeTab");
                    }
                    else{
                        t.getStyleClass().removeIf(style -> style.equals("activeTab"));
                    }
                }
            }
        });

        // Set focus to the tab that represents the current ingestion time
        SingleSelectionModel<Tab> selectionModel = tabPane.getSelectionModel();
        eIngestionTime eTime = Help.getCurrentIngestionTime();
        selectionModel.select(eTime.ordinal());


        btnShowPatient.setText(MainWindow.sBtnShowPatient);
        btnShowPatient.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                try {
                    FXMLLoader fxmlLoaderPatient = new FXMLLoader(getClass().getResource("patient.fxml"));
                    Parent patientWindow = fxmlLoaderPatient.load();
                    PatientController pc = fxmlLoaderPatient.getController();


                    // Create a new stage and fill it with the content of the fxml definition (defined above)
                    Stage sp = new Stage();
                    sp.setScene(new Scene(patientWindow));

                    // Set window size to fullscreen
                    sp.setMaximized(true);
                    sp.setResizable(false);

                    // Specifies the modality for new window.
                    sp.initModality(Modality.WINDOW_MODAL);

                    // Specifies the owner Window (parent) for new window
                    sp.initOwner(primaryStage);

                    pc.initializePatient(sp);

                    sp.show();

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        btnEditTimes.setText(MainWindow.sBtnEditTimes);
        btnEditTimes.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                try {
                    FXMLLoader fxmlLoaderEditTimes = new FXMLLoader(getClass().getResource("times.fxml"));
                    Parent editTimesWindow = fxmlLoaderEditTimes.load();
                    TimesController tc = fxmlLoaderEditTimes.getController();

                    // Create a new stage and fill it with the content of the fxml definition (defined above)
                    Stage s = new Stage();
                    s.setScene(new Scene(editTimesWindow));

                    // Set window size to fullscreen
                    s.setMaximized(true);
                    s.setResizable(false);

                    // Specifies the modality for new window, if needed
                    //s.initModality(Modality.WINDOW_MODAL);

                    // Specifies the owner Window (parent) for new window
                    s.initOwner(primaryStage);

                    // Set a listener to the close event of the created stage in order to refresh the table instantly after updating the data.
                    s.setOnCloseRequest(new EventHandler<WindowEvent>() {
                        public void handle(WindowEvent we) {
                            refreshTitledPanes();
                            refreshAllTables();
                        }
                    });


                    tc.initializeEditTimes(p, s);

                    s.show();

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        // Set behaviour of the history button in mainWindow
        btnHistory.setText(MainWindow.sBtnHistory);
        btnHistory.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {

                try {
                    FXMLLoader fxmlLoaderHistory = new FXMLLoader(getClass().getResource("history.fxml"));
                    Parent historyWindow = fxmlLoaderHistory.load();
                    HistoryController c = fxmlLoaderHistory.getController();

                    // Create a new stage and fill it with the content of the fxml definition (defined above)
                    Stage historyStage = new Stage();
                    historyStage.setScene(new Scene(historyWindow));
                    historyStage.setTitle("Verlauf");

                    // Set window size to fullscreen
                    historyStage.setMaximized(true);
                    // ToDO: Forbid to resize the historyStage
                    //historyStage.setResizable(false);

                    // Specifies the modality for new window.
                    // ToDO: Make the historyStage modal
                    //historyStage.initModality(Modality.WINDOW_MODAL);

                    // Specifies the owner Window (parent) for new window
                    historyStage.initOwner(primaryStage);

                    c.initializeHistory(p, mainApp, primaryStage, historyStage);

                    historyStage.show();

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        btnRescan.setText(MainWindow.sBtnRescan);
        btnRescan.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {

                try {
                    FXMLLoader fxmlLoaderHistory = new FXMLLoader(getClass().getResource("scan.fxml"));
                    Parent scanWindow = fxmlLoaderHistory.load();
                    ScanController rc = fxmlLoaderHistory.getController();

                    // Create a new stage and fill it with the content of the fxml definition (defined above)
                    Stage rescanStage = new Stage();
                    rescanStage.setScene(new Scene(scanWindow));

                    // Set window size to fullscreen
                    rescanStage.setMaximized(true);
                    rescanStage.setResizable(false);

                    // Specifies the modality for new window.
                    rescanStage.initModality(Modality.WINDOW_MODAL);

                    // Specifies the owner Window (parent) for new window
                    rescanStage.initOwner(primaryStage);

                    rc.initializeScan(mainApp, primaryStage, rescanStage);

                    rescanStage.show();

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

    }


    /**
     * Sets the text of the tab headers depending on the stored ingestion times.
     */
    private void setTitledPaneText(){
        // Get all tabs
        ObservableList<Tab> tabs = tabPane.getTabs();

        // Check each tab
        for (eIngestionTime eTime : eIngestionTime.values()){

            switch (eTime) {
                case MORNING:
                    tabs.get(eTime.ordinal()).setText(Init.persistenceXMLObject.getIngestionTimeStringMorning() + MainWindow.sTabTime);
                    break;
                case MIDDAY:
                    tabs.get(eTime.ordinal()).setText(Init.persistenceXMLObject.getIngestionTimeStringMidday() + MainWindow.sTabTime);
                    break;
                case EVENING:
                    tabs.get(eTime.ordinal()).setText(Init.persistenceXMLObject.getIngestionTimeStringEvening() + MainWindow.sTabTime);
                    break;
                case NIGHT:
                    tabs.get(eTime.ordinal()).setText(Init.persistenceXMLObject.getIngestionTimeStringNight() + MainWindow.sTabTime);
                    break;
                case UNDEFINED:
                    tabs.get(eTime.ordinal()).setText(MainWindow.sTabUdef);
                    break;
            }

        }
    }

    /** <p>Highlights the titled pane in the accordion that has items that were not submitted yet. </p>
     * <p>Therefore, it sets its font size to a defined color.</p>
     */
    private boolean refreshTitledPanes(){

        // Get all tabs
        ObservableList<Tab> tabs = tabPane.getTabs();
        boolean hasToUpdateData = false;

        // Check each tab
        for (eIngestionTime eTime : eIngestionTime.values()){

            // Update the text of the tab if the time has changed (by the user)
            switch (eTime){

                case MORNING:
                    tabs.get(eTime.ordinal()).setText(Init.persistenceXMLObject.getIngestionTimeStringMorning());
                    break;
                case MIDDAY:
                    tabs.get(eTime.ordinal()).setText(Init.persistenceXMLObject.getIngestionTimeStringMidday());
                    break;
                case EVENING:
                    tabs.get(eTime.ordinal()).setText(Init.persistenceXMLObject.getIngestionTimeStringEvening());
                    break;
                case NIGHT:
                    tabs.get(eTime.ordinal()).setText(Init.persistenceXMLObject.getIngestionTimeStringNight());
                    break;
            }

            // Highlight the tab, depending on unsubmitted ingestions
            if (Help.bIsRemainingIngestion(eTime, new Date()) && Help.bHasIngestionTimeReleased(eTime)){
                tabs.get(eTime.ordinal()).getStyleClass().add("unsubmittedTab");
                hasToUpdateData = true;
            }
            else{
                tabs.get(eTime.ordinal()).getStyleClass().removeIf(style -> style.equals("unsubmittedTab"));
            }
        }

        // Refresh the data only if the ingestion time has reached another level
        return hasToUpdateData;
    }


    /**
     * <p>Check if there are unconfirmed medications in the past + today. </p>
     * <p>If yes: </p>
     * <ul>
     *  <li>Turn on the LED and play a sound track</li>
     * </ul>
     * <p>If no: </p>
     * <ul>
     * <li>Turn off the LED.</li>
     * </ul>
     * <p>This task can be executed periodically by a thread or task.</p>
     */
    private void updateLEDAndSound(){

        ArrayList<Date> dList = Help.getDateList();
        boolean hasToRemind = false;

        // Check each ingestion time
        for (eIngestionTime eTime : eIngestionTime.values()){

            // For each day in the past + today
            for (Date d : dList){

                if (Help.bIsRemainingIngestion(eTime, d)){

                    hasToRemind = true;
                    break;
                }
            }
        }


        // Trigger the LED stop blinking
        if (!hasToRemind){
            //try {
                Init.toggleGPIOStateInstance.toggleGPIOState(false);
            //} catch (InterruptedException e) {
             //   e.printStackTrace();
            //}
        }
        // Trigger the LED to blink and make some noise
        else{
            //try {
                Init.toggleGPIOStateInstance.toggleGPIOState(true);
            //} catch (InterruptedException e) {
            //    e.printStackTrace();
            //}

            // Start the sound task
            new SoundTask();
        }
    }


    /**
     * <p>Creates a new task. </p>
     * <p>For example: </p>
     * <ul>
     *     <li><b>period</b> is set to 10000, then the task runs every 10 seconds.</li>
     * </ul>
     * @param period The period that defines how often the task should run (in milliseconds).
     * @return A new task.
     */
    private Task getUpdateTask(final int period){
        Task taskTables = new Task<Void>() {
            @Override
            public Void call() throws Exception {
                while (true) {
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {

                            updateLEDAndSound();
                            refreshTitledPanes();
                        }

                    });
                    Thread.sleep(period);
                }
            }
        };
        return taskTables;
    }


    /**
     * Creates a custom cell that automatically adds a linebreak if the text content exceeds the column width
     * @param tCol The table column where the cell should be bound to.
     * @return The new table cell.
     */
    public static TableCell getWrappedCell(TableColumn tCol){
        TableCell<MedicationFX, String> cell = new TableCell<>();
        Text text = new Text();
        cell.setGraphic(text);
        cell.setPrefHeight(Control.USE_COMPUTED_SIZE);
        text.wrappingWidthProperty().bind(tCol.widthProperty());
        text.textProperty().bind(cell.itemProperty());
        return cell ;
    }


    /**
     * Set cellValueFactories to all columns of the tabPane.
     * @return True if successful, false if not.
     */
    private void setCellValueFactory(){

        ObservableList<TableView> ol_tViews = FXCollections.observableArrayList();
        ol_tViews.add(tviewPCM);
        ol_tViews.add(tviewPCD);
        ol_tViews.add(tviewPCV);
        ol_tViews.add(tviewHS);
        ol_tViews.add(tviewUdef);

        ObservableList<TableColumn<MedicationFX, String>> ol_colAmount = FXCollections.observableArrayList();
        ol_colAmount.add(colAmountPCM);
        ol_colAmount.add(colAmountPCD);
        ol_colAmount.add(colAmountPCV);
        ol_colAmount.add(colAmountHS);
        ol_colAmount.add(colAmountUdef);

        ObservableList<TableColumn<MedicationFX, String>> ol_colName = FXCollections.observableArrayList();
        ol_colName.add(colNamePCM);
        ol_colName.add(colNamePCD);
        ol_colName.add(colNamePCV);
        ol_colName.add(colNameHS);
        ol_colName.add(colNameUdef);

        ObservableList<TableColumn<MedicationFX, String>> ol_colReason = FXCollections.observableArrayList();
        ol_colReason.add(colReasonPCM);
        ol_colReason.add(colReasonPCD);
        ol_colReason.add(colReasonPCV);
        ol_colReason.add(colReasonHS);
        ol_colReason.add(colReasonUdef);


        for (TableColumn tc: ol_colName
             ) {
            tc.setCellValueFactory(new PropertyValueFactory<MedicationFX,String>("tradeName"));

            // Set wrapping property
            tc.setCellFactory(randomID -> {return getWrappedCell(tc);});

            for(TableView tv : ol_tViews){
                tc.prefWidthProperty().bind(tv.widthProperty().multiply(MainWindow.colNamePart));
            }
        }


        for (TableColumn tc: ol_colAmount
                ) {

            tc.setCellFactory(param -> new TableCell<MedicationFX, String>() {
                final Text txt = new Text();

                @Override
                public void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);


                    if (empty) {
                        setGraphic(null);
                        setText(null);
                    } else {

                        MedicationFX m = getTableView().getItems().get(getIndex());

                        if (tc == colAmountUdef){
                            txt.setText(m.getDosageFreeText() + " " + m.getDrugFormName());
                        }
                        else{
                            txt.setText(m.getDosageInformationFX() + " " + m.getDrugFormName());
                        }

                        txt.wrappingWidthProperty().bind(tc.widthProperty());

                        setGraphic(txt);
                        setText(null);
                    }
                }
            });

            // Set column width
            for (TableView tv : ol_tViews){
                tc.prefWidthProperty().bind(tv.widthProperty().multiply(MainWindow.colAmountPart));
            }
        }


        for (TableColumn tc: ol_colReason
                ) {
            tc.setCellValueFactory(new PropertyValueFactory<MedicationFX,String>("reason"));

            // Set wrapping property
            tc.setCellFactory(randomID -> {return getWrappedCell(tc);});

            for(TableView tv : ol_tViews){
                tc.prefWidthProperty().bind(tv.widthProperty().multiply(MainWindow.colReasonPart));
            }
        }

        colButtonPCM.prefWidthProperty().bind(tviewPCM.widthProperty().multiply(MainWindow.colButtonPart));
        colButtonPCD.prefWidthProperty().bind(tviewPCD.widthProperty().multiply(MainWindow.colButtonPart));
        colButtonPCV.prefWidthProperty().bind(tviewPCV.widthProperty().multiply(MainWindow.colButtonPart));
        colButtonHS.prefWidthProperty().bind(tviewHS.widthProperty().multiply(MainWindow.colButtonPart));
        colButtonUdef.prefWidthProperty().bind(tviewUdef.widthProperty().multiply(MainWindow.colButtonPart));

        colButtonPCM.setCellFactory(setButtonFactory(eIngestionTime.MORNING));
        colButtonPCD.setCellFactory(setButtonFactory(eIngestionTime.MIDDAY));
        colButtonPCV.setCellFactory(setButtonFactory(eIngestionTime.EVENING));
        colButtonHS.setCellFactory(setButtonFactory(eIngestionTime.NIGHT));
        colButtonUdef.setCellFactory(setButtonFactory(eIngestionTime.UNDEFINED));
    }


    /**
     * Refreshes the data of all tables in the primaryStage.
     */
    private void refreshAllTables(){

        refreshTable(eIngestionTime.MORNING);
        refreshTable(eIngestionTime.MIDDAY);
        refreshTable(eIngestionTime.EVENING);
        refreshTable(eIngestionTime.NIGHT);
        refreshTable(eIngestionTime.UNDEFINED);
    }

    /**
     * Set the row factory for the notes of a medication.
     * The content will be printed under the associated medication row.
     */
    public void setRowFactoryForNotes()  {

        tviewPCM.setRowFactory(tvm -> getNewRowFactory());
        tviewPCD.setRowFactory(tvd-> getNewRowFactory());
        tviewPCV.setRowFactory(tvv -> getNewRowFactory());
        tviewHS.setRowFactory(tvhs -> getNewRowFactory());
        tviewUdef.setRowFactory(tvudef -> getNewRowFactory());
    }


    /**
     * Refreshes the tableView that is associated with the given eTime
     * @param eTime The ingestion time of the tab / tableView
     */
    public void refreshTable(eIngestionTime eTime) {

        Task<List<MedicationFX>> task = new Task<List<MedicationFX>>() {
            @Override
            protected List<MedicationFX> call() {
                switch (eTime){
                    case MORNING:
                        return fetchData(eIngestionTime.MORNING);
                    case MIDDAY:
                        return fetchData(eIngestionTime.MIDDAY);
                    case EVENING:
                        return fetchData(eIngestionTime.EVENING);
                    case NIGHT:
                        return fetchData(eIngestionTime.NIGHT);
                    case UNDEFINED:
                        return fetchData(eIngestionTime.UNDEFINED);
                    default:
                        return null;
                }
            }

            @Override
            protected void succeeded() {
                // Clear the table and add the updated data
                // In addition, toggle the "SubmitAll" buttons depending if there are elements or not.

                boolean bHasReleased = Help.bHasIngestionTimeReleased(eTime);
                Label ph = new Label(MainWindow.sLblPlaceholder);

                switch (eTime){
                    case MORNING:
                        tviewPCM.getItems().clear();
                        tviewPCM.getItems().addAll( getValue() );
                        btnSubmitAllPCM.setDisable(getValue().isEmpty() || !bHasReleased);
                        btnSubmitAllPCM.setText(btnSubmitAllPCM.isDisabled() ? MainWindow.sBtnAllDone : MainWindow.sBtnSubmitAll);

                        if (!bHasReleased){
                            btnSubmitAllPCM.setText(MainWindow.sBtnStatusNotAvailableLong);
                        }
                        tviewPCM.setPlaceholder(ph);

                        break;
                    case MIDDAY:
                        tviewPCD.getItems().clear();
                        tviewPCD.getItems().addAll( getValue() );
                        btnSubmitAllPCD.setDisable(getValue().isEmpty() || !bHasReleased);
                        btnSubmitAllPCD.setText(btnSubmitAllPCD.isDisabled() ? MainWindow.sBtnAllDone : MainWindow.sBtnSubmitAll);

                        if (!bHasReleased){
                            btnSubmitAllPCD.setText(MainWindow.sBtnStatusNotAvailableLong);
                        }
                        tviewPCD.setPlaceholder(ph);

                        break;
                    case EVENING:
                        tviewPCV.getItems().clear();
                        tviewPCV.getItems().addAll( getValue() );

                        btnSubmitAllPCV.setDisable(getValue().isEmpty() || !bHasReleased);
                        btnSubmitAllPCV.setText(btnSubmitAllPCV.isDisabled() ? MainWindow.sBtnAllDone : MainWindow.sBtnSubmitAll);

                        if (!bHasReleased){
                            btnSubmitAllPCV.setText(MainWindow.sBtnStatusNotAvailableLong);
                        }
                        tviewPCV.setPlaceholder(ph);

                        break;
                    case NIGHT:
                        tviewHS.getItems().clear();
                        tviewHS.getItems().addAll( getValue() );

                        btnSubmitAllHS.setDisable(getValue().isEmpty() || !bHasReleased);
                        btnSubmitAllHS.setText(btnSubmitAllHS.isDisabled() ? MainWindow.sBtnAllDone : MainWindow.sBtnSubmitAll);

                        if (!bHasReleased){
                            btnSubmitAllHS.setText(MainWindow.sBtnStatusNotAvailableLong);
                        }
                        tviewHS.setPlaceholder(ph);

                        break;
                    case UNDEFINED:
                        tviewUdef.getItems().clear();
                        tviewUdef.getItems().addAll( getValue() );
                        // Toggle the button for the Udef tab, if no medications are available.
                        if (getValue().size() < 1){
                            btnSubmitAllUdef.setDisable(true);
                            btnSubmitAllUdef.setText(MainWindow.sBtnStatusNotAvailable);
                        }
                        else{
                            btnSubmitAllUdef.setDisable(false);
                            btnSubmitAllUdef.setText(MainWindow.sBtnSubmitAgain);
                        }
                        tviewUdef.setPlaceholder(ph);

                        break;
                }

            }
        };

        // Now do the work
        new Thread(task).start();
    }


    public void updateAllMedicationsPCM(){ updateAllMedicationsOfTime(eIngestionTime.MORNING);}
    public void updateAllMedicationsPCD(){
        updateAllMedicationsOfTime(eIngestionTime.MIDDAY);
    }
    public void updateAllMedicationsPCV(){
        updateAllMedicationsOfTime(eIngestionTime.EVENING);
    }
    public void updateAllMedicationsHS(){
        updateAllMedicationsOfTime(eIngestionTime.NIGHT);
    }
    public void updateAllMedicationsUdef(){ updateAllMedicationsOfTime(eIngestionTime.UNDEFINED);}

    /**
     * Set the cell factory for the last column of a table view, so that it becomes a button column.
     * In order to define the command that will be executed when hitting the button, the parameter eTime is needed to identify the time when the medication was ingested.
     * In addition, the current DAY and the current TIMESTAMP will be used to update the item in the persistence file.
     * @param eTime The tab id (=ingestion time)
     * @return A new button factory
     */
    public Callback<TableColumn<MedicationFX, String>, TableCell<MedicationFX, String>> setButtonFactory(eIngestionTime eTime){

        Callback<TableColumn<MedicationFX, String>, TableCell<MedicationFX, String>> cellFactoryForButton
                = new Callback<TableColumn<MedicationFX, String>, TableCell<MedicationFX, String>>() {
            @Override
            public TableCell call(final TableColumn<MedicationFX, String> param) {
                final TableCell<MedicationFX, String> cell = new TableCell<MedicationFX, String>() {

                    final Button btn = new Button();

                    @Override
                    public void updateItem(String item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                            setText(null);
                        } else {

                            btn.setOnAction(event -> {

                                ArrayList<MedicationFX> lMedFX = new ArrayList<>();
                                lMedFX.add(getTableView().getItems().get(getIndex()));

                                Stage s = mainApp.createIngestionTimeStage(lMedFX, eTime, new Date(), p, primaryStage);

                                // Set a listener to the close event of the created stage in order to refresh the table instantly after updating the data.
                                if (s != null) {
                                    s.setOnCloseRequest(new EventHandler<WindowEvent>() {
                                        public void handle(WindowEvent we) {
                                            refreshTitledPanes();
                                            refreshTable(eTime);
                                        }
                                    });
                                }

                            });
                            setGraphic(btn);
                            setText(null);

                            // Set the style of the button depending on the ingestion status
                            MedicationFX medFXAtIndex = getTableView().getItems().get(getIndex());
                            mainApp.setButtonStyle(btn, medFXAtIndex.getIngestedStatus(), eTime, medFXAtIndex.getIngestedTime(), new Date());
                        }
                    }
                };
                return cell;
            }
        };

        return cellFactoryForButton;
    }

    /**
     * <p>Creates a new row factory for the tableViews of the medications.</p>
     * <p>The factory adds an additional line under each table element.</p>
     * <p>This line contains the information about:</p>
     * <ol>
     *     <li>The title of its section, if available</li>
     *     <li>The note of the medication, if available.</li>
     * </ol>
     * @return The new row factory in order to assign it to a tableView.
     */
    public static TableRow<MedicationFX> getNewRowFactory()  {

        TableRow<MedicationFX> t = new TableRow<MedicationFX>() {

            javafx.scene.Node detailsPane;
            {
                try {
                    itemProperty().addListener((observable -> {

                        // Add the details pane only when there is none of it already
                        if ((getChildren().size()) < (MainWindow.numberOfColumns + 1)){
                            getChildren().add(detailsPane);
                        }
                    }));

                    detailsPane = createDetailsPane(itemProperty());
                }
                catch (Exception e){
                    e.printStackTrace();
                }

            }

            @Override
            protected double computePrefHeight(double width) {
                return super.computePrefHeight(width) + detailsPane.prefHeight(getWidth());
            }

            @Override
            protected void layoutChildren() {
                super.layoutChildren();
                double width = getWidth();
                double paneHeight = detailsPane.prefHeight(width);
                detailsPane.resizeRelocate(0, getHeight() - paneHeight, width, paneHeight);
            }
        };
        return t;
    }

    /**
     * <p>This function creates a new borderPane in order to add it to a list entry.</p>
     * <p>The borderPane contains two text elements, containing the following information:</p>
     * <ol>
     *     <li>isSelfMedication</li>
     *     <li>note</li>
     * </ol>
     * @param item The list element where the new borderPane should be added to afterwards.
     * @return The new borderPane element.
     */
    public static javafx.scene.Node createDetailsPane(ObjectProperty<MedicationFX> item) {
        BorderPane detailsPane = new BorderPane();

        // Note
        Text tNote = new Text();
        tNote.setFill(Color.RED);
        detailsPane.setCenter(tNote);

        // Button (to toggle the free text of the section)
        Button btnInfo = new Button();
        btnInfo.setWrapText(true);
        Text tFreeText = new Text();
        tFreeText.setWrappingWidth(detailsPane.getWidth() -20);

        Text lblInfo = new Text();

        btnInfo.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if (detailsPane.getBottom() == null ){
                    detailsPane.setBottom(tFreeText);
                }
                else{
                    detailsPane.setBottom(null);
                }
            }
        });


        item.addListener((obs, oldItem, newItem) -> {

            if (newItem == null) {

                lblInfo.setText("");
                tNote.setText("");
                detailsPane.setBottom(null);
                btnInfo.setVisible(false);

            } else {

                if (!util.String.isEmpty(newItem.getSectionFreeText())) {
                    tFreeText.setWrappingWidth(detailsPane.getWidth() -20);
                    tFreeText.setText(newItem.getSectionFreeText());
                    btnInfo.setText(newItem.getSectionTitle());
                    btnInfo.setVisible(true);
                    detailsPane.setLeft(btnInfo);
                }
                else{
                    lblInfo.setText(newItem.getSectionTitle());
                    detailsPane.setLeft(lblInfo);
                }

                tNote.setText(newItem.getNote());
            }
        });

        return detailsPane ;
    }

    /**
     * Collects all remaining ingestions of the tableView and updates them in the persistence file.
     * @param eTime The ingestion time that identifies the table view to be searched for remaining ingestions.
     */
    private void updateAllMedicationsOfTime(eIngestionTime eTime){

        ObservableList<MedicationFX> list = FXCollections.observableArrayList();

        // Get elements from list
        switch (eTime){
            case MORNING:
                list = tviewPCM.getItems();
                break;

            case MIDDAY:
                list = tviewPCD.getItems();
                break;

            case EVENING:
                list = tviewPCV.getItems();
                break;

            case NIGHT:
                list = tviewHS.getItems();
                break;

            case UNDEFINED:
                list = tviewUdef.getItems();
                // Do not disable the button for the undefined ingestion times tab!
                break;
        }


        // Convert Observable List to ArrayList
        ArrayList<MedicationFX> lMedFX = new ArrayList<>();
        lMedFX.addAll(list);

        Stage s = mainApp.createIngestionTimeStage(lMedFX, eTime, new Date(), p, primaryStage);

        // Set a listener to the close event of the created stage in order to refresh the table instantly after updating the data.
        if (s != null) {
            s.setOnCloseRequest(new EventHandler<WindowEvent>() {
                public void handle(WindowEvent we) {
                    refreshTitledPanes();
                    refreshTable(eTime);

                }
            });
        }

    }


    /**
     * <p>Calls the 'getData' method within the MainWindow instance, depending on the provided ingestion time.</p>
     * <p>The ID of the table view that will be updated depends on the parameter</p>
     * @param eTime The ingestion time that identifies the table view to be updated.
     * @return A list of medications that is returned from the 'getData' method.
     */
    private List<MedicationFX> fetchData(eIngestionTime eTime) {

        return mainApp.getData(eTime, new Date(), false, false);
    }

}


