package top.cinema.app.api.assembler;

import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;
import top.cinema.app.api.CinemaController;
import top.cinema.app.api.MovieController;
import top.cinema.app.dto.model.ShowingSummaryDto;
import top.cinema.app.entities.core.Showing;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class ShowingSummaryDtoAssembler extends RepresentationModelAssemblerSupport<Showing, ShowingSummaryDto> {

    public ShowingSummaryDtoAssembler() {
        super(CinemaController.class, ShowingSummaryDto.class);
    }

    @Override
    public ShowingSummaryDto toModel(Showing entity) {
        ShowingSummaryDto dto = instantiateModel(entity);
        dto.add(linkTo(methodOn(MovieController.class)
                        .getMovieById(entity.getMovie().getId()))
                .withRel("movie"));
        return dto;
    }

    @Override
    protected ShowingSummaryDto instantiateModel(Showing entity) {
        return entity.toShowingSummaryDtoForCinema();
    }

    public ShowingSummaryDto toModelForMovie(Showing entity) {
        ShowingSummaryDto dto = entity.toShowingSummaryDtoForMovie();
        dto.add(linkTo(methodOn(CinemaController.class)
                        .getCinemaById(entity.getCinema().getId()))
                .withRel("cinema"));
        return dto;
    }
}
