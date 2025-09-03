package top.cinema.app.fetching.service;

import java.text.Normalizer;
import java.util.regex.Pattern;

public class TitleNormalizer {

    private static final Pattern DIACRITICAL_MARKS = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
    private static final Pattern NON_ALPHANUMERIC_SYMBOLS = Pattern.compile("[^\\p{L}\\p{N}\\s]");
    private static final Pattern WHITESPACE = Pattern.compile("\\s+");

    public static String normalize(String title) {
        if (title == null) {
            return "";
        }
        String normalized = title.toLowerCase();
        normalized = Normalizer.normalize(normalized, Normalizer.Form.NFD);
        normalized = DIACRITICAL_MARKS.matcher(normalized).replaceAll("");
        normalized = NON_ALPHANUMERIC_SYMBOLS.matcher(normalized).replaceAll("");
        normalized = WHITESPACE.matcher(normalized).replaceAll(" ");
        normalized = normalized.replace(" & ", " i ").replace('&', 'i');
        return normalized.trim();
    }
}
