package Data.Lesson.Result.Success;

public record Lesson(String title, String imgUrl, String content, String contentUrl, String createdBy) {
    public static Lesson of(String title, String imgUrl, String content, String contentUrl, String createdBy) {
        return new Lesson(title, imgUrl, content, contentUrl, createdBy);
    }
}
