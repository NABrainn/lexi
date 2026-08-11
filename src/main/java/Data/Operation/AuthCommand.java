package Data.Operation;

import DataValidator.Data.Core.Command;

public record AuthCommand(String login, String password) implements Command {
    public static AuthCommand of(String login, String password) {
        return new AuthCommand(login, password);
    }
}
