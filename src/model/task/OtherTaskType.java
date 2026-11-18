package model.task;

import model.enums.TaskType;

public final class OtherTaskType extends TaskType{
    public OtherTaskType(){
        super("Other Task Type");
    }

    @Override 
    public double getWeight(){
        return 4.0;
    }

    @Override 
    public String getCode(){
        return "OTHER TASK TYPE";
    }
}
