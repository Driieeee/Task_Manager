package model.enums;

import model.assessment.*;
import model.task.*;
import exception.TaskValidationException;
import model.Reportable;

public abstract class TaskType implements Reportable{
    
    public static final TaskType EXAM = new ExamType();
    public static final TaskType LONG_QUIZ = new LongQuizType();
    public static final TaskType QUIZ = new QuizType();       
    public static final TaskType SUMMATIVE_EXAM = new SummativeExamType();
    public static final TaskType ASSIGNMENT = new AssignmentType();
    public static final TaskType LABORATORY_EXERCISE = new LaboratoryExerciseType();
    public static final TaskType OTHER_TASK = new OtherTaskType();
    public static final TaskType PROJECT = new ProjectType();
    public static final TaskType RECITATION = new RecitationType();

    private static final TaskType[] VALUES = {
        EXAM, LONG_QUIZ, QUIZ, SUMMATIVE_EXAM, ASSIGNMENT,
        LABORATORY_EXERCISE, OTHER_TASK, PROJECT, RECITATION
    };

    // attributes
    private final String label;

    // constructor
    protected TaskType(String label){
        if(label == null || label.isBlank()){
            throw new TaskValidationException("Task type label cannot be empty!");
        }
        this.label = label;
    }

    public abstract double getWeight();
    public abstract String getCode();

    public String getLabel(){
        return label;
    }

    @Override 
    public String describe(){
        return label + " (" + getCode() + ", weight: " + getWeight() + ")";
    }

    public static TaskType[] values(){
        return VALUES.clone();
    }

    public static TaskType defaultType(){
        return EXAM;
    }

    public static TaskType fromCsv(String value){
        if(value == null){
            return OTHER_TASK;
        }

        return switch (value.trim().toUpperCase()){
            case "EXAM" -> EXAM;
            case "LONG QUIZ" -> LONG_QUIZ;
            case "QUIZ" -> QUIZ;
            case "SUMMATIVE EXAM" -> SUMMATIVE_EXAM;
            case "ASSIGNMENT" -> ASSIGNMENT;
            case "LABORATORY EXERCISE" -> LABORATORY_EXERCISE;
            case "OTHER TASK" -> OTHER_TASK;
            case "PROJECT" -> PROJECT;
            case "RECITATION" -> RECITATION;
            default -> OTHER_TASK;
        };
    }

    @Override
    public String toString(){
        return  label;
    } 

}