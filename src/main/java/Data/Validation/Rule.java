package Data.Validation;

import java.util.Objects;
import java.util.function.Predicate;

public sealed interface Rule<T> permits Rule.Simple {
  Class<T> type();
  Predicate<T> validator();
  String identifier();
  String message();

  record Simple<T>(Class<T> type, Predicate<T> validator, String identifier, String message)
      implements Rule<T> {
    public Simple {
      Objects.requireNonNull(type);
      Objects.requireNonNull(validator);
      Objects.requireNonNull(identifier);
      Objects.requireNonNull(message);
    }

    @SuppressWarnings("unchecked")
    public Simple(Predicate<T> validator, String identifier, String message) {
      this((Class<T>) Object.class, validator, identifier, message);
    }
  }
}
