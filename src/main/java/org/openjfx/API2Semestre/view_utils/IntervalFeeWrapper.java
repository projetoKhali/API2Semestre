package org.openjfx.api2semestre.view_utils;

import org.openjfx.api2semestre.report.IntervalFee;
import org.openjfx.api2semestre.report.Week;

public class IntervalFeeWrapper {
    
    private IntervalFee intervalFee;
    
    public IntervalFeeWrapper(IntervalFee intervalFee) {
        this.intervalFee = intervalFee;
    }
    
    public IntervalFee getIntervalFee() { return intervalFee; }

    public Integer getVerba() { return intervalFee.getCode(); }
    public String getTipo() { return intervalFee.getType(); }
    public String getExpediente() {
        return Expedient.get(intervalFee.getStartHour(), intervalFee.getEndHour());
    }
    public String getFimDeSemana() {
        boolean[] days = intervalFee.getDaysOfWeek();
        if (Week.FDS.compare(days)) return "Sim";
        if (!Week.FDS.compare(days)) return "Não";
        return "N/A";
    } 
    public String getHoraMinimo() { return intervalFee.getMinHourCount(); }
    public Double getHoraDuracao() { return intervalFee.get; }
    public Double getPorcentagem() { return intervalFee.get; }

}
