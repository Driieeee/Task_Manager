package model.task;

import model.enums.TaskType;

public final class ProjectType extends TaskType{
    public ProjectType(){
        super("Project");
    }

    @Override 
    public double getWeight(){
        return 9.0;
    }

    @Override 
    public String getCode(){
        return "Project";
    }
}
