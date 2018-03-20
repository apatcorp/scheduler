package controller;

import com.calendarfx.model.Calendar;
import com.calendarfx.model.Entry;
import com.calendarfx.view.YearMonthView;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import custom.CustomDateCell;
import custom.NewAppointmentEntry;
import data_structures.Appointment;
import data_structures.AppointmentProperty;
import data_structures.DailyRoutine;
import data_structures.DailyRoutineWrapper;
import database.SchedulerDB;
import javafx.application.Platform;
import javafx.collections.SetChangeListener;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import main.Main;
import org.controlsfx.control.MasterDetailPane;
import org.controlsfx.control.ToggleSwitch;
import utility.ScreenHandler;
import utility.Utility;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.*;
import java.util.concurrent.ExecutionException;


public class NewDailyRoutineController extends Controller {

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
    public Button newAppointmentButton;

    @FXML
    public Button createEntriesButton;

    @FXML
    public ProgressIndicator progressbar;

    private ListView<DailyRoutineWrapper> appointmentListView;
    private List<LocalDate> selectedDates = new ArrayList<>();
    private List<LocalDate> dailyRoutineDates = new ArrayList<>();

    private NewAppointmentDetailController newAppointmentDetailController;

    private boolean loading;

    @Override
    public void setup(Calendar calendar) {
        super.setup();

        // setup progressbar
        setupProgressbar();

        // setup list of appointments
        setupAppointmentListView();

        // setup template date picker
        setupTemplateDatePicker();

        // setup master detail pane
        setupMasterDetailPane();

        // setup appointment date picker
        fetchAllDailyRoutines();

        newAppointmentButton.setOnAction(event -> {
            // define local date and local time for new appointment
            LocalDate localDate = LocalDate.now();
            LocalTime localTime = LocalTime.now();

            // create appointment and wrap it inside custom daily routine class
            AppointmentProperty appointment = new AppointmentProperty("Neuer Termin", localDate, localTime, new ArrayList<>());
            DailyRoutineWrapper dailyRoutineWrapper = new DailyRoutineWrapper(appointment, selectedDates);
            // add new appointment to the appointment list view
            appointmentListView.getItems().add(dailyRoutineWrapper);
            // scroll to latest entry in the appointment list view
            appointmentListView.scrollTo(appointmentListView.getItems().size() - 1);
            appointmentListView.layout();
        });

        createEntriesButton.setOnAction(event -> {
            for (DailyRoutineWrapper dailyRoutineWrapper: appointmentListView.getItems()) {
                for (LocalDate localDate: dailyRoutineWrapper.getLocalDateList()) {
                    Entry<AppointmentProperty> appointmentEntry = new NewAppointmentEntry(dailyRoutineWrapper.getAppointmentProperty(), localDate);

                    Map<LocalDate, List<Entry<?>>> entryMap = calendar.findEntries(localDate, localDate, ZoneId.systemDefault());
                    List<Entry<?>> entries = entryMap.get(localDate);

                    calendar.removeEntries(entries);
                    calendar.addEntry(appointmentEntry);
                }
            }

            pushToServer(calendar);

            fetchAllDailyRoutines();
            appointmentListView.getItems().clear();
            selectedDates.clear();
        });
    }

    private void pushToServer (Calendar calendar) {
        for (Entry entry: calendar.findEntries("")) {
            System.out.println(entry.getStartDate());
        }
    }

    private void fetchAppointments (LocalDate localDate) {
        String documentID = Utility.getDocumentIDFromLocalDate(localDate);
        final ApiFuture<DocumentSnapshot> query = SchedulerDB.getDB().collection(SchedulerDB.DB_NAME).document(documentID).get();

        // loading section
        isLoading(true);

        new Thread(() -> {
            try {
                DocumentSnapshot documentSnapshot = query.get();
                if (documentSnapshot != null) {
                    DailyRoutine dailyRoutine = documentSnapshot.toObject(DailyRoutine.class);

                    Platform.runLater(() -> {
                        if (dailyRoutine != null) {
                            Platform.runLater(() -> onDataReceived(dailyRoutine));
                        } else {
                            isLoading(false);
                        }
                    });
                } else {
                    isLoading(false);
                }

            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
                isLoading(false);
            }
        }).start();
    }

