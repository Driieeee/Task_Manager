package model;

import java.time.LocalDate;

public class WorkloadWarning implements Reportable {
    
    // attributes
    private LocalDate day;
    private double totalHours;
    private double limitHours;

    // constructor
    public WorkloadWarning(LocalDate day, double totalHours, double limitHours){
        this.day = day;
        this.totalHours = totalHours;
        this.limitHours = limitHours;
    }

    public LocalDate getDay(){
        return day;
    }

    public double getTotalHours(){
        return  totalHours;
    }

    public double getLimitHours(){
        return limitHours;
    }

    @Override 
    public String describe(){
        return String.format("Overload on %s: %.1f h due (limit %.1f h).",
          day, totalHours, limitHours); 
    }
}
