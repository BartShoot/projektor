package top.cinema.app.fetching.movie_data.model;

import com.fasterxml.jackson.annotation.JsonCreator;

import java.util.Collection;

public record IMDbInterests(Collection<Category> categories) {
    public enum Categories {
        ACTION, ADVENTURE, ANIMATION, ANIME, COMEDY, CRIME, DOCUMENTARY, DRAMA, FAMILY, FANTASY, GAME_SHOW, HORROR, LIFESTYLE, MUSIC, MUSICAL, MYSTERY, REALITY_TV, ROMANCE, SCI_FI, SEASONAL, SHORT, SPORT, THRILLER, WESTERN;

        @JsonCreator
        public static Categories fromString(String text) {
            String sanitized = text.toUpperCase().replace('-', '_').replace(' ', '_');
            return Categories.valueOf(sanitized);
        }
    }

    public record Category(Categories category, Collection<Interest> interests) {

        public record Interest(String id,
                               String name,
                               PrimaryImage primaryImage,
                               String description,
                               Boolean isSubgenre) {

            public record PrimaryImage(String url) {
            }
        }
    }

}
