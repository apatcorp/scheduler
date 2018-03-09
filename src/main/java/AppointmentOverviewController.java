import com.calendarfx.model.Calendar;
import com.calendarfx.model.CalendarSource;
import com.calendarfx.model.Entry;
import com.calendarfx.view.page.WeekPage;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.google.cloud.firestore.QuerySnapshot;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.BorderPane;
import javafx.stage.Popup;
import org.controlsfx.control.MasterDetailPane;
import org.controlsfx.control.PopOver;

import java.net.URL;
import java.util.*;
import java.util.concurrent.ExecutionException;

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
        calendar.setStyle(Calendar.Style.STYLE2);

        // pull daily routines from the database
        List<DailyRoutine> dailyRoutines = getDailyRoutinesFromDatabase();
        for (DailyRoutine dailyRoutine: dailyRoutines) {
            List<Appointment> appointments = new ArrayList<>(dailyRoutine.getAppointments().values());
            // create new appointment entry for each appointment
            for (Appointment appointment: appointments) {
                Entry<?> appointmentEntry = new NewAppointmentEntry(appointment).createNewAppointmentEntry();
                calendar.addEntry(appointmentEntry);
            }
        }

        // create new calendar resource and add calendar resource with custom calendar to the week page
        CalendarSource calendarSource = new CalendarSource("Appointments");
        calendarSource.getCalendars().add(calendar);
        weekPage.getCalendarSources().add(calendarSource);

        // set create entry factory callback
        weekPage.setEntryFactory(param -> new NewAppointmentEntry(param.getZonedDateTime()).createNewAppointmentEntry());
        weekPage.setDefaultCalendarProvider(param -> calendar);
        // set custom popover on double click factory callback
        weekPage.setEntryDetailsPopOverContentCallback(param -> new EntryPopover(param.getEntry()));

        // set custom context menus on right click
        weekPage.setContextMenuCallback(param -> new CustomContextMenu(param.getZonedDateTime(), param.getCalendar()));

        newAppointment.setOnAction(event -> {
            PopOver popOver = new PopOver();
            popOver.setAnimated(true);
            BorderPane borderPane = (BorderPane)ScreenHandler.getInstance().getScreenInfo("NewDailyRoutine").getPane();
            Controller controller = ScreenHandler.getInstance().getScreenInfo("NewDailyRoutine").getController();
            controller.setup();

            popOver.setContentNode(borderPane);
            popOver.setArrowSize(0);
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
