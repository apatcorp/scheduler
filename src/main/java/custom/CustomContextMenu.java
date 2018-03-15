package custom;

import com.calendarfx.model.Calendar;
import com.calendarfx.model.Entry;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;

import java.time.ZonedDateTime;

public class CustomContextMenu extends ContextMenu {

    public CustomContextMenu(ZonedDateTime zonedDateTime, Calendar calendar) {
        MenuItem menuItem = new MenuItem("Create New Appointment");

        menuItem.setOnAction(event -> {
            Entry<?> newEntry = new NewAppointmentEntry(zonedDateTime);
            calendar.addEntry(newEntry);
        });

        getItems().add(menuItem);
    }
}
