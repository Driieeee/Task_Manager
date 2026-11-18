package model.assessment;

import model.enums.TaskType;

public final class LongQuizType extends TaskType {
    public LongQuizType(){
        super("Long Quiz");
    }

    @Override 
    public double getWeight(){
        return 8.0;
    }

    @Override 
    public String getCode(){
        return "LONG QUIZ";
    }
}
