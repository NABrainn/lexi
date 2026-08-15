package Data.Auth.Result;

public record User(long id, String username) {
    public static User of(long id, String username){
        return new User(id, username);
    }
}
