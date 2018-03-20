package controller;

import com.calendarfx.model.Entry;
import com.calendarfx.view.TimeField;
import custom.CustomListCell;
import data_structures.AppointmentProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;

import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.util.StringConverter;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Objects;

public class AppointmentDetailsController extends Controller {

    @FXML
    TextField titleField;

    @FXML
    TimeField timeField;

    @FXML
    DatePicker dateField;

    @FXML
    ListView<String> detailList;

    @FXML
    Button addButton;

    private Entry<?> appointmentPropertyEntry;
    private AppointmentProperty appointment;

    @Override
    public void setup (Entry<?> appointmentEntry) {
        if (appointment != null && appointmentEntry != null)
            reset();

        appointmentPropertyEntry = appointmentEntry;
        appointment = (AppointmentProperty) Objects.requireNonNull(appointmentEntry).getUserObject();

        timeField.setValue(appointment.getLocalTimeObjectProperty());
        timeField.valueProperty().addListener(localTimeChangeListener);

        dateField.setValue(appointment.getLocalDateObjectProperty());
        dateField.valueProperty().addListener(localDateChangeListener);

        titleField.textProperty().bindBidirectional(appointment.titleProperty());

        detailList.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        detailList.setCellFactory(param -> new CustomListCell(new StringConverter<String>() {
            @Override
            public String toString(String object) {
                return object.trim();
            }

            @Override
            public String fromString(String string) {
                return string.trim();
            }
        }));

        detailList.setItems(appointment.getDetailListProperty());

        addButton.setOnAction(event -> {
            detailList.getItems().add("");
            detailList.scrollTo(detailList.getItems().size() - 1);
            detailList.layout();
            detailList.edit(detailList.getItems().size() - 1);
        });
    }

    private void reset () {
        titleField.textProperty().unbindBidirectional(appointment.titleProperty());
        timeField.valueProperty().removeListener(localTimeChangeListener);
        dateField.valueProperty().removeListener(localDateChangeListener);
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
