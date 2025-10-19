package top.cinema.app.api.assembler;

import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;
import top.cinema.app.api.CinemaController;
import top.cinema.app.dto.model.CinemaSummaryDto;
import top.cinema.app.entities.core.Cinema;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class CinemaSummaryDtoAssembler extends RepresentationModelAssemblerSupport<Cinema, CinemaSummaryDto> {

    public CinemaSummaryDtoAssembler() {
        super(CinemaController.class, CinemaSummaryDto.class);
    }

    @Override
    public CinemaSummaryDto toModel(Cinema entity) {
        CinemaSummaryDto dto = instantiateModel(entity);
        dto.add(linkTo(methodOn(CinemaController.class).getCinemaById(entity.getId()))
                .withSelfRel());
        return dto;
    }

    @Override
    protected CinemaSummaryDto instantiateModel(Cinema entity) {
        return entity.toSummaryDto();
    }
}
