package model.task;

import model.enums.TaskType;

public final class AssignmentType extends TaskType{
    public AssignmentType(){
        super("Assignment");
    }

    @Override 
    public double getWeight(){
        return 6.0;
    }

    @Override 
    public String getCode(){
        return "ASSIGNMENT";
    }
}
