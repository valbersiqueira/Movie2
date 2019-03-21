package br.com.valber.movie.json;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;


public interface MoviesService {

    @GET("popular")
    Call<ResultMovieJSON> getAllMovies(@Query("api_key") String apiKey,
                                       @Query("page") Integer page,
                                       @Query("language") String language);

    @GET("upcoming")
    Call<ResultMovieJSON> getLancamentos(@Query("api_key") String apiKey,
                                         @Query("page") Integer page,
                                         @Query("language") String language);


    @GET("{movie_id}/videos")
    Call<ResultVideoJSON> getVideo(@Path("movie_id") Integer movieId,
                                   @Query("api_key") String apiKey);

    @GET("{movie_id}/reviews")
    Call<ResultReviewsJSON> getReviews(@Path("movie_id") Integer id,
                                       @Query("api_key") String apiKey,
                                       @Query("language") String language);

}
