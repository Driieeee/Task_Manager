package model.assessment;

import model.enums.TaskType;

public final class ExamType extends TaskType {
    public ExamType() {
        super("Exam");
    }

    @Override
    public double getWeight() {
        return 10.0;
    }

    @Override
    public String getCode() {
        return "EXAM";
    }
}
    

