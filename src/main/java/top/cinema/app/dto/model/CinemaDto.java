package top.cinema.app.dto.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;
import top.cinema.app.model.CinemaChain;

import java.util.Objects;

@Relation(collectionRelation = "cinemas", itemRelation = "cinema")
public class CinemaDto extends RepresentationModel<CinemaDto> {
    private final Integer id;
    private final String name;
    private final String location;
    private final CinemaChain cinemaChain;
    private final CitySummaryDto city;
    private final Integer showingCounts;

    @JsonCreator
    public CinemaDto(
            @JsonProperty("id") Integer id,
            @JsonProperty("name") String name,
            @JsonProperty("location") String location,
            @JsonProperty("cinemaChain") CinemaChain cinemaChain,
            @JsonProperty("city") CitySummaryDto city,
            @JsonProperty("showingCounts") Integer showingCounts) {
        this.id = id;
        this.name = name;
        this.location = location;
        this.cinemaChain = cinemaChain;
        this.city = city;
        this.showingCounts = showingCounts;
    }

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getLocation() {
        return location;
    }

    public CinemaChain getCinemaChain() {
        return cinemaChain;
    }

    public CitySummaryDto getCity() {
        return city;
    }

    public Integer getShowingCounts() {
        return showingCounts;
    }

}
