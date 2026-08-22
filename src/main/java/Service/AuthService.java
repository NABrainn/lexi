package Service;

import Data.Auth.Result.Error.*;
import Data.Auth.Result.Value.User;
import Data.Operation.Implementations.AuthCommand;
import Data.Result.Failure;
import Data.Result.Result;
import Data.Result.Success;
import Data.Result.Unit;
import Repository.AuthRepository;

public class AuthService {

    private static final PasswordManager passwordManager = PasswordManager.of();

    public static Result<Unit, RegisterError> register(AuthCommand operation) {
        var loginAlreadyExists = AuthRepository.existsLogin(operation.login());

        if(loginAlreadyExists) {
            var error = UserAlreadyExistsError.of("User with that name already exists");
            return Failure.of(error);
        }

        var login = operation.login();
        var password = operation.password();
        var hashedPassword = passwordManager.hashPassword(password);
        var createdRows = AuthRepository.createUser(login, hashedPassword);

        if(createdRows == 0) {
            throw new RuntimeException("Failed to create user");
        }

        return Success.of(Unit.INSTANCE);
    }

    public static Result<User, LoginError> login(AuthCommand command) {
        var login = command.login();
        var password = command.password();

        var optionalUser = AuthRepository.findUserByLogin(login);

        if(optionalUser.isEmpty()) {
            var error = UserDoesNotExistError.of("User with that name does not exist");
            return Failure.of(error);
        }

        var optionalStoredPasswordHash = AuthRepository.getPasswordHash(login);

        if(optionalStoredPasswordHash.isEmpty()) {
            throw new RuntimeException("Failed to read user hash");
        }

        var storedPasswordHash = optionalStoredPasswordHash.get();


        if(!passwordManager.verifyPassword(password, storedPasswordHash)) {
            var error = InvalidPasswordError.of("Invalid password");
            return Failure.of(error);
        }

        var user = optionalUser.get();
        return Success.of(user);
    }
}
