package edu.ifpb.state;

public class ToDoState implements TaskState {
    // Padrão: State
// Onde aplicado: ToDoState
// Como implementado: representa o estado "Pendente" de uma Task, permitindo transição para o próximo estado
// Por que foi escolhido: separa o comportamento dependente do estado da lógica principal da Task, permitindo fácil adição de novos estados

    @Override
    public void next(Task task) {
        task.setState(new InProgressState());
    }

    @Override
    public void prev(Task task) {
        System.out.println("Tarefa já está no estado inicial.");
    }

    @Override
    public String getStatus() {
        return "Pendente";
    }
}
