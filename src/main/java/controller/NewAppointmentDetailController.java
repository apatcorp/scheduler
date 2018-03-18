package controller;

import com.calendarfx.view.TimeField;
import com.calendarfx.view.YearMonthView;
import custom.CustomDateCell;
import data_structures.DailyRoutineWrapper;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.TextFieldListCell;
import javafx.scene.input.KeyCode;

import java.util.List;

public class NewAppointmentDetailController extends Controller {

    @FXML
    public TextField titleEditText;

    @FXML
    public TimeField timeEditText;

    @FXML
    YearMonthView dateField;

    @FXML
    public ListView<String> detailList;

    @FXML
    public Button addDetailButton;

    private DailyRoutineWrapper dailyRoutineWrapper = null;

    public void setup(DailyRoutineWrapper _dailyRoutineWrapper) {
        if (dailyRoutineWrapper != null)
            reset();

        dailyRoutineWrapper = _dailyRoutineWrapper;

        System.out.println(dailyRoutineWrapper.toString());

        titleEditText.textProperty().bindBidirectional(dailyRoutineWrapper.getAppointmentProperty().titleProperty());
        timeEditText.valueProperty().bindBidirectional(dailyRoutineWrapper.getAppointmentProperty().localTimeObjectProperty());
        dateField.getSelectedDates().addAll(dailyRoutineWrapper.getLocalDateList());
        dateField.setCellFactory(param -> new CustomDateCell(dateField));
        if (dailyRoutineWrapper.getLocalDateList().size() > 0)
            dateField.setDate(dailyRoutineWrapper.getLocalDateList().get(0));

        detailList.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        detailList.setCellFactory(TextFieldListCell.forListView());
        detailList.setItems(dailyRoutineWrapper.getAppointmentProperty().getDetailListProperty());

        detailList.setOnKeyPressed(event -> {
            if (event.getCode().equals(KeyCode.DELETE)) {
                List<String> selectedItems = detailList.getSelectionModel().getSelectedItems();

                if (selectedItems.size() > 0) {
                    detailList.getItems().removeAll(selectedItems);
                }
            }
        });

        addDetailButton.setOnAction(event -> {
            detailList.getItems().add("");
            detailList.scrollTo(detailList.getItems().size() - 1);
            detailList.layout();
            detailList.edit(detailList.getItems().size() - 1);
        });
    }

    private void reset () {
        titleEditText.textProperty().unbindBidirectional(dailyRoutineWrapper.getAppointmentProperty().titleProperty());
        timeEditText.valueProperty().unbindBidirectional(dailyRoutineWrapper.getAppointmentProperty().localTimeObjectProperty());
    }
}
