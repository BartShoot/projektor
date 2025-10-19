package top.cinema.app.api.assembler;

import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;
import top.cinema.app.api.MovieController;
import top.cinema.app.dto.model.MovieSummaryDto;
import top.cinema.app.entities.core.Movie;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class MovieSummaryDtoAssembler extends RepresentationModelAssemblerSupport<Movie, MovieSummaryDto> {

    public MovieSummaryDtoAssembler() {
        super(MovieController.class, MovieSummaryDto.class);
    }

    @Override
    public MovieSummaryDto toModel(Movie entity) {
        MovieSummaryDto dto = instantiateModel(entity);
        dto.add(linkTo(methodOn(MovieController.class).getMovieById(entity.getId()))
                .withSelfRel());
        return dto;
    }

    @Override
    protected MovieSummaryDto instantiateModel(Movie entity) {
        return entity.toSummaryDto();
    }
}
