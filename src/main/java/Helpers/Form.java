package Helpers;

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
}
