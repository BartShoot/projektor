package top.cinema.app.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import top.cinema.app.model.CinemaChain;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record CinemaFront(Integer id, String name, String location, String externalId, CinemaChain cinemaChain, CityFront city,
                          Integer showingCounts, java.util.List<ShowingFront> list) {
}
