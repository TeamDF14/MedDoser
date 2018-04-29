package guifx;

import enums.eIngestedStatus;
import enums.eIngestionTime;
import init.Init;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.util.Callback;
import persistenceXML.Persistence;
import help.Help;

import java.text.SimpleDateFormat;
import java.util.*;

public class HistoryController {

    Stage primaryStage;
    Stage historyStage;
    Persistence p;
    ArrayList<Date> dayToIntList;
    private ArrayList<Date> currentWeekList;

    @FXML // Reference to the main application.
    private MainWindow mainApp;

    @FXML
    public GridPane historyGridPane;
    @FXML
    public Button btnCloseHistory;

    @FXML
    public Button btnSubmitAllHistoryPCM;
    @FXML
    public Button btnSubmitAllHistoryPCD;
    @FXML
    public Button btnSubmitAllHistoryPCV;
    @FXML
    public Button btnSubmitAllHistoryHS;
    @FXML
    public Button btnSubmitAllHistoryUdef;


    @FXML
    public TableView tViewHistoryPCM;
    @FXML
    public TableView tViewHistoryPCD;
    @FXML
    public TableView tViewHistoryPCV;
    @FXML
    public TableView tViewHistoryHS;
    @FXML
    public TableView tViewHistoryUdef;


    @FXML
    public TableColumn colNameHistoryPCD;
    @FXML
    public TableColumn colAmountHistoryPCD;
    @FXML
    public TableColumn colReasonHistoryPCD;
    @FXML
    public TableColumn colButtonHistoryPCD;

    @FXML
    public TableColumn colNameHistoryPCV;
    @FXML
    public TableColumn colAmountHistoryPCV;
    @FXML
    public TableColumn colReasonHistoryPCV;
    @FXML
    public TableColumn colButtonHistoryPCV;

    @FXML
    public TableColumn colNameHistoryHS;
    @FXML
    public TableColumn colAmountHistoryHS;
    @FXML
    public TableColumn colReasonHistoryHS;
    @FXML
    public TableColumn colButtonHistoryHS;

    @FXML
    public TableColumn colNameHistoryUdef;
    @FXML
    public TableColumn colAmountHistoryUdef;
    @FXML
    public TableColumn colReasonHistoryUdef;
    @FXML
    public TableColumn colButtonHistoryUdef;

    @FXML
    public TableColumn colNameHistoryPCM;
    @FXML
    public TableColumn colAmountHistoryPCM;
    @FXML
    public TableColumn colReasonHistoryPCM;
    @FXML
    public TableColumn colButtonHistoryPCM;
    @FXML
    TableColumn colTimesHistoryUdef;


    // content
    @FXML
    public Accordion accHistory;
    @FXML
    public TitledPane titledPanePCM;
    @FXML
    public TitledPane titledPanePCD;
    @FXML
    public TitledPane titledPanePCV;
    @FXML
    public TitledPane titledPaneHS;
    @FXML
    public TitledPane titledPaneUdef;

    // week switcher elements
    @FXML
    public Label lblCurrentWeek;
    @FXML
    public Button btnSwitchWeekR;
    @FXML
    public Button btnSwitchWeekL;
    @FXML
    public ListView lViewHistory;


    public HistoryController(){

    }

    /**
     * <p>Initializes the history time stage. On this stage, the user can manage his ingestions that were scheduled on a days in the past.</p>
     * <p>The user can submit or decline ingestions, despite the ingestion time has passed already over days</p>
     *
     * @param p An instance of the persistence controller.
     * @param mainApp An instance of the class 'MainWindow'.
     * @param primaryStage The parent stage (where the user comes from), here: The primary stage.
     * @param historyStage The stage where the following elements are bound to.
     */
    public void initializeHistory(Persistence p, MainWindow mainApp, Stage primaryStage, Stage historyStage) {
        this.mainApp = mainApp;
        this.p = p;
        this.primaryStage = primaryStage;
        this.historyStage = historyStage;

        // Create a list that contains an entry for each day in the persistence file
        dayToIntList = Help.getDateList();

        // Define the columns of the tableViews and their content
        setCellValueFactory();

        // Define a custom row factory for the medication note(s)
        setRowFactoryForNotes();

        // Init all elements that belong to the week switcher
        initWeekSwitcher();


        // Initialize data in the tableViews for today
        for (eIngestionTime eTime : eIngestionTime.values()){

            // Fetch the data the first time so that the variable Init.persistenceXMLObject is not null and items are showing up.
            refreshHistoryTable(eTime, dayToIntList.get(dayToIntList.size() - 1));

            // Toggle the disability of the submitAll buttons depending on the stati of the ingestions
            toggleSubmitAllButtonDisability(eTime);
        }


        // Set listener to the listView
        lViewHistory.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {

            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {

                // Pass the selected date to the refreshing method, if there are items to update
                if (lViewHistory.getItems().size() > 0){
                    for (eIngestionTime eTime: eIngestionTime.values()){
                        refreshHistoryTable(eTime, currentWeekList.get(lViewHistory.getSelectionModel().getSelectedIndex()));
                    }
                }
            }
        });

