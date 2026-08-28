import Configuration.*;
import io.github.cdimascio.dotenv.Dotenv;
import io.javalin.Javalin;

public class Application {
    public static void run() {
        var dotenv = Dotenv.load();
        Javalin
                .create(config -> {
                    Concurrency.configure(config.concurrency);
                    TemplateRendering.configure(dotenv, config);
                    Startup.configure(config.startup);
                    StaticFiles.configure(dotenv, config.staticFiles);
                    Routes.configure(config.routes);
                    Json.configure(config);
                })
                .start(9000);
    }
}
