package top.cinema.app.dto.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;

@Relation(collectionRelation = "cinemas", itemRelation = "cinema")
public class CinemaSummaryDto extends RepresentationModel<CinemaSummaryDto> {
    private final Integer id;
    private final String name;
    private final String location;
    private final Integer showingCounts;

    @JsonCreator
    public CinemaSummaryDto(
            @JsonProperty("id") Integer id,
            @JsonProperty("name") String name,
            @JsonProperty("location") String location,
            @JsonProperty("showingCounts") Integer showingCounts) {
        this.id = id;
        this.name = name;
        this.location = location;
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

    public Integer getShowingCounts() {
        return showingCounts;
    }

}
