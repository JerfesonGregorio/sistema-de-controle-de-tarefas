package edu.ifpb.state;

public class CompletedState implements TaskState {

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
        return "Concluída";
    }
}
