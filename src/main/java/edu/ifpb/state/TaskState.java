package edu.ifpb.state;

public interface TaskState {

    void next(Task task);
    void prev(Task task);
    String getStatus();
}
