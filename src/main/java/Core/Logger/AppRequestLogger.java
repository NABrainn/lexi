package Core.Logger;

import io.javalin.http.Context;
import io.javalin.http.RequestLogger;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public record AppRequestLogger(@NotNull Logger logger) implements RequestLogger {

    @Override
    public void handle(@NotNull Context ctx, @NotNull Float executionTimeMs) {
        var logger = LoggerFactory.getLogger(AppRequestLogger.class);
        logger.info(ctx.body(), executionTimeMs);
    }

    public static AppRequestLogger of(@NotNull Logger logger) {
        return new AppRequestLogger(logger);
    }
}