    private void fetchAllDailyRoutines () {
        ApiFuture<QuerySnapshot> query = SchedulerDB.getDB().collection(SchedulerDB.DB_NAME).get();

        // loading section
        isLoading(true);

        new Thread(() -> {
            try {
                QuerySnapshot snapshots = query.get();
                List<QueryDocumentSnapshot> documents = snapshots.getDocuments();

                for (QueryDocumentSnapshot documentSnapshot: documents) {
                    DailyRoutine dailyRoutine = documentSnapshot.toObject(DailyRoutine.class);
                    LocalDate date = Utility.localDateFromAppointmentDate(dailyRoutine.getAppointmentDate());
                    if (!dailyRoutineDates.contains(date))
                        dailyRoutineDates.add(date);
                }

                Platform.runLater(this::onDataReceived);
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
                isLoading(false);
            }

        }).start();
    }

    private void onDataReceived(DailyRoutine dailyRoutine) {
        List<Appointment> appointments = new ArrayList<>(dailyRoutine.getAppointments().values());

        for (Appointment appointment: appointments) {
            AppointmentProperty appointmentProperty = new AppointmentProperty(appointment);
            DailyRoutineWrapper dailyRoutineWrapper = new DailyRoutineWrapper(appointmentProperty, selectedDates);
            if (!appointmentListView.getItems().contains(dailyRoutineWrapper)) {
                appointmentListView.getItems().add(dailyRoutineWrapper);
            }
        }

        isLoading(false);
    }

    private void onDataReceived() {
        // setup template date picker
        templateDatePicker.setCellFactory(param -> new YearMonthView.DateCell() {
            @Override
            protected void update(LocalDate date) {
                super.update(date);

                boolean outside = !dailyRoutineDates.contains(date);
                setDisable(outside);
            }
        });

        datePicker.setCellFactory(param -> new CustomDateCell(datePicker, selectedDates, dailyRoutineDates));
        if (datePicker.getSelectedDates().size() > 0) {
            LocalDate first = datePicker.getSelectedDates().stream().reduce((localDate, localDate2) -> localDate).get();
            datePicker.setDate(first);
        } else if (dailyRoutineDates.size() > 0) {
            datePicker.setDate(dailyRoutineDates.get(0));
        }

        isLoading(false);
    }

    private void setupProgressbar () {
        loading = false;
        progressbar.setProgress(-1);
        progressbar.setVisible(loading);
    }

    private void setupAppointmentListView () {
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
                else {
                    textProperty().unbind();
                    setText("");
                }
            }
        });

        appointmentListView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        appointmentListView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                System.out.println(newValue.toString());
                newAppointmentDetailController.setup(newValue);
            }
            masterDetailPane.setShowDetailNode(newValue != null);
        });

        appointmentListView.setOnKeyPressed(event -> {
            if (event.getCode().equals(KeyCode.DELETE)) {
                List<DailyRoutineWrapper> selectedItems = appointmentListView.getSelectionModel().getSelectedItems();

                if (selectedItems.size() > 0) {
                    appointmentListView.getItems().removeAll(selectedItems);
                }
            }
        });
    }

    private void setupTemplateDatePicker () {
        templateToggle.selectedProperty().addListener((observable, oldValue, newValue) -> {
            if (observable.getValue() != null && observable.getValue()) {
                fetchAllDailyRoutines();
            }
            templateBox.setVisible(observable.getValue());
        });

        templateDatePicker.getSelectedDates().addListener((SetChangeListener<? super LocalDate>) change -> {
            if (change.getElementAdded() != null) fetchAppointments(change.getElementAdded());
        });
    }

    private void setupMasterDetailPane () {
        ScreenHandler.ScreenInfo screenInfo = ScreenHandler.getInstance().getScreenInfo("NewAppointmentDetail");
        Pane pane = screenInfo.getPane();
        newAppointmentDetailController = (NewAppointmentDetailController)screenInfo.getController();

        masterDetailPane.setMasterNode(appointmentListView);
        masterDetailPane.setDetailNode(pane);
        masterDetailPane.setShowDetailNode(false);
    }

    private void isLoading (boolean value) {
        loading = value;
        progressbar.setVisible(loading);

        // enable/disable controls depending on current loading state
        appointmentListView.setDisable(loading);
        datePicker.setDisable(loading);
        templateDatePicker.setDisable(loading);
        templateToggle.setDisable(loading);
        newAppointmentButton.setDisable(loading);
        createEntriesButton.setDisable(loading);
    }
}
