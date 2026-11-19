package model.enums;

import exception.TaskValidationException;

public enum TaskStatus{

    // enum constant
    NOT_STARTED("Not Yet Started"),
    DONE("Done");

    // attribute
    private final String label;

    // constructor
    TaskStatus(String label){
        this.label = label;
    }

    public String getLabel(){
        return label;
    }

    public boolean isDone(){
        return this == DONE;
    }

    public static TaskStatus fromCsv(String value){
        if(value == null){
            return NOT_STARTED;
        }

        return switch (value.trim().toUpperCase().replace('_', ' ')){
            case "NOT STARTED", "NOT YET STARTED" -> NOT_STARTED;
            case "DONE" -> DONE;
            default -> NOT_STARTED; 
        };
    }

    /** Strict variant: an unknown status word is a validation failure. */
    public static TaskStatus fromTextStrict(String value){
        if(value == null || value.isBlank()){
            throw new TaskValidationException("Status is required.");
        }

        return switch (value.trim().toUpperCase().replace('_', ' ')){
            case "NOT STARTED", "NOT YET STARTED" -> NOT_STARTED;
            case "DONE" -> DONE;
            default -> throw new TaskValidationException("Unknown status: " + value);
        };
    }

    @Override 
    public String toString(){
        return label;
    }
}
