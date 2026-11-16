package model.enums;

public enum TaskStatus{

    // enum constant
    NOT_STARTED("Not started"),
    IN_PROGRESS("In progress"),
    DONE("Done");

    // attribute
    private String label;

    // constructor
    TaskStatus(String label){
        this.label = label;
    }

    public static TaskStatus fromText(String text){
        for(TaskStatus status : TaskStatus.values()){
            if(status.name().equalsIgnoreCase(text.trim())
            || status.label.equalsIgnoreCase(text.trim())){
                return status;
            }
        }
        return NOT_STARTED;
    }

    @Override 
    public String toString(){
        return label;
    }
}
