package model;

import java.time.LocalDate;
import model.enums.*;
import java.time.format.DateTimeFormatter;

public class Task {
    private static final DateTimeFormatter FMT = DateTimeFormatter.ofPattern("MM/dd/yyyy");
    private String taskName;
    private String course;
    private LocalDate deadline;
    private Difficulty difficulty;
    private TaskType taskType;
    private TaskStatus status;

    public Task(String taskName, String course, LocalDate deadline, 
                Difficulty difficulty, TaskType taskType, TaskStatus status){
        this.taskName = taskName;
        this.course = course;
        this.deadline = deadline;
        this.difficulty = difficulty;
        this.taskType = taskType;
        this.status = status;
    }

    public double computePriority(){
        long daysLeft = java.time.temporal.ChronoUnit.DAYS.between(LocalDate.now(), deadline); // calculate days left until deadline
        
        if(daysLeft < 1){
            daysLeft = 1;
        }

        double urgency = 1.0 / daysLeft; // fewer days left -> more urgent
        double rawPriority = taskType.getWeight() * difficulty.getWeight() * urgency; // 
        double priority = rawPriority * 2; // 100 instead of 50 maximum
        priority = Math.round(priority * 100.0) / 100.0;

        return priority;
    }

    public boolean isDueSoon(){
        long daysLeft = java.time.temporal.ChronoUnit.DAYS.between(LocalDate.now(), deadline);
        boolean isDone = status.toString().equalsIgnoreCase("Done");
        return  daysLeft >= 0  && daysLeft <= 1 && !isDone;
    }

    public boolean isOverDue(){
        long daysLeft = java.time.temporal.ChronoUnit.DAYS.between(LocalDate.now(), deadline);
        boolean isDone = status.toString().equalsIgnoreCase("Done");
        return  daysLeft < 0 && !isDone;
    }

    // getters
    public String getTaskName() { 
        return taskName; 
    }
    
    public String getCourse() { 
        return course; 
    }

    public LocalDate getDeadline() { 
        return deadline; 
    }

    public Difficulty getDifficulty() { 
        return  difficulty; 
    }
    
    public TaskType getTaskType() { 
        return taskType; 
    }
    
    public TaskStatus getStatus() { 
        return  status; 
    }

    // setters
    public void setTaskName(String taskName){
        this.taskName = taskName;
    }

    public void setCourse(String course){
        this.course = course;
    }

    public void setDeadline(LocalDate deadline){
        this.deadline = deadline;
    }

    public void setDifficulty(Difficulty difficulty){
        this.difficulty = difficulty;
    }

    public void setTaskType(TaskType taskType){
        this.taskType = taskType;
    }

    public void setStatus(TaskStatus status){
        this.status = status;
    }

    // Turns the Task into 7 column row that mr.csv expects
    // Matches the Headers because its not good if it doesnt
    public Object[] toCsvRow() {
        return new Object[] {
            taskName,
            course,
            deadline.format(FMT),
            difficulty.getLabel(),
            taskType.getLabel(),
            String.valueOf(computePriority()),
            status.toString()
        };
    }
    
    public static Task fromCsvRow(String[] row) {
        return new Task(row[0], row[1], LocalDate.parse(row[2], FMT), Difficulty.fromText(row[3]), TaskType.fromCsv(row[4]), TaskStatus.fromCsv(row[6]));
    }
}