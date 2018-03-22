package controller;

import com.calendarfx.view.YearMonthView;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXListView;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.JFXTimePicker;
import custom.CustomDateCell;
import custom.CustomListCell;
import data_structures.DailyRoutineWrapper;
import javafx.fxml.FXML;
import javafx.scene.control.SelectionMode;
import javafx.scene.input.KeyCode;
import javafx.util.StringConverter;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Comparator;
import java.util.List;

public class NewEntryDetailsController extends Controller {

    @FXML
    private JFXTextField titleField;

    @FXML
    private JFXTimePicker timePicker;

    @FXML
    private YearMonthView detailDatePicker;

    @FXML
    private JFXButton addDetailButton;

    @FXML
    private JFXListView<String> detailsList;

    private DailyRoutineWrapper dailyRoutineWrapper;

    @Override
    void setup(DailyRoutineWrapper _dailyRoutineWrapper) {
        if (dailyRoutineWrapper != null)
            reset();

        dailyRoutineWrapper = _dailyRoutineWrapper;

        titleField.textProperty().bindBidirectional(dailyRoutineWrapper.getAppointmentProperty().titleProperty());
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
        timePicker.valueProperty().bindBidirectional(dailyRoutineWrapper.getAppointmentProperty().localTimeObjectProperty());

        detailDatePicker.getSelectedDates().clear();
        detailDatePicker.getSelectedDates().addAll(dailyRoutineWrapper.getLocalDateList());
        detailDatePicker.setCellFactory(param -> new CustomDateCell(param, dailyRoutineWrapper.getLocalDateList(), LocalDate.now(), LocalDate.MAX));
        if (dailyRoutineWrapper.getLocalDateList().size() > 0) {
            LocalDate min = dailyRoutineWrapper.getLocalDateList().stream().min(Comparator.naturalOrder()).get();
            detailDatePicker.setDate(min);
        }

        detailsList.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        detailsList.setCellFactory(param -> new CustomListCell(new StringConverter<String>() {
            @Override
            public String toString(String object) {
                if (object != null)
                    return object.trim();
                else
                    return "";
            }

            @Override
            public String fromString(String string) {
                return string.trim();
            }
        }));
        detailsList.setItems(dailyRoutineWrapper.getAppointmentProperty().getDetailListProperty());

        detailsList.setOnKeyPressed(event -> {
            if (event.getCode().equals(KeyCode.DELETE)) {
                List<String> selectedItems = detailsList.getSelectionModel().getSelectedItems();

                if (selectedItems.size() > 0) {
                    detailsList.getItems().removeAll(selectedItems);
                }
            }
        });

        addDetailButton.setOnAction(event -> {
            detailsList.getItems().add("");
            detailsList.scrollTo(detailsList.getItems().size() - 1);
            detailsList.edit(detailsList.getItems().size() - 1);
        });
    }

    private void reset () {
        titleField.textProperty().unbindBidirectional(dailyRoutineWrapper.getAppointmentProperty().titleProperty());
        timePicker.valueProperty().unbindBidirectional(dailyRoutineWrapper.getAppointmentProperty().localTimeObjectProperty());
    }
}
