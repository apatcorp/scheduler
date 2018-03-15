package controller;

import com.calendarfx.view.YearMonthView;
import custom.CustomDateCell;
import data_structures.Appointment;
import data_structures.AppointmentDate;
import data_structures.AppointmentTime;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;

import org.controlsfx.control.MasterDetailPane;
import utility.ScreenHandler;

import java.util.ArrayList;
import java.util.List;

public class NewDailyRoutineController extends Controller {

    @FXML
    public MasterDetailPane masterDetailPane;

    @FXML
    public YearMonthView datePicker;

    @FXML
    public Button newAppointment;

    private ListView<Appointment> appointmentListView;
    private List<Appointment> appointments = new ArrayList<>();

    private NewAppointmentDetailController newAppointmentDetailController;

    @Override
    public void setup() {
        super.setup();

        appointmentListView = new ListView<>();

        appointmentListView.setCellFactory(param -> new ListCell<Appointment>() {

            @Override
            protected void updateItem(Appointment item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setText(null);
                } else
                    setText(item.getAppointmentTitle());
            }
        });

        ScreenHandler.ScreenInfo screenInfo = ScreenHandler.getInstance().getScreenInfo("NewAppointmentDetail");

        masterDetailPane.setMasterNode(appointmentListView);
        masterDetailPane.setDetailNode(screenInfo.getPane());
        newAppointmentDetailController = (NewAppointmentDetailController)screenInfo.getController();

        masterDetailPane.setShowDetailNode(false);

        datePicker.setCellFactory(param -> new CustomDateCell(param.getDate()));

        newAppointment.setOnAction(event -> {
            AppointmentDate appointmentDate = new AppointmentDate(1, 1, 2000);
            AppointmentTime appointmentTime = new AppointmentTime(0,0);
            Appointment appointment = new Appointment("New Appointment", appointmentDate, appointmentTime, new ArrayList<>());
            appointments.add(appointment);

            appointmentListView.getItems().add(appointment);
            appointmentListView.scrollTo(appointmentListView.getItems().size() - 1);
            appointmentListView.layout();
            appointmentListView.edit(appointmentListView.getItems().size() - 1);
        });

        appointmentListView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {

            newAppointmentDetailController.setup(newValue);
            masterDetailPane.setShowDetailNode(true);

        });

    }
}
