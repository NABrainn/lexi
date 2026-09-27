package Repository;

import org.jdbi.v3.core.Jdbi;

public class LessonRepository {

    private final Jdbi jdbi;

    public LessonRepository(Jdbi jdbi) {
        this.jdbi = jdbi;
    }

    public static LessonRepository of(Jdbi jdbi) {
        return new LessonRepository(jdbi);
    }
}
