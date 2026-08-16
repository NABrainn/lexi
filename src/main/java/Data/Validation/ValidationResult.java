package Data.Validation;

public sealed interface ValidationResult<T> permits ValidationSuccess, ValidationFailure {}
