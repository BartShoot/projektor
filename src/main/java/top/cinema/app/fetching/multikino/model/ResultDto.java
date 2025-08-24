package top.cinema.app.fetching.multikino.model;

import java.util.List;

public record ResultDto(List<ShowingGroupsDto> showingGroups, String filmId) {
}
