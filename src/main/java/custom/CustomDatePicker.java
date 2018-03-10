package custom;

import javafx.scene.control.DatePicker;

public class CustomDatePicker extends DatePicker {

    private boolean dateSelected = false;

    @Override
    public void hide() {
        if (!dateSelected) {
            super.hide();
        }
    }

    public boolean isDateSelected() {
        return dateSelected;
    }

    public void setDateSelected(boolean dateSelected) {
        this.dateSelected = dateSelected;
    }
}
