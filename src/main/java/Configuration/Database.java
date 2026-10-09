package Configuration;

import io.github.cdimascio.dotenv.Dotenv;
import org.flywaydb.core.Flyway;
import org.flywaydb.core.api.MigrationVersion;
import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.core.statement.SqlStatements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Database {
    private static final Logger LOG = LoggerFactory.getLogger(Database.class);
    private static String DB_URL = "";
    private static Flyway flyway = null;

    public static Jdbi getConnection() {
        if(DB_URL.isEmpty()) {
            throw new RuntimeException("Database URL not set");
        }
        var connection = Jdbi.create(DB_URL);
        connection
                .getConfig(SqlStatements.class)
                .addExceptionHandler((handler) -> {
                    LOG.error(handler.getSQLState());
                    LOG.error(handler.getMessage());
                    return new RuntimeException(handler.getMessage());
                });
        return connection;
    }

    public static void configure(Dotenv dotenv) {
        DB_URL = dotenv.get("DB_URL");
        flyway = Flyway.configure()
                .dataSource(DB_URL, null, null)
                .load();
    }

    public static void migrate() {
        flyway.migrate();
    }
}
