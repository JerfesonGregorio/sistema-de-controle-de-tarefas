package edu.ifpb.observer;

// Padrão: Observer (interface)
// Onde aplicado: Observer
// Como implementado: define o contrato de atualização para qualquer classe que queira receber notificações
// Por que foi escolhido: fornece polimorfismo e desacopla emissor e receptor de eventos
public interface Observer {
    void update(String message);
}
