package edu.ifpb;

import edu.ifpb.state.Task;

import java.util.List;

public class Util {

    public void listar(List<Task> tasks, String titulo) {
        System.out.println("########## " + titulo + " ##########");
        if (tasks.isEmpty()) {
            System.out.println("(nenhuma tarefa encontrada)");
        } else {
            tasks.forEach(t ->
                    System.out.println(t.getId() + " - " + t.getName() + " | Status: " + t.getStatus())
            );
        }
    }
}
