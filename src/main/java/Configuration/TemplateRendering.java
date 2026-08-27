package Configuration;

import gg.jte.ContentType;
import gg.jte.TemplateEngine;
import io.github.cdimascio.dotenv.Dotenv;
import io.javalin.config.JavalinConfig;
import io.javalin.rendering.template.JavalinJte;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Path;

public class TemplateRendering {

    public static final Logger logger = LoggerFactory.getLogger(TemplateRendering.class);

    public static void configure(Dotenv dotenv, JavalinConfig config) {
        var mode = dotenv.get("MODE");
        var templateEngine = switch(mode) {
            case "DEV" -> JavalinJte.Companion.directoryTemplateEngine();
            case "PROD" -> TemplateEngine.createPrecompiled(Path.of("target/jte-classes"), ContentType.Html);
            default -> {
                logger.info("Failed to resolve value of MODE in .env, falling back TemplateEngine config to DEV");
                yield JavalinJte.Companion.directoryTemplateEngine();
            }
        };
        var jte = new JavalinJte(templateEngine);
        config.fileRenderer(jte);
    }
}