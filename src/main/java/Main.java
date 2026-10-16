import Controller.AuthController;
import Controller.IndexController;
import Controller.LessonController;
import Data.Auth.Result.Success.User;
import Helpers.*;
import Repository.AuthRepository;
import Repository.LessonRepository;
import Repository.TranslationRepository;
import Service.AuthService;
import Service.JsoupService;
import Service.LessonService;
import Service.PasswordManager;
import Service.Tokenizer;
import com.google.gson.GsonBuilder;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import gg.jte.ContentType;
import gg.jte.TemplateEngine;
import io.github.cdimascio.dotenv.Dotenv;
import io.javalin.Javalin;
import io.javalin.http.staticfiles.Location;
import io.javalin.json.JsonMapper;
import io.javalin.rendering.template.JavalinJte;
import org.flywaydb.core.Flyway;
import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.core.statement.SqlStatements;
import org.jetbrains.annotations.NotNull;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Type;
import java.nio.file.Path;
import java.util.List;

import static io.javalin.apibuilder.ApiBuilder.*;

void main() {
    Javalin.create(config -> {
        // Environment and logging
        var dotenv = Dotenv.load();
        var mode = dotenv.get("MODE");
        var databaseUrl = dotenv.get("DB_URL");
        var logger = LoggerFactory.getLogger("Main");

        // Database and migrations
        var flyway = Flyway
                .configure()
                .dataSource(databaseUrl, null, null)
                .load();

        if (databaseUrl.isEmpty()) {
            throw new RuntimeException("Database URL not set");
        }
        var hikariConfig = new HikariConfig();
        hikariConfig.setDriverClassName("org.sqlite.JDBC");
        hikariConfig.setJdbcUrl(databaseUrl);
        hikariConfig.setMaximumPoolSize(10);
        hikariConfig.setMinimumIdle(2);
        hikariConfig.setConnectionTimeout(10000);
        var dataSource = new HikariDataSource(hikariConfig);
        var jdbi = Jdbi.create(dataSource);
        jdbi
            .registerRowMapper(new AbstractRowMapperFactory())
            .getConfig(SqlStatements.class)
            .addExceptionHandler(handler -> {
                logger.error(handler.getSQLState());
                logger.error(handler.getMessage());
                return new RuntimeException(handler.getMessage());
            });
        DB.init(jdbi);
        flyway.migrate();

        // Server settings
        config.concurrency.useVirtualThreads = true;
        config.startup.showJavalinBanner = false;
        config.startup.showOldJavalinVersionWarning = false;

        // JSON serialization
        var gson = new GsonBuilder().create();
        config.jsonMapper(new JsonMapper() {
            @NotNull
            @Override
            public String toJsonString(@NotNull Object obj, @NotNull Type type) {
                return gson.toJson(obj, type);
            }

            @NotNull
            @Override
            public <T> T fromJsonString(@NotNull String json, @NotNull Type targetType) {
                return gson.fromJson(json, targetType);
            }
        });

        // Template rendering
        var templateEngine = switch (mode) {
            case "DEV" -> JavalinJte.Companion.directoryTemplateEngine();
            case "PROD" -> TemplateEngine.createPrecompiled(Path.of("target/jte-classes"), ContentType.Html);
            default -> {
                logger.info("Failed to resolve value of MODE in .env, falling back TemplateEngine config to DEV");
                yield JavalinJte.Companion.directoryTemplateEngine();
            }
        };
        config.fileRenderer(new JavalinJte(templateEngine));

        // Static files
        var baseDirectory = switch (mode) {
            case "DEV" -> Path.of(System.getProperty("user.dir"))
                    .resolve("src/main/resources").toAbsolutePath().normalize().toString();
            case "PROD" -> "";
            default -> {
                logger.info("MODE is unset; falling back to DEV static files directory path");
                yield Path.of(System.getProperty("user.dir"))
                        .resolve("src/main/resources").toAbsolutePath().normalize().toString();
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
        config.staticFiles.add(files -> {
            files.hostedPath = "/public/css";
            files.directory = baseDirectory + "/public/css";
            files.location = location;
        });
        config.staticFiles.add(files -> {
            files.hostedPath = "/public/js";
            files.directory = baseDirectory + "/public/js";
            files.location = location;
        });
        config.staticFiles.add(files -> {
            files.hostedPath = "/public/img";
            files.directory = baseDirectory + "/public/img";
            files.location = location;
        });

        // Authentication dependencies
        var passwordManager = PasswordManager.of();
        var authRepository = AuthRepository.of(jdbi);
        var authService = AuthService.of(passwordManager, authRepository);
        var authController = AuthController.of(authService);

        // Lesson dependencies
        var lessonRepository = LessonRepository.of(jdbi);
        var jsoupService = JsoupService.of();
        var translationRepository = TranslationRepository.of(jdbi);
        var tokenizer = Tokenizer.of(translationRepository);
        var lessonService = LessonService.of(jsoupService, tokenizer, lessonRepository);
        var lessonController = LessonController.of(lessonService);

        // Index dependencies
        var indexController = IndexController.of();

        // Error handling
        var serverErrorExceptions = List.of(
                NullPointerException.class,
                RuntimeException.class,
                IllegalArgumentException.class,
                IllegalStateException.class
        );
        serverErrorExceptions.forEach(exceptionClass -> {
            config.routes.exception(exceptionClass, (e, ctx) -> {
                if (e.getCause() != null) {
                    logger.error(e.getCause().getMessage());
                }
                if (e.getStackTrace() != null) {
                    for (var element : e.getStackTrace()) {
                        logger.error(element.toString());
                    }
                }
                if (e.getMessage() != null) {
                    logger.error(e.getMessage());
                }
                ctx.status(500);
            });
        });
        config.routes.error(500, ctx -> {
            if (Headers.isHxRequest(ctx)) {
                Headers.hxRedirect(ctx, "/error-500");
            } else {
                ctx.redirect("/error-500");
            }
        });

        // Session and authentication filters
        var protectedPaths = List.of("/");
        protectedPaths.forEach(path -> config.routes.before(path, ctx -> {
            if (!Session.isAuthenticated(ctx)) {
                ctx.redirect("/login");
            }
        }));

        // Routes
        config.routes.beforeMatched(ctx -> {
            var secFetchDestHeaderValue = ctx.header("Sec-Fetch-Dest");
            var isDatastarRequest = ctx.header("Datastar-Request");
            var isRouteRequest = (secFetchDestHeaderValue != null && secFetchDestHeaderValue.equals("document")) || (isDatastarRequest != null && isDatastarRequest.equals("true"));
            if(isRouteRequest) {
                var user = Session.getUser(ctx);
                if(user != null) {
                    var updatedUser = user.withPath(ctx.path());
                    Session.updateUser(ctx, updatedUser);
                    Session.refresh(ctx);
                }
            }
        });
        config.routes.get("/error-500", ctx -> JteResponses.with(ctx).render("pages/error-500.jte"));
        config.routes.apiBuilder(() -> {
            path("/auth", () -> {
                post("/register", authController::register);
                post("/login", authController::login);
                get("/logout", authController::logout);
                get("/login-form", authController::loginForm);
                get("/register-form", authController::registerForm);
            });
            path("/lessons", () -> get("/create", lessonController::createLessonForm));
            path("/", () -> {
                get("", indexController::indexPage);
                get("/register", authController::registerPage);
                get("/login", authController::loginPage);
            });
        });
    }).start(9000);
}
