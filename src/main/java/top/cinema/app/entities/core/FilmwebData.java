package top.cinema.app.entities.core;

import jakarta.persistence.Embeddable;

@Embeddable
public class FilmwebData {
    private String id;
    private String filmwebUrl;
    private Float filmwebRating;
    private Integer filmwebRatingCount;
    private String filmwebPosterUrl;

    public FilmwebData() {
    }

    public FilmwebData(String id,
                       String filmwebUrl,
                       Float filmwebRating,
                       Integer filmwebRatingCount,
                       String filmwebPosterUrl) {
        this.id = id;
        this.filmwebUrl = filmwebUrl;
        this.filmwebRating = filmwebRating;
        this.filmwebRatingCount = filmwebRatingCount;
        this.filmwebPosterUrl = filmwebPosterUrl;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFilmwebUrl() {
        return filmwebUrl;
    }

    public void setFilmwebUrl(String filmwebUrl) {
        this.filmwebUrl = filmwebUrl;
    }

    public Float getFilmwebRating() {
        return filmwebRating;
    }

    public void setFilmwebRating(Float filmwebRating) {
        this.filmwebRating = filmwebRating;
    }

    public Integer getFilmwebRatingCount() {
        return filmwebRatingCount;
    }

    public void setFilmwebRatingCount(Integer filmwebRatingCount) {
        this.filmwebRatingCount = filmwebRatingCount;
    }

    public String getFilmwebPosterUrl() {
        return filmwebPosterUrl;
    }

    public void setFilmwebPosterUrl(String filmwebPosterUrl) {
        this.filmwebPosterUrl = filmwebPosterUrl;
    }

}
