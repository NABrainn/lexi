package Helpers;

import org.jdbi.v3.core.Jdbi;

public class DB {
    private static boolean inited = false;
    private static Jdbi connection;

    public static void init(Jdbi jdbi) {
        if (!inited) {
            connection = jdbi;
            inited = true;
        }
    }

    public static Jdbi conn() {
        if(connection == null) {
            throw new IllegalStateException("Connection is not initialized");
        }
        return connection;
    }
}
