package edu.ifpb.observer;

public abstract class TaskSubject extends Subject {
    // Padrão: Observer
    // Onde aplicado: TaskSubject
    // Como implementado: classe abstrata que estende Subject e define a mensagem padrão de notificação
    // Por que foi escolhido: centraliza o comportamento de notificação para todas as Tasks que podem ser observadas
    @Override
    protected String getNotificationMessage() {
        return "A tarefa foi atualizada.";
    }
}
