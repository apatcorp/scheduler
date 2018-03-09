import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.SimpleObjectProperty;

import java.beans.Transient;
import java.io.Serializable;
import java.time.LocalTime;

/**
 * Created by Arrigo on 27.02.2018.
 * Reminder
 */

public class AppointmentTime implements Serializable {

    public int hour;
    public int minute;

    public AppointmentTime () {}

    public AppointmentTime(int hour, int minute) {
        this.hour = hour;
        this.minute = minute;
    }

    public int getHour() {
        return hour;
    }

    public void setHour(int hour) {
        this.hour = hour;
    }

    public int getMinute() {
        return minute;
    }

    public void setMinute(int minute) {
        this.minute = minute;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        AppointmentTime that = (AppointmentTime) o;

        return minute == that.getMinute() && hour == that.getHour();
    }

    @Override
    public int hashCode() {
        int result = hour;
        result = 31 * result + minute;
        return result;
    }

    @Override
    public String toString() {
        return Utility.appointmentTimeToString(this);
    }
}
