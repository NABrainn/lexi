package Core;

import Core.Configuration.*;
import Core.Configuration.Log.RequestLogging;
import io.github.cdimascio.dotenv.Dotenv;
import io.javalin.Javalin;

public class Application {
    public static void run() {
        var dotenv = Dotenv.load();
        Javalin
            .create(config -> {
                Concurrency.configure(config.concurrency);
                TemplateRendering.configure(dotenv, config);
                RequestLogging.configure(config.requestLogger);
                Startup.configure(config.startup);
                StaticFiles.configure(dotenv, config.staticFiles);
                Routes.configure(config.routes);
                Json.configure(config);
            })
            .start(9000);
    }
}
