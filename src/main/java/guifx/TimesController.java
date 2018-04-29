package guifx;

import enums.eIngestionTime;
import init.Init;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import persistenceXML.Persistence;
import help.Help;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class TimesController {


    @FXML
    public Label lblEditTimesHeader;

    @FXML
    public Label lblEditTimePCM;
    @FXML
    public Label lblEditTimePCD;
    @FXML
    public Label lblEditTimePCV;
    @FXML
    public Label lblEditTimeHS;

    @FXML
    public Label lblHoursPCM;
    @FXML
    public Label lblHoursPCD;
    @FXML
    public Label lblHoursPCV;
    @FXML
    public Label lblHoursHS;

    @FXML
    public Label lblMinutesPCM;
    @FXML
    public Label lblMinutesPCD;
    @FXML
    public Label lblMinutesPCV;
    @FXML
    public Label lblMinutesHS;

    @FXML
    Button btnCloseEditTimes;
    @FXML
    Button btnSaveEditTimes;

    @FXML
    public Slider sldHoursPCM;
    @FXML
    public Slider sldHoursPCD;
    @FXML
    public Slider sldHoursPCV;
    @FXML
    public Slider sldHoursHS;

    @FXML
    public Slider sldMinutesPCM;
    @FXML
    public Slider sldMinutesPCD;
    @FXML
    public Slider sldMinutesPCV;
    @FXML
    public Slider sldMinutesHS;

    @FXML
    public Label lblEditTimesPCM;
    @FXML
    public Label lblEditTimesPCD;
    @FXML
    public Label lblEditTimesPCV;
    @FXML
    public Label lblEditTimesHS;

    public TimesController(){}


    /**
     * <p>Initialized the 'edit times' stage, where the user can modify the times of the ingestion.</p>
     * <p>Therefore, the times stored in the header of the persistence file are loaded and the values are bound to the vertical sliders.</p>
     * @param p An instance of the persistence class.
     * @param s The edit times stage where the following elements will be bound to.
     */
    public void initializeEditTimes(Persistence p, Stage s){

        lblEditTimesHeader.setText(MainWindow.sLblEditTimesHeader);

        lblHoursPCM.setText(MainWindow.sLblHours);
        lblHoursPCD.setText(MainWindow.sLblHours);
        lblHoursPCV.setText(MainWindow.sLblHours);
        lblHoursHS.setText(MainWindow.sLblHours);

        lblMinutesPCM.setText(MainWindow.sLblMinutes);
        lblMinutesPCD.setText(MainWindow.sLblMinutes);
        lblMinutesPCV.setText(MainWindow.sLblMinutes);
        lblMinutesHS.setText(MainWindow.sLblMinutes);

        lblEditTimesPCM.setText(MainWindow.sLblMorning);
        lblEditTimesPCD.setText(MainWindow.sLblMidday);
        lblEditTimesPCV.setText(MainWindow.sLblEvening);
        lblEditTimesHS.setText(MainWindow.sLblNight);


        // Init slider values and label with current time above them
        Calendar calEditTimes = new GregorianCalendar(util.Date.timeZone);
        for (eIngestionTime eTime: eIngestionTime.values()){

            Date dateFromPersistence;
            if (eTime != eIngestionTime.UNDEFINED){

                switch (eTime){
                    case MORNING:
                        dateFromPersistence = Init.persistenceXMLObject.getIngestionTimeMorning();
                        calEditTimes.setTime(dateFromPersistence);
                        sldHoursPCM.setValue(calEditTimes.get(Calendar.HOUR_OF_DAY));
                        sldMinutesPCM.setValue(calEditTimes.get(Calendar.MINUTE));

                        // Set min and max value of slider
                        sldHoursPCM.setMin(0.0);
                        sldHoursPCM.setMax(10.0);

                        lblEditTimePCM.setText((MainWindow.decimalFormat.format(calEditTimes.get(Calendar.HOUR_OF_DAY)) + ":" + (MainWindow.decimalFormat.format(calEditTimes.get(Calendar.MINUTE))  + MainWindow.sTabTime)));

                        // Update label text when slider values changes
                        sldHoursPCM.valueProperty().addListener(new ChangeListener<Number>() {
                            @Override
                            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                                lblEditTimePCM.setText(MainWindow.decimalFormat.format(newValue) + ":" + MainWindow.decimalFormat.format(sldMinutesPCM.getValue())  + MainWindow.sTabTime);
                            }
                        });
                        // Update label text when slider values changes
                        sldMinutesPCM.valueProperty().addListener(new ChangeListener<Number>() {
                            @Override
                            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {

                                lblEditTimePCM.setText(MainWindow.decimalFormat.format(sldHoursPCM.getValue()) + ":" + MainWindow.decimalFormat.format(newValue) +  MainWindow.sTabTime);
                            }
                        });
                        // Round double value to an integer value on value changing
                        sldHoursPCM.valueChangingProperty().addListener(new ChangeListener<Boolean>() {
                            @Override
                            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                                sldHoursPCM.setValue((int) sldHoursPCM.getValue());
                            }
                        });
                        // Round double value to an integer value on value changing
                        sldMinutesPCM.valueChangingProperty().addListener(new ChangeListener<Boolean>() {
                            @Override
                            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                                sldMinutesPCM.setValue((int) sldMinutesPCM.getValue());
                            }
                        });

                        break;

                    case MIDDAY:
                        dateFromPersistence = Init.persistenceXMLObject.getIngestionTimeMidday();
                        calEditTimes.setTime(dateFromPersistence);
                        sldHoursPCD.setValue(calEditTimes.get(Calendar.HOUR_OF_DAY));
                        sldMinutesPCD.setValue(calEditTimes.get(Calendar.MINUTE));

                        // Set min and max value of slider
                        sldHoursPCD.setMin(11.0);
                        sldHoursPCD.setMax(14.0);

                        lblEditTimePCD.setText((MainWindow.decimalFormat.format(calEditTimes.get(Calendar.HOUR_OF_DAY)) + ":" + (MainWindow.decimalFormat.format(calEditTimes.get(Calendar.MINUTE))  + MainWindow.sTabTime)));

                        // Update label text when slider values changes
                        sldHoursPCD.valueProperty().addListener(new ChangeListener<Number>() {
                            @Override
                            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                                lblEditTimePCD.setText(MainWindow.decimalFormat.format(newValue) + ":" + MainWindow.decimalFormat.format(sldMinutesPCD.getValue())  + MainWindow.sTabTime);
                            }
                        });
                        // Update label text when slider values changes
                        sldMinutesPCD.valueProperty().addListener(new ChangeListener<Number>() {
                            @Override
                            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {

                                lblEditTimePCD.setText(MainWindow.decimalFormat.format(sldHoursPCD.getValue()) + ":" + MainWindow.decimalFormat.format(newValue) +  MainWindow.sTabTime);
                            }
                        });
                        // Round double value to an integer value on value changing
                        sldHoursPCD.valueChangingProperty().addListener(new ChangeListener<Boolean>() {
                            @Override
                            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                                sldHoursPCD.setValue((int) sldHoursPCD.getValue());
                            }
                        });
                        // Round double value to an integer value on value changing
                        sldMinutesPCD.valueChangingProperty().addListener(new ChangeListener<Boolean>() {
                            @Override
                            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                                sldMinutesPCD.setValue((int) sldMinutesPCD.getValue());
                            }
                        });

                        break;

                    case EVENING:
                        dateFromPersistence = Init.persistenceXMLObject.getIngestionTimeEvening();
                        calEditTimes.setTime(dateFromPersistence);
                        sldHoursPCV.setValue(calEditTimes.get(Calendar.HOUR_OF_DAY));
                        sldMinutesPCV.setValue(calEditTimes.get(Calendar.MINUTE));

                        // Set min and max value of slider
                        sldHoursPCV.setMin(15.0);
                        sldHoursPCV.setMax(19.0);

                        lblEditTimePCV.setText((MainWindow.decimalFormat.format(calEditTimes.get(Calendar.HOUR_OF_DAY)) + ":" + (MainWindow.decimalFormat.format(calEditTimes.get(Calendar.MINUTE))  + MainWindow.sTabTime)));

                        // Update label text when slider values changes
                        sldHoursPCV.valueProperty().addListener(new ChangeListener<Number>() {
                            @Override
                            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                                lblEditTimePCV.setText(MainWindow.decimalFormat.format(newValue) + ":" + MainWindow.decimalFormat.format(sldMinutesPCV.getValue())  + MainWindow.sTabTime);
                            }
                        });
                        // Update label text when slider values changes
                        sldMinutesPCV.valueProperty().addListener(new ChangeListener<Number>() {
                            @Override
                            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {

                                lblEditTimePCV.setText(MainWindow.decimalFormat.format(sldHoursPCV.getValue()) + ":" + MainWindow.decimalFormat.format(newValue) +  MainWindow.sTabTime);
                            }
                        });
                        // Round double value to an integer value on value changing
                        sldHoursPCV.valueChangingProperty().addListener(new ChangeListener<Boolean>() {
                            @Override
                            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                                sldHoursPCV.setValue((int) sldHoursPCV.getValue());
                            }
                        });
                        // Round double value to an integer value on value changing
                        sldMinutesPCV.valueChangingProperty().addListener(new ChangeListener<Boolean>() {
                            @Override
                            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                                sldMinutesPCV.setValue((int) sldMinutesPCV.getValue());
                            }
                        });

                        break;

                    case NIGHT:
                        dateFromPersistence = Init.persistenceXMLObject.getIngestionTimeNight();
                        calEditTimes.setTime(dateFromPersistence);
                        sldHoursHS.setValue(calEditTimes.get(Calendar.HOUR_OF_DAY));
                        sldMinutesHS.setValue(calEditTimes.get(Calendar.MINUTE));

                        // Set min and max value of slider
                        sldHoursHS.setMin(20.0);
                        sldHoursHS.setMax(23.0);

                        lblEditTimeHS.setText((MainWindow.decimalFormat.format(calEditTimes.get(Calendar.HOUR_OF_DAY)) + ":" + (MainWindow.decimalFormat.format(calEditTimes.get(Calendar.MINUTE))  + MainWindow.sTabTime)));

                        // Update label text when slider values changes
                        sldHoursHS.valueProperty().addListener(new ChangeListener<Number>() {
                            @Override
                            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                                lblEditTimeHS.setText(MainWindow.decimalFormat.format(newValue) + ":" + MainWindow.decimalFormat.format(sldMinutesHS.getValue())  + MainWindow.sTabTime);
                            }
                        });
                        // Update label text when slider values changes
                        sldMinutesHS.valueProperty().addListener(new ChangeListener<Number>() {
                            @Override
                            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {

                                lblEditTimeHS.setText(MainWindow.decimalFormat.format(sldHoursHS.getValue()) + ":" + MainWindow.decimalFormat.format(newValue) +  MainWindow.sTabTime);
                            }
                        });
                        // Round double value to an integer value on value changing
                        sldHoursHS.valueChangingProperty().addListener(new ChangeListener<Boolean>() {
                            @Override
                            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                                sldHoursHS.setValue((int) sldHoursHS.getValue());
                            }
                        });
                        // Round double value to an integer value on value changing
                        sldMinutesHS.valueChangingProperty().addListener(new ChangeListener<Boolean>() {
                            @Override
                            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                                sldMinutesHS.setValue((int) sldMinutesHS.getValue());
                            }
                        });

                        break;
                }
            }
        }


        btnSaveEditTimes.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {

                // Get the hours and minutes from the sliders
                Calendar cal = new GregorianCalendar(util.Date.timeZone);

                for (eIngestionTime eTime : eIngestionTime.values()){

                    Date timeDefinedByUser;
                    switch (eTime) {
                        case MORNING:
                            cal.set(Calendar.HOUR_OF_DAY, (int) sldHoursPCM.getValue());
                            cal.set(Calendar.MINUTE, (int) sldMinutesPCM.getValue());
                            timeDefinedByUser = cal.getTime();
                            p.writeIngestionTimeMorning(timeDefinedByUser);

                        case MIDDAY:
                            cal.set(Calendar.HOUR_OF_DAY, (int) sldHoursPCD.getValue());
                            cal.set(Calendar.MINUTE, (int) sldMinutesPCD.getValue());
                            timeDefinedByUser = cal.getTime();
                            p.writeIngestionTimeMidday(timeDefinedByUser);
                            break;

                        case EVENING:
                            cal.set(Calendar.HOUR_OF_DAY, (int) sldHoursPCV.getValue());
                            cal.set(Calendar.MINUTE, (int) sldMinutesPCV.getValue());
                            timeDefinedByUser = cal.getTime();
                            p.writeIngestionTimeEvening(timeDefinedByUser);
                            break;

                        case NIGHT:
                            cal.set(Calendar.HOUR_OF_DAY, (int) sldHoursHS.getValue());
                            cal.set(Calendar.MINUTE, (int) sldMinutesHS.getValue());
                            timeDefinedByUser = cal.getTime();
                            p.writeIngestionTimeNight(timeDefinedByUser);
                            break;
                    }
                }

                // We need an EXTERNAL close request in order to get the onCloseListener in the calling class activated
                s.fireEvent(
                        new WindowEvent(s, WindowEvent.WINDOW_CLOSE_REQUEST )
                );

            }
        });

        btnCloseEditTimes.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {

                // We need an EXTERNAL close request in order to get the onCloseListener in the calling class activated
                s.fireEvent(
                        new WindowEvent(s, WindowEvent.WINDOW_CLOSE_REQUEST )
                );
            }
        });
    }
}
