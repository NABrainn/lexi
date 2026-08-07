package Core;

import Core.Configuration.*;
import gg.jte.ContentType;
import gg.jte.TemplateEngine;
import io.github.cdimascio.dotenv.Dotenv;
import io.javalin.Javalin;
import io.javalin.rendering.template.JavalinJte;

import java.nio.file.Path;

public class Application {
    public static void run() {
        var app = Javalin
                .create(config -> {
                    Concurrency.configure(config.concurrency);
                    TemplateRendering.configure(config);
                    RequestLogging.configure(config.requestLogger);
                    Startup.configure(config.startup);
                    Routes.configure(config.routes);
                })
                .start(9000);
    }
}
