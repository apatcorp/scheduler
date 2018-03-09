import javafx.beans.property.ObjectProperty;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Arrigo on 27.02.2018.
 * Reminder
 */

public class Appointment implements Serializable {

    public String appointmentTitle;
    public AppointmentDate appointmentDate;
    public AppointmentTime appointmentTime;
    public List<String> appointmentDetails;

    public Appointment() {}

    public Appointment (Appointment other) {
        this.appointmentTitle = other.getAppointmentTitle();
        this.appointmentDate = other.getAppointmentDate();
        this.appointmentTime = other.getAppointmentTime();
        this.appointmentDetails = other.getAppointmentDetails();
    }

    public Appointment(String appointmentTitle, AppointmentTime appointmentTime, List<String> appointmentDetails) {
        this.appointmentTitle = appointmentTitle;
        this.appointmentDate = new AppointmentDate(1, 1, 2000);
        this.appointmentTime = appointmentTime;
        this.appointmentDetails = appointmentDetails;
    }

    public Appointment(String appointmentTitle, AppointmentDate appointmentDate, AppointmentTime appointmentTime, List<String> appointmentDetails) {
        this.appointmentTitle = appointmentTitle;
        this.appointmentDate = appointmentDate;
        this.appointmentTime = appointmentTime;
        this.appointmentDetails = appointmentDetails;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Appointment that = (Appointment) o;

        return appointmentTitle.equals(that.getAppointmentTitle()) &&
                appointmentDate.equals(that.getAppointmentDate()) &&
                appointmentTime.equals(that.getAppointmentTime()) &&
                appointmentDetails.equals(that.getAppointmentDetails());
    }

    @Override
    public int hashCode() {
        int result = appointmentTitle.hashCode();
        result = 31 * result + appointmentDate.hashCode();
        result = 31 * result + appointmentTime.hashCode();
        result = 31 * result + appointmentDetails.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "Appointment{" +
                "appointmentTitle='" + appointmentTitle + '\'' +
                ", appointmentDate=" + appointmentDate.toString() +
                ", appointmentTime=" + appointmentTime.toString() +
                ", appointmentDetails=" + appointmentDetails.toString() +
                '}';
    }

    public String appointmentID () {
        return appointmentTitle + "|" + appointmentTime.toString();
    }

    public AppointmentDate getAppointmentDate() {
        return appointmentDate;
    }

    public void setAppointmentDate(AppointmentDate appointmentDate) {
        this.appointmentDate = appointmentDate;
    }

    public String getAppointmentTitle() {
        return appointmentTitle;
    }

    public void setAppointmentTitle(String appointmentTitle) {
        this.appointmentTitle = appointmentTitle;
    }

    public AppointmentTime getAppointmentTime() {
        return appointmentTime;
    }

    public void setAppointmentTime(AppointmentTime appointmentTime) {
        this.appointmentTime = appointmentTime;
    }

    public List<String> getAppointmentDetails() {
        return appointmentDetails;
    }

    public void setAppointmentDetails(List<String> appointmentDetails) {
        this.appointmentDetails = appointmentDetails;
    }
}
