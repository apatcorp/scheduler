import com.calendarfx.model.Calendar;
import com.calendarfx.model.Entry;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;

import java.time.ZonedDateTime;

class CustomContextMenu extends ContextMenu {

    CustomContextMenu(ZonedDateTime zonedDateTime, Calendar calendar) {
        MenuItem menuItem = new MenuItem("Create New Appointment");

        menuItem.setOnAction(event -> {
            Entry<?> newEntry = new NewAppointmentEntry(zonedDateTime).createNewAppointmentEntry();
            calendar.addEntry(newEntry);
        });

        getItems().add(menuItem);
    }
}
