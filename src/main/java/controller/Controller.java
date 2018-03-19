package controller;

import com.calendarfx.model.Calendar;
import com.calendarfx.model.Entry;
import data_structures.DailyRoutine;

import java.util.List;

public abstract class Controller {

    void setup() { }

    void setup (Entry<?> entry) { }

    void setup (Calendar calendar) {}

    void setup(List<DailyRoutine> dailyRoutines) {}
}
