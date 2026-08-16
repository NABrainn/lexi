package Data.Auth.Result.Error;

public sealed interface RegisterError extends AuthError permits UserAlreadyExistsError{
}
