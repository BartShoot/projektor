package top.cinema.app.dto.model;

import org.springframework.hateoas.RepresentationModel;

import java.util.List;

public class CityWithCinemasDto extends RepresentationModel<CityWithCinemasDto> {
    private final Integer id;
    private final String name;
    private final List<CinemaSummaryDto> cinemas;

    public CityWithCinemasDto(Integer id, String name, List<CinemaSummaryDto> cinemas) {
        this.id = id;
        this.name = name;
        this.cinemas = cinemas;
    }

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public List<CinemaSummaryDto> getCinemas() {
        return cinemas;
    }
}
