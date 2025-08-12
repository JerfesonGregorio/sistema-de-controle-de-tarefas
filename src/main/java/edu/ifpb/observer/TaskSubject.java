package edu.ifpb.observer;

public abstract class TaskSubject extends Subject {
    @Override
    protected String getNotificationMessage() {
        return "A tarefa foi atualizada.";
    }
}
