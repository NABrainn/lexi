package Helpers.Validation;

import Data.Validation.Rule;
import Helpers.Validators;
import java.util.Collection;

public final class Rules {
  private Rules() {}

  public static Rule<String> notEmpty(String message) {
    return stringRule(Validators::notEmpty, "not_empty", message);
  }

  public static Rule<String> required(String message) {
    return stringRule(Validators::required, "required", message);
  }

  public static Rule<String> minLength(int minimum, String message) {
    return stringRule(value -> Validators.minLength(value, minimum), "min_length", message);
  }

  public static Rule<String> alphabetic(String message) {
    return stringRule(Validators::alphabetic, "alphabetic", message);
  }
  public static Rule<String> alphanumeric(String message) {
    return stringRule(Validators::alphanumeric, "alphanumeric", message);
  }
  public static Rule<String> email(String message) {
    return stringRule(Validators::email, "email", message);
  }
  public static Rule<String> numeric(String message) {
    return stringRule(Validators::numeric, "numeric", message);
  }
  public static Rule<String> matches(String expression, String message) {
    return stringRule(value -> Validators.matches(value, expression), "matches", message);
  }
  public static Rule<String> maxLength(int maximum, String message) {
    return stringRule(value -> Validators.maxLength(value, maximum), "max_length", message);
  }
  public static Rule<String> lengthBetween(int minimum, int maximum, String message) {
    return stringRule(value -> Validators.lengthBetween(value, minimum, maximum), "length_between", message);
  }
  public static Rule<String> url(String message) {
    return stringRule(Validators::url, "url", message);
  }
  public static Rule<Number> positive(String message) {
    return new Rule.Simple<>(Validators::positive, "positive", message);
  }
  public static Rule<Number> negative(String message) {
    return new Rule.Simple<>(Validators::negative, "negative", message);
  }
  public static Rule<Number> nonZero(String message) {
    return new Rule.Simple<>(Validators::nonZero, "non_zero", message);
  }
  public static Rule<Collection<?>> minSize(int minimum, String message) {
    return new Rule.Simple<>(value -> Validators.minSize(value, minimum), "min_size", message);
  }
  public static Rule<Collection<?>> maxSize(int maximum, String message) {
    return new Rule.Simple<>(value -> Validators.maxSize(value, maximum), "max_size", message);
  }

  private static Rule<String> stringRule(
      java.util.function.Predicate<CharSequence> predicate, String identifier, String message) {
    return new Rule.Simple<>(predicate::test, identifier, message);
  }
}
