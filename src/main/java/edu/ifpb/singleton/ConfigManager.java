package edu.ifpb.singleton;

public class ConfigManager {
    private static ConfigManager instance;
    private String dbConnection;

    private ConfigManager() {
        dbConnection = "jdbc:sqlite:tasks.db";
    }

    public static ConfigManager getInstance() {
        if (instance == null) {
            instance = new ConfigManager();
        }
        return instance;
    }

    public String getDbConnection() {
        return dbConnection;
    }
}
