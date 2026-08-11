package Controller;

import Data.Auth.Request.AuthData;
import Data.Auth.Result.UserAlreadyExistsError;
import Data.Operation.AuthCommand;
import Data.Result.Failure;
import Data.Result.Success;
import DataValidator.Data.Result.ValidationFailure;
import DataValidator.Data.Result.ValidationSuccess;
import DataValidator.Service.Validator;
import Helpers.FormBinder;
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
        var authForm = FormBinder.bind(ctx, AuthData.class);
        var result = Validator.validate(authForm);

        switch (result) {
            case ValidationFailure(var errors) -> {
                var params = Map.of(
                        "errors", errors
                );
                ctx.render("partials/register-form.jte", params);
            }
            case ValidationSuccess(var authValidForm) -> {
                var command = (AuthCommand) authValidForm.mapToOperation();
                var registerResult = AuthService.register(command);

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
