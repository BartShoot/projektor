package top.cinema.app.fetching.movie_data.service;

import org.springframework.stereotype.Service;
import top.cinema.app.fetching.movie_data.api.ImdbApiClient;
import top.cinema.app.fetching.movie_data.dto.IMDbMovieData;
import top.cinema.app.fetching.movie_data.model.IMDbMovieDetails;

@Service
public class IMDbDataFetcherImpl implements IMDbDataFetcher {
    private final ImdbApiClient client;

    public IMDbDataFetcherImpl(ImdbApiClient client) {
        this.client = client;
    }

    @Override
    public IMDbMovieData getDataForMovie(String imdbId) {
        var details = client.fetchDetails(imdbId).getBody();

        Float rating = null;
        Integer ratingCount = null;
        if (details.rating() != null) {
            rating = details.rating().aggregateRating();
            ratingCount = details.rating().voteCount();
        }

        String posterUrl = null;
        if (details.primaryImage() != null) {
            posterUrl = details.primaryImage().url();
        }

        return new IMDbMovieData(
                imdbId,
                details.getImdbUrl(),
                rating,
                ratingCount,
                posterUrl);
    }
}
