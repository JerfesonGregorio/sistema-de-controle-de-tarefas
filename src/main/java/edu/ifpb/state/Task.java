package edu.ifpb.state;

import edu.ifpb.observer.TaskSubject;

public class Task extends TaskSubject {

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

    public String getName() {
        return name;
    }

    void setState(TaskState state) {
        this.state = state;
    }
}