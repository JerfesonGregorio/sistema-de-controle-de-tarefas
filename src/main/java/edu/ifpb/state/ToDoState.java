package edu.ifpb.state;

public class ToDoState implements TaskState {

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
