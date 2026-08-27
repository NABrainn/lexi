package Configuration;

import Controller.AuthController;
import Controller.IndexController;
import Controller.LessonController;
import Repository.AuthRepository;
import Service.AuthService;
import Service.PasswordManager;

public class Components {
    private static final PasswordManager passwordManager = PasswordManager.of();
    private static final AuthRepository authRepository = AuthRepository.of(Database.getConnection());
    private static final AuthService authService = AuthService.of(passwordManager, authRepository);
    private static final AuthController authController = AuthController.of(authService);
    private static final IndexController indexController = IndexController.of();
    private static final LessonController lessonController = LessonController.of();

    public static AuthController authController() {
        return authController;
    }

    public static IndexController indexController() {
        return indexController;
    }

    public static LessonController lessonController() {
        return lessonController;
    }
}