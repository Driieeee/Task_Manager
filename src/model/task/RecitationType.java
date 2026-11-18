package model.task;

import model.enums.TaskType;

public final class RecitationType extends TaskType{
    public RecitationType(){
        super("Recitation");
    }

    @Override 
    public double getWeight(){
        return 7.0;
    }

    @Override 
    public String getCode(){
        return "RECITATION";
    }
}
