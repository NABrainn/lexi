package Service;

import com.lucimber.bcrypt.*;

public class PasswordManager {
    private final BCryptService bcryptService = BCryptService.getInstance();

    public String hashPassword(String rawPassword) {
        Password password = new Password(rawPassword);
        try {
            Hash hash = bcryptService.hash(password, BCryptVersion.VERSION_2B, new CostFactor(12));
            return hash.getValue();
        } finally {
            password.clear();
        }
    }

    public boolean verifyPassword(String rawPassword, String storedHash) {
        Password password = new Password(rawPassword);
        try {
            Hash hash = new Hash(storedHash);
            return bcryptService.verify(password, hash);
        } finally {
            password.clear();
        }
    }

    public static PasswordManager of() {
        return new PasswordManager();
    }
}
