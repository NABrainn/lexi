package Controller;

import Data.Auth.Request.AuthRequest;
import Data.Auth.Result.UserAlreadyExistsError;
import Data.Auth.Result.UserDoesNotExistError;
import Data.Operation.AuthCommand;
import Data.Result.Failure;
import Data.Result.Success;
import DataValidator.Data.Result.ValidationFailure;
import DataValidator.Data.Result.ValidationSuccess;
import DataValidator.Service.Validator;
import Helpers.FormBinder;
import Helpers.Session;
import Service.AuthService;
import io.javalin.http.Context;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Map;

public class AuthController {

    public static void registerPage(Context ctx) {
        var styles = List.of("pages/auth.css");
        var params = Map.of(
                "styles", styles
        );

        if(Session.isAuthenticated(ctx)) {
            ctx.redirect("/");
        }

        ctx.render("pages/register.jte", params);
    }

    public static void registerForm(Context ctx) {
        ctx.render("partials/register-form.jte", Map.of());
    }

    public static void register(Context ctx) {
        var authForm = FormBinder.bind(ctx, AuthRequest.class);
        var result = Validator.validate(authForm);

        switch (result) {
            case ValidationFailure(var errors) -> {
                var params = Map.of(
                        "login", authForm.login(),
                        "errors", errors
                );
                ctx.status(400);
                ctx.render("partials/register-form.jte", params);
            }
            case ValidationSuccess(var validAuthForm) -> {
                var command = AuthCommand.of(validAuthForm);
                var registerResult = AuthService.register(command);

                switch (registerResult) {
                    case Failure(UserAlreadyExistsError(var message)) -> {
                        ctx.status(400);
                        ctx.render("partials/register-form.jte", Map.of(
                                "serviceError", message
                        ));
                    }
                    case Success(var _) -> ctx.render("partials/login-form.jte");
                }
            }
        }
    }

    public static void loginPage(Context ctx) {
        var styles = List.of("pages/auth.css");
        var params = Map.of(
                "styles", styles
        );

        if(Session.isAuthenticated(ctx)) {
            ctx.redirect("/");
        }

        ctx.render("pages/login.jte", params);
    }

    public static void loginForm(Context ctx) {
        ctx.render("partials/login-form.jte", Map.of());
    }

    public static void login(@NotNull Context ctx) {
        var authForm = FormBinder.bind(ctx, AuthRequest.class);
        var result = Validator.validate(authForm);

        switch (result) {
            case ValidationFailure(var errors) -> {
                var params = Map.of(
                        "login", authForm.login(),
                        "errors", errors
                );
                ctx.status(400);
                ctx.render("partials/login-form.jte", params);
            }
            case ValidationSuccess(var validAuthForm) -> {
                var command = AuthCommand.of(validAuthForm);
                var loginResult = AuthService.login(command);

                switch (loginResult) {
                    case Failure(UserDoesNotExistError(var message)) -> {
                        ctx.status(400);
                        ctx.render("partials/login-form.jte", Map.of(
                                "serviceError", message
                        ));
                    }
                    case Success(var user) -> {
                        Session.authenticate(ctx, user);
                        ctx.redirect("/");
                    }
                }
            }
        }
    }

    public static void logout(Context ctx) {
        Session.logout(ctx);
        ctx.redirect("/login");
    }
}
