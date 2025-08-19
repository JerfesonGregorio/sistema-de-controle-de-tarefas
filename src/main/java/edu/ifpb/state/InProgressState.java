package edu.ifpb.state;

public class InProgressState implements TaskState {
    private static final String BLUE = "\u001B[34m";
    private static final String RESET = "\u001B[0m";

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
        return BLUE + "⏳ Em andamento" + RESET;
    }
}
