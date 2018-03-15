package data_structures;

import java.io.Serializable;
import java.util.Map;

/**
 * Created by Arrigo on 19.02.2018.
 * Reminder
 */

public class DailyRoutine implements Serializable {

    public AppointmentDate appointmentDate;
    public Map<String, Appointment> appointments;

    public DailyRoutine() {}

    public DailyRoutine(AppointmentDate appointmentDate, Map<String, Appointment> appointments) {
        this.appointmentDate = appointmentDate;
        this.appointments = appointments;
    }

    public AppointmentDate getAppointmentDate() {
        return appointmentDate;
    }

    public void setAppointmentDate(AppointmentDate appointmentDate) {
        this.appointmentDate = appointmentDate;
    }

    public Map<String, Appointment> getAppointments() {
        return appointments;
    }

    public void setAppointments(Map<String, Appointment> appointments) {
        this.appointments = appointments;
    }

    public boolean equalAppointmentDate (AppointmentDate other) {
        return appointmentDate.getYear() == other.getYear()
                && appointmentDate.getMonth() == other.getMonth()
                && appointmentDate.getDate() == other.getDate();
    }

    @Override
    public String toString() {
        return appointmentDate.toString() + " | " + appointments.toString() + " | \n";
    }
}
