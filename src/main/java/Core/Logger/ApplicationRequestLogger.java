package Core.Logger;

import io.javalin.http.Context;
import io.javalin.http.RequestLogger;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;

public record ApplicationRequestLogger(@NotNull Logger logger) implements RequestLogger {

    @Override
    public void handle(@NotNull Context ctx, @NotNull Float executionTimeMs) {
        logger.info(ctx.body(), executionTimeMs);
    }

    public static ApplicationRequestLogger of(@NotNull Logger logger) {
        return new ApplicationRequestLogger(logger);
    }
}
