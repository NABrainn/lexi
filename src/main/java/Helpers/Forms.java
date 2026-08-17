package Helpers;

import io.javalin.http.Context;

public class Forms {
    public static boolean hasErrors(Form form) {
        for(var validator : form.validators()) {
            if(!validator.errors().isEmpty()) {
                return true;
            }
        }
        return false;
    }

    public static boolean isValid(Form form) {
        for(var validator : form.validators()) {
            if(!validator.errors().isEmpty()) {
                return false;
            }
        }
        return true;
    }

    public static String readInputValue(Context ctx, String param) {
        return ctx.formParam(param) == null
                ? ""
                : ctx.formParam(param);
    }
}
