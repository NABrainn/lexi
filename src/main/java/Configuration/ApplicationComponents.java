package Configuration;

import Controller.AuthController;
import Controller.IndexController;
import Controller.LessonController;
import Repository.AuthRepository;
import Service.AuthService;
import Service.PasswordManager;

public class ApplicationComponents {
    private final AuthController authController;
    private final IndexController indexController;
    private final LessonController lessonController;

    private ApplicationComponents() {
        var passwordManager = PasswordManager.of();
        var authRepository = AuthRepository.of(Database.getConnection());
        var authService = AuthService.of(passwordManager, authRepository);

        authController = AuthController.of(authService);
        indexController = IndexController.of();
        lessonController = LessonController.of();
    }

    public AuthController authController() {
        return authController;
    }

    public IndexController indexController() {
        return indexController;
    }

    public LessonController lessonController() {
        return lessonController;
    }

    public static ApplicationComponents of() {
        return new ApplicationComponents();
    }
}