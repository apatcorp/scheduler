package controller;

import com.calendarfx.model.Calendar;
import com.calendarfx.model.CalendarSource;
import com.calendarfx.model.Entry;
import com.calendarfx.view.CalendarView;
import com.jfoenix.controls.JFXDialog;
import custom.CustomContextMenu;
import custom.CustomEntryPopover;
import custom.NewAppointmentEntry;
import data_structures.Appointment;
import data_structures.AppointmentProperty;
import data_structures.DailyRoutine;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SplitPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.*;

public class MainSceneController extends Controller  {

    @FXML
    private MenuItem newAppointment;

    @FXML
    private MenuItem deleteAppointments;

    @FXML
    private VBox parent;

    @FXML
    private StackPane root;

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
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getClassLoader().getResource("fxml/new_entries_view.fxml"));
            try {
                SplitPane pane = fxmlLoader.load();
                Controller controller = fxmlLoader.getController();
                controller.setup(calendar);

                // setup dialog window
                Stage stage = new Stage();
                stage.setTitle("Neue Termine erstellen");
                stage.setAlwaysOnTop(true);
                stage.setResizable(true);
                stage.setMinWidth(pane.getWidth());
                stage.setMinHeight(pane.getHeight());

                Scene scene = new Scene(pane);
                scene.setRoot(pane);
                stage.setScene(scene);
                stage.show();

                stage.setMinWidth(pane.getWidth());
                stage.setMinHeight(pane.getHeight());
                scene.getWindow().sizeToScene();

                SplitPane.setResizableWithParent(pane, Boolean.TRUE);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        deleteAppointments.setOnAction(event -> {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getClassLoader().getResource("fxml/delete_daily_routines_view.fxml"));
            try {
                JFXDialog dialog = new JFXDialog();

                Pane pane = fxmlLoader.load();
                Controller controller = fxmlLoader.getController();
                controller.setup(dialog, calendar);

                dialog.setDialogContainer(root);
                dialog.setContent(pane);
                dialog.show(root);

            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }
}
