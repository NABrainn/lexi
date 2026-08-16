package Controller;

import Data.Auth.Result.Error.UserAlreadyExistsError;
import Data.Auth.Result.Error.UserDoesNotExistError;
import Data.Operation.Implementations.AuthCommand;
import Data.Result.Failure;
import Data.Result.Success;
import Helpers.Forms;
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
            JteResponses.with(ctx)
                    .params(params)
                    .render("pages/register.jte");
        }
    }

    public static void registerForm(Context ctx) {
        JteResponses.with(ctx)
                .render("partials/register-form.jte");
    }

    public static void register(Context ctx) {
        var loginValidator = ctx.formParamAsClass("login", String.class)
                .check(Rules.required(), "Login is required")
                .check(Rules.minLength(8), "Login must be at least 8 characters");

        var passwordValidator = ctx.formParamAsClass("password", String.class)
                .check(Rules.required(), "Password is required")
                .check(Rules.minLength(8), "Password must be at least 8 characters");

        if (Forms.isValid(loginValidator, passwordValidator)) {
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

                    JteResponses.with(ctx)
                            .params(params)
                            .status(400)
                            .render("partials/register-form.jte");
                }

                case Success(var _) ->
                        JteResponses.with(ctx)
                                .render("partials/login-form.jte");
            }
        }
        else {
            var login = Forms.readInputValue(ctx, "login");
            var errorMap = Validation.collectErrors(loginValidator, passwordValidator);
            var errors = FormErrors.of(errorMap);
            var params = Map.of(
                    "login", login,
                    "errors", errors
            );
            JteResponses.with(ctx)
                    .params(params)
                    .status(400)
                    .render("partials/register-form.jte");
        }
    }

    public static void loginPage(Context ctx) {
        var styles = List.of("pages/auth.css");
        var params = Map.of("styles", styles);

        if (Session.isAuthenticated(ctx)) {
            ctx.redirect("/");
        }
        else {
            JteResponses.with(ctx)
                    .params(params)
                    .render("pages/login.jte");
        }
    }

    public static void loginForm(Context ctx) {
        JteResponses.with(ctx)
                .render("partials/login-form.jte");
    }

    public static void login(Context ctx) {
        var loginValidator = ctx.formParamAsClass("login", String.class)
                .check(Rules.required(), "Login is required")
                .check(Rules.minLength(8), "Login must be at least 8 characters");

        var passwordValidator = ctx.formParamAsClass("password", String.class)
                .check(Rules.required(), "Password is required")
                .check(Rules.minLength(8), "Password must be at least 8 characters");

        if (Forms.isValid(loginValidator, passwordValidator)) {
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
                    JteResponses.with(ctx)
                            .params(params)
                            .status(400)
                            .render("partials/login-form.jte");
                }

                case Success(var user) -> {
                    Session.authenticate(ctx, user);
                    Headers.hxRedirect(ctx, "/");
                }
            }
        }
        else {
            var login = Forms.readInputValue(ctx, "login");
            var errorMap = Validation.collectErrors(loginValidator, passwordValidator);
            var errors = FormErrors.of(errorMap);
            var params = Map.of(
                    "login", login,
                    "errors", errors
            );
            JteResponses.with(ctx)
                    .params(params)
                    .status(400)
                    .render("partials/login-form.jte");
        }
    }

    public static void logout(Context ctx) {
        Session.logout(ctx);
        Headers.hxRedirect(ctx, "/login");
    }
}