package Data.Result;

public sealed interface Result<T, E> permits Success, Failure {
}
