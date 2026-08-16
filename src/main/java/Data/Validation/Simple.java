package Data.Validation;

import java.util.Objects;
import java.util.function.Predicate;

public record Simple<T>(Class<T> type, Predicate<T> validator, String identifier, String message) implements Rule<T> {
  public Simple {
    Objects.requireNonNull(type);
    Objects.requireNonNull(validator);
    Objects.requireNonNull(identifier);
    Objects.requireNonNull(message);
  }
}
