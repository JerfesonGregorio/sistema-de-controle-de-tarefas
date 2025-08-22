package edu.ifpb.state;

public class CompletedState implements TaskState {
    // Padrão: State
    // Onde aplicado: CompletedState
    // Como implementado: representa o estado "Concluída" de uma Task, permitindo apenas transição para estado anterior
    // Por que foi escolhido: encapsula regras do estado final e evita alterações indevidas na Task

    @Override
    public void next(Task task) {
        System.out.println("Tarefa já está concluída.");
    }

    @Override
    public void prev(Task task) {
        task.setState(new InProgressState());
    }

    @Override
    public String getStatus() {
        return "Concluída" ;
    }
}
