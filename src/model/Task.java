package model;

import java.time.LocalDate;
import model.enums.*;

public class Task {
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
        double urgency = 1.0 / Math.max(daysLeft, 1); // calculate urgency based on days left
        return taskType.getWeight() * difficulty.getWeight() * urgency; // return priority based on type weight and urgency
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
}