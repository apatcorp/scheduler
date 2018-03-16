package custom;

import com.calendarfx.model.Calendar;
import com.calendarfx.model.Entry;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import org.controlsfx.control.PopOver;

import java.time.ZonedDateTime;

public class CustomContextMenu extends ContextMenu {

    public CustomContextMenu(ZonedDateTime zonedDateTime, Calendar calendar) {
        MenuItem menuItem = new MenuItem("Neuer Termin");

        menuItem.setOnAction(event -> {
            Entry<?> newEntry = new NewAppointmentEntry(zonedDateTime);
            calendar.addEntry(newEntry);
        });

        getItems().add(menuItem);
    }


    public CustomContextMenu(Entry<?> entry, Calendar calendar) {
        MenuItem menuItemDelete = new MenuItem("Löschen");

        menuItemDelete.setOnAction(event -> calendar.removeEntry(entry));

        getItems().add(menuItemDelete);
    }
}
