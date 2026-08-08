package Core.Configuration;

import Controllers.AuthController;
import io.javalin.config.RoutesConfig;

import static io.javalin.apibuilder.ApiBuilder.*;


public class Routes {
    public static void configure(RoutesConfig config) {
        config.apiBuilder(() -> {
           path("/auth", () -> {
               post("/register", AuthController::register);
           });
           path("/", () -> {
               get("/", ctx -> ctx.render("pages/register.jte"));
           });
        });
    }
}
