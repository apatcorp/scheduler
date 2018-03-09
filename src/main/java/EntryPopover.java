import com.calendarfx.model.Entry;
import com.calendarfx.view.popover.PopOverContentPane;
import com.calendarfx.view.popover.PopOverTitledPane;
import javafx.scene.layout.Pane;

class EntryPopover extends PopOverContentPane {

    EntryPopover(Entry<?> entry) {

        ScreenHandler.ScreenInfo screenInfo = ScreenHandler.getInstance().getScreenInfo("AppointmentDetails");
        Pane borderPane = screenInfo.getPane();

        PopOverTitledPane popOverTitledPane = new PopOverTitledPane("Appointment", borderPane);

        Controller appointmentDetailsController = screenInfo.getController();
        appointmentDetailsController.setup(entry);

        getPanes().addAll(popOverTitledPane);
        setExpandedPane(popOverTitledPane);
    }
}
