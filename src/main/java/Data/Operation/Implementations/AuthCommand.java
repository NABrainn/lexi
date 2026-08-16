package Data.Operation.Implementations;

import Data.Auth.Request.AuthRequest;
import Data.Operation.Interfaces.Command;

public record AuthCommand(String login, String password) implements Command {

    public static AuthCommand of(String login, String password) {
        return new AuthCommand(login, password);
    }
}
