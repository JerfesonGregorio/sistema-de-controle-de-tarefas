package edu.ifpb;

import edu.ifpb.repository.TaskRepository;
import edu.ifpb.singleton.ConfigManager;

import java.sql.SQLException;

public class Main {

    public static void main(String[] args) throws SQLException {

        TaskRepository taskRepository = new TaskRepository(ConfigManager.getInstance().getConnection());

        taskRepository.pingDatabase();

    }
}
