package entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.UUID;
/**
 * Класс для создания пользователей
 *
 */
@ToString
@NoArgsConstructor
public class User {
    @Getter
    private String uuid;
    @Getter
    @Setter
    private String password;
    @Getter
    @Setter
    private String nickname;
    @Getter
    @Setter
    private String name;
    @Getter
    @Setter
    private int age;
    @Getter
    @Setter
    private Sex sex;
    @Getter
    @Setter
    private String email;
    @Getter
    @Setter
    private boolean isModerator;

    public User(String password, String nickname, String name, int age, Sex sex, String email) {
        this.uuid = UUID.randomUUID().toString();
        this.password = password;
        this.nickname = nickname;
        this.name = name;
        this.age = age;
        this.sex = sex;
        this.email = email;
        this.isModerator = false;
    }

}
