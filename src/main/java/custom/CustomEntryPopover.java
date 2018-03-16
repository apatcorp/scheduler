package custom;

import com.calendarfx.model.Entry;
import com.calendarfx.view.popover.PopOverContentPane;
import com.calendarfx.view.popover.PopOverTitledPane;
import controller.AppointmentDetailsController;
import javafx.scene.layout.Pane;
import org.controlsfx.control.PopOver;
import utility.ScreenHandler;

public class CustomEntryPopover extends PopOverContentPane {

    public CustomEntryPopover(Entry<?> entry, PopOver popOver) {

        System.out.println(entry.getUserObject().toString());

        ScreenHandler.ScreenInfo screenInfo = ScreenHandler.getInstance().getScreenInfo("AppointmentDetails");
        Pane borderPane = screenInfo.getPane();
        popOver.setContentNode(borderPane);

        PopOverTitledPane popOverTitledPane = new PopOverTitledPane("Appointment", popOver.getContentNode());
        AppointmentDetailsController appointmentDetailsController = (AppointmentDetailsController)screenInfo.getController();
        appointmentDetailsController.setup(entry);

        popOver.showingProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) {
                appointmentDetailsController.reset();
            }
        });

        getPanes().addAll(popOverTitledPane);
        setExpandedPane(popOverTitledPane);
    }


}
