package Core.Configuration;

import io.javalin.config.StaticFilesConfig;
import io.javalin.http.staticfiles.Location;

public class StaticFiles {
    public static void configure(StaticFilesConfig config) {
        config.add(staticFiles -> {
            staticFiles.hostedPath = "/css";
            staticFiles.directory = "/css";
            staticFiles.location = Location.CLASSPATH;
        });
        config.add(staticFiles -> {
            staticFiles.hostedPath = "/js";
            staticFiles.directory = "/js";
            staticFiles.location = Location.CLASSPATH;
        });
    }
}
