package Data.Lesson.Request;

import Data.Operation.Interfaces.Command;

public record CreateLessonCommand(String title, String contentUrl) implements Command {
    public static CreateLessonCommand of(String validTitle, String validContentUrl) {
        return new CreateLessonCommand(validTitle, validContentUrl);
    }
}
