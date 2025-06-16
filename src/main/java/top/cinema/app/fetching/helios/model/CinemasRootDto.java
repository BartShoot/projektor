package top.cinema.app.fetching.helios.model;

import java.util.List;

public record CinemasRootDto(
        int status,
        List<CinemaDto> data,
        CinemaListMetaDto meta
) {}