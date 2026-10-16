package Data.Auth.Result.Success;

public record User(long id, String username, String pathName) {
    public static User of(long id, String username, String pathName){
        return new User(id, username, pathName);
    }

    public User withPath(String path) {
        return new User(id, username, path);
    }
}
