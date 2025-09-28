package top.cinema.app.fetching.movie_data.service;

import org.springframework.stereotype.Service;
import top.cinema.app.fetching.movie_data.api.ImdbApiClient;
import top.cinema.app.fetching.movie_data.dto.IMDbMovieData;

@Service
public class IMDbDataFetcherImpl implements IMDbDataFetcher {
    private final ImdbApiClient client;

    public IMDbDataFetcherImpl(ImdbApiClient client) {
        this.client = client;
    }

    @Override
    public IMDbMovieData getDataForMovie(String imdbId) {
        var details = client.fetchDetails(imdbId).getBody();
        return new IMDbMovieData(
                imdbId,
                details.getImdbUrl(),
                details.rating().aggregateRating(),
                details.rating().voteCount(),
                details.primaryImage().url());
    }
}
