package guifx;

import init.Init;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import ukfparser.UKFToFHIRParser;
import help.Help;

import java.util.logging.Level;
import static logging.Logging.logger;

public class ScanController {

    Stage primaryStage;
    Stage rescanStage;

    // Reference to the main application.
    @FXML
    private MainWindow mainApp;
    @FXML
    public Label lblScanStatus;
    @FXML
    public Label lblScanNow;
    @FXML
    public TextField tfScannedString;

    @FXML
    public Button btnScanNow;
    @FXML
    public Button btnCloseRescan;


    /**
     * The closing tag of a ukf string is always in capitals.
     */
    final char[] correctDelimitingString = new char []{'<','/', 'M', 'P', '>'};

    /**
     * A wrong delimiting string is passed when the scanner has the wrong configuration.
     */
    final char[] wrongDelimitingString = new char[]{'&', 'S', '&', 'M', 'P'};

    public ScanController(){

    }

    /**
     * Closes the rescan stage.
     */
    public void closeRescan(){
        rescanStage.close();

    }

    /**
     * Initializes the scan stage
     * @param mainApp An instance of the 'MainWindow' class.
     * @param primaryStage The parent stage where the user comes from.
     * @param scanStage The stage where the following elements will be bound to.
     */
    public void initializeScan(MainWindow mainApp, Stage primaryStage, Stage scanStage) {

        this.mainApp = mainApp;
        this.primaryStage = primaryStage;
        this.rescanStage = scanStage;


        // Disable the text field initially, till the button gets pressed
        // The invisibility of the text field is commanded by the css file that is attached to the fxml file
        tfScannedString.setDisable(true);

        // Hide the Cancel button as there is no opportunity for the user to go back on the start screen
        if (!util.FileSystem.bCheckFileExists(Init.newInputFile)){
            btnCloseRescan.setVisible(false);
        }

        lblScanNow.setText(MainWindow.sBtnScanNow);
        btnScanNow.setText("\n\n (Achtung: Alle bisherigen Daten werden überschrieben!)");


        btnScanNow.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {

                // Show that the application is waiting for a code to be scanned
                lblScanStatus.setText(MainWindow.sBtnScanInProgress);
                lblScanStatus.setStyle(MainWindow.sBgColorBtnScanInProgress);

                btnScanNow.setDisable(true);

                // Activate the text field
                tfScannedString.setDisable(false);

                // Important: Set the focus to the text field in order to receive the scanned string
                tfScannedString.requestFocus();
            }
        });


        // Initialize the ukf parser once
        UKFToFHIRParser ukftofhirparser = new UKFToFHIRParser();


        // Wait for an input in the invisible text field. The input is triggered by the barcode scanner. Each added character triggers the listener.
        tfScannedString.textProperty().addListener((observable, oldValue, newValue) -> {

            // Empty inputs are ignored
            if (newValue.isEmpty()){
                return;
            }

            boolean wasParsingSuccessful;

            // Prepare an array that holds the last five entered characters
            char [] enteredCharacters = new char[5];

            // Check the minimum length of the input string, then cut out the last five characters
            if (tfScannedString.getText().length() > 5){
                newValue.getChars(newValue.length() - 5 ,newValue.length(), enteredCharacters, 0);
            }

            // Check for correct delimiting string
            boolean match = true;
            for (int c = 0; c < enteredCharacters.length; c++){
                if (correctDelimitingString[c] != enteredCharacters[c]){
                    match = false;
                    break;
                }
            }

            // ToDo: Check for a wrong delimiting string
            boolean validEndingString = false;
                for (int c = 0; c < enteredCharacters.length; c++){
                    if (wrongDelimitingString[c] != enteredCharacters[c]){
                        validEndingString  = true;
                        break;
                    }
                }

            // Print a hint to the user that he uses the wrong scanner configuration
            if (!validEndingString ){
                logger.log(Level.SEVERE, "The parsing of the UKF string went wrong! ");

                lblScanStatus.setText("Fehlerhafter Scan! Bitte überpüfen Sie, ob die Einstellung des Scanners für 'US' aktiv ist!");
                btnScanNow.setText("\n\nNochmals Scannen \n(Auf US-Scanner-Konfiguration achten)");
                lblScanStatus.setStyle(MainWindow.sBgColorBtnDeclined);
                btnScanNow.setDisable(false);

                // Reset the text
                tfScannedString.clear();
                tfScannedString.setDisable(true);
                btnScanNow.requestFocus();
            }
            else{
                // The scanning has detected the end of an UKF string, if the value of 'match' hasn't changed.
                if (match){

                    lblScanStatus.setText("Validierung läuft!");
                    lblScanStatus.setStyle(MainWindow.sBgColorBtnIngested);
                    btnScanNow.setDisable(true);

                    // Disable the text field for the time of parsing
                    tfScannedString.setDisable(true);

                    // Parse the first page - Pass the ukf string to the init method
                    wasParsingSuccessful = ukftofhirparser.parsing(tfScannedString.getText());

                    // Fail
                    if (!wasParsingSuccessful){

                        logger.log(Level.SEVERE, "The parsing of the UKF string went wrong! ");

                        lblScanStatus.setText("Fehlerhafter Scan! Bitte die aktuelle Seite nochmals scannen.");
                        btnScanNow.setText("\n\nNochmals Scannen");
                        lblScanStatus.setStyle(MainWindow.sBgColorBtnDeclined);
                        btnScanNow.setDisable(false);

                        // Reset the text
                        tfScannedString.clear();
                        tfScannedString.setDisable(true);
                        btnScanNow.requestFocus();
                    }
                    // Success
                    else {

                        if (ukftofhirparser.arePagesLeft()){

                            int currentPage = ukftofhirparser.getCurrentPage();
                            boolean[] readStatusArray = ukftofhirparser.getReadStatusOfUKFString();

                            if (!readStatusArray[currentPage - 1]){
                                // Okay, we can continue.

                                ukftofhirparser.setReadStatusOfUKFString(currentPage);

                                // If all pages were scanned, the scanning can be finalized.
                                if (!ukftofhirparser.arePagesLeft()){
                                    finalizeScanning(ukftofhirparser);
                                }
                                else{

                                    lblScanStatus.setText("Scan der Seite " + currentPage + " (von " + ukftofhirparser.getTotalPages() + ") war erfolgreich!\nBitte jetzt die nächste Seite scannen.");
                                    btnScanNow.setText("\n\nNächste Seite scannen");
                                    lblScanStatus.setStyle(MainWindow.sBgColorBtnIngested);
                                    btnScanNow.setDisable(false);

                                    tfScannedString.clear();
                                    tfScannedString.setDisable(true);
                                    btnScanNow.requestFocus();
                                }
                            }
                            else
                            {
                                lblScanStatus.setText("Die aktuelle Seite (" + currentPage + ") wurde bereits gescannt!");
                                btnScanNow.setText("\n\nBitte eine andere Seite scannen");
                                lblScanStatus.setStyle(MainWindow.sBgColorBtnDeclined);
                                btnScanNow.setDisable(false);
                                tfScannedString.clear();
                                tfScannedString.setDisable(true);
                                btnScanNow.requestFocus();
                            }

                        }
                        else{
                            // There are no more pages to scan
                            finalizeScanning(ukftofhirparser);
                        }
                    }
                }
                // No match
                else{
                    // Continue scanning
                    btnScanNow.setText("\n\n" + MainWindow.sBtnScanInProgress);
                    btnScanNow.setStyle(null);
                    tfScannedString.setDisable(false);
                }

            } // correct ending string


        });
    }

    /**
     * <p>This method is called after a successful scan.</p>
     * <p>The following steps are executed:</p>
     * <ol>
     *     <li>The old persistence file is deleted and hence all the data that was stored before.</li>
     *     <li>A new FHIR file is created.</li>
     *     <li>The global variables in class 'Init' are initialized again.</li>
     *     <li>The primary stage is re-drawn</li>
     * </ol>
     * <p>At the end, the current scan stage is closed.</p>
     * @param ukf2fhirP The instance of the UKF to FHIR parser, that was used to validate the scanning.
     */
    private void finalizeScanning(UKFToFHIRParser ukf2fhirP){

        // Delete the old persistence file
        if (util.FileSystem.bCheckFileExists(Init.persistenceFile)){
            Init.persistenceFile.delete();
        }

        // Create a new FHIR file
        ukf2fhirP.print();

        // Re-initialize the primaryStage and the scene
        Init.initialize();
        mainApp.init();

        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Show the primary stage
        primaryStage.show();

        // If finished, close the rescan stage
        closeRescan();
    }


}
