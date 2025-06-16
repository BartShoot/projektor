package top.cinema.app.fetching.helios.model;

// Assuming PhotoMetaDto is in the same package or imported correctly
// import top.cinema.app.fetching.helios.model.PhotoMetaDto;

public record MainPhotoDto(
        String filePath,
        String url,
        PhotoMetaDto meta
) {}
