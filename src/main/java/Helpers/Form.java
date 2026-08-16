package Helpers;

import io.javalin.http.Context;
import io.javalin.validation.Validator;

public class Form {
    public static boolean hasErrors(Validator<?>... validators) {
        for(var validator : validators) {
            if(!validator.errors().isEmpty()) {
                return true;
            }
        }
        return false;
    }

    public static boolean isValid(Validator<?>... validators) {
        for(var validator : validators) {
            if(!validator.errors().isEmpty()) {
                return false;
            }
        }
        return true;
    }

    public static String inputValue(Context ctx, String param) {
        return ctx.formParam(param) == null
                ? ""
                : ctx.formParam(param);
    }
}
