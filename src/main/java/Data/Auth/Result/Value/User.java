package Data.Auth.Result.Value;

public record User(long id, String username) {
    public static User of(long id, String username){
        return new User(id, username);
    }
}
