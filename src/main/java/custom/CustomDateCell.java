package custom;

import com.calendarfx.view.YearMonthView;
import javafx.scene.control.SelectionMode;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class CustomDateCell extends YearMonthView.DateCell {

    private YearMonthView datePicker;
    private List<LocalDate> highlightedDates;

    private String standardStyle, selectedStyle, rangeSelectedStyle, highlightedStyle;

    public CustomDateCell(YearMonthView datePicker) {
        this.datePicker = datePicker;
        highlightedDates = new ArrayList<>();

        standardStyle = getStyle();
        selectedStyle = "-fx-background-color: #60E8AD;";
        rangeSelectedStyle = "-fx-background-color: #B0E0E6;";
        highlightedStyle = "-fx-background-color: #E9967A;";

        this.setOnMouseClicked(event -> {
            if (datePicker.getSelectedDates().size() == 2) {
                setRange();
            }
        });
    }

    public CustomDateCell(YearMonthView datePicker, List<LocalDate> highlightedDates) {
        this.datePicker = datePicker;
        this.highlightedDates = new ArrayList<>();
        this.highlightedDates.addAll(highlightedDates);

        standardStyle = getStyle();
        selectedStyle = "-fx-background-color: #60E8AD;";
        rangeSelectedStyle = "-fx-background-color: #B0E0E6;";
        highlightedStyle = "-fx-background-color: #E9967A;";
        datePicker.setSelectionMode(SelectionMode.MULTIPLE);

        this.setOnMouseClicked(event -> {
            if (datePicker.getSelectedDates().size() == 2) {
                setRange();
            }
        });
    }

    @Override
    protected void update(LocalDate date) {
        super.update(date);

        setDisable(date.isBefore(LocalDate.now()));

        if (datePicker.getSelectedDates().contains(date)) {
            LocalDate first = datePicker.getSelectedDates().stream().reduce((localDate, localDate2) -> localDate).get();
            LocalDate last = datePicker.getSelectedDates().stream().reduce((localDate, localDate2) -> localDate2).get();
            setStyle(first.equals(date) || last.equals(date) ? selectedStyle: rangeSelectedStyle);
        } else if (highlightedDates.contains(date)){
            setStyle(highlightedStyle);
        } else {
            setStyle(standardStyle);
        }
    }

    private void setRange () {
        LocalDate startDate = datePicker.getSelectedDates().stream().reduce((localDate, localDate2) -> localDate).get();
        LocalDate endDate = datePicker.getSelectedDates().stream().reduce((localDate, localDate2) -> localDate2).get();

        if (startDate.isAfter(endDate)) {
            LocalDate tmp = endDate;
            endDate = startDate;
            startDate = tmp;
        }

        datePicker.getSelectedDates().clear();
        datePicker.getSelectedDates().addAll(Stream.iterate(startDate, date -> date.plusDays(1)).limit(ChronoUnit.DAYS.between(startDate, endDate) + 1).collect(Collectors.toList()));
    }
}