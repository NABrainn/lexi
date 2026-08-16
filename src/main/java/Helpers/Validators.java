package Helpers;

import Data.Validation.ValidationErrors;
import io.javalin.validation.ValidationError;
import io.javalin.validation.Validator;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Validators {
    public static boolean hasErrors(Validator<?>... validators) {
        for(var validator : validators) {
            if(!validator.errors().isEmpty()) {
                return true;
            }
        }
        return false;
    }

    public static ValidationErrors errors(Validator<?>... validators) {
        Map<String, List<ValidationError<?>>> errorMap = Arrays.stream(validators)
                .map(Validator::errors)
                .flatMap(errors -> errors.entrySet().stream())
                .collect(Collectors.toUnmodifiableMap(
                        Map.Entry::getKey,
                        entry -> List.copyOf(entry.getValue())
                ));

        return new ValidationErrors(errorMap);
    }

}
