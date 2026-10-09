package Unit;

import Configuration.Components;
import Controller.LessonController;
import Data.Auth.Result.Success.User;
import io.javalin.http.Context;
import io.javalin.validation.Params;
import io.javalin.validation.Validator;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.*;

public class LessonTest {

    private final Context ctx = mock(Context.class);
    private final HttpServletRequest request = mock(HttpServletRequest.class);
    private final LessonController lessonController = Components.lessonController();
    private final User user = User.of(123, "nabrain");

    @Test
    public void HX_GET_to_create_lesson_gives_200() {
        mockHxHeaders(true);
        lessonController.createLessonForm(ctx);
        verify(ctx).status(200);
    }

    @Test
    public void GET_to_create_lesson_gives_200() {
        mockHxHeaders(false);
        mockRequest(ctx);
        lessonController.createLessonForm(ctx);
        verify(ctx).status(200);
    }

    @Test
    public void HX_POST_to_create_lesson_gives_201_for_valid_form_data() {
        mockHxHeaders(true);
        mockUser();
        mockFormParamAsClass("title", "title of the lesson");
        mockFormParamAsClass("contentUrl", "contentUrl of the lesson");
        mockFormParamAsClass("content", "content of the lesson");
        lessonController.createLesson(ctx);
        verify(ctx).status(201);
    }

    @Test
    public void HX_POST_to_create_lesson_gives_400_for_invalid_form_data() {
        mockHxHeaders(true);
        mockUser();
        mockFormParamAsClass("title", null);
        mockFormParamAsClass("contentUrl", "contentUrl of the lesson");
        mockFormParamAsClass("content", "content of the lesson");
        lessonController.createLesson(ctx);
        verify(ctx).status(400);
    }

    @Test
    public void POST_to_create_lesson_gives_400_for_any_data() {
        mockHxHeaders(false);
        mockUser();
        lessonController.createLesson(ctx);
        verify(ctx).status(400);
    }

    private void mockHxHeaders(boolean value) {
        when(ctx.header("Hx-Request")).thenReturn(String.valueOf(value));
    }

    private void mockRequest(Context ctx) {
        when(ctx.req()).thenReturn(request);
    }

    private void mockUser() {
        when(ctx.sessionAttribute("user")).thenReturn(user);
    }

    private void mockFormParamAsClass(String name, String value, Class<?> clazz) {
        when(ctx.formParamAsClass(name, clazz)).thenReturn(
                new Validator<>(
                        new Params(name, clazz, value, value, () -> value),
                        name
                )
        );
    }
    private void mockFormParamAsClass(String name, String value) {
        mockFormParamAsClass(name, value, String.class);
    }
}
