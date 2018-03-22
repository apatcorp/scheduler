package controller;

import com.calendarfx.model.Calendar;
import com.calendarfx.model.Entry;
import com.calendarfx.view.YearMonthView;
import com.jfoenix.controls.*;
import custom.CustomDateCell;
import custom.NewAppointmentEntry;
import data_structures.AppointmentProperty;
import data_structures.DailyRoutineWrapper;
import javafx.animation.FadeTransition;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.DateCell;
import javafx.scene.control.SelectionMode;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Paint;
import javafx.util.Duration;
import org.controlsfx.control.Notifications;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class NewEntriesController extends Controller {

    @FXML
    private YearMonthView datePicker;

    @FXML
    private JFXToggleButton templateToggle;

    @FXML
    private HBox templateBox;

    @FXML
    private VBox detailViewBox;

    @FXML
    private JFXDatePicker templateDatePicker;

    @FXML
    private JFXListView<DailyRoutineWrapper> appointmentsList;

    @FXML
    private JFXButton newAppointmentButton;

    @FXML
    private JFXButton createEntriesButton;

    private Calendar calendar;

    private List<LocalDate> selectedDates;
    private List<LocalDate> allEntryDates;

    @Override
    void setup(Calendar calendar) {
        this.calendar = calendar;

        // fetch all entry dates in the calendar
        fetchAllEntryDates();

        // setup main date picker
        setupMainDatePicker();

        // setup template
        setupTemplate();

        // setup the appointments list
        setupAppointmentsList();

        // setup add new appointment and new entries button
        setupNewAppointmentButton();
        setupCreateEntriesButton();
    }

    private void fetchAllEntryDates () {
        if (allEntryDates == null)
            allEntryDates = new ArrayList<>();
        else
            allEntryDates.clear();

        List<Entry<?>> entries = calendar.findEntries("");
        for (Entry entry: entries) {
            if (!allEntryDates.contains(entry.getStartDate()))
                allEntryDates.add(entry.getStartDate());
        }
    }

    private void addEntriesWithDateToAppointmentList (LocalDate localDate) {
        Map<LocalDate, List<Entry<?>>> listMap = calendar.findEntries(localDate, localDate, ZoneId.systemDefault());
        List<Entry<?>> entries = listMap.getOrDefault(localDate, new ArrayList<>());

        for (Entry entry: entries) {
            AppointmentProperty appointmentProperty = (AppointmentProperty)entry.getUserObject();
            DailyRoutineWrapper dailyRoutineWrapper = new DailyRoutineWrapper(appointmentProperty, selectedDates);
            if (!appointmentsList.getItems().contains(dailyRoutineWrapper)) {
                appointmentsList.getItems().add(dailyRoutineWrapper);
            }
        }
    }

    private void removeEntriesWithDateFromAppointmentList (LocalDate localDate) {
        for (DailyRoutineWrapper dailyRoutineWrapper: appointmentsList.getItems()) {
            if (dailyRoutineWrapper.getAppointmentProperty().getLocalDateObjectProperty().equals(localDate))
                appointmentsList.getItems().remove(dailyRoutineWrapper);
        }
    }

    private void setupMainDatePicker () {
        if (selectedDates == null)
            selectedDates = new ArrayList<>();

        datePicker.setCellFactory(param -> new CustomDateCell(param, selectedDates, allEntryDates, LocalDate.now(), LocalDate.MAX));
        if (datePicker.getSelectedDates().size() > 0) {
            LocalDate first = datePicker.getSelectedDates().stream().reduce((localDate, localDate2) -> localDate).get();
            datePicker.setDate(first);
        }
    }

    private void setupTemplate () {
        templateToggle.selectedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                if (!newValue && templateDatePicker.getValue() != null) {
                    // remove all entries with date from template dae picker from appointments list
                    removeEntriesWithDateFromAppointmentList(templateDatePicker.getValue());
                }
                templateBox.setVisible(newValue);
            }
        });

        templateDatePicker.setDayCellFactory(param -> new DateCell() {
            @Override
            public void updateItem(LocalDate item, boolean empty) {
                super.updateItem(item, empty);

                setDisable(!allEntryDates.contains(item));
            }
        });

        templateDatePicker.valueProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                addEntriesWithDateToAppointmentList(newValue);
            }
        });
    }

    private void setupAppointmentsList () {
        appointmentsList.setVerticalGap(5d);
        appointmentsList.setExpanded(true);

        appointmentsList.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        appointmentsList.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                System.out.println(newValue.toString());

                // delete old view
                if (detailViewBox.getChildren().size() > 1)
                    detailViewBox.getChildren().remove(detailViewBox.getChildren().size() - 1);

                // open new entry details view and pass the daily routine wrapper
                try {
                    FXMLLoader fxmlLoader = new FXMLLoader(getClass().getClassLoader().getResource("fxml/new_entry_details_view.fxml"));
                    Pane pane = fxmlLoader.load();
                    Controller controller = fxmlLoader.getController();
                    controller.setup(newValue);

                    pane.setOpacity(0);
                    FadeTransition fadeIn = new FadeTransition(Duration.seconds(0.25), pane);
                    fadeIn.setCycleCount(1);
                    fadeIn.setFromValue(0);
                    fadeIn.setToValue(1);
                    detailViewBox.getChildren().add(pane);
                    fadeIn.play();

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        appointmentsList.setCellFactory(param -> new CustomJFXObjectListCell());
    }

    private void setupNewAppointmentButton () {
        newAppointmentButton.setOnAction(event -> {
            // create new appointment
            LocalDate localDate = LocalDate.now();
            LocalTime localTime = LocalTime.now();

            // create appointment property and wrap it inside custom daily routine wrapper class
            AppointmentProperty appointment = new AppointmentProperty("Neuer Termin", localDate, localTime, new ArrayList<>());
            DailyRoutineWrapper dailyRoutineWrapper = new DailyRoutineWrapper(appointment, selectedDates);
            // add new appointment to the appointment list view
            appointmentsList.getItems().add(dailyRoutineWrapper);
            // scroll to latest entry in the appointment list view
            appointmentsList.scrollTo(appointmentsList.getItems().size() - 1);
        });
    }

    private void setupCreateEntriesButton () {
        createEntriesButton.setOnAction(event -> {

            List<Entry<?>> newEntries = new ArrayList<>();

            for (DailyRoutineWrapper dailyRoutineWrapper: appointmentsList.getItems()) {
                for (LocalDate localDate: dailyRoutineWrapper.getLocalDateList()) {
                    Entry<AppointmentProperty> appointmentEntry = new NewAppointmentEntry(dailyRoutineWrapper.getAppointmentProperty(), localDate);

                    Map<LocalDate, List<Entry<?>>> entryMap = calendar.findEntries(localDate, localDate, ZoneId.systemDefault());
                    List<Entry<?>> entries = entryMap.get(localDate);

                    calendar.removeEntries(entries);
                    newEntries.add(appointmentEntry);
                }
            }

            calendar.addEntries(newEntries);

            // show success info panel
            ImageView imageView = new ImageView("file:img/success.png");
            imageView.setFitHeight(40);
            imageView.setFitWidth(40);

            Notifications notificationBuilder = Notifications.create();

            notificationBuilder.title("Bestätigung");
            notificationBuilder.text("Tagesabläufe erfolgreich erstellt");
            notificationBuilder.hideAfter(Duration.seconds(3));
            notificationBuilder.position(Pos.TOP_RIGHT);
            notificationBuilder.darkStyle();
            notificationBuilder.graphic(imageView);
            notificationBuilder.show();

            appointmentsList.getItems().clear();
            selectedDates.clear();
        });
    }

    private class CustomJFXObjectListCell extends JFXListCell<DailyRoutineWrapper> {

        CustomJFXObjectListCell() {
            super();
            setupPopup();
        }

        @Override
        protected void updateItem(DailyRoutineWrapper item, boolean empty) {
            DailyRoutineWrapper oldItem = getItem();
            if (oldItem != null)
                textProperty().unbind();

            super.updateItem(item, empty);

            if (!empty && item != null)
                textProperty().bind(item.getAppointmentProperty().titleProperty());
            else {
                textProperty().unbind();
                setText("");
            }
        }

        private void setupPopup () {
            JFXButton button = new JFXButton("Löschen");
            button.setButtonType(JFXButton.ButtonType.RAISED);
            button.setRipplerFill(Paint.valueOf("#9a9a9a"));
            button.setPadding(new Insets(10));

            VBox vBox = new VBox(button);

            JFXPopup popup = new JFXPopup();
            popup.setPopupContent(vBox);

            setOnMouseClicked(event -> {
                if (event.getButton().equals(MouseButton.SECONDARY)) {
                    if (getListView().getSelectionModel().getSelectedItems().size() > 1)
                        popup.show(getListView(), JFXPopup.PopupVPosition.TOP, JFXPopup.PopupHPosition.RIGHT);
                    else
                        popup.show(this, JFXPopup.PopupVPosition.TOP, JFXPopup.PopupHPosition.RIGHT);
                }
            });

            button.setOnAction(event -> {
                getListView().getItems().removeAll(getListView().getSelectionModel().getSelectedItems());
                popup.hide();
            });
        }
    }
}
