package Core.Configuration;

import Controller.AuthController;
import Controller.IndexController;
import Helpers.Session;
import io.javalin.config.RoutesConfig;

import java.util.List;

import static io.javalin.apibuilder.ApiBuilder.*;


public class Routes {

    private static final List<String> protectedPaths = List.of("/");

    public static void configure(RoutesConfig config) {

        protectedPaths.forEach(path -> config.before(path, ctx -> {
            var refreshed = Session.tryToRefresh(ctx);
            if(!refreshed) {
                ctx.redirect("/login");
            }
        }));

        config.apiBuilder(() -> {
           path("/auth", () -> {
               post("/register", AuthController::register);
               post("/login", AuthController::login);
               get("/logout", AuthController::logout);
               get("/partials/login-form", AuthController::loginForm);
               get("/partials/register-form", AuthController::registerForm);
           });
           path("/", () -> {
               get("", IndexController::indexPage);
               get("/register", AuthController::registerPage);
               get("/login", AuthController::loginPage);
           });
        });
    }
}
