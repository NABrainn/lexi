package Repository;

import Core.Configuration.Database;
import Data.Auth.Result.Value.User;

import java.util.Optional;

public class AuthRepository {
    public static boolean existsLogin(String login) {
        return Database.connect().withHandle(handle -> {
            var sql = """
            SELECT login
            FROM users
            WHERE login = :login
            """;
            return handle
                    .createQuery(sql)
                    .bind("login", login)
                    .mapTo(String.class)
                    .stream()
                    .findFirst()
                    .isPresent();
        });
    }

    public static int createUser(String login, String password) {
        return Database.connect().withHandle(handle -> {
            var sql = """
            INSERT INTO users (login, password)
            VALUES (:login, :password)
            """;
            return handle
                    .createUpdate(sql)
                    .bind("login", login)
                    .bind("password", password)
                    .execute();
        });
    }

    public static Optional<User> findUserByLogin(String login) {
        return Database.connect().withHandle(handle -> {
            var sql = """
            SELECT id, login
            FROM users
            WHERE login = :login
            """;
            return handle
                    .createQuery(sql)
                    .bind("login", login)
                    .map((rs, _) -> User.of(rs.getLong("id"), rs.getString("login")))
                    .stream()
                    .findFirst();
        });
    }

    public static Optional<String> getPasswordHash(String login) {
        return Database.connect().withHandle(handle -> {
            var sql = """
            SELECT password
            FROM users
            WHERE login = :login
            """;
            return handle
                    .createQuery(sql)
                    .bind("login", login)
                    .mapTo(String.class)
                    .stream()
                    .findFirst();
        });
    }
}
