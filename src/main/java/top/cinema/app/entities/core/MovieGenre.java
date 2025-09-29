package top.cinema.app.entities.core;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

public enum MovieGenre {
    // Common Genres
    ACTION("ACTION", "Akcja", "Sensacyjny"),
    ADVENTURE("ADVENTURE", "Przygodowy"),
    ANIMATION("ANIMATION", "Animacja"),
    ANIME("ANIME", "Anime"),
    COMEDY("COMEDY", "Komedia"),
    CRIME("CRIME", "Kryminał"),
    DOCUMENTARY("DOCUMENTARY", "Dokumentalny"),
    DRAMA("DRAMA", "Dramat"),
    FAMILY("FAMILY", "Familijny"),
    FANTASY("FANTASY", "Fantasy"),
    HORROR("HORROR", "Horror"),
    MUSICAL("MUSICAL", "Musical"),
    MUSIC("MUSIC", "Muzyczny"),
    MYSTERY("MYSTERY"),
    ROMANCE("ROMANCE", "Romans", "Melodramat", "Komedia rom."),
    SCI_FI("SCI_FI", "Sci-Fi"),
    SHORT("SHORT", "Krótkometrażowy"),
    SPORT("SPORT", "Sportowy"),
    THRILLER("THRILLER", "Thriller", "Dreszczowiec"),
    WESTERN("WESTERN", "Western"),
    WAR(null, "Wojenny"),

    // IMDb Specific
    GAME_SHOW("GAME_SHOW"),
    LIFESTYLE("LIFESTYLE"),
    REALITY_TV("REALITY_TV"),
    SEASONAL("SEASONAL", "Świąteczny"),

    // Filmweb Specific
    ADULT_ANIMATION(null, "Animacja dla dorosłych"),
    FAIRY_TALE(null, "Baśń"),
    BIBLICAL(null, "Biblijny"),
    BIOGRAPHICAL(null, "Biograficzny"),
    BLACK_COMEDY(null, "Czarna komedia"),
    FOR_CHILDREN(null, "Dla dzieci"),
    FOR_YOUTH(null, "Dla młodzieży"),
    DOCUMENTARIZED(null, "Dokumentalizowany"),
    HISTORICAL_DRAMA(null, "Dramat historyczny"),
    SOCIAL_DRAMA(null, "Dramat obyczajowy"),
    COURT_DRAMA(null, "Dramat sądowy"),
    EROTIC(null, "Erotyczny"),
    DOCUDRAMA(null, "Fabularyzowany dok."),
    FILM_NOIR(null, "Film-Noir"),
    GANGSTER(null, "Gangsterski"),
    GROTESQUE(null, "Groteska filmowa"),
    HISTORICAL(null, "Historyczny"),
    DISASTER(null, "Katastroficzny"),
    CRIME_COMEDY(null, "Komedia kryminalna"),
    COMEDY_DRAMA(null, "Komedia obycz."),
    COSTUME(null, "Kostiumowy"),
    SILENT(null, "Niemy"),
    SOCIAL(null, "Obyczajowy"),
    POETIC(null, "Poetycki"),
    POLITICAL(null, "Polityczny"),
    PROPAGANDA(null, "Propagandowy"),
    NATURE(null, "Przyrodniczy"),
    PSYCHOLOGICAL(null, "Psychologiczny"),
    RELIGIOUS(null, "Religijny"),
    SATIRE(null, "Satyra"),
    SURREALIST(null, "Surrealistyczny"),
    SPY(null, "Szpiegowski"),
    MARTIAL_ARTS(null, "Sztuki walki"),
    TRUE_CRIME(null, "True crime"),
    XXX(null, "XXX");

    private final String imdbGenre;
    private final List<String> filmwebGenres;

    MovieGenre(String imdbGenre, String... filmwebGenres) {
        this.imdbGenre = imdbGenre;
        this.filmwebGenres = Arrays.asList(filmwebGenres);
    }

    public String getImdbGenre() {
        return imdbGenre;
    }

    public List<String> getFilmwebGenres() {
        return filmwebGenres;
    }

    public static MovieGenre fromImdbGenre(String genre) {
        if (genre == null) {
            return null;
        }
        return Arrays.stream(values())
                .filter(g -> genre.equalsIgnoreCase(g.imdbGenre))
                .findFirst()
                .orElse(null);
    }

    public static MovieGenre fromFilmwebGenre(String genre) {
        if (genre == null) {
            return null;
        }
        return Arrays.stream(values())
                .filter(g -> g.filmwebGenres.stream().anyMatch(genre::equalsIgnoreCase))
                .findFirst()
                .orElse(null);
    }

    public static Set<MovieGenre> fromImdbGenres(List<String> genres) {
        if (genres == null) {
            return Collections.emptySet();
        }
        return genres.stream()
                .map(MovieGenre::fromImdbGenre)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());
    }

    public static Set<MovieGenre> fromFilmwebGenres(List<String> genres) {
        if (genres == null) {
            return Collections.emptySet();
        }
        return genres.stream()
                .map(MovieGenre::fromFilmwebGenre)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());
    }
}
