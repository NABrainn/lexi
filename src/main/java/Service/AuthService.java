package Service;

import Data.Auth.Request.AuthRequest;
import Data.Auth.Result.AuthError;
import Data.Auth.Result.UserFoundError;
import Data.Result.Failure;
import Data.Result.Result;
import Data.Result.Success;
import Data.Result.Unit;

public class AuthService {
    public static Result<Unit, AuthError> register(AuthRequest authRequest) {
        boolean found = true;

        if(found) {
            var error = UserFoundError.of("User with that name found");
            return Failure.of(error);
        }

        return Success.of(Unit.INSTANCE);
    }
}
