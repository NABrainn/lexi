package Helpers;

import io.javalin.validation.Validator;

public record Form(Validator<?>... validators) {
    public static Form of(Validator<?>... validators) {
        return new Form(validators);
    }
}
