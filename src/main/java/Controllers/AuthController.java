package Controllers;

import Data.Auth.AuthRequest;
import Data.Result.ValidationFailure;
import Data.Result.ValidationSuccess;
import Service.Validator;
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
                ctx.render("pages/register.jte", Map.of(
                        "errors", errors
                ));
            }
            case ValidationSuccess(var _) -> {
                ctx.render("pages/login.jte");
            }
        }
    }
}
