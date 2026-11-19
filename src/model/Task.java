package model;

public class Task {
    private String name;
    private String course;
    private String deadline;
    private String difficulty;
    private String type;
    private String priority;

    public Task(String key, String name, String course, String deadline, String difficulty, String type, String priority){
        this.name = name;
        this.course = course;
        this.deadline = deadline;
        this.difficulty = difficulty;
        this.type = type;
        this.priority = priority;
    }

}
