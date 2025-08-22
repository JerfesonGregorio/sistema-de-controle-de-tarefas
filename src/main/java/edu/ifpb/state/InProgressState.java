package edu.ifpb.state;

public class InProgressState implements TaskState {
    // Padrão: State
    // Onde aplicado: InProgressState
    // Como implementado: representa o estado "Em andamento" de uma Task, permitindo transição para estados anterior e próximo
    // Por que foi escolhido: mantém regras de transição encapsuladas, separadas da classe Task

    @Override
    public void next(Task task) {
        task.setState(new CompletedState());
    }

    @Override
    public void prev(Task task) {
        task.setState(new ToDoState());
    }

    @Override
    public String getStatus() {
        return "Em andamento";
    }
}
