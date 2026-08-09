package Core;

import Core.Configuration.*;
import io.javalin.Javalin;

public class Application {
    public static void run() {
        Javalin
            .create(config -> {
                Concurrency.configure(config.concurrency);
                TemplateRendering.configure(config);
                RequestLogging.configure(config.requestLogger);
                Startup.configure(config.startup);
                StaticFiles.configure(config.staticFiles);
                Routes.configure(config.routes);
            })
            .start(9000);
    }
}
