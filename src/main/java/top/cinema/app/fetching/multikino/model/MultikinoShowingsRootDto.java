package top.cinema.app.fetching.multikino.model;

import java.util.List;

public record MultikinoShowingsRootDto(
        List<ResultDto> result
) {
}
