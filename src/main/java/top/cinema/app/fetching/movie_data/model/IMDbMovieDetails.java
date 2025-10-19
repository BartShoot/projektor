package top.cinema.app.fetching.movie_data.model;

import java.util.Collection;

public record IMDbMovieDetails(String id,
                               String primaryTitle,
                               IMDbSearchResults.Titles.PrimaryImage primaryImage,
                               Integer startYear,
                               Integer runtimeSeconds,
                               Collection<String> genres,
                               IMDbSearchResults.Titles.Rating rating,
                               String plot,
                               Collection<OriginCountries> originCountries,
                               Collection<SpokenLanguages> spokenLanguages,
                               Collection<Interests> interests) {
    public record OriginCountries(String code, String name) {
    }

    public record SpokenLanguages(String code, String name) {
    }

    public record Interests(String id, String name) {
    }

    public String getImdbUrl() {
        return "https://www.imdb.com/title/" + id;
    }
}
