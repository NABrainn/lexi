package Configuration;

import Controller.AuthController;
import Controller.IndexController;
import Controller.LessonController;
import Repository.AuthRepository;
import Service.AuthService;
import Service.PasswordManager;

import java.util.HashMap;
import java.util.Map;

public class Container {
    private final Map<Class<?>, Object> instances = new HashMap<>();

    private <T> void register(Class<T> type, T instance) {
        instances.put(type, instance);
    }

    private Container() {
        var passwordManager = PasswordManager.of();
        register(PasswordManager.class, passwordManager);

        var authRepository = AuthRepository.of(Database.getConnection());
        register(AuthRepository.class, authRepository);

        var authService = AuthService.of(passwordManager, authRepository);
        register(AuthService.class, authService);

        var authController = AuthController.of(authService);
        register(AuthController.class, authController);

        var indexController = IndexController.of();
        register(IndexController.class, indexController);

        var lessonController = LessonController.of();
        register(LessonController.class, lessonController);
    }

    public <T> T resolve(Class<T> type) {
        T instance = (T) instances.get(type);

        if (instance == null) {
            throw new IllegalArgumentException("No registered instance found for type: " + type.getName());
        }

        return instance;
    }
    public static Container of() {
        return new Container();
    }
}
