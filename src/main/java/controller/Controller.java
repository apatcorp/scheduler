package controller;

import com.calendarfx.model.Calendar;
import com.calendarfx.model.Entry;
import com.jfoenix.controls.JFXDialog;
import data_structures.DailyRoutine;
import data_structures.DailyRoutineWrapper;

import java.util.List;

public abstract class Controller {

    void setup() { }

    void setup (Calendar calendar) {}

    public void setup (Entry<?> entry) {}

    public void setup (JFXDialog dialog, Calendar calendar) {}

    void setup (List<DailyRoutine> dailyRoutines) {}

    void setup (DailyRoutineWrapper dailyRoutineWrapper) {}
}
