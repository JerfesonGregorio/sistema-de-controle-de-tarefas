package edu.ifpb.observer;

import edu.ifpb.state.Task;

import java.util.ArrayList;
import java.util.List;

public class User implements Observer {

    private int id;
    private String name;
    private List<Task> tasks = new ArrayList<>();

    public User(String name) {
        this.name = name;
    }

    @Override
    public void update(String message) {
        System.out.println("[" + name + "] Notificação: " + message);
    }

    public void assignTask(Task task) {
        tasks.add(task);
        task.addObserver(this);
    }

    public List<Task> getTasks() {
        return tasks;
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
}