        // Set the width of the listView items, depending on the number of items.
        lViewHistory.setCellFactory(param -> new ListCell<String>() {
            {
                prefWidthProperty().bind(lViewHistory.widthProperty().divide(currentWeekList.size()).subtract(0.8));
                setMaxWidth(Control.USE_PREF_SIZE);
            }
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);

                if (item != null && !empty) {

                    setText(item);

                    // Remove item highlighting
                    getStyleClass().removeIf(style -> style.equals("lViewHighlightedItem"));

                    // Then add the highlighting, if required
                    for (eIngestionTime eTime : eIngestionTime.values()){
                        if (Help.bIsRemainingIngestion(eTime, currentWeekList.get(getIndex()))){
                            getStyleClass().add("lViewHighlightedItem");
                            break;
                        }
                    }

                } else {
                    setText(null);
                }
            }
        });

        btnSubmitAllHistoryPCM.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                submitAll(eIngestionTime.MORNING);
            }
        });
        btnSubmitAllHistoryPCD.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                submitAll(eIngestionTime.MIDDAY);
            }
        });
        btnSubmitAllHistoryPCV.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                submitAll(eIngestionTime.EVENING);
            }
        });
        btnSubmitAllHistoryHS.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                submitAll(eIngestionTime.NIGHT);
            }
        });
        btnSubmitAllHistoryUdef.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                submitAll(eIngestionTime.UNDEFINED);
            }
        });

    }

    /**
     * Closes the history stage.
     */
    public void closeHistory(){
        historyStage.close();
    }


    /**
     * <p>Updates the list view, depending on the dates defined in the given list.</p>
     * <p>The items that hold remaining ingestions are highlighted.</p>
     *
     * @param weekList A list containing dates in order to show in the list view
     */
    private void fillListView(ArrayList<Date> weekList){
        // Fill the list view
        ObservableList<String> items = FXCollections.observableArrayList();
        Calendar calD1 = new GregorianCalendar(util.Date.timeZone);
        SimpleDateFormat sdF = new SimpleDateFormat("dd. MMMM");

        for (int i = 0; i < weekList.size(); i++){
            calD1.setTime(weekList.get(i));
            items.add(sdF.format(calD1.getTime()));
        }

        // Clear the listView
        if (lViewHistory.getItems() != null && lViewHistory.getItems().size() > 0){
            lViewHistory.getItems().clear();
        }


        // Pass the items to the list view
        if (items.isEmpty()){
            // Set a placeholder item
            items.add(MainWindow.sTViewPlaceholder);
            lViewHistory.setItems(items);
        }
        else{
            lViewHistory.setItems(items);
        }

        // Update the current global list
        currentWeekList = weekList;

    }


    /**
     * Check if the accessibility of the buttons has to be toggled because the highest or lowest possible date is already shown in the list view.
     */
    private void toggleBtnSwitch(){

            // Selected index == last possible index?
            if (currentWeekList.get(lViewHistory.getSelectionModel().getSelectedIndex()) ==  dayToIntList.get(dayToIntList.size() -1)){
                btnSwitchWeekR.setDisable(true);
            }
            else{
                btnSwitchWeekR.setDisable(false);
            }

            // Selected index == first possible index?
            if (currentWeekList.get(lViewHistory.getSelectionModel().getSelectedIndex()) == dayToIntList.get(0)){
                btnSwitchWeekL.setDisable(true);
            }
            else{
                btnSwitchWeekL.setDisable(false);
            }
    }

    /**
     * <p>Initializes the week switcher.</p>
     * <p>The assigned list view is filled with data, by default for today.</p>
     * <p>In addition, the action listeners of the switch buttons are set so that they update the tableView with the data, depending on the chosen date.</p>
     * <p>The currently selected item is highlighted.</p>
     */
    private void initWeekSwitcher(){

        // If there is no entry in the persistence file, we add TODAY manually in order
        // to let the user submit the medications without ingestion time assigned
        if (dayToIntList.isEmpty()){
            dayToIntList.add(new Date());
        }

        Calendar cal = new GregorianCalendar(util.Date.timeZone);
        Date mostActualDate = dayToIntList.get(dayToIntList.size() -1);
        ArrayList<Date> weekList = util.Date.getWeekList(dayToIntList, mostActualDate, true, true);

        cal.setTime(weekList.get(weekList.size() -1 ));
        lblCurrentWeek.setText(MainWindow.sLblWeek + " " + MainWindow.decimalFormat.format(cal.get(Calendar.WEEK_OF_YEAR)));

        // Update the listView
        fillListView(weekList);

        // Set focus on the latest date
        lViewHistory.getSelectionModel().selectLast();

        // Toggle the buttons
        toggleBtnSwitch();


        btnSwitchWeekL.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {

                // Load new week only if the position in the listView is at the most left
                if (lViewHistory.getSelectionModel().getSelectedIndex() == 0){
                    // Get the latest date from the switcher
                    Date lastVisibleDate = currentWeekList.get(0);
                    ArrayList<Date> newWeekList = util.Date.getWeekList(dayToIntList, lastVisibleDate, false, true);

                    if (!newWeekList.isEmpty()){
                        // Update label text
                        Calendar calHandler = new GregorianCalendar(util.Date.timeZone);
                        calHandler.setTime(newWeekList.get(newWeekList.size() -1 ));
                        lblCurrentWeek.setText(MainWindow.sLblWeek + " " + MainWindow.decimalFormat.format(calHandler.get(Calendar.WEEK_OF_YEAR)));

                        // Update the listView
                        fillListView(newWeekList);

                        // Set focus on the right most element in the list View
                        lViewHistory.getSelectionModel().selectLast();
                    }
                }
                // Set selection on the desired date (one to the left)
                else{
                    lViewHistory.getSelectionModel().selectPrevious();
                }

                // Toggle the buttons
                toggleBtnSwitch();



            }
        });

        btnSwitchWeekR.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {

                // Load new week only if the position in the listView is at the most right
                if (lViewHistory.getSelectionModel().getSelectedIndex() == (currentWeekList.size() -1)){
                    // Get the latest date from the switcher
                    Date lastVisibleDate = currentWeekList.get(currentWeekList.size() -1);
                    ArrayList<Date> newWeekList = util.Date.getWeekList(dayToIntList, lastVisibleDate, false, false);

                    if (!newWeekList.isEmpty()){
                        // Update label text
                        Calendar calHandler = new GregorianCalendar(util.Date.timeZone);
                        calHandler.setTime(newWeekList.get(newWeekList.size() -1 ));
                        lblCurrentWeek.setText(MainWindow.sLblWeek + " " + MainWindow.decimalFormat.format(calHandler.get(Calendar.WEEK_OF_YEAR)));

                        // Update the listView
                        fillListView(newWeekList);

                        // Set focus on the left most element in the list View
                        lViewHistory.getSelectionModel().selectFirst();
                    }
                }
                // Set selection on the desired date (one to the right)
                else{
                    lViewHistory.getSelectionModel().selectNext();
                }

                // Toggle the buttons
                toggleBtnSwitch();


            }
        });
    }

    /**
     * Sets a cellValueFactory to all columns of an accordion, that holds a tableView.
     */
    private void setCellValueFactory( ){

        ObservableList<TableView> ol_tViewHistory = FXCollections.observableArrayList();
        ol_tViewHistory.add(tViewHistoryPCM);
        ol_tViewHistory.add(tViewHistoryPCD);
        ol_tViewHistory.add(tViewHistoryPCV);
        ol_tViewHistory.add(tViewHistoryHS);
        ol_tViewHistory.add(tViewHistoryUdef);

        ObservableList<TableColumn<MedicationFX, String>> ol_colAmount = FXCollections.observableArrayList();
        ol_colAmount.add(colAmountHistoryPCM);
        ol_colAmount.add(colAmountHistoryPCD);
        ol_colAmount.add(colAmountHistoryPCV);
        ol_colAmount.add(colAmountHistoryHS);
        ol_colAmount.add(colAmountHistoryUdef);

        ObservableList<TableColumn<MedicationFX, String>> ol_colName = FXCollections.observableArrayList();
        ol_colName.add(colNameHistoryPCM);
        ol_colName.add(colNameHistoryPCD);
        ol_colName.add(colNameHistoryPCV);
        ol_colName.add(colNameHistoryHS);
        ol_colName.add(colNameHistoryUdef);

        ObservableList<TableColumn<MedicationFX, String>> ol_colReason = FXCollections.observableArrayList();
        ol_colReason.add(colReasonHistoryPCM);
        ol_colReason.add(colReasonHistoryPCD);
        ol_colReason.add(colReasonHistoryPCV);
        ol_colReason.add(colReasonHistoryHS);
        ol_colReason.add(colReasonHistoryUdef);


        for (TableColumn tc: ol_colName
                ) {
            tc.setCellValueFactory(new PropertyValueFactory<MedicationFX,String>("tradeName"));
            for(TableView tv: ol_tViewHistory){
                tc.prefWidthProperty().bind(tv.widthProperty().multiply(MainWindow.colNamePart));

                // Set wrapping property
                tc.setCellFactory(randomID -> {return MedicationOverviewController.getWrappedCell(tc);});
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

                        if (tc == colAmountHistoryUdef){
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
            for (TableView tv : ol_tViewHistory){
                tc.prefWidthProperty().bind(tv.widthProperty().multiply(MainWindow.colAmountPart));
            }
        }


        for (TableColumn tc: ol_colReason
                ) {
            tc.setCellValueFactory(new PropertyValueFactory<MedicationFX,String>("reason"));

            // Set wrapping property
            tc.setCellFactory(randomID -> {return MedicationOverviewController.getWrappedCell(tc);});

            for(TableView tv: ol_tViewHistory){
                tc.prefWidthProperty().bind(tv.widthProperty().multiply(MainWindow.colReasonPart));
            }
        }

        colButtonHistoryPCM.prefWidthProperty().bind(tViewHistoryPCM.widthProperty().multiply(MainWindow.colButtonPart));
        colButtonHistoryPCD.prefWidthProperty().bind(tViewHistoryPCD.widthProperty().multiply(MainWindow.colButtonPart));
        colButtonHistoryPCV.prefWidthProperty().bind(tViewHistoryPCV.widthProperty().multiply(MainWindow.colButtonPart));
        colButtonHistoryHS.prefWidthProperty().bind(tViewHistoryHS.widthProperty().multiply(MainWindow.colButtonPart));
        // For colTimesHistoryUdef.prefWidthProperty(), look below

        colButtonHistoryPCM.setCellFactory(setButtonHistoryFactory(eIngestionTime.MORNING));
        colButtonHistoryPCD.setCellFactory(setButtonHistoryFactory(eIngestionTime.MIDDAY));
        colButtonHistoryPCV.setCellFactory(setButtonHistoryFactory(eIngestionTime.EVENING));
        colButtonHistoryHS.setCellFactory(setButtonHistoryFactory(eIngestionTime.NIGHT));
        colButtonHistoryUdef.setCellFactory(setButtonHistoryFactory(eIngestionTime.UNDEFINED));

        colTimesHistoryUdef.setCellFactory(param -> new TableCell<MedicationFX, String>() {
            final Label lbl = new Label();

            @Override
            public void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                    setText(null);
                } else {
                    ArrayList<Date> l = getTableView().getItems().get(getIndex()).getIngestedTimesUdef();
                    String sIngestedTimes = "";
                    for (Date d : l){
                        if (d != null){
                            sIngestedTimes = sIngestedTimes + util.Date.convertTimeToString(d) + MainWindow.sTabTime + "\n";
                        }
                    }
                    lbl.setText(sIngestedTimes);
                    setGraphic(lbl);
                    setText(null);
                }
            }
        });

        // Set widths for times column
        colNameHistoryUdef.prefWidthProperty().bind(tViewHistoryUdef.widthProperty().multiply(MainWindow.colNamePart));
        colAmountHistoryUdef.prefWidthProperty().bind(tViewHistoryUdef.widthProperty().multiply(MainWindow.colAmountPart));
        colReasonHistoryUdef.prefWidthProperty().bind(tViewHistoryUdef.widthProperty().multiply(MainWindow.colReasonPart - 0.1));
        colTimesHistoryUdef.prefWidthProperty().bind(tViewHistoryUdef.widthProperty().multiply(0.1));
        colButtonHistoryUdef.prefWidthProperty().bind(tViewHistoryHS.widthProperty().multiply(MainWindow.colButtonPart));

    }


    /**
     * Set the row factory for the notes of a medication.
     * The content will be printed under the associated medication row.
     */
    public void setRowFactoryForNotes(){

        tViewHistoryPCM.setRowFactory(tvm -> MedicationOverviewController.getNewRowFactory());
        tViewHistoryPCD.setRowFactory(tvd -> MedicationOverviewController.getNewRowFactory());
        tViewHistoryPCV.setRowFactory(tvv -> MedicationOverviewController.getNewRowFactory());
        tViewHistoryHS.setRowFactory(tvhs -> MedicationOverviewController.getNewRowFactory());
        tViewHistoryUdef.setRowFactory(tvudef -> MedicationOverviewController.getNewRowFactory());
    }

    /**
     * <p>Sets the cell factory for the last column of a table view, so that it becomes a button column.</p>
     * <p>In order to define the command that will be executed when hitting the button, the parameter eTime is needed to identify the time when the medication was ingested.</p>
     * <p>In addition, the current DAY and the current TIMESTAMP will be used to update the item in the persistence file.</p>
     * @param eTime The tab id (=ingestion time)
     * @return A new button factory
     */
    public Callback<TableColumn<MedicationFX, String>, TableCell<MedicationFX, String>> setButtonHistoryFactory(eIngestionTime eTime){

        Callback<TableColumn<MedicationFX, String>, TableCell<MedicationFX, String>> cellFactoryForHistoryButton
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
                        }
                        else {

                            btn.setOnAction(event -> {

                                ArrayList<MedicationFX> lMedFX= new ArrayList<>();
                                MedicationFX medFX = getTableView().getItems().get(getIndex());
                                lMedFX.add(medFX);

                                // Important: Get currently selected item in listView in order to get the asserted date. This date is used to identify the dataset that will be updated within the persistence file.
                                Date selectedDate = currentWeekList.get(lViewHistory.getSelectionModel().getSelectedIndex());

                                Stage s = mainApp.createIngestionTimeStage(lMedFX, eTime, selectedDate, p, historyStage);

                                // Set a listener to the close event of the created stage in order to refresh the table instantly after updating the data.
                                if (s != null) {
                                    s.setOnCloseRequest(new EventHandler<WindowEvent>() {
                                        public void handle(WindowEvent we) {
                                            // Update the view - Get the selected index of the listView in order to update the correct tableView
                                            refreshHistoryTable(eTime, selectedDate);

                                            // Save the currently selected index
                                            int selectedIndex = lViewHistory.getSelectionModel().getSelectedIndex();

                                            // Update the listView
                                            fillListView(currentWeekList);

                                            // Set focus on the latest date
                                            lViewHistory.getSelectionModel().select(selectedIndex);
                                        }
                                    });
                                }

                            });

                            // Set the style of the button depending on the ingestion status
                            Date day = currentWeekList.get(lViewHistory.getSelectionModel().getSelectedIndex());
                            MedicationFX medFXAtIndex = getTableView().getItems().get(getIndex());
                            mainApp.setButtonStyle(btn, medFXAtIndex.getIngestedStatus(), eTime, medFXAtIndex.getIngestedTime(), day);

                            setGraphic(btn);
                            setText(null);
                        }
                    }
                };
                return cell;
            }
        };

        return cellFactoryForHistoryButton;
    }

    /**
     * Refreshes the tableView that is associated with the given eTime.
     * @param eTime The ingestion time of the tab / tableView.
     * @param currentDate The date of the ingestions to refresh.
     */
    public void refreshHistoryTable(eIngestionTime eTime, Date currentDate) {

        Task<List<MedicationFX>> task = new Task<List<MedicationFX>>() {
            @Override
            protected List<MedicationFX> call()  {

                // Get the requested data
                if (!dayToIntList.isEmpty() && ((util.Date.bIsDayEqual(currentDate, new Date())))){
                    return mainApp.getData(eTime, currentDate, true, true);
                }
                else{
                    return mainApp.getData(eTime, currentDate, true, false);
                }
            }

            @Override
            protected void succeeded() {
                // Clear the table and add the updated data and toggle the visibility of the titledPanes if no data is available

                // ToDo: Open the next available pane by default
                Label lblPH = new Label(MainWindow.sTViewPlaceholder);

                switch (eTime){
                    case MORNING:
                        tViewHistoryPCM.getItems().clear();
                        titledPanePCM.setDisable(getValue().isEmpty());
                        titledPanePCM.setText(MainWindow.sLblMorning + ": " + Init.persistenceXMLObject.getIngestionTimeStringMorning() + MainWindow.sTabTime);

                        if (!getValue().isEmpty()){
                            titledPanePCM.setExpanded(true);
                        }
                        tViewHistoryPCM.setPlaceholder(lblPH);
                        tViewHistoryPCM.getItems().addAll( getValue() );
                        break;
                    case MIDDAY:
                        tViewHistoryPCD.getItems().clear();
                        titledPanePCD.setDisable(getValue().isEmpty());
                        titledPanePCD.setText(MainWindow.sLblMidday + ": " + Init.persistenceXMLObject.getIngestionTimeStringMidday() + MainWindow.sTabTime);

                        if (!getValue().isEmpty() && !titledPanePCM.isExpanded()){
                            titledPanePCD.setExpanded(true);
                        }

                        tViewHistoryPCD.setPlaceholder(lblPH);
                        tViewHistoryPCD.getItems().addAll( getValue() );
                        break;
                    case EVENING:
                        tViewHistoryPCV.getItems().clear();
                        titledPanePCV.setDisable(getValue().isEmpty());
                        titledPanePCV.setText(MainWindow.sLblEvening + ": " + Init.persistenceXMLObject.getIngestionTimeStringEvening() + MainWindow.sTabTime);

                        if (!getValue().isEmpty() && !titledPanePCM.isExpanded() && !titledPanePCD.isExpanded()){
                            titledPanePCV.setExpanded(true);
                        }

                        tViewHistoryPCV.setPlaceholder(lblPH);
                        tViewHistoryPCV.getItems().addAll( getValue() );
                        break;
                    case NIGHT:
                        tViewHistoryHS.getItems().clear();
                        titledPaneHS.setDisable(getValue().isEmpty());
                        titledPaneHS.setText(MainWindow.sLblNight + ": " + Init.persistenceXMLObject.getIngestionTimeStringNight() + MainWindow.sTabTime);

                        if (!getValue().isEmpty() &&  !titledPanePCM.isExpanded() && !titledPanePCD.isExpanded() && !titledPanePCV.isExpanded()){
                            titledPaneHS.setExpanded(true);
                        }

                        tViewHistoryHS.setPlaceholder(lblPH);
                        tViewHistoryHS.getItems().addAll( getValue() );
                        break;
                    case UNDEFINED:
                        tViewHistoryUdef.getItems().clear();
                        titledPaneUdef.setText(MainWindow.sTabUdef);

                        // Always show the Udef titledPane
                        tViewHistoryUdef.setPlaceholder(lblPH);
                        tViewHistoryUdef.getItems().addAll( getValue());

                        break;
                }

                toggleSubmitAllButtonDisability(eTime);

            }
        };

        // Now do the work
        new Thread(task).start();
    }

    /**
     * Returns the elements of the tableView that are assigned with the given ingestion time.
     * @param eTime The ingestion time in order to identify the tableView
     * @return A list with the elements of the identified tableView. If no elements are contained, null is returned.
     */
    private ObservableList<MedicationFX> getCorrespondingListView(eIngestionTime eTime){
        switch (eTime){
            case MORNING:
                return tViewHistoryPCM.getItems();
            case MIDDAY:
                return tViewHistoryPCD.getItems();
            case EVENING:
                return tViewHistoryPCV.getItems();
            case NIGHT:
                return tViewHistoryHS.getItems();
            case UNDEFINED:
                return tViewHistoryUdef.getItems();
        }
        return null;
    }



    /**
     * <p>Submits all ingestions that are contained in the assigned tableView. </p>
     * <p>The ID of the tableView depends on the eIngestionTime.</p>
     * <p>The method filters out the entries that were already updated by the user</p>
     * @param eTime The ingestion time of the tableView assigned to the button.
     */
    private void submitAll(eIngestionTime eTime){

        ObservableList<MedicationFX> list;

        // Get elements from the corresponding list
        list = getCorrespondingListView(eTime);

        // Convert Observable List to ArrayList and filter out the entries that were already updated by the user
        ArrayList<MedicationFX> lMedFX = new ArrayList<>();
        if (list == null) {
            return;
        }
        for (MedicationFX medFX: list) {
            if (medFX.getIngestedStatus() == eIngestedStatus.DEFAULT){
                lMedFX.add(medFX);
            }
        }

        Stage s = mainApp.createIngestionTimeStage(lMedFX, eTime, currentWeekList.get(lViewHistory.getSelectionModel().getSelectedIndex()), p, historyStage);

        // Set a listener to the close event of the created stage in order to refresh the table instantly after updating the data.
        if (s != null) {
            s.setOnCloseRequest(new EventHandler<WindowEvent>() {
                public void handle(WindowEvent we) {
                    refreshHistoryTable(eTime, currentWeekList.get(lViewHistory.getSelectionModel().getSelectedIndex()));

                    // Save the currently selected index
                    int selectedIndex = lViewHistory.getSelectionModel().getSelectedIndex();

                    // Update the listView
                    fillListView(currentWeekList);

                    // Set focus on the latest date
                    lViewHistory.getSelectionModel().select(selectedIndex);
                }
            });
        }

    }


    /**
     * <p>Toggles the accessibility of the 'submit all' buttons on the primary stage.</p>
     * <p>In addition, their texts are set according to the texts defined in class 'MainWindow'.</p>
     * @param eTime The ingestion time in order to identify the buttons.
     */
    private void toggleSubmitAllButtonDisability(eIngestionTime eTime){

        switch (eTime){
            case MORNING:
                btnSubmitAllHistoryPCM.setDisable(!areUnsubmittedIngestionsLeft(tViewHistoryPCM));
                btnSubmitAllHistoryPCM.setText(btnSubmitAllHistoryPCM.isDisabled() ? MainWindow.sBtnAllDone : MainWindow.sBtnSubmitAll);
                break;
            case MIDDAY:
                btnSubmitAllHistoryPCD.setDisable(!areUnsubmittedIngestionsLeft(tViewHistoryPCD));
                btnSubmitAllHistoryPCD.setText(btnSubmitAllHistoryPCD.isDisabled() ? MainWindow.sBtnAllDone : MainWindow.sBtnSubmitAll);
                break;
            case EVENING:
                btnSubmitAllHistoryPCV.setDisable(!areUnsubmittedIngestionsLeft(tViewHistoryPCV));
                btnSubmitAllHistoryPCV.setText(btnSubmitAllHistoryPCV.isDisabled() ? MainWindow.sBtnAllDone : MainWindow.sBtnSubmitAll);
                break;
            case NIGHT:
                btnSubmitAllHistoryHS.setDisable(!areUnsubmittedIngestionsLeft(tViewHistoryHS));
                btnSubmitAllHistoryHS.setText(btnSubmitAllHistoryHS.isDisabled() ? MainWindow.sBtnAllDone : MainWindow.sBtnSubmitAll);
                break;
            case UNDEFINED:
                btnSubmitAllHistoryUdef.setDisable(!areUnsubmittedIngestionsLeft(tViewHistoryUdef));
                btnSubmitAllHistoryUdef.setText(MainWindow.sBtnSubmitAgain);
                break;
        }
    }

    /**
     * <p>Checks if there is at least one entry left within the given tableView.</p>
     * <p>There is an ingestion left, if it is unsubmitted (eIngestionsTatus == DEFAULT). </p>
     * @param tv The tableView to look at.
     * @return True if there is at least one ingestion left, false if not.
     */
    private boolean areUnsubmittedIngestionsLeft(TableView tv){

        ObservableList<MedicationFX> ol = tv.getItems();

        if (ol.isEmpty()){
            return false;
        }

        for (MedicationFX medFX : ol){
            if (medFX.getIngestedStatus() == eIngestedStatus.DEFAULT){
                return true;
            }
        }

        return false;
    }


}
