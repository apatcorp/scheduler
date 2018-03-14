import com.calendarfx.view.YearMonthView;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

class CustomDateCell extends YearMonthView.DateCell {

    private boolean selected;

    private final String standardStyle;
    private final String selectedStyle;
    private final String rangeSelecedStyle;
    private String currentStyle;

    private static LocalDate startDate = null, endDate = null;
    private static List<LocalDate> selectedDates = new ArrayList<>();
    private static List<CustomDateCell> customDateCells = new ArrayList<>();

    CustomDateCell(LocalDate localDate) {
        // not selected by default
        selected = false;

        // add this new DateCell to an static array, which holds all the date cells
        if (!customDateCells.contains(this)) {
            customDateCells.add(this);
        }

        // define styles for different states the cell can be in
        standardStyle = getStyle();
        selectedStyle = "-fx-background-color: #60E8AD;";
        rangeSelecedStyle = "-fx-background-color: #B0E0E6";

        // set the current style to the standard style
        currentStyle = standardStyle;

        // define on mouse clicked on cell event
        setOnMouseClicked(event -> {
            onDateCellClicked();
        });

        // set the date and the style of the cell
        setDate(localDate);
        setStyle(currentStyle);
    }

    private void onDateCellClicked () {
        // toggle current selected state of the cell
        selected = !selected;

        if (selected) {
            if (startDate != null && endDate != null) {
                reset();
                selected = true;
                startDate = getDate();
            }
            else if (startDate == null && endDate == null) {
                startDate = getDate();
            }
            else if (startDate != null) {
                endDate = getDate();
                setRange();
            }

            currentStyle = selectedStyle;

        } else {
            if (getDate().equals(endDate)) {
                endDate = null;
                resetRangeList();
            }

            if (getDate().equals(startDate)) {
                if (endDate != null) {
                    startDate = endDate;
                    endDate = null;
                } else {
                    startDate = null;
                }

                resetRangeList();
            }

            currentStyle = standardStyle;
        }

        setStyle(currentStyle);
    }

    private void reset () {
        selectedDates.clear();
        customDateCells.forEach(customDateCell -> {
            customDateCell.selected = false;
            customDateCell.currentStyle = standardStyle;
            customDateCell.setStyle(currentStyle);
        });

        startDate = endDate = null;
    }

    private void resetRangeList () {
        selectedDates.clear();

        customDateCells.forEach(customDateCell -> {
            customDateCell.currentStyle = customDateCell.getDate().equals(startDate) || customDateCell.getDate().equals(endDate) ? selectedStyle : standardStyle;
            customDateCell.setStyle(customDateCell.currentStyle);
        });
    }

    private void setRange () {
        if (startDate.isAfter(endDate)) {
            LocalDate tmp = endDate;
            endDate = startDate;
            startDate = tmp;
        }

        selectedDates.addAll(Stream.iterate(startDate, date -> date.plusDays(1)).limit(ChronoUnit.DAYS.between(startDate, endDate) + 1).collect(Collectors.toList()));
        System.out.println(selectedDates);

        customDateCells.forEach(customDateCell -> {
            customDateCell.currentStyle = selectedDates.contains(customDateCell.getDate()) &&
                                            !customDateCell.getDate().equals(startDate) &&
                                            !customDateCell.getDate().equals(endDate)? customDateCell.rangeSelecedStyle : customDateCell.currentStyle;
            customDateCell.setStyle(customDateCell.currentStyle);
        });
    }
}
