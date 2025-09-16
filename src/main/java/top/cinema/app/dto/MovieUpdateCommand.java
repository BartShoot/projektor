package top.cinema.app.dto;

public record MovieUpdateCommand(String title,
                                 String normalizedTitle,
                                 String originalTitle,
                                 Integer durationMinutes,
                                 String cinemaCityId,
                                 Integer heliosId,
                                 String multikinoId,
                                 String filmwebId,
                                 String imdbId) {
    public Boolean isEmpty() {
        return title == null && normalizedTitle == null && originalTitle == null && durationMinutes == null &&
                cinemaCityId == null && heliosId == null && multikinoId == null && filmwebId == null && imdbId == null;
    }
}