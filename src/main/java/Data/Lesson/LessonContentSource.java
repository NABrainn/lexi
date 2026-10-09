package Data.Lesson;

public sealed interface LessonContentSource permits MissingContentSource, ManualContentSource, UrlContentSource {
    static LessonContentSource from(String contentUrl, String content) {
        var hasUrl = contentUrl != null && !contentUrl.trim().isEmpty();
        var hasContent = content != null && !content.trim().isEmpty();

        if (!hasUrl && !hasContent) {
            return new MissingContentSource();
        }

        if (hasUrl && !hasContent) {
            return new UrlContentSource();
        }

        if (!hasUrl) {
            return new ManualContentSource();
        }

        return new UrlContentSource();
    }
}
