package Controller;

import Data.Auth.Result.Error.InvalidPasswordError;
import Data.Auth.Result.Error.UserAlreadyExistsError;
import Data.Auth.Result.Error.UserDoesNotExistError;
import Data.Operation.Implementations.AuthCommand;
import Data.Result.Failure;
import Data.Result.Success;
import Helpers.*;
import Service.AuthService;
import io.javalin.http.Context;
import io.javalin.validation.Validation;

import java.util.Map;

public class AuthController {
    public static void registerPage(Context ctx) {
        switch (Session.isAuthenticated(ctx)) {
            case true -> ctx.redirect("/");
            case false ->
                    JteResponses.with(ctx)
                            .withUser()
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

        var form = Form.of(loginValidator, passwordValidator);
        switch(Forms.isValid(form)) {
            case true -> {
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
            case false -> {
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
    }

    public static void loginPage(Context ctx) {
        switch (Session.isAuthenticated(ctx)) {
            case true -> ctx.redirect("/");
            case false ->
                    JteResponses.with(ctx)
                            .withUser()
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

        var form = Form.of(loginValidator, passwordValidator);
        switch (Forms.isValid(form)) {
            case true -> {
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
                    case Failure(InvalidPasswordError(var message)) -> {
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
            case false -> {
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
    }

    public static void logout(Context ctx) {
        Session.logout(ctx);
        Headers.hxRedirect(ctx, "/login");
    }
}
