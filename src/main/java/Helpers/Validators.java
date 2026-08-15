package Helpers;

import java.net.URI;
import java.util.Collection;
import java.util.Objects;
import java.util.regex.Pattern;

public final class Validators {
  private static final Pattern EMAIL = Pattern.compile("^[^\\s@]+@[^\\s@]+\\.[^\\s@]+$");

  private Validators() {}

  public static boolean alphabetic(CharSequence value) {
    return value == null || value.toString().matches("\\p{L}+");
  }

  public static boolean alphanumeric(CharSequence value) {
    return value == null || value.toString().matches("[\\p{L}\\p{N}]+");
  }

  public static <T extends Comparable<? super T>> boolean between(T value, T minimum, T maximum) {
    requireRange(minimum, maximum);
    return value == null || (value.compareTo(minimum) >= 0 && value.compareTo(maximum) <= 0);
  }
  public static boolean email(CharSequence value) {
    return value == null || EMAIL.matcher(value).matches();
  }

  public static boolean lengthBetween(CharSequence value, int minimum, int maximum) {
    requireLengthRange(minimum, maximum);
    return value == null || (value.length() >= minimum && value.length() <= maximum);
  }
  public static boolean matches(CharSequence value, String expression) {
    return value == null || Pattern.compile(Objects.requireNonNull(expression)).matcher(value).matches();
  }
  public static <T extends Comparable<? super T>> boolean max(T value, T maximum) {
    var bound = Objects.requireNonNull(maximum);
    return value == null || value.compareTo(bound) <= 0;
  }
  public static boolean maxLength(CharSequence value, int maximum) {
    if (maximum < 0) {
      throw new IllegalArgumentException("maximum cannot be negative");
    }
    return value == null || value.length() <= maximum;
  }
  public static boolean maxSize(Collection<?> value, int maximum) {
    if (maximum < 0) {
      throw new IllegalArgumentException("maximum cannot be negative");
    }
    return value == null || value.size() <= maximum;
  }
  public static <T extends Comparable<? super T>> boolean min(T value, T minimum) {
    var bound = Objects.requireNonNull(minimum);
    return value == null || value.compareTo(bound) >= 0;
  }
  public static boolean minLength(CharSequence value, int minimum) {
    if (minimum < 0) {
      throw new IllegalArgumentException("minimum cannot be negative");
    }
    return value == null || value.length() >= minimum;
  }
  public static boolean minLength(CharSequence value) {
    return minLength(value, 8);
  }

  public static boolean minSize(Collection<?> value, int minimum) {
    if (minimum < 0) {
      throw new IllegalArgumentException("minimum cannot be negative");
    }
    return value == null || value.size() >= minimum;
  }
  public static boolean negative(Number value) {
    return value == null || value.doubleValue() < 0;
  }
  public static boolean nonZero(Number value) {
    return value == null || value.doubleValue() != 0;
  }
  public static boolean notEmpty(Collection<?> value) {
    return value == null || !value.isEmpty();
  }
  public static boolean notEmpty(CharSequence value) {
    return value == null || !value.isEmpty();
  }
  public static boolean numeric(CharSequence value) {
    return value == null || value.toString().matches("\\p{N}+");
  }
  public static boolean positive(Number value) {
    return value == null || value.doubleValue() > 0;
  }
  public static boolean required(Object value) {
    return value != null && (!(value instanceof CharSequence text) || !text.toString().trim().isBlank());
  }
  public static boolean url(CharSequence value) {
    if (value == null) {
      return true;
    }
    try {
      var uri = URI.create(value.toString());
      return ("http".equalsIgnoreCase(uri.getScheme()) || "https".equalsIgnoreCase(uri.getScheme()))
          && uri.getHost() != null;
    } catch (IllegalArgumentException ignored) {
      return false;
    }
  }

  private static <T extends Comparable<? super T>> void requireRange(T minimum, T maximum) {
    if (minimum == null || maximum == null) {
      throw new NullPointerException("range bounds cannot be null");
    }
    if (minimum.compareTo(maximum) > 0) {
      throw new IllegalArgumentException("minimum cannot exceed maximum");
    }
  }

  private static void requireLengthRange(int minimum, int maximum) {
    if (minimum < 0 || maximum < minimum) {
      throw new IllegalArgumentException("invalid length range");
    }
  }
}
