package edu.ifpb.observer;

import java.util.ArrayList;
import java.util.List;

// Padrão: Observer / Subject
// Onde aplicado: Subject
// Como implementado: mantém lista de observers e fornece métodos para adicionar, remover e notificar
// Por que foi escolhido: encapsula a lógica de notificação, permitindo que classes derivadas foquem no conteúdo da mensagem
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

