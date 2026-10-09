package Configuration;

import Controller.AuthController;
import Controller.IndexController;
import Controller.LessonController;
import Repository.AuthRepository;
import Repository.LessonRepository;
import Repository.TranslationRepository;
import Service.*;
import org.jdbi.v3.core.Jdbi;

public class Components {
    private static final Jdbi jdbi = Database.getConnection();
    private static final PasswordManager passwordManager = PasswordManager.of();
    private static final AuthRepository authRepository = AuthRepository.of(jdbi);
    private static final AuthService authService = AuthService.of(passwordManager, authRepository);
    private static final AuthController authController = AuthController.of(authService);
    private static final IndexController indexController = IndexController.of();
    private static final LessonRepository lessonRepository = LessonRepository.of(jdbi);
    private static final JsoupService jsoupService = JsoupService.of();
    private static final TranslationRepository translationRepository = TranslationRepository.of(jdbi);
    private static final Tokenizer tokenizer = Tokenizer.of(translationRepository);
    private static final LessonService lessonService = LessonService.of(jsoupService, tokenizer, lessonRepository);
    private static final LessonController lessonController = LessonController.of(lessonService);

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