package top.cinema.app.fetching.helios.model;

public record PosterPhotoDto(
        String filePath,
        String url,
        PhotoMetaDto meta
) {}