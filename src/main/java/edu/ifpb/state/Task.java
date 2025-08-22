package edu.ifpb.state;

import edu.ifpb.observer.TaskSubject;

public class Task extends TaskSubject {

    private int id;
    private String name;
    private TaskState state;
    private String assignedUser;

    public Task() {

    }

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

    public TaskState getState() {
        return state;
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

    public void setState(TaskState state) {
        this.state = state;
    }

    public String getAssignedUser() {
        return assignedUser;
    }

    public void setAssignedUser(String assignedUser) {
        this.assignedUser = assignedUser;
    }

    @Override
    public String toString() {
        return String.format(
                "\n📌 [Tarefa #%d]\n   Nome: %s\n   Status: %s\n   Responsável: %s\n",
                id,
                name,
                state.getStatus(),
                (assignedUser != null ? assignedUser : "— (não atribuída)")
        );
    }
}