package org.openjfx.api2semestre.view.utils.wrappers;

import org.openjfx.api2semestre.report.IntervalFee;
import org.openjfx.api2semestre.report.Week;
import org.openjfx.api2semestre.utils.Expedient;

public class IntervalFeeWrapper {
    
    private IntervalFee intervalFee;
    
    public IntervalFeeWrapper(IntervalFee intervalFee) {
        this.intervalFee = intervalFee;
    }
    
    public IntervalFee getIntervalFee() { return intervalFee; }

    public int getCodigo() { return intervalFee.getCode(); }
    public String getTipo() { return intervalFee.getType().getStringValue(); }
    public String getExpediente() {
        return Expedient.getExpedient(intervalFee.getStartHour(), intervalFee.getEndHour());
    }
    public String getFimDeSemana() {
        boolean[] days = intervalFee.getDaysOfWeek();
        if (Week.FDS.compare(days)) return "Sim";
        if (!Week.FDS.compare(days)) return "Não";
        return "N/A";
    } 
    public long getHoraMinimo() { return intervalFee.getMinHourCount(); }
    public double getHoraDuracao() { return intervalFee.getHourDuration(); }
    public double getPorcentagem() { return intervalFee.getPercent(); }

    public void setCodigo (int codigo) { intervalFee.setCode(codigo); }
    public void setPorcentagem (double porcentagem) { intervalFee.setPercent(porcentagem); }

}
