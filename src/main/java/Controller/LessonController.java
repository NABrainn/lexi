package Controller;

import Data.Lesson.Request.CreateLessonCommand;
import Data.Lesson.Result.Error.CreateLessonError;
import Data.Result.Failure;
import Data.Result.Success;
import Helpers.*;
import Service.LessonService;
import io.javalin.http.Context;
import org.jetbrains.annotations.NotNull;

public class LessonController {

    private final LessonService lessonService;

    public LessonController(LessonService lessonService) {
        this.lessonService = lessonService;
    }

    public void createLessonForm(@NotNull Context ctx) {
        JteResponses.with(ctx)
            .withUser()
            .render("pages/create-lesson-page.jte");
    }

    public void createLesson(Context ctx) {
        Requests.requireHx(ctx);
        var url = ctx.formParamAsClass("url", String.class)
                .check(Rules.url(), "Content URL must be correctly formatted");

        var title = ctx.formParamAsClass("title", String.class)
                .check(Rules.requiredWhen(url.get().isBlank()), "Title is required");
        var form = Form.of(title, url);

        switch (Forms.isValid(form)) {
            case true -> {
                var validTitle = title.get();
                var validContentUrl = url.get();
                var createLessonCommand = CreateLessonCommand.of(validTitle, validContentUrl);
                var result = lessonService.createLesson(createLessonCommand);

                switch (result) {
                    case Failure(CreateLessonError(var message)) -> {
                        JteResponses.with(ctx)
                                .withError(message)
                                .status(400)
                                .render("partials/create-lesson-form.jte");
                    }
                    case Success(var lesson) -> {
                        JteResponses.with(ctx)
                                .status(200)
                                .render("partials/lesson.jte");
                    }
                }
            }
            case false -> JteResponses.with(ctx)
                    .withErrors(title, url)
                    .status(400)
                    .render("partials/create-lesson-form.jte");
        }
        JteResponses.with(ctx)
                .status(201)
                .render("partials/create-lesson-form.jte");
    }
    public static LessonController of(LessonService lessonService) {
        return new LessonController(lessonService);
    }
}
