import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Optional;
import java.util.Properties;

/**
 * Класс для подключения к БД PostgreSQL blog_user
 *
 */
@Slf4j
public class BlogDBConnector {

    private BlogDBConnector() {
    }

    /**
     * Метод для подключения к БД. Использует файл свойств database.properties
     *
     * @return коннект к БД
     * @throws SQLException
     * @throws IOException
     */
    public static Connection getConnection() {
        Properties properties = new Properties();
        Connection connection = null;
        try {
            FileInputStream fis = new FileInputStream("src/main/resources/database.properties");
            properties.load(fis);
        } catch (IOException e) {
        }
        String url = properties.getProperty("db.url");
        String user = properties.getProperty("db.user");
        String password = properties.getProperty("db.password");
        try {
            connection = DriverManager.getConnection(url, user, password);
            log.info("connection got");
        } catch (SQLException e) {
            log.error(e.getMessage());
        }
        return connection;
    }

    /**
     * Метод для отключения от БД
     *
     * @throws SQLException
     * @throws IOException
     */
    public static void disconnectFromDatabase(Connection connection) {
        Optional.ofNullable(connection).ifPresent(connection1 -> {
            try {
                connection1.close();
                log.debug("Connection closed");
            } catch (SQLException e) {
                log.error(e.getMessage());
            }
        });
    }

}
