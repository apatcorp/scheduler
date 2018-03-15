package controller;

import com.calendarfx.model.Entry;
import data_structures.Appointment;

public abstract class Controller {

    public void setup () { }

    public void setup (Entry<?> entry) { }

    public void setup (Appointment appointment) {}
}
