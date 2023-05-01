package org.openjfx.api2semestre.report;

public class IntervalFee {
    private int code;
    private float percent;
    private IntervalFeeCondition condition;

    public IntervalFee(int code, float percent, IntervalFeeCondition condition) {
        this.code = code;
        this.percent = percent;
        this.condition = condition;
    }

    public int getCode() { return code; }
    public float getPercent() { return percent; }
    // public IntervalFeeCondition getCondition() { return condition; }

    public void setCode(int code) { this.code = code; }
    public void setPercent(float percent) { this.percent = percent; }
    // public void setCondition(IntervalFeeCondition condition) { this.condition = condition; }
    
}
