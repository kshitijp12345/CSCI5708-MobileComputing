package com.eventtracker;

public class TaskDetails {
    private String TaskTitle;
    private String TaskDueDate;
    private String TaskPriority;

    public TaskDetails() {
    }

    public TaskDetails(String TaskTitle, String TaskDueDate, String TaskPriority) {
        this.TaskTitle = TaskTitle;
        this.TaskDueDate = TaskDueDate;
        this.TaskPriority = TaskPriority;
    }

    public String getTaskTitle() {
        return TaskTitle;
    }

    public void setTaskTitle(String taskTitle) {
        TaskTitle = taskTitle;
    }

    public String getTaskDueDate() {
        return TaskDueDate;
    }

    public void setTaskDueDate(String taskDueDate) {
        TaskDueDate = taskDueDate;
    }

    public String getTaskPriority() {
        return TaskPriority;
    }

    public void setTaskPriority(String taskPriority) {
        TaskPriority = taskPriority;
    }
}
