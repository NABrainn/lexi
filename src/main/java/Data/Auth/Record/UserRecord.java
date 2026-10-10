package Data.Auth.Record;

import Data.Auth.Result.Success.User;
import org.jdbi.v3.core.mapper.reflect.ColumnName;

public record UserRecord(
        @ColumnName("id")
        long id,
        @ColumnName("login")
        String username
) {
    public static UserRecord of(long id, String username){
        return new UserRecord(id, username);
    }

    public User mapToUser() {
        return User.of(id, username);
    }
}
