package edu.ifpb.state;

public class InProgressState implements TaskState {
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
