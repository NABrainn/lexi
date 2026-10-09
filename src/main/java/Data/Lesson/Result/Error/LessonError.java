package Data.Lesson.Result.Error;

public sealed interface LessonError permits CreateLessonError{
    String message();
}
