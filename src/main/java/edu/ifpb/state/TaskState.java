package edu.ifpb.state;

public interface TaskState {

    void next();
    void prev();
    String getStatus();
}
