package custom;

import com.calendarfx.model.Entry;
import com.calendarfx.view.popover.PopOverContentPane;
import com.calendarfx.view.popover.PopOverTitledPane;
import controller.AppointmentDetailsController;
import javafx.scene.layout.Pane;
import org.controlsfx.control.PopOver;
import utility.ScreenHandler;

public class CustomEntryPopover extends PopOverContentPane {

    public CustomEntryPopover(Entry<?> entry) {

        System.out.println(entry.getUserObject().toString());

        ScreenHandler.ScreenInfo screenInfo = ScreenHandler.getInstance().getScreenInfo("AppointmentDetails");
        Pane pane = screenInfo.getPane();

        PopOverTitledPane popOverTitledPane = new PopOverTitledPane("Termin", pane);
        AppointmentDetailsController appointmentDetailsController = (AppointmentDetailsController)screenInfo.getController();
        appointmentDetailsController.setup(entry);

        getPanes().addAll(popOverTitledPane);
        setExpandedPane(popOverTitledPane);
    }
}