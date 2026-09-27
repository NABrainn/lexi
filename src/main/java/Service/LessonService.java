package Service;

import Data.Lesson.*;
import Data.Lesson.Request.CreateLessonCommand;
import Data.Lesson.Result.Error.CreateLessonError;
import Data.Lesson.Result.Error.LessonError;
import Data.Result.Failure;
import Data.Result.Result;
import Data.Result.Success;
import Data.Result.Unit;
import Repository.LessonRepository;

import java.util.Objects;

public class LessonService {

    private final JsoupService jsoupService;
    private final Tokenizer tokenizer;
    private final LessonRepository lessonRepository;

    public LessonService(JsoupService jsoupService, Tokenizer tokenizer, LessonRepository lessonRepository) {
        this.jsoupService = jsoupService;
        this.tokenizer = tokenizer;
        this.lessonRepository = lessonRepository;
    }

    public Result<?, LessonError> createLesson(CreateLessonCommand createLessonCommand) {
        Objects.requireNonNull(createLessonCommand);
        var title = createLessonCommand.title();
        var contentUrl = createLessonCommand.contentUrl();
        var content = createLessonCommand.content();
        var condition = LessonContentSource.from(contentUrl, content);

        return switch (condition) {
            case UrlContentSource() -> {
                var result = jsoupService.getWholeText(contentUrl);

                yield switch (result) {
                    case Failure(var error) -> Failure.of(CreateLessonError.of(error.message()));
                    case Success(var importedText) -> {
                        tokenizer.splitAsJson(importedText);
                        yield Success.of(Unit.INSTANCE);
                    }
                };
            }
            case ManualContentSource() -> Success.of(Unit.INSTANCE);
            case MissingContentSource() -> Failure.of(CreateLessonError.of("Fill out either url or manually insert content"));
        };
    }

    public static LessonService of(JsoupService jsoupService, Tokenizer tokenizer, LessonRepository lessonRepository) {
        return new LessonService(jsoupService, tokenizer, lessonRepository);
    }
}
