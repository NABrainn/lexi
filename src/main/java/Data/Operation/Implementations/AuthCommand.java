package Data.Operation.Implementations;

import Data.Auth.Request.AuthRequest;
import Data.Operation.Interfaces.Command;

public record AuthCommand(String login, String password) implements Command {
    public static AuthCommand of(AuthRequest registerRequest) {
        return new AuthCommand(registerRequest.login(), registerRequest.password());
    }
}
