package top.cinema.app.entities.core;

import jakarta.persistence.Embeddable;

@Embeddable
public class IMDbData {
    private String id;
    private String imdbUrl;
    private Float imdbRating;
    private Integer imdbRatingCount;
    private String imdbPosterUrl;

    public IMDbData() {
    }

    public IMDbData(String id, String imdbUrl, Float imdbRating, Integer imdbRatingCount, String imdbPosterUrl) {
        this.id = id;
        this.imdbUrl = imdbUrl;
        this.imdbRating = imdbRating;
        this.imdbRatingCount = imdbRatingCount;
        this.imdbPosterUrl = imdbPosterUrl;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getImdbUrl() {
        return imdbUrl;
    }

    public void setImdbUrl(String imdbUrl) {
        this.imdbUrl = imdbUrl;
    }

    public Float getImdbRating() {
        return imdbRating;
    }

    public void setImdbRating(Float imdbRating) {
        this.imdbRating = imdbRating;
    }

    public Integer getImdbRatingCount() {
        return imdbRatingCount;
    }

    public void setImdbRatingCount(Integer imdbRatingCount) {
        this.imdbRatingCount = imdbRatingCount;
    }

    public String getImdbPosterUrl() {
        return imdbPosterUrl;
    }

    public void setImdbPosterUrl(String imdbPosterUrl) {
        this.imdbPosterUrl = imdbPosterUrl;
    }

}
