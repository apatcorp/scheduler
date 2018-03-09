import java.io.Serializable;

/**
 * Created by Arrigo on 27.02.2018.
 */

public class AppointmentDate implements Serializable {

    public int date;
    public int month;
    public int year;

    public AppointmentDate () {}

    public AppointmentDate (int month, int year) {
        this.month = month;
        this.year = year;
        date = 0;
    }

    public AppointmentDate(int date, int month, int year) {
        this.date = date;
        this.month = month;
        this.year = year;
    }

    public int getDate() {
        return date;
    }

    public void setDate(int date) {
        this.date = date;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    @Override
    public String toString() {
        return Utility.appointmentDateToString(this);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        AppointmentDate that = (AppointmentDate) o;

        return year == that.getYear() && month == that.getMonth() && date == that.getDate();
    }

    @Override
    public int hashCode() {
        int result = date;
        result = 31 * result + month;
        result = 31 * result + year;
        return result;
    }
}
