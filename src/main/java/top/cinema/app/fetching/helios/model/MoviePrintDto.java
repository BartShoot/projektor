package top.cinema.app.fetching.helios.model;

public record MoviePrintDto(
        String printType,
        String soundType,
        String speakingTypeLabel, // Can be null
        String printRelease
) {}
