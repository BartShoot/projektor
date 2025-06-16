package top.cinema.app.fetching.helios.model;

public record LocationDto(
        String province,
        String city,
        String street,
        double latitude,
        double longitude
) {}
