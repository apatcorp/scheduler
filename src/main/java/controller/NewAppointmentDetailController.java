package controller;

import com.calendarfx.view.TimeField;
import com.calendarfx.view.YearMonthView;
import data_structures.AppointmentProperty;
import data_structures.DailyRoutineWrapper;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.TextFieldListCell;

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
        dateField.setCellFactory(param -> new NewDailyRoutineController.DateCellCustom(dateField, dailyRoutineWrapper.getLocalDateList()));

        detailList.setCellFactory(TextFieldListCell.forListView());
        detailList.setItems(dailyRoutineWrapper.getAppointmentProperty().getDetailListProperty());

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
