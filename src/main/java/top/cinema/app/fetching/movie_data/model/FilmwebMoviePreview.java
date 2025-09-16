package top.cinema.app.fetching.movie_data.model;

import java.util.Collection;

public record FilmwebMoviePreview(Integer year,
                                  Plot plot,
                                  Title title,
                                  Title originalTitle,
                                  Poster poster,
                                  Collection<FilmwebGenre> genres,
                                  Collection<Countries> countries,
                                  Integer duration) {
    public record Plot(String synopsis) {
    }

    public record Title(String title, String country, String lang, Boolean original) {
    }

    public record Poster(String path) {
    }

    public record Countries(Integer id, String code) {
    }

    public String getPosterUrl() {
        return "https://fwcdn.pl/fpo" + poster().path();
    }

    public String getHtmlUrl(Integer id) {
        String title = this.title == null ? this.originalTitle.title() : this.title.title();
        return "https://www.filmweb.pl/film/%s-%s-%s".formatted(title, year(), id);
    }
}
