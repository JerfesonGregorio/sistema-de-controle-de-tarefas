package edu.ifpb.state;

public class ToDoState implements TaskState {
    private static final String YELLOW = "\u001B[33m";
    private static final String RESET = "\u001B[0m";

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
