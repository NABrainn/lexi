package Controller;

import Data.Auth.Result.Error.UserAlreadyExistsError;
import Data.Auth.Result.Error.UserDoesNotExistError;
import Data.Operation.Implementations.AuthCommand;
import Data.Result.Failure;
import Data.Result.Success;
import Data.Validation.Rule;
import Helpers.*;
import Helpers.Validation.Rules;
import Service.AuthService;
import io.javalin.http.Context;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class AuthController{
    private static final Map<String, List<Rule<?>>> authRules = Map.of(
            "login", List.of(
                    Rules.required("Login is required"),
                    Rules.minLength(8, "Login must be at least 8 characters")
            ),
            "password", List.of(
                    Rules.required("Password is required"),
                    Rules.minLength(8, "Password must be at least 8 characters")
            )
    );

    public static void registerPage(Context ctx) {
        var styles = List.of("pages/auth.css");
        var params = Map.of("styles", styles);

        if (Session.isAuthenticated(ctx)) {
            ctx.redirect("/");
        }

        JteResponses
                .ctx(ctx)
                .path("pages/register.jte")
                .params(params)
                .render();
    }

    public static void registerForm(Context ctx) {
        JteResponses
                .ctx(ctx)
                .path("partials/register-form.jte")
                .render();
    }

    public static void register(Context ctx) {
        var loginValidator = ctx.formParamAsClass("login", String.class)
                .check(Objects::nonNull, "Login cannot be null")
                .check(login -> !login.trim().isEmpty(), "Login cannot be empty")
                .check(login -> login.trim().length() >= 8, "Login must be at least 8 characters");

        var passwordValidator = ctx.formParamAsClass("password", String.class)
                .check(Objects::nonNull, "Password cannot be null")
                .check(password -> !password.trim().isEmpty(), "Password cannot be empty")
                .check(password -> password.trim().length() >= 8, "Password must be at least 8 characters");

        if(Validators.hasErrors(loginValidator, passwordValidator)) {
            var errors = Validators.errors(loginValidator, passwordValidator);
            var login = loginValidator.getOrDefault("");
            var params = Map.of("login", login, "errors", errors);
            JteResponses
                    .ctx(ctx)
                    .path("partials/register-form.jte")
                    .params(params)
                    .status(400)
                    .render();
        }
        else {
            var login = loginValidator.get();
            var password = passwordValidator.get();
            var command = AuthCommand.of(login, password);
            var registerResult = AuthService.register(command);

            switch (registerResult) {
                case Failure(UserAlreadyExistsError(var message)) -> {
                    var params = Map.of("serviceError", message);
                    JteResponses
                            .ctx(ctx)
                            .path("partials/register-form.jte")
                            .params(params)
                            .status(400)
                            .render();
                }
                case Success(var _) ->
                        JteResponses
                                .ctx(ctx)
                                .path("partials/login-form.jte")
                                .render();

            }
        }
    }

    public static void loginPage(Context ctx) {
        var styles = List.of("pages/auth.css");
        var params = Map.of("styles", styles);

        if (Session.isAuthenticated(ctx)) {
            ctx.redirect("/");
        }

        JteResponses
                .ctx(ctx)
                .path("pages/login.jte")
                .params(params)
                .render();
    }

    public static void loginForm(Context ctx) {
        JteResponses
                .ctx(ctx)
                .path("partials/login-form.jte")
                .render();

    }

    public static void login(Context ctx) {
        var loginValidator = ctx.formParamAsClass("login", String.class)
                .check(Objects::nonNull, "Login cannot be null")
                .check(login -> !login.trim().isEmpty(), "Login cannot be empty")
                .check(login -> login.trim().length() >= 8, "Login must be at least 8 characters");

        var passwordValidator = ctx.formParamAsClass("password", String.class)
                .check(Objects::nonNull, "Password cannot be null")
                .check(password -> !password.trim().isEmpty(), "Password cannot be empty")
                .check(password -> password.trim().length() >= 8, "Password must be at least 8 characters");

        if(Validators.hasErrors(loginValidator, passwordValidator)) {
            var login = loginValidator.getOrDefault("");
            var errors = Validators.errors(loginValidator, passwordValidator);
            var params = Map.of("login", login, "errors", errors);
            JteResponses
                    .ctx(ctx)
                    .path("partials/login-form.jte")
                    .params(params)
                    .status(400)
                    .render();
        }
        else {
            var login = loginValidator.get();
            var password = passwordValidator.get();
            var command = AuthCommand.of(login, password);
            var loginResult = AuthService.login(command);

            switch (loginResult) {
                case Failure(UserDoesNotExistError(var message)) -> {
                    var params = Map.of("serviceError", message);
                    JteResponses
                            .ctx(ctx)
                            .path("partials/login-form.jte")
                            .params(params)
                            .status(400)
                            .render();
                }
                case Success(var user) -> {
                    Session.authenticate(ctx, user);
                    Headers.hxRedirect(ctx, "/");
                }
            }
        }
    }

    public static void logout(Context ctx) {
        Session.logout(ctx);
        Headers.hxRedirect(ctx, "/login");
    }
}
