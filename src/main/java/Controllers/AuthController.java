package Controllers;

import Data.Auth.Request.AuthRequest;
import Data.Auth.Result.UserFoundError;
import Data.Result.Failure;
import Data.Result.Success;
import DataValidator.Data.Result.ValidationFailure;
import DataValidator.Data.Result.ValidationSuccess;
import DataValidator.Service.Validator;
import Service.AuthService;
import io.javalin.http.Context;

import java.util.Map;

public class AuthController {
    public static void register(Context ctx) {
        var formLogin = ctx.formParam("login");
        var formPassword = ctx.formParam("password");
        var authRequest = AuthRequest.of(formLogin, formPassword);
        var result = Validator.validate(authRequest);

        switch (result) {
            case ValidationFailure(var errors) -> {
                var firstMessage = errors
                        .getMessages("login")
                        .getFirst();
                ctx.render("pages/register.jte", Map.of(
                        "errors", errors
                ));
            }
            case ValidationSuccess(var _) -> {
                var registerResult = AuthService.register(authRequest);

                switch (registerResult) {
                    case Failure(var error) -> {

                        switch (error) {
                            case UserFoundError userFoundError -> {
                            }
                        }

                    }
                    case Success(var _) -> {
                        ctx.render("pages/login.jte");
                    }
                }
            }
        }
    }
}
