package custom;

import com.calendarfx.view.YearMonthView;
import javafx.scene.input.*;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class CustomDateCell extends YearMonthView.DateCell {

    private YearMonthView datePicker;
    private List<LocalDate> selectedDates;
    private List<LocalDate> highlightedDates;

    private String standardStyle;
    private String selectedStyle = "-fx-background-color: #17A2B8;";
    private String highlightedStyle = "-fx-background-color: #d75466;";

    public CustomDateCell(YearMonthView datePicker, List<LocalDate> selectedDate) {
        this.datePicker = datePicker;
        this.selectedDates = selectedDate;
        highlightedDates = new ArrayList<>();

        standardStyle = getStyle();

        setup();
    }

    public CustomDateCell(YearMonthView datePicker, List<LocalDate> selectedDates, List<LocalDate> highlightedDates) {
        this.datePicker = datePicker;
        this.selectedDates = selectedDates;
        this.highlightedDates = new ArrayList<>();
        this.highlightedDates.addAll(highlightedDates);

        standardStyle = getStyle();

        setup();
    }

    @Override
    protected void update(LocalDate date) {
        super.update(date);

        setDisable(date.isBefore(LocalDate.now()));

        if (datePicker.getSelectedDates().contains(date)) {
            setStyle(selectedStyle);
        } else if (highlightedDates.contains(date)){
            setStyle(highlightedStyle);
        } else {
            setStyle(standardStyle);
        }
    }

    private void setup () {
        setOnMouseClicked(event -> {
            selectedDates.clear();
            selectedDates.addAll(datePicker.getSelectedDates());
            if (event.getButton().equals(MouseButton.PRIMARY)) {
                if ((event.isAltDown() && event.isControlDown())) {
                    if (datePicker.getSelectedDates().size() == 2) {
                        setRange();
                    }
                }
            }
        });
    }

    private void setRange () {
        LocalDate startDate = datePicker.getSelectedDates().stream().reduce((localDate, localDate2) -> localDate).get();
        LocalDate endDate = datePicker.getSelectedDates().stream().reduce((localDate, localDate2) -> localDate2).get();
        if (startDate.isAfter(endDate)) {
            LocalDate tmp = endDate;
            endDate = startDate;
            startDate = tmp;
        }

        datePicker.getSelectedDates().addAll(Stream.iterate(startDate, date -> date.plusDays(1)).limit(ChronoUnit.DAYS.between(startDate, endDate) + 1).collect(Collectors.toList()));

        selectedDates.clear();
        selectedDates.addAll(datePicker.getSelectedDates());
    }
}