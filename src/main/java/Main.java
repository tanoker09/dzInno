import entity.Sex;
import entity.User;

import java.sql.SQLException;

public class Main {
    public static void main(String[] args) throws SQLException {
        User user = new User("password", "user1","peter", 25, Sex.MALE, "user1@mail.ru");
        User user2 = new User("password", "user2","peter", 25, Sex.MALE, "user1@mail.ru");
        BlogDBService blogDBService = new BlogDBService();
        blogDBService.addNewUser(user);
        blogDBService.editUserName(user, "Bob");
    }
}
