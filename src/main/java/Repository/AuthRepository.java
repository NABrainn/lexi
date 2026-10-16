package Repository;

import Data.Auth.Record.UserRecord;
import org.jdbi.v3.core.Jdbi;

import java.util.Optional;

public class AuthRepository {

    private final Jdbi jdbi;

    public AuthRepository(Jdbi jdbi) {
        this.jdbi = jdbi;
    }

    public boolean existsLogin(String login) {
        return jdbi.withHandle(handle -> handle
                .createQuery("""
                SELECT login
                FROM users
                WHERE login = :login
                """)
                .bind("login", login)
                .mapTo(String.class)
                .stream()
                .findFirst()
                .isPresent());
    }

    public int createUser(String login, String password) {
        return jdbi.withHandle(handle -> handle
                .createUpdate("""
                INSERT INTO users (login, password)
                VALUES (:login, :password)
                """)
                .bind("login", login)
                .bind("password", password)
                .execute());
    }

    public Optional<UserRecord> findUserByLogin(String login) {
        return jdbi.withHandle(handle -> handle
                .createQuery("""
                SELECT id, login
                FROM users
                WHERE login = :login
                """)
                .bind("login", login)
                .mapTo(UserRecord.class)
                .stream()
                .findFirst());
    }

    public Optional<String> findUserPasswordHash(String login) {
        return jdbi.withHandle(handle -> handle
                .createQuery("""
                SELECT password
                FROM users
                WHERE login = :login
                """)
                .bind("login", login)
                .mapTo(String.class)
                .stream()
                .findFirst());
    }

    public static AuthRepository of(Jdbi jdbi) {
        return new AuthRepository(jdbi);
    }
}
