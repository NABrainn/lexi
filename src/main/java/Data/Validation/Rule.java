package Data.Validation;

import java.util.function.Predicate;

public sealed interface Rule<T> permits Simple {
    Class<T> type();
    Predicate<T> validator();
    String identifier();
    String message();
}
