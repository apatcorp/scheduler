package controller;

import com.calendarfx.model.Entry;
import com.calendarfx.view.TimeField;
import data_structures.AppointmentProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.TextFieldListCell;
import javafx.scene.input.MouseButton;
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

    @FXML
    Button deleteButton;

    private Entry<?> appointmentPropertyEntry;
    private AppointmentProperty appointment;

    private int selectedItemIndex = -1;

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

        detailList.setCellFactory(param -> {
            ListCell<String> listCell = new TextFieldListCell<>(new StringConverter<String>() {
                @Override
                public String toString(String object) {
                    return object.trim();
                }

                @Override
                public String fromString(String string) {
                    return string.trim();
                }
            });

            listCell.setOnMousePressed(event -> {
                if (event.getButton() == MouseButton.SECONDARY) {
                    detailList.getItems().remove(listCell.getItem());
                    detailList.layout();
                }

                deleteButton.setVisible(true);
            });

            return listCell;
        });

        detailList.setItems(appointment.getDetailListProperty());

        addButton.setOnAction(event -> {
            detailList.getItems().add("");
            detailList.scrollTo(detailList.getItems().size() - 1);
            detailList.layout();
            detailList.edit(detailList.getItems().size() - 1);
        });

        deleteButton.setVisible(false);
        deleteButton.setOnAction(event -> {
            selectedItemIndex = detailList.getSelectionModel().getSelectedIndex();
            if (selectedItemIndex > -1) {
                detailList.getItems().remove(selectedItemIndex);
                detailList.layout();
            }
        });
    }

    public void reset () {
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
