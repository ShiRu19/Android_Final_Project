package t4.sers.util;

import org.joda.time.DateTime;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.Objects;

public class ConfirmCourse {
    String code;
    String name;
    String teacher;
    DateTime riskDurationStart;
    DateTime riskDurationEnd;

    public ConfirmCourse(String code, String name, String teacher, DateTime riskDurationStart, DateTime riskDurationEnd){
        this.code = code;
        this.riskDurationStart = riskDurationStart;
        this.riskDurationEnd = riskDurationEnd;
        this.name = name;
        this.teacher = teacher;
    }

    public String getCode() {
        return code;
    }

    public DateTime getRiskDurationStart() {
        return riskDurationStart;
    }

    public DateTime getRiskDurationEnd() {
        return riskDurationEnd;
    }

    public String getName() {
        return name;
    }

    public String getTeacher() {
        return teacher;
    }

    public String getDurationTime(){
        DateFormat dateFormatString = new SimpleDateFormat("MM/dd(E)", Locale.TAIWAN);
        String start = dateFormatString.format(riskDurationStart.toDate()).replace("週", "");
        String end = dateFormatString.format(riskDurationEnd.toDate()).replace("週", "");
        return start + "至\n" + end;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ConfirmCourse that = (ConfirmCourse) o;
        return Objects.equals(code, that.code) && Objects.equals(name, that.name) && Objects.equals(teacher, that.teacher) && Objects.equals(riskDurationStart, that.riskDurationStart) && Objects.equals(riskDurationEnd, that.riskDurationEnd);
    }

    @Override
    public int hashCode() {
        return Objects.hash(code, name, teacher, riskDurationStart, riskDurationEnd);
    }
}
