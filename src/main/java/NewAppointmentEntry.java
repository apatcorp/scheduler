import com.calendarfx.model.Entry;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;

class NewAppointmentEntry {

    private ZonedDateTime startDateTime, endDateTime;
    private Appointment appointment;

    NewAppointmentEntry(ZonedDateTime zonedDateTime) {
        startDateTime = ZonedDateTime.of(zonedDateTime.getYear(), zonedDateTime.getMonthValue(), zonedDateTime.getDayOfMonth(), zonedDateTime.getHour(), zonedDateTime.getMinute(), 0, 0, ZoneId.systemDefault());
        endDateTime = ZonedDateTime.of(zonedDateTime.getYear(), zonedDateTime.getMonthValue(), zonedDateTime.getDayOfMonth(), zonedDateTime.getHour() + 1, zonedDateTime.getMinute(), 0, 0, ZoneId.systemDefault());

        AppointmentDate appointmentDate = new AppointmentDate(startDateTime.getDayOfMonth(), startDateTime.getMonthValue(), startDateTime.getYear());
        AppointmentTime appointmentTime = new AppointmentTime(startDateTime.getHour(), startDateTime.getMinute());
        appointment = new Appointment("New Appointment", appointmentDate, appointmentTime, new ArrayList<>());
    }

    NewAppointmentEntry (Appointment appointment) {
        this.appointment = appointment;
        AppointmentDate appointmentDate = this.appointment.getAppointmentDate();
        AppointmentTime appointmentTime = this.appointment.getAppointmentTime();

        startDateTime = ZonedDateTime.of(appointmentDate.getYear(), appointmentDate.getMonth(), appointmentDate.getDate(), appointmentTime.getHour(), appointmentTime.getMinute(), 0, 0, ZoneId.systemDefault());
        endDateTime = ZonedDateTime.of(appointmentDate.getYear(), appointmentDate.getMonth(), appointmentDate.getDate(), appointmentTime.getHour() + 1, appointmentTime.getMinute(), 0, 0, ZoneId.systemDefault());
    }

    Entry<?> createNewAppointmentEntry () {
        Entry<Appointment> entry = new Entry<>();
        entry.setTitle(appointment.getAppointmentTitle());
        entry.setUserObject(appointment);
        entry.userObjectProperty().setValue(appointment);
        entry.setInterval(startDateTime, endDateTime);

        entry.startTimeProperty().addListener((observable, oldValue, newValue) -> entry.getUserObject().setAppointmentTime(new AppointmentTime(newValue.getHour(), newValue.getMinute())));
        entry.startDateProperty().addListener((observable, oldValue, newValue) -> entry.getUserObject().setAppointmentDate(new AppointmentDate(newValue.getDayOfMonth(), newValue.getMonthValue(), newValue.getYear())));

        return entry;
    }
}
