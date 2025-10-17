package top.cinema.app.dto.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;

import java.time.LocalDateTime;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Relation(collectionRelation = "showings", itemRelation = "showing")
public class ShowingSummaryDto extends RepresentationModel<ShowingSummaryDto> {
    private final Integer id;
    private final LocalDateTime showingTime;
    private final CinemaSummaryDto cinema;
    private final Integer movieId;

    @JsonCreator
    public ShowingSummaryDto(
            @JsonProperty("id") Integer id,
            @JsonProperty("showingTime") LocalDateTime showingTime,
            @JsonProperty("cinema") CinemaSummaryDto cinema,
            @JsonProperty("movieId") Integer movieId) {
        this.id = id;
        this.showingTime = showingTime;
        this.cinema = cinema;
        this.movieId = movieId;
    }

    public Integer getId() {
        return id;
    }

    public LocalDateTime getShowingTime() {
        return showingTime;
    }

    public CinemaSummaryDto getCinema() {
        return cinema;
    }

    public Integer getMovieId() {
        return movieId;
    }
}
