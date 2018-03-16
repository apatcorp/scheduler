package controller;

import com.calendarfx.view.YearMonthView;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.google.cloud.firestore.QuerySnapshot;
import data_structures.Appointment;
import data_structures.AppointmentProperty;
import data_structures.DailyRoutine;
import data_structures.DailyRoutineWrapper;
import database.SchedulerDB;
import interfaces.IDataReceiver;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.SetChangeListener;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;

import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import org.controlsfx.control.MasterDetailPane;
import org.controlsfx.control.ToggleSwitch;
import utility.ScreenHandler;
import utility.Utility;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class NewDailyRoutineController extends Controller implements IDataReceiver {

    @FXML
    public MasterDetailPane masterDetailPane;

    @FXML
    public YearMonthView datePicker;

    @FXML
    public HBox templateBox;

    @FXML
    public ToggleSwitch templateToggle;

    @FXML
    public YearMonthView templateDatePicker;

    @FXML
    public Button newAppointment;

    private ListView<DailyRoutineWrapper> appointmentListView;
    private List<DailyRoutineWrapper> dailyRoutines = new ArrayList<>();
    private List<LocalDate> selectedDates = new ArrayList<>();

    private NewAppointmentDetailController newAppointmentDetailController;

    @Override
    public void setup() {
        super.setup();

        appointmentListView = new ListView<>();
        appointmentListView.setCellFactory(param -> new ListCell<DailyRoutineWrapper>() {

            @Override
            protected void updateItem(DailyRoutineWrapper item, boolean empty) {

                DailyRoutineWrapper oldItem = getItem();
                if (oldItem != null)
                    textProperty().unbind();

                super.updateItem(item, empty);

                if (!empty && item != null)
                    textProperty().bind(item.getAppointmentProperty().titleProperty());

            }
        });

        templateToggle.selectedProperty().addListener((observable, oldValue, newValue) -> {
            templateBox.setVisible(newValue);
        });

        templateDatePicker.getSelectedDates().addListener((SetChangeListener<? super LocalDate>) change -> {
            fetchAppointments(change.getElementAdded());
        });

        ScreenHandler.ScreenInfo screenInfo = ScreenHandler.getInstance().getScreenInfo("NewAppointmentDetail");
        Pane pane = screenInfo.getPane();
        newAppointmentDetailController = (NewAppointmentDetailController)screenInfo.getController();

        masterDetailPane.setMasterNode(appointmentListView);
        masterDetailPane.setDetailNode(pane);
        masterDetailPane.setShowDetailNode(false);

        datePicker.setCellFactory(param -> new DateCellCustom(datePicker, selectedDates));

        newAppointment.setOnAction(event -> {
            LocalDate localDate = LocalDate.now();
            LocalTime localTime = LocalTime.now();

            AppointmentProperty appointment = new AppointmentProperty("New Appointment", localDate, localTime, new ArrayList<>());
            DailyRoutineWrapper dailyRoutineWrapper = new DailyRoutineWrapper(appointment, selectedDates);
            System.out.println(selectedDates);
            dailyRoutines.add(dailyRoutineWrapper);

            appointmentListView.getItems().add(dailyRoutineWrapper);

            appointmentListView.scrollTo(appointmentListView.getItems().size() - 1);
            appointmentListView.layout();
        });

        appointmentListView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            masterDetailPane.setShowDetailNode(true);
            newAppointmentDetailController.setup(newValue);
        });

    }

    private void fetchAppointments (LocalDate localDate) {

        String documentID = Utility.getDocumentIDFromLocalDate(localDate);

        DocumentReference documentReference = SchedulerDB.getDB().collection(SchedulerDB.DB_NAME).document(documentID);

        documentReference.addSnapshotListener((documentSnapshot, e) -> {
            DailyRoutine dailyRoutine = Objects.requireNonNull(documentSnapshot).toObject(DailyRoutine.class);

            OnDataReceived(dailyRoutine);
        });
    }

    @Override
    public void OnDataReceived(DailyRoutine dailyRoutine) {
        List<Appointment> appointments = new ArrayList<>(dailyRoutine.getAppointments().values());

        for (Appointment appointment: appointments) {
            AppointmentProperty appointmentProperty = new AppointmentProperty(appointment);
            DailyRoutineWrapper dailyRoutineWrapper = new DailyRoutineWrapper(appointmentProperty, selectedDates);

            appointmentListView.getItems().add(dailyRoutineWrapper);
        }
    }

    public static class DateCellCustom extends YearMonthView.DateCell {

        private List<LocalDate> selectedDates;

        DateCellCustom(YearMonthView yearMonthView, List<LocalDate> selectedDates) {
            super();

            this.selectedDates = selectedDates;

            this.setOnMouseClicked(event -> {
                if (selectedDates.size() > 2) {
                    selectedDates.clear();
                }

                if (selectedDates.contains(this.getDate())) {
                    yearMonthView.getSelectedDates().remove(getDate());
                } else {
                    selectedDates.add(this.getDate());

                }
                if (selectedDates.size() == 2) {
                    setRange();
                }
                yearMonthView.refreshData();
            });
        }

        @Override
        protected void update(LocalDate date) {
            super.update(date);

            if (selectedDates.contains(date)) {
                setStyle(selectedDates.get(0).equals(date) || selectedDates.get(selectedDates.size() - 1).equals(date) ? "-fx-background-color: #60E8AD;": "-fx-background-color: #B0E0E6;");
            } else {
                setStyle("");
            }
        }

        private void setRange () {
            LocalDate startDate = selectedDates.get(0);
            LocalDate endDate = selectedDates.get(1);

            if (startDate.isAfter(endDate)) {
                LocalDate tmp = endDate;
                endDate = startDate;
                startDate = tmp;
            }

            selectedDates.clear();
            selectedDates.addAll(Stream.iterate(startDate, date -> date.plusDays(1)).limit(ChronoUnit.DAYS.between(startDate, endDate) + 1).collect(Collectors.toList()));

            //System.out.println(selectedDates.toString());
        }
    }
}
