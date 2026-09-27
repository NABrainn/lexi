package Service;

import Repository.TranslationRepository;

import java.util.Objects;
import java.util.concurrent.StructuredTaskScope;

public class Tokenizer {

    private final TranslationRepository translationRepository;

    public Tokenizer(TranslationRepository translationRepository) {
        this.translationRepository = translationRepository;
    }

    public Object splitAsJson(String importedText) {
        Objects.requireNonNull(importedText);
        return null;
    }

    public Object mapFromJson() {
        return null;
    }

    public static Tokenizer of(TranslationRepository translationRepository) {
        return new Tokenizer(translationRepository);
    }
}
