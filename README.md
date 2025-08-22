# Task Management System

Sistema de gerenciamento de tarefas via terminal, desenvolvido em Java 21.

## Requisitos

Java 21 JDK ou JRE instalado.

## Variáveis de ambiente do banco de dados:

Configure as variáveis de ambiente no seu sistema. A baixo estão as credenciais de um banco postgres do neondb utilizado no projeto. Caso não seja possível estabelecer conexão, basta criar uma outra instância no neon para testar [link para neondb](https://console.neon.tech/). As tabelas serão criadas automaticamente no momento da conexão com o novo banco.

```
DB_URL=???

DB_USER=???

DB_PASSWORD=???
```

## Comando de execução

No local onde app.jar estiver, rode o comando:
```bash
java -jar app.jar
```

## Estrutura do projeto
```
src/
 └─ main/
    └─ java/
       └─ edu/ifpb/
           ├─ singleton/
           │   └─ ConfigManager.java
           │
           ├─ state/
           │   ├─ TaskState.java
           │   ├─ ToDoState.java
           │   ├─ InProgressState.java
           │   ├─ CompletedState.java
           │   └─ Task.java
           │
           ├─ repository/
           │   ├─ TaskRepository.java
           │   └─ UserRepository.java
           │
           ├─ observer/
           │   ├─ Observer.java
           │   ├─ Subject.java
           │   ├─ TaskSubject.java
           │   └─ User.java
           │
           ├─ facade/
           │   └─ TaskManagementFacade.java
           │
           ├─ ui/
           │   ├─ MainMenu.java
           │   ├─ Menu.java
           │   ├─ MenuUI.java
           │   ├─ RelationMenu.java
           │   ├─ SystemMenu.java
           │   ├─ TaskMenu.java
           │   └─ UserMenu.java
           │
           ├─ Util.java
           └─ Main.java
```

Este sistema de tarefas foi desenvolvido por **Jerfeson Gregorio Moreno**, como parte do projeto de estudo e prática de padrões de projeto.