package custom;

import javafx.scene.control.DatePicker;

public class CustomDatePicker extends DatePicker {

    public boolean selectedDate = false;

    @Override
    public void hide() {
        if (!selectedDate) {
            super.hide();
        } else {

        }
    }

    @Override
    public void show() {
        super.show();
    }
}
