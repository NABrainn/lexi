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
import Helpers.Session;
import Helpers.Validation.Rules;
import Helpers.Validation.Validator;
import Service.AuthService;
import io.javalin.http.Context;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import org.jetbrains.annotations.NotNull;

public class AuthController {
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

    ctx.render("pages/register.jte", params);
  }

  public static void registerForm(Context ctx) {
    ctx.render("partials/register-form.jte", Map.of());
  }

  public static void register(Context ctx) {
    var result = Validator.validate(ctx, authRules);

    switch (result) {
      case ValidationFailure(var errors) -> {
        var login = Objects.requireNonNullElse(ctx.formParam("login"), "");
        var params = Map.of("login", login, "errors", errors);
        ctx.status(400);
        ctx.render("partials/register-form.jte", params);
      }
      case ValidationSuccess(var _) -> {
        var authForm = FormBinder.bind(ctx, AuthRequest.class);
        var command = AuthCommand.of(authForm);
        var registerResult = AuthService.register(command);

        switch (registerResult) {
          case Failure(UserAlreadyExistsError(var message)) -> {
            ctx.status(400);
            ctx.render("partials/register-form.jte", Map.of("serviceError", message));
          }
          case Success(var _) -> ctx.render("partials/login-form.jte");
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

    ctx.render("pages/login.jte", params);
  }

  public static void loginForm(Context ctx) {
    ctx.render("partials/login-form.jte", Map.of());
  }

  public static void login(@NotNull Context ctx) {
    var result = Validator.validate(ctx, authRules);

    switch (result) {
      case ValidationFailure(var errors) -> {
        var login = Objects.requireNonNullElse(ctx.formParam("login"), "");
        var params = Map.of("login", login, "errors", errors);
        ctx.status(400);
        ctx.render("partials/login-form.jte", params);
      }
      case ValidationSuccess(var _) -> {
        var authForm = FormBinder.bind(ctx, AuthRequest.class);
        var command = AuthCommand.of(authForm);
        var loginResult = AuthService.login(command);

        switch (loginResult) {
          case Failure(UserDoesNotExistError(var message)) -> {
            ctx.status(400);
            ctx.render("partials/login-form.jte", Map.of("serviceError", message));
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
