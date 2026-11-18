package model.assessment;

import model.enums.TaskType;

public final class SummativeExamType extends TaskType{
    public SummativeExamType(){
        super("Summative Exam");
    }

    @Override 
    public double getWeight(){
        return 8.0;
    }

    @Override 
    public String getCode(){
        return "SUMMATIVE EXAM";
    }
}
