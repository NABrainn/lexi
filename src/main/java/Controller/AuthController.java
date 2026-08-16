package Controller;

import Data.Auth.Request.AuthRequest;
import Data.Auth.Result.Error.UserAlreadyExistsError;
import Data.Auth.Result.Error.UserDoesNotExistError;
import Data.Operation.Implementations.AuthCommand;
import Data.Result.Failure;
import Data.Result.Success;
import Data.Validation.Rule;
import Data.Validation.ValidationFailure;
import Data.Validation.ValidationSuccess;
import Helpers.FormBinder;
import Helpers.Headers;
import Helpers.JteResponses;
import Helpers.Session;
import Helpers.Validation.Rules;
import Helpers.Validation.Validator;
import Service.AuthService;
import io.javalin.http.Context;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import org.jetbrains.annotations.NotNull;

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
        var result = Validator.validate(ctx, authRules);

        switch (result) {
            case ValidationFailure(var errors) -> {
                var login = Objects.requireNonNullElse(ctx.formParam("login"), "");
                var params = Map.of("login", login, "errors", errors);
                JteResponses
                        .ctx(ctx)
                        .path("partials/register-form.jte")
                        .params(params)
                        .status(400)
                        .render();
            }
            case ValidationSuccess(var _) -> {
                var authForm = FormBinder.bind(ctx, AuthRequest.class);
                var command = AuthCommand.of(authForm);
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

    public static void login(@NotNull Context ctx) {
        var result = Validator.validate(ctx, authRules);

        switch (result) {
            case ValidationFailure(var errors) -> {
                var login = Objects.requireNonNullElse(ctx.formParam("login"), "");
                var params = Map.of("login", login, "errors", errors);
                JteResponses
                        .ctx(ctx)
                        .path("partials/login-form.jte")
                        .params(params)
                        .status(400)
                        .render();

            }
            case ValidationSuccess(var _) -> {
                var authForm = FormBinder.bind(ctx, AuthRequest.class);
                var command = AuthCommand.of(authForm);
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
    }

    public static void logout(Context ctx) {
        Session.logout(ctx);
        Headers.hxRedirect(ctx, "/login");
    }
}
