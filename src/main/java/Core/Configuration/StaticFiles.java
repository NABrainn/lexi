package Core.Configuration;

import io.github.cdimascio.dotenv.Dotenv;
import io.javalin.config.StaticFilesConfig;
import io.javalin.http.staticfiles.Location;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Path;

public class StaticFiles {

    public static final Logger logger = LoggerFactory.getLogger(StaticFiles.class);

    public static void configure(Dotenv dotenv, StaticFilesConfig config) {
        var mode = dotenv.get("MODE");
        var baseDirectory = switch (mode) {
            case "DEV" -> Path.of(System.getProperty("user.dir"))
                    .resolve("src/main/resources")
                    .toAbsolutePath()
                    .normalize();
            case "PROD" -> "";
            default -> {
                logger.info("MODE is unset; falling back to DEV static files directory path");
                yield Path.of(System.getProperty("user.dir"))
                        .resolve("src/main/resources")
                        .toAbsolutePath()
                        .normalize();
            }
        };

        var location = switch (mode) {
            case "DEV" -> Location.EXTERNAL;
            case "PROD" -> Location.CLASSPATH;
            default -> {
                logger.info("MODE is unset; falling back to DEV static files directory location");
                yield Location.EXTERNAL;
            }
        };

        config.add(staticFiles -> {
            staticFiles.hostedPath = "/public/css";
            staticFiles.directory = baseDirectory + "/public/css";
            staticFiles.location = location;
        });

        config.add(staticFiles -> {
            staticFiles.hostedPath = "/public/js";
            staticFiles.directory = baseDirectory + "/public/js";
            staticFiles.location = location;
        });
    }
}