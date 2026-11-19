package model.enums;

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

        return switch (value.trim().toUpperCase()){
            case "NOT_YET_STARTED", "NOT YET STARTED" -> NOT_STARTED;
            case "DONE" -> DONE;
            default -> NOT_STARTED; 
        };
    }

    @Override 
    public String toString(){
        return label;
    }
}
