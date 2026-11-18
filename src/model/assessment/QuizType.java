package model.assessment;

import model.enums.TaskType;

public final class QuizType extends TaskType {
    public QuizType(){
        super("Quiz");
    }

    @Override 
    public double getWeight(){
        return 6.0;
    }

    @Override 
    public String getCode(){
        return "QUIZ";
    }
}
