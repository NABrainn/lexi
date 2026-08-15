package Data.Validation;

import java.util.List;
import java.util.Map;

public record ValidationErrors(Map<String, Map<String, String>> errorMap) {
  public ValidationErrors {
    errorMap = Map.copyOf(errorMap);
  }

  public List<String> getMessages(String fieldName) {
    var errors = errorMap.get(fieldName);
    return errors == null ? List.of() : List.copyOf(errors.values());
  }
}
