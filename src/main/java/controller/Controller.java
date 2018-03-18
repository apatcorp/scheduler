package controller;

import com.calendarfx.model.Calendar;
import com.calendarfx.model.Entry;

public abstract class Controller {

    void setup() { }

    public void setup (Entry<?> entry) { }

    public void setup (Calendar calendar) {}
}
