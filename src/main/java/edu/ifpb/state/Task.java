package edu.ifpb.state;

import edu.ifpb.observer.TaskSubject;

public class Task extends TaskSubject {

    private int id;
    private String name;
    private TaskState state;

    public Task(String name) {
        this.name = name;
        this.state = new ToDoState();
    }

    public void nextState() {
        state.next(this);
        notifyObservers();
    }

    public void previousState() {
        state.prev(this);
        notifyObservers();
    }

    public String getStatus() {
        return state.getStatus();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    void setState(TaskState state) {
        this.state = state;
    }
}