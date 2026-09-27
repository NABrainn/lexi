package Service;

import Data.Jsoup.Error.JsoupError;
import Data.Result.Failure;
import Data.Result.Result;
import Data.Result.Success;
import org.jsoup.Jsoup;

import java.io.IOException;

public class JsoupService {

    public Result<String, JsoupError> getWholeText(String contentUrl) {
            try {
                var text = Jsoup
                        .connect(contentUrl)
                        .followRedirects(false)
                        .get()
                        .wholeText()
                        .trim();
                return Success.of(text);
            } catch (IOException e) {
                var error = JsoupError.of("Failed to import data from URL. Please try again.");
                return Failure.of(error);
            }
    }

    public static JsoupService of() {
        return new JsoupService();
    }
}
