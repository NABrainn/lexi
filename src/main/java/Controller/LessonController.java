package Controller;

import Helpers.Headers;
import Helpers.JteResponses;
import io.javalin.http.Context;
import org.jetbrains.annotations.NotNull;

public class LessonController {
    public void create(@NotNull Context ctx) {
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

    public static LessonController of() {
        return new LessonController();
    }
}
