package Repository;

import org.jdbi.v3.core.Jdbi;

import java.util.List;

public class TranslationRepository {

    private final Jdbi jdbi;

    public TranslationRepository(Jdbi jdbi) {
        this.jdbi = jdbi;
    }

    public static TranslationRepository of(Jdbi jdbi) {
        return new TranslationRepository(jdbi);
    }

    public List<Object> getTokens() {
//        return jdbi.withHandle(handle -> {
//            final var SQL = """
//
//                    """;
//            return handle
//                    .createQuery(SQL)
//                    .map((rs, _) -> null);
//        });
        return List.of();
    }
}
