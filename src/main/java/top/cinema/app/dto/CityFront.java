package top.cinema.app.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.Collection;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record CityFront(Integer id, String name, Integer cinemaCount, Collection<CinemaFront> cinemas) {

    public CityFront(Integer id, String name, Integer cinemaCount) {
        this(id, name, cinemaCount, null);
    }
}
