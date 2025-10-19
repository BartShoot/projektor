package top.cinema.app.api.assembler;

import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;
import top.cinema.app.api.CityController;
import top.cinema.app.dto.model.CityWithCinemasDto;
import top.cinema.app.entities.core.City;

import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class CityWithCinemasDtoAssembler extends RepresentationModelAssemblerSupport<City, CityWithCinemasDto> {

    private final CinemaSummaryDtoAssembler cinemaSummaryDtoAssembler;

    public CityWithCinemasDtoAssembler(CinemaSummaryDtoAssembler cinemaSummaryDtoAssembler) {
        super(CityController.class, CityWithCinemasDto.class);
        this.cinemaSummaryDtoAssembler = cinemaSummaryDtoAssembler;
    }

    @Override
    public CityWithCinemasDto toModel(City entity) {
        CityWithCinemasDto dto = instantiateModel(entity);
        dto.add(linkTo(methodOn(CityController.class).getCityById(entity.getId()))
                .withSelfRel());
        return dto;
    }

    @Override
    protected CityWithCinemasDto instantiateModel(City entity) {
        return new CityWithCinemasDto(
                entity.getId(),
                entity.getName(),
                entity.getCinemas().stream()
                        .map(cinemaSummaryDtoAssembler::toModel)
                        .collect(Collectors.toList()));
    }
}
