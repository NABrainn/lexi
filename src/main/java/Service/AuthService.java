package Service;

import Data.Auth.Result.AuthError;
import Data.Auth.Result.UserAlreadyExistsError;
import Data.Operation.RegisterCommand;
import Data.Result.Failure;
import Data.Result.Result;
import Data.Result.Success;
import Data.Result.Unit;

public class AuthService {
    public static Result<Unit, AuthError> register(RegisterCommand operation) {
        boolean found = true;

        if(found) {
            var error = UserAlreadyExistsError.of("User with that name found");
            return Failure.of(error);
        }

        return Success.of(Unit.INSTANCE);
    }
}
