package Core.Configuration;

import Controller.AuthController;
import Controller.IndexController;
import Controller.LessonController;
import Helpers.Headers;
import Helpers.JteResponses;
import Helpers.Session;
import io.javalin.config.RoutesConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.List;

import static io.javalin.apibuilder.ApiBuilder.*;


public class Routes {

    private static final Logger LOG = LoggerFactory.getLogger(Routes.class);
    private static final List<String> protectedPaths = List.of("/");
    private static final List<Class<? extends RuntimeException>> serverErrorExceptions = List.of(NullPointerException.class, RuntimeException.class);

    public static void configure(RoutesConfig config) {

        serverErrorExceptions.forEach(exceptionClass -> {
            config.exception(exceptionClass, (e, ctx) -> {
                if(e.getCause() != null) {
                    LOG.error(e.getCause().getMessage());
                }
                if(e.getStackTrace() != null) {
                    for (var element : e.getStackTrace()) {
                        LOG.error(element.toString());
                    }
                }
                if(e.getMessage() != null) {
                    LOG.error(e.getMessage());
                }
                ctx.status(500);
            });
            config.error(500, (ctx) -> {
                switch (Headers.isHxRequest(ctx)) {
                    case true -> Headers.hxRedirect(ctx, "/error-500");
                    case false -> ctx.redirect("/error-500");
                }
            });
        });
        protectedPaths.forEach(path -> config.before(path, ctx -> {
            Session.refresh(ctx);
            if(!Session.isAuthenticated(ctx)) {
                ctx.redirect("/login");
            }
        }));

        config.get("/error-500", (ctx) -> {
            JteResponses.with(ctx)
                    .render("pages/error-500.jte");
        });

        config.apiBuilder(() -> {
           path("/auth", () -> {
               post("/register", AuthController::register);
               post("/login", AuthController::login);
               get("/logout", AuthController::logout);
               get("/login-form", AuthController::loginForm);
               get("/register-form", AuthController::registerForm);
           });
           path("/lessons", () -> {
               get("/create", LessonController::create);
           });
           path("/", () -> {
               get("", IndexController::indexPage);
               get("/register", AuthController::registerPage);
               get("/login", AuthController::loginPage);
           });
        });
    }
}
