import com.calendarfx.view.YearMonthView;
import javafx.collections.SetChangeListener;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.input.MouseEvent;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class NewDailyRoutineController extends Controller {

    @FXML
    public YearMonthView datePicker;

    private static LocalDate startDate = null, endDate = null;

    public static List<CustomDateCell> customDateCells = new ArrayList<>();
    public static List<LocalDate> selectedDates = new ArrayList<>();

    @Override
    public void setup() {
        super.setup();

        datePicker.setCellFactory(param ->{
            CustomDateCell dateCell = new CustomDateCell(param.getDate());

            /*dateCell.setOnMouseClicked(event -> {
                dateCell.clicked = !dateCell.clicked;
                dateCell.setStyle(dateCell.clicked ? "-fx-background-color: cyan;" : dateCell.getStyle());

                if (startDate == null && endDate == null) {
                    startDate = dateCell.getDate();
                    reset();
                } else if (startDate != null && endDate == null)
                    endDate = dateCell.getDate();

                if (startDate != null && endDate != null) {
                    if (startDate.isAfter(endDate)) {
                        LocalDate tmp = endDate;
                        endDate = startDate;
                        startDate = tmp;
                    }

                    selectedDates.addAll(Stream.iterate(startDate, date -> date.plusDays(1)).limit(ChronoUnit.DAYS.between(startDate, endDate) + 1).collect(Collectors.toList()));
                    System.out.println(selectedDates);

                    setRange();

                    startDate = endDate = null;
                }
            });*/

            return dateCell;
        });


        datePicker.getSelectedDates().addListener((SetChangeListener<? super LocalDate>) change -> {
            if (change.wasAdded())
                System.out.println(change.getElementAdded());
        });
    }

    private void setRange () {
        customDateCells.forEach(customDateCell -> {
            customDateCell.setStyle(
                    selectedDates.contains(customDateCell.getDate()) &&
                    !customDateCell.getDate().equals(startDate) &&
                    !customDateCell.getDate().equals(endDate)? "-fx-background-color: green;" : customDateCell.getStyle());
        });
    }
}
