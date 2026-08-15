package Data.Operation;

import Data.Auth.Request.AuthRequest;
import Data.Command.Command;

public record AuthCommand(String login, String password) implements Command {
    public static AuthCommand of(AuthRequest registerRequest) {
        return new AuthCommand(registerRequest.login(), registerRequest.password());
    }
}
