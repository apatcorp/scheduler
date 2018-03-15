package controller;

import com.calendarfx.view.TimeField;
import data_structures.Appointment;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.TextFieldListCell;
import utility.Utility;

public class NewAppointmentDetailController extends Controller {

    @FXML
    public TextField titleEditText;

    @FXML
    public TimeField timeEditText;

    @FXML
    public ListView<String> detailList;

    @FXML
    public Button addDetailButton;

    Appointment appointment;

    @Override
    public void setup(Appointment appointment) {
        super.setup();

        this.appointment = appointment;

        titleEditText.setText(this.appointment.getAppointmentTitle());

        timeEditText.setValue(Utility.appointmentTimeToLocalTime(this.appointment.getAppointmentTime()));

        detailList.setCellFactory(TextFieldListCell.forListView());
        ObservableList<String> details = FXCollections.observableList(appointment.getAppointmentDetails());
        detailList.setItems(details);
        detailList.layout();

        addDetailButton.setOnAction(event -> {
            detailList.getItems().add("");
            detailList.scrollTo(detailList.getItems().size() - 1);
            detailList.layout();
            detailList.edit(detailList.getItems().size() - 1);
        });
    }
}
