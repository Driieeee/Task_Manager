package model.task;

import model.enums.TaskType;

public final class LaboratoryExerciseType extends TaskType {
    public LaboratoryExerciseType(){
        super("Laboratory Exercise");
    }

    @Override 
    public double getWeight(){
        return 7.0;
    }

    @Override 
    public String getCode(){
        return "LABORATORY EXERCISE";
    }
}
