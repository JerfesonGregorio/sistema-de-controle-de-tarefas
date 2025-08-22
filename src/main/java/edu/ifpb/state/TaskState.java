package edu.ifpb.state;

public interface TaskState {
    // Padrão: State
    // Onde aplicado: TaskState (interface)
    // Como implementado: define métodos para transição de estados (next, prev) e consulta de status
    // Por que foi escolhido: fornece contrato uniforme para todos os estados de Task, permitindo polimorfismo
    void next(Task task);
    void prev(Task task);
    String getStatus();
}
