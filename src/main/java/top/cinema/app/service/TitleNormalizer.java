package top.cinema.app.service;

import java.text.Normalizer;

public class TitleNormalizer {

    public static String normalize(String title) {
        if (title == null) {
            return "";
        }
        return Normalizer.normalize(title, Normalizer.Form.NFD)
                .replaceAll("\\p{InCombiningDiacriticalMarks}+", "")
                .toLowerCase()
                .replaceAll("&", "i")
                .replaceAll("[^a-z0-9\\s]", "")
                .replaceAll("\\s+", " ")
                .trim();
    }
}
