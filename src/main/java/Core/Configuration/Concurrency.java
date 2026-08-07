package Core.Configuration;

import io.javalin.config.ConcurrencyConfig;

public class Concurrency {
    public static void configure(ConcurrencyConfig config) {
        config.useVirtualThreads = true;
    }
}
