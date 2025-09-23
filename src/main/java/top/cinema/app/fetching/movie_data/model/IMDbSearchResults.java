package top.cinema.app.fetching.movie_data.model;

import java.util.Collection;

public record IMDbSearchResults(Collection<Titles> titles) {

    public record Titles(String id,
                         String primaryTitle,
                         String originalTitle,
                         PrimaryImage primaryImage,
                         Integer startYear,
                         Rating rating) {
        public record PrimaryImage(String url) {
        }

        public record Rating(Float aggregateRating, Integer voteCount) {
        }
    }
}
