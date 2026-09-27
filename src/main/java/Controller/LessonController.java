package Controller;

import Data.Lesson.Request.CreateLessonCommand;
import Data.Lesson.Result.Error.CreateLessonError;
import Data.Result.Failure;
import Data.Result.Success;
import Helpers.*;
import Service.LessonService;
import io.javalin.http.Context;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

public class LessonController {

    private final LessonService lessonService;

    public LessonController(LessonService lessonService) {
        this.lessonService = lessonService;
    }

    public void createLessonForm(@NotNull Context ctx) {
        switch (Headers.isHxRequest(ctx)) {
            case true ->
                    JteResponses.with(ctx)
                        .render("partials/create-lesson-form.jte");
            case false ->
                    JteResponses.with(ctx)
                        .withUser()
                        .render("pages/create-lesson-page.jte");
        }
    }

    public void createLesson(Context ctx) {
        Requests.requireHx(ctx);
        var title = ctx.formParamAsClass("title", String.class)
                .check(Rules.required(), "Title is required");
        var contentUrl = ctx.formParamAsClass("contentUrl", String.class)
                .check(Rules.url(), "Content URL must be correctly formatted");
        var content = ctx.formParamAsClass("content", String.class);
        var form = Form.of(title, contentUrl, content);

        switch (Forms.isValid(form)) {
            case true -> {
                var validTitle = title.get();
                var validContentUrl = contentUrl.get();
                var validContent = content.get();
                var createLessonCommand = CreateLessonCommand.of(validTitle, validContentUrl, validContent);
                var result = lessonService.createLesson(createLessonCommand);

                switch (result) {
                    case Failure(CreateLessonError(var message)) -> {
                        var errors = FormErrors.global(message);
                        var params = Map.of(
                                "errors", errors
                        );
                        JteResponses.with(ctx)
                                .params(params)
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
                    .withErrors(title, contentUrl, content)
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
