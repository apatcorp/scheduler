package custom;

import com.calendarfx.view.YearMonthView;
import javafx.scene.input.*;

import javax.annotation.Nullable;
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

    private LocalDate min, max;

    public CustomDateCell(YearMonthView datePicker, List<LocalDate> selectedDate, @Nullable LocalDate min, @Nullable LocalDate max) {
        this.datePicker = datePicker;
        this.selectedDates = selectedDate;
        highlightedDates = new ArrayList<>();

        standardStyle = getStyle();

        this.min = min;
        this.max = max;

        if (this.min == null)
            this.min = LocalDate.now();

        setup();
    }

    public CustomDateCell(YearMonthView datePicker, List<LocalDate> selectedDates, List<LocalDate> highlightedDates, @Nullable LocalDate min, @Nullable LocalDate max) {
        this.datePicker = datePicker;
        this.selectedDates = selectedDates;
        this.highlightedDates = new ArrayList<>();
        this.highlightedDates.addAll(highlightedDates);

        standardStyle = getStyle();

        this.min = min;
        this.max = max;

        if (this.min == null)
            this.min = LocalDate.now();

        setup();
    }

    @Override
    protected void update(LocalDate date) {
        super.update(date);

        setDisable(date.isBefore(min));

        if (max != null)
            setDisable(date.isAfter(max));

        if (datePicker.getSelectedDates().contains(date)) {
            setStyle("-fx-background-color: #008B8B;");
        } else if (highlightedDates.contains(date)){
            setStyle("-fx-background-color: #d75466;");
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