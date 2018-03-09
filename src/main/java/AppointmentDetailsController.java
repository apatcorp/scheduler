import com.calendarfx.model.Entry;
import com.calendarfx.view.TimeField;
import com.calendarfx.view.print.TimeRangeField;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
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

    @FXML
    Button deleteButton;

    private int selectedIndex = 0;

    @Override
    public void setup (Entry<?> appointmentEntry) {
        Appointment appointment = (Appointment)appointmentEntry.getUserObject();

        Bindings.bindBidirectional(titleField.textProperty(), appointmentEntry.titleProperty());

        LocalTime time = LocalTime.of(appointment.getAppointmentTime().getHour(), appointment.getAppointmentTime().getMinute(), 0, 0);
        timeClock.setValue(time);
        timeClock.valueProperty().addListener((observable, oldValue, newValue) -> {
            AppointmentTime appointmentTime = new AppointmentTime(newValue.getHour(), newValue.getMinute());
            appointment.setAppointmentTime(appointmentTime);

            LocalTime start = LocalTime.of(newValue.getHour(), newValue.getMinute(), newValue.getSecond());
            LocalTime end = LocalTime.of(newValue.getHour() + 1, newValue.getMinute(), newValue.getSecond());

            appointmentEntry.changeStartTime(start);
            appointmentEntry.changeEndTime(end);
        });

        LocalDate date = LocalDate.of(appointment.getAppointmentDate().getYear(), appointment.getAppointmentDate().getMonth(), appointment.getAppointmentDate().getDate());
        dateField.setOnDate(date);
        dateField.onDateProperty().addListener((observable, oldValue, newValue) -> {
            AppointmentDate appointmentDate = new AppointmentDate(newValue.getDayOfMonth(), newValue.getMonthValue(), newValue.getYear());
            appointment.setAppointmentDate(appointmentDate);

            LocalDate start = LocalDate.of(newValue.getYear(), newValue.getMonthValue(), newValue.getDayOfMonth());
            appointmentEntry.changeStartDate(start, true);
        });

        titleField.setText(appointment.getAppointmentTitle());
        titleField.textProperty().addListener((observable, oldValue, newValue) -> appointment.setAppointmentTitle(newValue));

        detailList.setCellFactory(TextFieldListCell.forListView());

        detailList.setItems(FXCollections.observableList(appointment.getAppointmentDetails()));

        detailList.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            String detail = observable.getValue();
            selectedIndex = detailList.getItems().indexOf(detail);
            deleteButton.setVisible(true);
        });

        addButton.setOnAction(event -> {
            detailList.getItems().add("");
            detailList.scrollTo(detailList.getItems().size() - 1);
            detailList.layout();
            detailList.edit(detailList.getItems().size() - 1);
        });

        deleteButton.setVisible(false);
        deleteButton.setOnAction(event -> detailList.getItems().remove(selectedIndex));
    }
}
