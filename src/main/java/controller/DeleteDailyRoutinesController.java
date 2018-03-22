package controller;

import com.calendarfx.model.Calendar;
import com.calendarfx.model.Entry;
import com.calendarfx.view.YearMonthView;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialog;
import custom.CustomDateCell;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.image.ImageView;
import javafx.util.Duration;
import org.controlsfx.control.Notifications;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class DeleteDailyRoutinesController extends Controller {

    @FXML
    YearMonthView deletePicker;

    @FXML
    JFXButton deleteRoutines;

    private Calendar calendar;
    private JFXDialog dialog;
    private List<LocalDate> dailyRoutineDates;
    private List<LocalDate> selectedDates;

    @Override
    public void setup(JFXDialog dialog, Calendar calendar) {
        super.setup();

        this.calendar = calendar;
        this.dialog = dialog;

        dailyRoutineDates = new ArrayList<>();
        selectedDates = new ArrayList<>();
        List<Entry<?>> entries = calendar.findEntries("");
        for (Entry entry: entries) {
            if (!dailyRoutineDates.contains(entry.getStartDate()))
                dailyRoutineDates.add(entry.getStartDate());
        }

        Collections.sort(dailyRoutineDates);
        LocalDate min = dailyRoutineDates.stream().reduce((localDate, localDate2) -> localDate).get();
        LocalDate max = dailyRoutineDates.stream().reduce((localDate, localDate2) -> localDate2).get();

        deletePicker.setCellFactory(param -> new CustomDateCell(deletePicker, selectedDates, dailyRoutineDates, min, max));
        deleteRoutines.setOnAction(event -> showSuccessNotification());
    }

    private void showSuccessNotification () {

        for (LocalDate date: selectedDates) {
            Map<LocalDate, List<Entry<?>>> map = calendar.findEntries(date, date, ZoneId.systemDefault());
            List<Entry<?>> entries = map.get(date);
            calendar.removeEntries(entries);
        }

        ImageView imageView = new ImageView("file:img/success.png");
        imageView.setFitHeight(40);
        imageView.setFitWidth(40);

        Notifications notificationBuilder = Notifications.create();

        notificationBuilder.title("Bestätigung");
        notificationBuilder.text("Tagesabläufe erfolgreich gelöscht");
        notificationBuilder.hideAfter(Duration.seconds(3));
        notificationBuilder.position(Pos.TOP_RIGHT);
        notificationBuilder.darkStyle();
        notificationBuilder.graphic(imageView);
        notificationBuilder.show();

        dialog.close();
    }
}
