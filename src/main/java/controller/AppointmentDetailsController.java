package controller;

import com.calendarfx.model.Entry;
import com.calendarfx.view.TimeField;
import com.calendarfx.view.print.TimeRangeField;
import data_structures.AppointmentProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.TextFieldListCell;

import java.time.LocalDate;
import java.time.LocalTime;

public class AppointmentDetailsController extends Controller {

    @FXML
    TextField titleField;

    @FXML
    TimeField timeClock;

    @FXML
    TimeRangeField dateField;

    @FXML
    ListView<String> detailList;

    @FXML
    Button addButton;

    private Entry<?> appointmentPropertyEntry;
    private AppointmentProperty appointment;

    @Override
    public void setup (Entry<?> appointmentEntry) {
        appointmentPropertyEntry = appointmentEntry;
        appointment= (AppointmentProperty)appointmentEntry.getUserObject();

        timeClock.setValue(appointment.getLocalTimeObjectProperty());
        timeClock.valueProperty().addListener(localTimeChangeListener);

        dateField.setOnDate(appointment.getLocalDateObjectProperty());
        dateField.onDateProperty().addListener(localDateChangeListener);

        titleField.textProperty().bindBidirectional(appointment.titleProperty());

        /*detailList.setCellFactory(param -> {
            ListCell<String> listCell = new TextFieldListCell<String>(){
                @Override
                public void commitEdit(String newValue) {
                    super.commitEdit(newValue);
                }
            };


            listCell.setOnMousePressed(event -> {
                if (event.getButton() == MouseButton.SECONDARY) {
                    detailList.getItems().remove(selectedIndex);

                    detailList.layout();
                    detailList.edit(detailList.getItems().size() - 1);
                }
            });

            return listCell;
        });*/
        detailList.setCellFactory(TextFieldListCell.forListView());

        detailList.setItems(appointment.getDetailListProperty());

        addButton.setOnAction(event -> {
            detailList.getItems().add("");
            detailList.scrollTo(detailList.getItems().size() - 1);
            detailList.layout();
            detailList.edit(detailList.getItems().size() - 1);
        });
    }

    public void reset () {
        titleField.textProperty().unbindBidirectional(appointment.titleProperty());
        timeClock.valueProperty().removeListener(localTimeChangeListener);
        dateField.onDateProperty().removeListener(localDateChangeListener);
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
