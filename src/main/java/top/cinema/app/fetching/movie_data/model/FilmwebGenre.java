package top.cinema.app.fetching.movie_data.model;

public record FilmwebGenre(Integer id, Name name, String nameKey
) {
    public record Name(String text) {
    }
}
