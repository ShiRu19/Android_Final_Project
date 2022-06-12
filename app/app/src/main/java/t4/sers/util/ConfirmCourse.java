package t4.sers.util;

import org.joda.time.DateTime;

public class ConfirmCourse {
    String code;
    DateTime riskDurationStart;
    DateTime riskDurationEnd;

    public ConfirmCourse(String code, DateTime riskDurationStart, DateTime riskDurationEnd){
        this.code = code;
        this.riskDurationStart = riskDurationStart;
        this.riskDurationEnd = riskDurationEnd;
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
}
