package Repository;

import Data.Auth.Result.Value.User;
import org.jdbi.v3.core.Jdbi;

import java.util.Optional;

public class AuthRepository {

    private final Jdbi jdbi;

    public AuthRepository(Jdbi jdbi) {
        this.jdbi = jdbi;
    }

    public boolean existsLogin(String login) {
        return jdbi.withHandle(handle -> {
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

    public int createUser(String login, String password) {
        return jdbi.withHandle(handle -> {
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

    public Optional<User> findUserByLogin(String login) {
        return jdbi.withHandle(handle -> {
            var sql = """
            SELECT id, login
            FROM users
            WHERE login = :login
            """;
            return handle
                    .createQuery(sql)
                    .bind("login", login)
                    .map((rs, _) -> {
                        var rsId = rs.getInt("id");
                        var rsLogin = rs.getString("login");
                        return User.of(rsId, rsLogin);
                    })
                    .stream()
                    .findFirst();
        });
    }

    public Optional<String> findUserPasswordHash(String login) {
        return jdbi.withHandle(handle -> {
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

    public static AuthRepository of(Jdbi jdbi) {
        return new AuthRepository(jdbi);
    }
}
