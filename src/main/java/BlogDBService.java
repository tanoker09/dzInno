import entity.Sex;
import entity.User;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.Arrays;

/**
 * Класс для обслуживания БД blog_user
 *
 */
@Slf4j
public class BlogDBService {
    private static final String SQL_INSERT_USER = "INSERT INTO blog_user (uuid, password, nickname, name, age, sex, email, moderator) " +
            "VALUES(?,?,?,?,?,?,?,?)";
    private static final String SQL_UPDATE_USER_NAME = "UPDATE blog_user SET name = ? WHERE uuid = ?";
    private static final String SQL_SELECT_USER = "SELECT * FROM blog_user WHERE uuid = ?";

    /**
     * Метод для добавления нового пользователя в БД
     *
     * @param newUser - новый пользователь
     */
    public void addNewUser(User newUser) {
        Connection connection = BlogDBConnector.getConnection();
        log.info(String.format("Connection to db - %s", connection.toString()));
        try (PreparedStatement preparedStatement = connection.prepareStatement(SQL_INSERT_USER)) {
            log.info("User for adding:");
            log.info(newUser.toString());
            preparedStatement.setString(1, newUser.getUuid());
            preparedStatement.setString(2, newUser.getPassword());
            preparedStatement.setString(3, newUser.getNickname());
            preparedStatement.setString(4, newUser.getName());
            preparedStatement.setInt(5, newUser.getAge());
            preparedStatement.setString(6, newUser.getSex().toString());
            preparedStatement.setString(7, newUser.getEmail());
            preparedStatement.setBoolean(8, newUser.isModerator());
            preparedStatement.executeUpdate();
            log.info("User added");
        } catch (SQLException e) {
            log.error(e.getMessage());
        } finally {
            BlogDBConnector.disconnectFromDatabase(connection);
        }
    }

    /**
     * Метод для пакетного добавления новых пользователей в БД
     *
     * @param users - массив пользователей
     * @throws SQLException
     */
    public void addUsers(User... users) throws SQLException {
        if (users.length < 500) {
            Connection connection = BlogDBConnector.getConnection();
            try (PreparedStatement preparedStatement = connection.prepareStatement(SQL_INSERT_USER)) {
                connection.setAutoCommit(false);
                connection.rollback();
                for (int i = 0; i < users.length; i++) {
                    preparedStatement.setString(1, users[i].getUuid());
                    preparedStatement.setString(2, users[i].getPassword());
                    preparedStatement.setString(3, users[i].getNickname());
                    preparedStatement.setString(4, users[i].getName());
                    preparedStatement.setInt(5, users[i].getAge());
                    preparedStatement.setString(6, users[i].getSex().toString());
                    preparedStatement.setString(7, users[i].getEmail());
                    preparedStatement.setBoolean(8, users[i].isModerator());
                    preparedStatement.addBatch();
                }
                preparedStatement.executeBatch();
                connection.commit();
            } catch (SQLException e) {
                connection.rollback();
                e.printStackTrace();
            } finally {
                BlogDBConnector.disconnectFromDatabase(connection);
            }
        } else {
            IllegalArgumentException e = new IllegalArgumentException("Слишком много запросов, укажите меньше пользователей!");
            throw e;
        }
    }

    /**
     * Метод для изменения имени пользователя в БД
     *
     * @param user    - пользователь, имя которого необходимо сменить
     * @param newName - новое имя
     * @throws SQLException
     */
    public void editUserName(User user, String newName) throws SQLException {
        Savepoint beforeUpdateSaving = null;
        Connection connection = BlogDBConnector.getConnection();
        try (PreparedStatement psUpdate = connection.prepareStatement(SQL_UPDATE_USER_NAME)) {
            connection.setAutoCommit(false);
            connection.rollback();
            selectUserByUuid(user.getUuid(), connection);
            beforeUpdateSaving = connection.setSavepoint();

            psUpdate.setString(1, newName);
            psUpdate.setString(2, user.getUuid());
            psUpdate.executeUpdate();
            connection.commit();

            selectUserByUuid(user.getUuid(), connection);
        } catch (SQLException e) {
            log.error(e.getMessage());
            connection.rollback(beforeUpdateSaving);
        } finally {
            BlogDBConnector.disconnectFromDatabase(connection);
        }
    }

    /**
     * Метод для выборки пользователей из БД
     *
     * @param uuid - критерий выбора
     */
    private void selectUserByUuid(String uuid, Connection connection) {
        try (PreparedStatement psSelect = connection.prepareStatement(SQL_SELECT_USER)) {
            psSelect.setString(1, uuid);
            ResultSet resultSet = psSelect.executeQuery();
            while (resultSet.next()) {
                User newUser = new User();
                newUser.setNickname(resultSet.getString(3));
                newUser.setName(resultSet.getString(4));
                newUser.setSex(Sex.fromString(resultSet.getString(5)));
                newUser.setEmail(resultSet.getString(6));
                newUser.setModerator(resultSet.getBoolean(7));
                newUser.setAge(resultSet.getInt(8));
                log.info(String.format("Select user - %s", newUser));
            }
        } catch (SQLException e) {
            log.error(e.getMessage());
        }
    }


}