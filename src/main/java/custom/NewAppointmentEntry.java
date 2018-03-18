package custom;

import com.calendarfx.model.Entry;
import com.calendarfx.model.Interval;
import data_structures.Appointment;
import data_structures.AppointmentDate;
import data_structures.AppointmentProperty;
import data_structures.AppointmentTime;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;

public class NewAppointmentEntry extends Entry<AppointmentProperty> {

    public NewAppointmentEntry(ZonedDateTime zonedDateTime) {
        ZonedDateTime startDateTime = ZonedDateTime.of(zonedDateTime.getYear(), zonedDateTime.getMonthValue(), zonedDateTime.getDayOfMonth(), zonedDateTime.getHour(), zonedDateTime.getMinute(), 0, 0, ZoneId.systemDefault());
        ZonedDateTime endDateTime = ZonedDateTime.of(zonedDateTime.getYear(), zonedDateTime.getMonthValue(), zonedDateTime.getDayOfMonth(), zonedDateTime.getHour() + 1, zonedDateTime.getMinute(), 0, 0, ZoneId.systemDefault());

        AppointmentDate appointmentDate = new AppointmentDate(startDateTime.getDayOfMonth(), startDateTime.getMonthValue(), startDateTime.getYear());
        AppointmentTime appointmentTime = new AppointmentTime(startDateTime.getHour(), startDateTime.getMinute());
        Appointment appointment = new Appointment("Neuer Termin", appointmentDate, appointmentTime, new ArrayList<>());

        setEntryProperties(appointment, startDateTime, endDateTime);
    }

    public NewAppointmentEntry (Appointment _appointment) {
        Appointment appointment = new Appointment(_appointment);
        AppointmentDate appointmentDate = appointment.getAppointmentDate();
        AppointmentTime appointmentTime = appointment.getAppointmentTime();

        ZonedDateTime startDateTime = ZonedDateTime.of(appointmentDate.getYear(), appointmentDate.getMonth(), appointmentDate.getDate(), appointmentTime.getHour(), appointmentTime.getMinute(), 0, 0, ZoneId.systemDefault());
        ZonedDateTime endDateTime = ZonedDateTime.of(appointmentDate.getYear(), appointmentDate.getMonth(), appointmentDate.getDate(), appointmentTime.getHour() + 1, appointmentTime.getMinute(), 0, 0, ZoneId.systemDefault());

        setEntryProperties(appointment, startDateTime, endDateTime);
    }

    public NewAppointmentEntry (AppointmentProperty appointmentProperty, LocalDate localDate) {
        int hour = appointmentProperty.getLocalTimeObjectProperty().getHour();
        int minute = appointmentProperty.getLocalTimeObjectProperty().getMinute();

        ZonedDateTime startDateTime = ZonedDateTime.of(localDate.getYear(), localDate.getMonthValue(), localDate.getDayOfMonth(), hour, minute, 0, 0, ZoneId.systemDefault());
        ZonedDateTime endDateTime = ZonedDateTime.of(localDate.getYear(), localDate.getMonthValue(), localDate.getDayOfMonth(), hour + 1, minute, 0, 0, ZoneId.systemDefault());

        setEntryProperty(appointmentProperty, startDateTime, endDateTime);
    }

    private void setEntryProperties (Appointment appointment, ZonedDateTime startDateTime, ZonedDateTime endDateTime) {
        AppointmentProperty appointmentProperty = new AppointmentProperty(appointment);
        setUserObject(appointmentProperty);

        titleProperty().bindBidirectional(getUserObject().titleProperty());

        Interval interval = new Interval();
        interval.withDates(startDateTime.toLocalDateTime(), endDateTime.toLocalDateTime());

        intervalProperty().setValue(interval);

        startDateProperty().addListener((observable, oldValue, newValue) -> {
            getUserObject().localDateObjectProperty().setValue(newValue);
            changeStartDate(newValue, true);
        });
        startTimeProperty().addListener((observable, oldValue, newValue) -> {
            getUserObject().localTimeObjectProperty().setValue(newValue);
            changeStartTime(newValue, true);
        });

        setInterval(startDateTime, endDateTime);
    }

    private void setEntryProperty (AppointmentProperty _appointmentProperty, ZonedDateTime startDateTime, ZonedDateTime endDateTime) {
        AppointmentProperty appointmentProperty = new AppointmentProperty(_appointmentProperty.getTitleProperty(), _appointmentProperty.getLocalDateObjectProperty(), _appointmentProperty.getLocalTimeObjectProperty(), _appointmentProperty.getDetailListProperty());
        setUserObject(appointmentProperty);

        titleProperty().bindBidirectional(getUserObject().titleProperty());

        Interval interval = new Interval();
        interval.withDates(startDateTime.toLocalDateTime(), endDateTime.toLocalDateTime());

        intervalProperty().setValue(interval);

        startDateProperty().addListener((observable, oldValue, newValue) -> {
            getUserObject().localDateObjectProperty().setValue(newValue);
            changeStartDate(newValue, true);
        });
        startTimeProperty().addListener((observable, oldValue, newValue) -> {
            getUserObject().localTimeObjectProperty().setValue(newValue);
            changeStartTime(newValue, true);
        });

        setInterval(startDateTime, endDateTime);
    }
}
