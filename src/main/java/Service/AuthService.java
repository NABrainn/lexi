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

    private final PasswordManager passwordManager;
    private final AuthRepository authRepository;

    public AuthService(PasswordManager passwordManager, AuthRepository authRepository) {
        this.passwordManager = passwordManager;
        this.authRepository = authRepository;
    }

    public Result<Unit, RegisterError> register(AuthCommand operation) {
        var loginAlreadyExists = authRepository.existsLogin(operation.login());

        if(loginAlreadyExists) {
            var error = UserAlreadyExistsError.of("User with that name already exists");
            return Failure.of(error);
        }

        var login = operation.login();
        var password = operation.password();
        var hashedPassword = passwordManager.hashPassword(password);
        var createdRows = authRepository.createUser(login, hashedPassword);

        if(createdRows == 0) {
            throw new RuntimeException("Failed to create user");
        }

        return Success.of(Unit.INSTANCE);
    }

    public Result<User, LoginError> login(AuthCommand command) {
        var login = command.login();
        var password = command.password();
        var optionalUser = authRepository.findUserByLogin(login);

        if(optionalUser.isEmpty()) {
            var error = UserDoesNotExistError.of("User with that name does not exist");
            return Failure.of(error);
        }

        var optionalUserPasswordHash = authRepository.findUserPasswordHash(login);

        if(optionalUserPasswordHash.isEmpty()) {
            throw new RuntimeException("Failed to read user hash");
        }

        var userPasswordHash = optionalUserPasswordHash.get();
        var passwordsDoNotMatch = !passwordManager.verifyPassword(password, userPasswordHash);

        if(passwordsDoNotMatch) {
            var error = InvalidPasswordError.of("Invalid password");
            return Failure.of(error);
        }

        var user = optionalUser.get();
        return Success.of(user);
    }

    public static AuthService of(PasswordManager passwordManager, AuthRepository authRepository) {
        return new AuthService(passwordManager, authRepository);
    }
}
