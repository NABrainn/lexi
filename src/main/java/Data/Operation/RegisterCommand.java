package Data.Operation;

import DataValidator.Data.Core.Command;

public record RegisterCommand(String login, String password) implements Command {
    public static RegisterCommand of(String login, String password) {
        return new RegisterCommand(login, password);
    }
}
