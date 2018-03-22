package custom;

import com.calendarfx.model.Entry;
import com.calendarfx.view.popover.PopOverContentPane;
import com.calendarfx.view.popover.PopOverTitledPane;
import controller.Controller;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.Pane;

import java.io.IOException;

public class CustomEntryPopover extends PopOverContentPane {

    public CustomEntryPopover(Entry<?> entry) {

        System.out.println(entry.getUserObject().toString());

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getClassLoader().getResource("fxml/appointment_view.fxml"));
        try {
            Pane pane = fxmlLoader.load();

            PopOverTitledPane popOverTitledPane = new PopOverTitledPane("Termin", pane);
            Controller controller = fxmlLoader.getController();
            controller.setup(entry);

            getPanes().clear();
            getPanes().addAll(popOverTitledPane);
            setExpandedPane(popOverTitledPane);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}