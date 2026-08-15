package Core.Configuration;

import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.core.statement.SqlStatements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Database {
    private static final Logger LOG = LoggerFactory.getLogger(Database.class);
    private static final String URL = "jdbc:sqlite:lexi.db";

    public static Jdbi connect() {
        var connection = Jdbi.create(URL);
        connection
                .getConfig(SqlStatements.class)
                .addExceptionHandler((handler) -> {
                    LOG.error(handler.getSQLState());
                    return new RuntimeException(handler.getMessage());
                });
        return connection;
    }
}
