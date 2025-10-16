package top.cinema.app.dto.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;
import top.cinema.app.dto.MovieFront;

import java.util.Objects;
import java.util.Set;

@Relation(collectionRelation = "movies", itemRelation = "movie")
public class MovieDto extends RepresentationModel<MovieDto> {
    private final Integer id;
    private final String title;
    private final Integer durationMinutes;
    private final Integer showingsCount;
    private final MovieFront.ExternalSourceData filmweb;
    private final MovieFront.ExternalSourceData imdb;
    private final Set<String> genres;

    @JsonCreator
    public MovieDto(
            @JsonProperty("id") Integer id,
            @JsonProperty("title") String title,
            @JsonProperty("durationMinutes") Integer durationMinutes,
            @JsonProperty("showingsCount") Integer showingsCount,
            @JsonProperty("filmweb") MovieFront.ExternalSourceData filmweb,
            @JsonProperty("imdb") MovieFront.ExternalSourceData imdb,
            @JsonProperty("genres") Set<String> genres) {
        this.id = id;
        this.title = title;
        this.durationMinutes = durationMinutes;
        this.showingsCount = showingsCount;
        this.filmweb = filmweb;
        this.imdb = imdb;
        this.genres = genres;
    }

    public Integer getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public Integer getDurationMinutes() {
        return durationMinutes;
    }

    public Integer getShowingsCount() {
        return showingsCount;
    }

    public MovieFront.ExternalSourceData getFilmweb() {
        return filmweb;
    }

    public MovieFront.ExternalSourceData getImdb() {
        return imdb;
    }

    public Set<String> getGenres() {
        return genres;
    }

}
