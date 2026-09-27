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

import java.util.Map;

public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    public void registerPage(Context ctx) {
        switch (Session.isAuthenticated(ctx)) {
            case true -> ctx.redirect("/");
            case false ->
                    JteResponses.with(ctx)
                            .withUser()
                            .render("pages/register.jte");
        }
    }

    public void registerForm(Context ctx) {
        JteResponses.with(ctx)
                .patch("#login-form")
                .render("partials/register-form.jte");
    }

    public void register(Context ctx) {
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
                var registerResult = authService.register(command);

                switch (registerResult) {
                    case Failure(UserAlreadyExistsError(var message)) -> {
                        var params = Map.of("login", login);
                        JteResponses.with(ctx)
                                .withError(message)
                                .params(params)
                                .patch("#register-form")
                                .render("partials/register-form.jte");
                    }

                    case Success(var _) ->
                            JteResponses.with(ctx)
                                    .patch("#register-form")
                                    .render("partials/login-form.jte");
                }
            }
            case false -> {
                var login = Forms.readInputValue(ctx, "login");
                var params = Map.of("login", login);
                JteResponses.with(ctx)
                        .withErrors(loginValidator, passwordValidator)
                        .params(params)
                        .patch("#register-form")
                        .render("partials/register-form.jte");
            }
        }
    }

    public void loginPage(Context ctx) {
        switch (Session.isAuthenticated(ctx)) {
            case true -> ctx.redirect("/");
            case false ->
                    JteResponses.with(ctx)
                        .withUser()
                        .render("pages/login.jte");
        }
    }

    public void loginForm(Context ctx) {
        JteResponses.with(ctx)
                .patch("#login-form")
                .render("partials/login-form.jte");
    }

    public void login(Context ctx) {
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
                var loginResult = authService.login(command);

                switch (loginResult) {
                    case Failure(UserDoesNotExistError(var message)) -> {
                        var params = Map.of("login", login);
                        JteResponses.with(ctx)
                                .withError(message)
                                .params(params)
                                .patch("#login-form")
                                .render("partials/login-form.jte");
                    }
                    case Failure(InvalidPasswordError(var message)) -> {
                        var params = Map.of("login", login);
                        JteResponses.with(ctx)
                                .withError(message)
                                .params(params)
                                .patch("#login-form")
                                .render("partials/login-form.jte");
                    }

                    case Success(var user) -> {
                        Session.authenticate(ctx, user);
                        ctx.redirect("/");
                    }
                }
            }
            case false -> {
                var login = Forms.readInputValue(ctx, "login");
                var params = Map.of("login", login);
                JteResponses.with(ctx)
                        .withErrors(loginValidator, passwordValidator)
                        .params(params)
                        .patch("#login-form")
                        .render("partials/login-form.jte");
            }
        }
    }

    public void logout(Context ctx) {
        Session.logout(ctx);
        ctx.redirect("/login");
    }

    public static AuthController of(AuthService authService) {
        return new AuthController(authService);
    }
}
