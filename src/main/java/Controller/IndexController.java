package Controller;

import Helpers.JteResponses;
import io.javalin.http.Context;
import org.jetbrains.annotations.NotNull;

public class IndexController {
    public void indexPage(@NotNull Context ctx) {
        JteResponses.with(ctx)
                .withUser()
                .render("pages/index.jte");
    }

    public static IndexController of() {
        return new IndexController();
    }
}
