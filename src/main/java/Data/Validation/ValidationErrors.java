package Data.Validation;

import io.javalin.validation.ValidationError;

import java.util.List;
import java.util.Map;

public record ValidationErrors(Map<String, List<ValidationError<?>>> errorMap) {
    public ValidationErrors {
        errorMap = Map.copyOf(errorMap);
    }

    public List<ValidationError<?>> getMessages(String fieldName) {
        var errors = errorMap.get(fieldName);
        return errors == null
                ? List.of()
                : List.copyOf(errors);
    }
}
