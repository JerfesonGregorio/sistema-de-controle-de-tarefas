package edu.ifpb.state;

public class CompletedState implements TaskState {
    private static final String GREEN = "\u001B[32m";
    private static final String RESET = "\u001B[0m";

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
