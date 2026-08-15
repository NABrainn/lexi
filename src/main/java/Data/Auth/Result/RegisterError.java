package Data.Auth.Result;

public sealed interface RegisterError extends AuthError permits UserAlreadyExistsError{
}
