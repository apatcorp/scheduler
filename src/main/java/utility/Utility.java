package utility;

import data_structures.Appointment;
import data_structures.AppointmentDate;
import data_structures.AppointmentTime;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;


/**
 * Created by Arrigo on 27.02.2018.
 */

public class Utility {

    private static final String TAG = "utility.Utility";
    private static final String KEY = "sharedPreferenceKey";


    public static AppointmentDate getAppointmentDateFromDate (Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);

        return getAppointmentDateFromDate(calendar);
    }

    public static AppointmentDate getAppointmentDateFromDate (Calendar calendar) {
        calendar.add(Calendar.MONTH, 1);

        return new AppointmentDate(calendar.get(Calendar.DATE), calendar.get(Calendar.MONTH), calendar.get(Calendar.YEAR));
    }

    public static AppointmentTime getAppointmentTimeFromDate (Calendar calendar) {
        return new AppointmentTime(calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE));
    }

    public static AppointmentTime getFullAppointmentTimeFromDate (Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.MONTH, 1);

        return new AppointmentTime(calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE));
    }

    public static String appointmentDateToString (AppointmentDate appointmentDate) {

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, appointmentDate.getYear());
        calendar.set(Calendar.MONTH, appointmentDate.getMonth() - 1);
        calendar.set(Calendar.DATE, appointmentDate.getDate());

        return getDocumentIDFromDate(calendar.getTime());
    }

    public static LocalTime appointmentTimeToLocalTime (AppointmentTime appointmentTime) {
        return LocalTime.of(appointmentTime.getHour(), appointmentTime.getMinute(), 0, 0);
    }

    public static String getDocumentIDFromDate(Date date) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.GERMANY);
        return simpleDateFormat.format(date);
    }

    public static String getDocumentIDFromAppointmentDate(AppointmentDate date) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.GERMANY);
        return simpleDateFormat.format(dateFromAppointmentDate(date));
    }

    public static String getDocumentIDFromLocalDate (LocalDate localDate) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.GERMANY);
        return simpleDateFormat.format(Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant()));
    }

    public static Date dateFromAppointmentDate(AppointmentDate appointmentDate) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(appointmentDate.getYear(), appointmentDate.getMonth() - 1, appointmentDate.getDate());
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        return calendar.getTime();
    }

    public static Calendar appointmentDateAndAppointmentTimeToCalendar(Appointment appointment) {
        Calendar calendar = Calendar.getInstance();

        calendar.set(Calendar.DATE, appointment.getAppointmentDate().getDate());
        calendar.set(Calendar.MONTH, appointment.getAppointmentDate().getMonth() - 1);
        calendar.set(Calendar.YEAR, appointment.getAppointmentDate().getYear());
        calendar.set(Calendar.HOUR_OF_DAY, appointment.getAppointmentTime().getHour());
        calendar.set(Calendar.MINUTE, appointment.getAppointmentTime().getMinute());
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        return calendar;
    }

    public static Date findEarliestDate(List<Date> dates) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(4000, 1, 1);
        Date earliestDate = calendar.getTime();

        for (Date date : dates) {
            if (date.before(earliestDate))
                earliestDate = date;
        }

        return earliestDate;
    }

    public static Date findLatestDate(List<Date> dates) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(1900, 1, 1);
        Date latestDate = calendar.getTime();

        for (Date date: dates) {
            if (date.after(latestDate))
                latestDate = date;
        }

        calendar.setTime(latestDate);
        calendar.add(Calendar.DATE, 1);
        latestDate = calendar.getTime();

        return latestDate;
    }

    public static String appointmentTimeToDateString (AppointmentTime appointmentTime) {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS", Locale.GERMAN);

        Calendar calendar = Calendar.getInstance();

        calendar.set(Calendar.HOUR_OF_DAY, appointmentTime.getHour());
        calendar.set(Calendar.MINUTE, appointmentTime.getMinute());
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        return dateFormat.format(calendar.getTime());
    }

    public static String appointmentTimeToString(AppointmentTime appointmentTime) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm", Locale.GERMAN);

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, appointmentTime.getHour());
        calendar.set(Calendar.MINUTE, appointmentTime.getMinute());

        return simpleDateFormat.format(calendar.getTime());
    }

    public static String calendarToISO8601String (Calendar calendar) {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS", Locale.GERMAN);
        return dateFormat.format(calendar.getTime());
    }

    public static AppointmentDate ISO8601StringToAppointmentDate (String date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS", Locale.GERMAN);
        Date convertedDate;
        AppointmentDate appointmentDate = new AppointmentDate();

        try {
            convertedDate = dateFormat.parse(date);

            Calendar calendar = Calendar.getInstance();
            calendar.setTime(convertedDate);
            calendar.add(Calendar.MONTH, 1);

            appointmentDate.setDate(calendar.get(Calendar.DATE));
            appointmentDate.setMonth(calendar.get(Calendar.MONTH));
            appointmentDate.setYear(calendar.get(Calendar.YEAR));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return appointmentDate;
    }

    public static AppointmentTime ISO8601StringToAppointmentTime (String date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS", Locale.GERMAN);
        Date convertedDate;
        AppointmentTime appointmentTime = new AppointmentTime();

        try {
            convertedDate = dateFormat.parse(date);

            Calendar calendar = Calendar.getInstance();
            calendar.setTime(convertedDate);

            appointmentTime.setHour(calendar.get(Calendar.HOUR_OF_DAY));
            appointmentTime.setMinute(calendar.get(Calendar.MINUTE));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return appointmentTime;
    }
}
