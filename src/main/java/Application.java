import io.github.cdimascio.dotenv.Dotenv;
import io.javalin.Javalin;

public class Application {
    public static void run() {
        var dotenv = Dotenv.load();
        Javalin
                .create(config -> {
                    Configuration.Concurrency.configure(config.concurrency);
                    Configuration.TemplateRendering.configure(dotenv, config);
                    Configuration.Startup.configure(config.startup);
                    Configuration.StaticFiles.configure(dotenv, config.staticFiles);
                    Configuration.Routes.configure(config.routes);
                    Configuration.Json.configure(config);
                })
                .start(9000);
    }
}
