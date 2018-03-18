package custom;

import com.calendarfx.view.YearMonthView;
import javafx.scene.control.SelectionMode;
import javafx.scene.input.MouseButton;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class CustomDateCell extends YearMonthView.DateCell {

    private YearMonthView datePicker;
    private List<LocalDate> highlightedDates;
    private static List<LocalDate> minMaxDates;

    private String standardStyle, selectedStyle, highlightedStyle;

    public CustomDateCell(YearMonthView datePicker) {
        this.datePicker = datePicker;
        highlightedDates = new ArrayList<>();
        minMaxDates = new ArrayList<>();

        standardStyle = getStyle();
        selectedStyle = "-fx-background-color: #60E8AD;";
        highlightedStyle = "-fx-background-color: #E9967A;";

        this.setOnMouseClicked(event -> {
            /*if (datePicker.getSelectedDates().size() == 2) {
                setRange();
            }*/
        });
    }

    public CustomDateCell(YearMonthView datePicker, List<LocalDate> highlightedDates) {
        this.datePicker = datePicker;
        this.highlightedDates = new ArrayList<>();
        this.highlightedDates.addAll(highlightedDates);
        minMaxDates = new ArrayList<>();

        standardStyle = getStyle();
        selectedStyle = "-fx-background-color: #60E8AD;";
        highlightedStyle = "-fx-background-color: #E9967A;";
        datePicker.setSelectionMode(SelectionMode.MULTIPLE);

        this.setOnMouseClicked(event -> {
            if (event.getButton().equals(MouseButton.SECONDARY)) {
                if (!datePicker.getSelectedDates().contains(getDate()))
                    datePicker.getSelectedDates().add(getDate());

                minMaxDates.add(getDate());
                if (minMaxDates.size() == 2) {
                    setRange();
                    minMaxDates.clear();
                }
            }
        });
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

    private void setRange () {
        LocalDate startDate = minMaxDates.get(0);
        LocalDate endDate = minMaxDates.get(1);
        if (startDate.isAfter(endDate)) {
            LocalDate tmp = endDate;
            endDate = startDate;
            startDate = tmp;
        }

        datePicker.getSelectedDates().addAll(Stream.iterate(startDate, date -> date.plusDays(1)).limit(ChronoUnit.DAYS.between(startDate, endDate) + 1).collect(Collectors.toList()));
        /*LocalDate startDate = datePicker.getSelectedDates().stream().reduce((localDate, localDate2) -> localDate).get();
        LocalDate endDate = datePicker.getSelectedDates().stream().reduce((localDate, localDate2) -> localDate2).get();

        if (startDate.isAfter(endDate)) {
            LocalDate tmp = endDate;
            endDate = startDate;
            startDate = tmp;
        }

        datePicker.getSelectedDates().clear();
        datePicker.getSelectedDates().addAll(Stream.iterate(startDate, date -> date.plusDays(1)).limit(ChronoUnit.DAYS.between(startDate, endDate) + 1).collect(Collectors.toList()));
        */
    }
}