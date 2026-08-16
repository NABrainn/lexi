package Controller;

import Data.Auth.Result.Error.UserAlreadyExistsError;
import Data.Auth.Result.Error.UserDoesNotExistError;
import Data.Operation.Implementations.AuthCommand;
import Data.Result.Failure;
import Data.Result.Success;
import Helpers.Form;
import Helpers.FormErrors;
import Helpers.Headers;
import Helpers.JteResponses;
import Helpers.Rules;
import Helpers.Session;
import Service.AuthService;
import io.javalin.http.Context;
import io.javalin.validation.Validation;

import java.util.List;
import java.util.Map;

public class AuthController {
    public static void registerPage(Context ctx) {
        var styles = List.of("pages/auth.css");
        var params = Map.of("styles", styles);

        if (Session.isAuthenticated(ctx)) {
            ctx.redirect("/");
        }
        else {
            JteResponses
                    .ctx(ctx)
                    .path("pages/register.jte")
                    .params(params)
                    .render();
        }
    }

    public static void registerForm(Context ctx) {
        JteResponses
                .ctx(ctx)
                .path("partials/register-form.jte")
                .render();
    }

    public static void register(Context ctx) {
        var loginValidator = ctx.formParamAsClass("login", String.class)
                .check(Rules.required(), "Login is required")
                .check(Rules.minLength(8), "Login must be at least 8 characters");

        var passwordValidator = ctx.formParamAsClass("password", String.class)
                .check(Rules.required(), "Password is required")
                .check(Rules.minLength(8), "Password must be at least 8 characters");

        if (!Form.hasErrors(loginValidator, passwordValidator)) {
            var login = loginValidator.get();
            var password = passwordValidator.get();
            var command = AuthCommand.of(login, password);
            var registerResult = AuthService.register(command);

            switch (registerResult) {
                case Failure(UserAlreadyExistsError(var message)) -> {
                    var params = Map.of(
                            "login", login,
                            "errors", FormErrors.global(message)
                    );

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
        else {
            var login = ctx.formParam("login");
            var errors = FormErrors.of(Validation.collectErrors(loginValidator, passwordValidator));
            var params = Map.of(
                    "login", login == null ? "" : login,
                    "errors", errors
            );
            JteResponses
                    .ctx(ctx)
                    .path("partials/register-form.jte")
                    .params(params)
                    .status(400)
                    .render();
        }
    }

    public static void loginPage(Context ctx) {
        var styles = List.of("pages/auth.css");
        var params = Map.of("styles", styles);

        if (Session.isAuthenticated(ctx)) {
            ctx.redirect("/");
        }
        else {
            JteResponses
                    .ctx(ctx)
                    .path("pages/login.jte")
                    .params(params)
                    .render();
        }
    }

    public static void loginForm(Context ctx) {
        JteResponses
                .ctx(ctx)
                .path("partials/login-form.jte")
                .render();
    }

    public static void login(Context ctx) {
        var loginValidator = ctx.formParamAsClass("login", String.class)
                .check(Rules.required(), "Login is required")
                .check(Rules.minLength(8), "Login must be at least 8 characters");

        var passwordValidator = ctx.formParamAsClass("password", String.class)
                .check(Rules.required(), "Password is required")
                .check(Rules.minLength(8), "Password must be at least 8 characters");

        if (!Form.hasErrors(loginValidator, passwordValidator)) {
            var login = loginValidator.get();
            var password = passwordValidator.get();
            var command = AuthCommand.of(login, password);
            var loginResult = AuthService.login(command);

            switch (loginResult) {
                case Failure(UserDoesNotExistError(var message)) -> {
                    var params = Map.of(
                            "login", login,
                            "errors", FormErrors.global(message)
                    );
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
        else {
            var login = ctx.formParam("login");
            var errors = FormErrors.of(Validation.collectErrors(loginValidator, passwordValidator));
            var params = Map.of(
                    "login", login == null ? "" : login,
                    "errors", errors
            );
            JteResponses
                    .ctx(ctx)
                    .path("partials/login-form.jte")
                    .params(params)
                    .status(400)
                    .render();
        }
    }

    public static void logout(Context ctx) {
        Session.logout(ctx);
        Headers.hxRedirect(ctx, "/login");
    }
}