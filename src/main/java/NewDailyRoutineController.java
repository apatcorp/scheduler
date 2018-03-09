import custom.CustomDatePicker;
import javafx.fxml.FXML;
import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import javafx.util.Callback;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class NewDailyRoutineController extends Controller {

    @FXML
    public CustomDatePicker datePicker;

    private boolean dateSelected = false;

    @Override
    public void setup() {
        super.setup();

        List<LocalDate> localDates = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            LocalDate localDate = LocalDate.of(2018, 3, i + 10);
            localDates.add(localDate);
        }

        datePicker.setOnMouseEntered(event -> {
            if (datePicker.isShowing()) {
                datePicker.selectedDate = false;
                datePicker.hide();
            }
        });

        datePicker.setOnAction(event -> {
            if (!localDates.contains(datePicker.getValue()))
                localDates.add(datePicker.getValue());
            else
                localDates.remove(datePicker.getValue());

            datePicker.selectedDate = true;
        });

        datePicker.setDayCellFactory(new Callback<DatePicker, DateCell>() {
            @Override
            public DateCell call(DatePicker param) {
                return new DateCell() {
                    @Override
                    public void updateItem(LocalDate item, boolean empty) {
                        boolean alreadySelected = localDates.contains(item);
                        setStyle(alreadySelected ? "-fx-background-color: #09a30f;" : "");

                        super.updateItem(item, empty);
                    }
                };
            }
        });
    }
}
