import custom.CustomDatePicker;
import javafx.fxml.FXML;
import javafx.scene.control.DateCell;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class NewDailyRoutineController extends Controller {

    @FXML
    public CustomDatePicker datePicker;

    private List<LocalDate> selectedDates = new ArrayList<>();

    @Override
    public void setup() {
        super.setup();

        datePicker.setOnMouseEntered(event -> {
            if (datePicker.isShowing()) {
                datePicker.setDateSelected(false);
                datePicker.hide();
            }
        });

        datePicker.setOnAction(event -> {
            if (!selectedDates.contains(datePicker.getValue()))
                selectedDates.add(datePicker.getValue());
            else
                selectedDates.remove(datePicker.getValue());

            datePicker.setDateSelected(true);

            System.out.println("Test");
        });

        datePicker.setDayCellFactory(param ->
            new DateCell() {
                @Override
                public void updateItem(LocalDate item, boolean empty) {
                    super.updateItem(item, empty);
                    setStyle(selectedDates.contains(item) ? "-fx-background-color: cyan;" : "");
                    setDisabled(false);
                    setEditable(true);

                }
            });
    }
}
