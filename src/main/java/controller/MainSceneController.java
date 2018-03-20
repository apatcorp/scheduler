package controller;

import com.calendarfx.model.Calendar;
import com.calendarfx.model.CalendarSource;
import com.calendarfx.model.Entry;
import com.calendarfx.view.CalendarView;
import custom.CustomContextMenu;
import custom.CustomEntryPopover;
import custom.NewAppointmentEntry;
import data_structures.Appointment;
import data_structures.AppointmentProperty;
import data_structures.DailyRoutine;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import main.Main;
import org.controlsfx.control.PopOver;
import utility.ScreenHandler;

import java.awt.event.KeyEvent;
import java.util.*;

public class MainSceneController extends Controller  {

    @FXML
    public MenuItem newAppointment;

    @FXML
    public VBox parent;

    @Override
    public void setup(List<DailyRoutine> dailyRoutines) {

        CalendarView calendarView = new CalendarView();
        parent.getChildren().add(calendarView);

        calendarView.setShowAddCalendarButton(false);
        calendarView.setShowPrintButton(false);
        calendarView.setShowDeveloperConsole(false);
        calendarView.setShowPageToolBarControls(false);
        calendarView.setShowSearchField(false);

        calendarView.getCalendarSources().remove(0);

        // create new calendar
        Calendar calendar = new Calendar("Termin");
        calendar.setShortName("Termin");
        calendar.setStyle(Calendar.Style.STYLE1);

        // pull daily routines from the database
        for (DailyRoutine dailyRoutine : dailyRoutines) {
            List<Appointment> appointments = new ArrayList<>(dailyRoutine.getAppointments().values());
            // create new appointment entry for each appointment
            for (Appointment appointment : appointments) {
                Entry<AppointmentProperty> appointmentEntry = new NewAppointmentEntry(appointment);
                calendar.addEntry(appointmentEntry);
            }
        }

        // create new calendar resource and add calendar resource with custom calendar to the week page
        CalendarSource calendarSource = new CalendarSource("Tagesabläufe");
        calendarSource.getCalendars().add(calendar);
        calendarView.getCalendarSources().add(calendarSource);

        // set create entry factory callback
        calendarView.setEntryFactory(param -> new NewAppointmentEntry(param.getZonedDateTime()));
        calendarView.setDefaultCalendarProvider(param -> calendar);
        // set custom popover on double click factory callback
        calendarView.setEntryDetailsPopOverContentCallback(param -> new CustomEntryPopover(param.getEntry()));

        // set custom context menus on right click
        calendarView.setEntryContextMenuCallback(param -> new CustomContextMenu(param.getEntry(), param.getCalendar()));
        calendarView.setContextMenuCallback(param -> new CustomContextMenu(param.getZonedDateTime(), param.getCalendar()));

        newAppointment.setOnAction(event -> {
            PopOver popOver = new PopOver();
            ScreenHandler.ScreenInfo screenInfo = ScreenHandler.getInstance().getScreenInfo("NewDailyRoutine");
            Pane pane = screenInfo.getPane();
            Controller controller = screenInfo.getController();
            controller.setup(calendar);

            popOver.setAnimated(true);
            popOver.setArrowSize(0);
            popOver.setTitle("Neuer Tagesablauf");
            popOver.setCloseButtonEnabled(true);
            popOver.setHeaderAlwaysVisible(true);

            popOver.setContentNode(pane);
            popOver.show(Main.scene.getWindow());
        });
    }
}
