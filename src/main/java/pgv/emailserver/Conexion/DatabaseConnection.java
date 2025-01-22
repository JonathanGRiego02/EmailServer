package pgv.emailserver.Conexion;

import com.zaxxer.hikari.*;

import java.sql.*;

public class DatabaseConnection {

    private static HikariDataSource dataSource;

    static {
        try {
            HikariConfig config = new HikariConfig();
            config.setDriverClassName("com.mysql.cj.jdbc.Driver");
            config.setJdbcUrl("jdbc:mysql://localhost:3306/mysql");
            config.setUsername("root");
            config.setPassword("");
            config.setMaximumPoolSize(10);
            config.setMinimumIdle(2);
            dataSource = new HikariDataSource(config);

            try (Connection connection = dataSource.getConnection();
                 Statement statement = connection.createStatement()) {

                ResultSet resultSet = statement.executeQuery("SHOW DATABASES LIKE 'mercury'");
                if (!resultSet.next()) {
                    statement.executeUpdate("CREATE DATABASE mercury");
                    System.out.println("Base de datos 'mercury' creada.");
                } else {
                    System.out.println("La base de datos 'mercury' ya existe.");
                }
            }

            config.setJdbcUrl("jdbc:mysql://localhost:3306/mercury");
            dataSource = new HikariDataSource(config);

        } catch (Exception e) {
            System.err.println("Error configurando la conexión: " + e.getMessage());
            throw new ExceptionInInitializerError(e);
        }
    }

    public static Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }

    public static void close() {
        if (dataSource != null) {
            dataSource.close();
        }
    }
}
