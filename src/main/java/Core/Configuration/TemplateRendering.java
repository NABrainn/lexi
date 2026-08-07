package Core.Configuration;

import gg.jte.ContentType;
import gg.jte.TemplateEngine;
import io.github.cdimascio.dotenv.Dotenv;
import io.javalin.config.JavalinConfig;
import io.javalin.rendering.template.JavalinJte;

import java.nio.file.Path;

public class TemplateRendering {
    public static void configure(JavalinConfig config) {
        final var ENV = Dotenv.load();
        var isDev = Boolean.parseBoolean(ENV.get("DEV"));
        var templateEngine = isDev
                ? JavalinJte.Companion.directoryTemplateEngine()
                : TemplateEngine.createPrecompiled(Path.of("target/jte-classes"), ContentType.Html);
        var jte = new JavalinJte(templateEngine);
        config.fileRenderer(jte);
    }
}
