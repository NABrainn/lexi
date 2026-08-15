package Core.Configuration;

import Core.Application;
import io.javalin.config.RequestLoggerConfig;
import org.slf4j.LoggerFactory;

public class RequestLogging {
    public static void configure(RequestLoggerConfig config) {
        var logger = LoggerFactory.getLogger(Application.class);
        var appLogger = ApplicationRequestLogger.of(logger);
        config.http(appLogger);
    }
}
