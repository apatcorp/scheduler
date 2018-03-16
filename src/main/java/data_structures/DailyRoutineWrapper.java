package data_structures;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class DailyRoutineWrapper {

    private AppointmentProperty appointmentProperty;
    private List<LocalDate> localDateList;

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
                ", localDateObservableList=" + localDateList.toString() +
                '}';
    }
}
