package Data.Lesson.Result.Error;

public record CreateLessonError(String message) implements LessonError {
    public static LessonError of(String message) {
        return new CreateLessonError(message);
    }
}
