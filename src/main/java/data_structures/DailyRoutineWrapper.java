package data_structures;

import javafx.collections.ObservableSet;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class DailyRoutineWrapper {

    private AppointmentProperty appointmentProperty;
    private List<LocalDate> localDateList;

    public DailyRoutineWrapper(AppointmentProperty appointmentProperty, ObservableSet<LocalDate> localDates) {
        this.appointmentProperty = appointmentProperty;
        this.localDateList = new ArrayList<>();
        this.localDateList.addAll(localDates);
    }

    public DailyRoutineWrapper(AppointmentProperty appointmentProperty, List<LocalDate> localDates) {
        this.appointmentProperty = appointmentProperty;
        this.localDateList = new ArrayList<>();
        this.localDateList.addAll(localDates);
    }

    public AppointmentProperty getAppointmentProperty() {
        return appointmentProperty;
    }

    public List<LocalDate> getLocalDateList() {
        return localDateList;
    }

    @Override
    public String toString() {
        return "DailyRoutineWrapper{" +
                "appointmentProperty=" + appointmentProperty.toString() +
                ", localDateList=" + localDateList.toString() +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DailyRoutineWrapper that = (DailyRoutineWrapper) o;
        return appointmentProperty.equals(that.appointmentProperty);
    }

    @Override
    public int hashCode() {
        return Objects.hash(appointmentProperty);
    }
}
