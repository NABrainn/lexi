package Data.Lesson.Request;

import Data.Operation.Interfaces.Command;

public record CreateLessonCommand(String title, String contentUrl, String content) implements Command {
    public static CreateLessonCommand of(String validTitle, String validContentUrl, String validContent) {
        return new CreateLessonCommand(validTitle, validContentUrl, validContent);
    }
}
