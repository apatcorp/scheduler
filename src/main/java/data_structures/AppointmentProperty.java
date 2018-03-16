package data_structures;

import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public class AppointmentProperty {

    private StringProperty title;
    private ObjectProperty<LocalDate> localDate;
    private ObjectProperty<LocalTime> localTime;
    private ObservableList<String> detailList;

    public AppointmentProperty(Appointment appointment) {
        title = new SimpleStringProperty();
        localDate = new SimpleObjectProperty<>();
        localTime = new SimpleObjectProperty<>();

        LocalDate localDateTmp = LocalDate.of(appointment.getAppointmentDate().getYear(), appointment.getAppointmentDate().getMonth(), appointment.getAppointmentDate().getDate());
        LocalTime localTimeTmp = LocalTime.of(appointment.getAppointmentTime().getHour(), appointment.getAppointmentTime().getMinute(), 0, 0);

        title.setValue(appointment.getAppointmentTitle());

        localDate.setValue(localDateTmp);
        localTime.setValue(localTimeTmp);

        detailList = FXCollections.observableList(appointment.getAppointmentDetails());
    }

    public AppointmentProperty (String _title, LocalDate _localDate, LocalTime _localTime, List<String> details) {
        title = new SimpleStringProperty();
        localDate = new SimpleObjectProperty<>();
        localTime = new SimpleObjectProperty<>();

        title.setValue(_title);

        localDate.setValue(_localDate);
        localTime.setValue(_localTime);

        detailList = FXCollections.observableList(details);
    }

    public String getTitleProperty() {
        return title.get();
    }

    public StringProperty titleProperty() {
        return title;
    }

    public LocalDate getLocalDateObjectProperty() {
        return localDate.get();
    }

    public ObjectProperty<LocalDate> localDateObjectProperty() {
        return localDate;
    }

    public LocalTime getLocalTimeObjectProperty() {
        return localTime.get();
    }

    public ObjectProperty<LocalTime> localTimeObjectProperty() {
        return localTime;
    }

    public ObservableList<String> getDetailListProperty() {
        return detailList;
    }

    @Override
    public String toString() {
        return "AppointmentProperty{" +
                "titleProperty=" + title.getValue() +
                ", localDateObjectProperty=" + localDate.getValue() +
                ", localTimeObjectProperty=" + localTime.getValue() +
                ", detailListProperty=" + detailList.toString() +
                '}';
    }
}
