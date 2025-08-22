package edu.ifpb.observer;

import edu.ifpb.state.Task;

import java.util.ArrayList;
import java.util.List;

public class User implements Observer {

    // Padrão: Observer
    // Onde aplicado: User
    // Como implementado: implementa a interface Observer e mantém lista de Tasks atribuídas
    // Por que foi escolhido: permite que usuários recebam notificações automáticas quando as Tasks são atualizadas
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