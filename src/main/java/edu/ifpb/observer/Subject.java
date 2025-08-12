package edu.ifpb.observer;

import java.util.ArrayList;
import java.util.List;

public abstract class Subject {
    protected List<Observer> observers = new ArrayList<>();

    public void addObserver(Observer observer) {
        observers.add(observer);
    }

    public void removeObserver(Observer observer) {
        observers.remove(observer);
    }

    protected void notifyObservers() {
        for (Observer obs : observers) {
            obs.update(getNotificationMessage());
        }
    }

    protected abstract String getNotificationMessage();
}

