package Core;

import Core.Configuration.*;
import io.javalin.Javalin;

public class Application {
    public static void run() {
        var app = Javalin
                .create(config -> {
                    Concurrency.configure(config.concurrency);
                    TemplateRendering.configure(config);
                    RequestLogging.configure(config.requestLogger);
                    Startup.configure(config.startup);
                    Routes.configure(config.routes);
                    StaticFiles.configure(config.staticFiles);
                })
                .start(9000);
    }
}
