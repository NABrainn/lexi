package Core.Configuration;

import io.javalin.config.RoutesConfig;

import static io.javalin.apibuilder.ApiBuilder.path;

public class Routes {
    public static void configure(RoutesConfig config) {
        config.apiBuilder(() -> {
           path("/users", () -> {

           });
        });
    }
}
