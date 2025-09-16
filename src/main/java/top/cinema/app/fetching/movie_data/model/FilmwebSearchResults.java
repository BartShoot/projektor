package top.cinema.app.fetching.movie_data.model;

import java.util.Collection;

public record FilmwebSearchResults(Integer total, Collection<Hits> searchHits) {
    public record Hits(Integer id, String type, String matchedTitle, String matchedLang) {
    }
}
