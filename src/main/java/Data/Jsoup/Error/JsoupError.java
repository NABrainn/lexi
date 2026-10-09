package Data.Jsoup.Error;

public record JsoupError(String message) {
    public static JsoupError of(String message) {
        return new JsoupError(message);
    }
}
