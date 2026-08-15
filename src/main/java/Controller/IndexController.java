package Controller;

import io.javalin.http.Context;
import org.jetbrains.annotations.NotNull;

public class IndexController {
    public static void indexPage(@NotNull Context ctx) {
        ctx.render("pages/index.jte");
    }
}
