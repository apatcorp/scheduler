package controller;

import com.calendarfx.model.Calendar;
import com.calendarfx.model.CalendarSource;
import com.calendarfx.model.Entry;
import com.calendarfx.view.DateControl;
import com.calendarfx.view.page.WeekPage;
import com.calendarfx.view.popover.EntryPopOverContentPane;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.google.cloud.firestore.QuerySnapshot;
import custom.CustomContextMenu;
import custom.CustomEntryPopover;
import custom.NewAppointmentEntry;
import data_structures.Appointment;
import data_structures.AppointmentProperty;
import data_structures.DailyRoutine;
import database.SchedulerDB;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.Pane;
import main.Main;
import org.controlsfx.control.PopOver;
import utility.ScreenHandler;

import java.net.URL;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;

public class AppointmentOverviewController extends Controller implements Initializable {

    @FXML
    public WeekPage weekPage;

    @FXML
    public MenuItem newAppointment;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // remove all calendar resources
        weekPage.getCalendarSources().remove(0);

        // create new calendar
        Calendar calendar = new Calendar("Appointment");
        calendar.setShortName("App");
        calendar.setStyle(Calendar.Style.STYLE3);

        // pull daily routines from the database
        List<DailyRoutine> dailyRoutines = getDailyRoutinesFromDatabase();
        for (DailyRoutine dailyRoutine: dailyRoutines) {
            List<Appointment> appointments = new ArrayList<>(dailyRoutine.getAppointments().values());
            // create new appointment entry for each appointment
            for (Appointment appointment: appointments) {
                Entry<AppointmentProperty> appointmentEntry = new NewAppointmentEntry(appointment);
                calendar.addEntry(appointmentEntry);
            }
        }

        // create new calendar resource and add calendar resource with custom calendar to the week page
        CalendarSource calendarSource = new CalendarSource("Appointments");
        calendarSource.getCalendars().add(calendar);
        weekPage.getCalendarSources().add(calendarSource);

        // set create entry factory callback
        weekPage.setEntryFactory(param -> new NewAppointmentEntry(param.getZonedDateTime()));
        weekPage.setDefaultCalendarProvider(param -> calendar);
        // set custom popover on double click factory callback
        weekPage.setEntryDetailsPopOverContentCallback(param -> new CustomEntryPopover(param.getEntry(), param.getPopOver()));

        // set custom context menus on right click
        weekPage.setEntryContextMenuCallback(param -> new CustomContextMenu(param.getEntry(), param.getCalendar()));
        weekPage.setContextMenuCallback(param -> new CustomContextMenu(param.getZonedDateTime(), param.getCalendar()));

        newAppointment.setOnAction(event -> {
            PopOver popOver = new PopOver();
            ScreenHandler.ScreenInfo screenInfo = ScreenHandler.getInstance().getScreenInfo("NewDailyRoutine");
            Pane pane = screenInfo.getPane();
            Controller controller = screenInfo.getController();
            controller.setup();

            popOver.setAnimated(true);
            popOver.setArrowSize(0);
            popOver.setTitle("Neuer Tagesablauf");
            popOver.setCloseButtonEnabled(true);
            popOver.setHeaderAlwaysVisible(true);
            popOver.setDetached(true);

            popOver.setContentNode(pane);
            popOver.show(Main.scene.getWindow());
        });
    }

    private List<DailyRoutine> getDailyRoutinesFromDatabase () {
        List<DailyRoutine> dailyRoutines = new ArrayList<>();

        ApiFuture<QuerySnapshot> future = SchedulerDB.getDB().collection(SchedulerDB.DB_NAME).get();

        List<QueryDocumentSnapshot> documentSnapshots;
        try {
            documentSnapshots = future.get().getDocuments();

            for (QueryDocumentSnapshot documentSnapshot: documentSnapshots) {
                DailyRoutine dailyRoutine = documentSnapshot.toObject(DailyRoutine.class);
                dailyRoutines.add(dailyRoutine);
            }
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }

        return dailyRoutines;
    }
}
