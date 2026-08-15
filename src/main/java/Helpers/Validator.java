package Helpers;

import Data.Validation.Rule;
import Data.Validation.ValidationErrors;
import Data.Validation.ValidationFailure;
import Data.Validation.ValidationResult;
import Data.Validation.ValidationSuccess;
import io.javalin.http.Context;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public final class Validator {
  private Validator() {}

  public static ValidationResult<Context> validate(
      Context ctx, Map<String, ? extends List<? extends Rule<?>>> rulesByField) {
    var errorMap = new LinkedHashMap<String, Map<String, String>>();

    for (var entry : rulesByField.entrySet()) {
      var field = entry.getKey();
      var rules = entry.getValue();
      var type = rules.getFirst().type();
      var parameter = ctx.formParamAsClass(field, type);

      for (var rule : rules) {
        parameter.check(value -> test(rule, value), rule.message());
      }

      var fieldErrors = new LinkedHashMap<String, String>();
      for (var error : parameter.errors().getOrDefault(field, List.of())) {
        var rule = rules.stream().filter(candidate -> candidate.message().equals(error.getMessage())).findFirst();
        fieldErrors.put(rule.map(Rule::identifier).orElse("conversion"), error.getMessage());
      }
      if (!fieldErrors.isEmpty()) {
        errorMap.put(field, Map.copyOf(fieldErrors));
      }
    }

    var errors = new ValidationErrors(errorMap);
    return errorMap.isEmpty() ? ValidationSuccess.of(ctx) : ValidationFailure.of(errors);
  }

  @SuppressWarnings({"rawtypes", "unchecked"})
  private static boolean test(Rule<?> rule, Object value) {
    return ((Rule) rule).validator().test(value);
  }
}
