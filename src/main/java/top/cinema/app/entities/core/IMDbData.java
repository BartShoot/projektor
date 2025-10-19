package top.cinema.app.entities.core;

import jakarta.persistence.Embeddable;

@Embeddable
public class IMDbData {
    private String externalId;
    private String url;
    private Float rating;
    private Integer ratingCount;
    private String posterUrl;

    public IMDbData() {
    }

    public IMDbData(String externalId, String url, Float rating, Integer ratingCount, String posterUrl) {
        this.externalId = externalId;
        this.url = url;
        this.rating = rating;
        this.ratingCount = ratingCount;
        this.posterUrl = posterUrl;
    }

    public String getExternalId() {
        return externalId;
    }

    public void setExternalId(String externalId) {
        this.externalId = externalId;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Float getRating() {
        return rating;
    }

    public void setRating(Float rating) {
        this.rating = rating;
    }

    public Integer getRatingCount() {
        return ratingCount;
    }

    public void setRatingCount(Integer ratingCount) {
        this.ratingCount = ratingCount;
    }

    public String getPosterUrl() {
        return posterUrl;
    }

    public void setPosterUrl(String posterUrl) {
        this.posterUrl = posterUrl;
    }

}
