package Controller;

import Helpers.JteResponses;
import io.javalin.http.Context;
import org.jetbrains.annotations.NotNull;

public class IndexController {
    public static void indexPage(@NotNull Context ctx) {
        JteResponses.with(ctx)
                .withUser()
                .render("pages/index.jte");
    }
}
