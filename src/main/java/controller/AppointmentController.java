package controller;

import com.calendarfx.model.Entry;
import com.jfoenix.controls.*;
import custom.CustomJFXListCell;
import data_structures.AppointmentProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;

import javafx.scene.control.*;
import javafx.util.StringConverter;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Objects;

public class AppointmentController extends Controller {

    @FXML
    JFXTextField titleField;

    @FXML
    JFXTimePicker timePicker;

    @FXML
    JFXDatePicker datePicker;

    @FXML
    JFXButton addDetailButton;

    @FXML
    JFXListView<String> detailsList;

    private Entry<?> appointmentPropertyEntry;
    private AppointmentProperty appointment;

    public void setup (Entry<?> appointmentEntry) {
        if (appointment != null && appointmentEntry != null)
            reset();

        appointmentPropertyEntry = appointmentEntry;
        appointment = (AppointmentProperty) Objects.requireNonNull(appointmentEntry).getUserObject();

        timePicker.setIs24HourView(true);
        timePicker.setConverter(new StringConverter<LocalTime>() {
            @Override
            public String toString(LocalTime object) {
                return object.toString();
            }

            @Override
            public LocalTime fromString(String string) {
                return LocalTime.parse(string);
            }
        });
        timePicker.setValue(appointment.getLocalTimeObjectProperty());
        timePicker.valueProperty().addListener(localTimeChangeListener);

        datePicker.setValue(appointment.getLocalDateObjectProperty());
        datePicker.valueProperty().addListener(localDateChangeListener);

        titleField.textProperty().bindBidirectional(appointment.titleProperty());

        detailsList.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        detailsList.setExpanded(true);
        detailsList.setShowTooltip(false);
        detailsList.setVerticalGap(5d);

        detailsList.setCellFactory(param -> new CustomJFXListCell());
        detailsList.setItems(appointment.getDetailListProperty());

        addDetailButton.setOnAction(event -> {
            detailsList.getItems().add("");
            detailsList.scrollTo(detailsList.getItems().size() - 1);
            detailsList.edit(detailsList.getItems().size() - 1);
        });
    }

    private void reset () {
        titleField.textProperty().unbindBidirectional(appointment.titleProperty());
        timePicker.valueProperty().removeListener(localTimeChangeListener);
        datePicker.valueProperty().removeListener(localDateChangeListener);
    }

    private ChangeListener<LocalTime> localTimeChangeListener = new ChangeListener<LocalTime>() {
        @Override
        public void changed(ObservableValue<? extends LocalTime> observable, LocalTime oldValue, LocalTime newValue) {
            appointment.localTimeObjectProperty().setValue(newValue);
            appointmentPropertyEntry.changeStartTime(newValue, true);
        }
    };

    private ChangeListener<LocalDate> localDateChangeListener = new ChangeListener<LocalDate>() {
        @Override
        public void changed(ObservableValue<? extends LocalDate> observable, LocalDate oldValue, LocalDate newValue) {
            appointment.localDateObjectProperty().setValue(newValue);
            appointmentPropertyEntry.changeStartDate(newValue, true);
        }
    };
}
