package Controller;

import Data.Auth.Request.AuthRequest;
import Data.Auth.Result.UserAlreadyExistsError;
import Data.Result.Failure;
import Data.Result.Success;
import DataValidator.Data.Result.ValidationFailure;
import DataValidator.Data.Result.ValidationSuccess;
import DataValidator.Service.Validator;
import Service.AuthService;
import io.javalin.http.Context;

import java.util.List;
import java.util.Map;

public class AuthController {

    public static void registerPage(Context ctx) {
        var styles = List.of("pages/auth.css");
        var params = Map.of(
                "styles", styles
        );
        ctx.render("pages/register.jte", params);
    }

    public static void register(Context ctx) {
        var formLogin = ctx.formParam("login");
        var formPassword = ctx.formParam("password");
        var authRequest = AuthRequest.of(formLogin, formPassword);
        var result = Validator.validate(authRequest);

        switch (result) {
            case ValidationFailure(var errors) -> {
                var params = Map.of(
                        "errors", errors
                );
                ctx.render("partials/register-form.jte", params);
            }
            case ValidationSuccess(var validAuthRequest) -> {
                var registerResult = AuthService.register(validAuthRequest);

                switch (registerResult) {
                    case Failure(UserAlreadyExistsError(var message)) -> IO.println(message);
                    case Success(var _) -> {
                        ctx.render("pages/login.jte");
                    }
                }
            }
        }
    }
}
