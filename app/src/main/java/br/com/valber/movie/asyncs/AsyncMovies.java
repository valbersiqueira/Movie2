package br.com.valber.movie.asyncs;

import android.os.AsyncTask;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import br.com.valber.movie.BuildConfig;
import br.com.valber.movie.entity.Movie;
import br.com.valber.movie.json.JsonConverter;
import br.com.valber.movie.json.MoviesService;
import br.com.valber.movie.json.ResultMovieJSON;
import br.com.valber.movie.utils.ResultAsync;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class AsyncMovies extends AsyncTask<Integer, Void, Void> {

    private ResultAsync resultAsync;
    public static final String LOG = AsyncMovies.class.getSimpleName();

    public AsyncMovies(final ResultAsync resultAsync) {
        this.resultAsync = resultAsync;
    }

    @Override
    protected Void doInBackground(Integer... values) {
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(Movie.class, new JsonConverter())
                .create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BuildConfig.OPEN_URL_PATTERN)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        MoviesService service = retrofit.create(MoviesService.class);
        Call<ResultMovieJSON> call = service.getAllMovies(
                BuildConfig.OPEN_MOVIES_MAP_KEY,
                values[0],
                "pt-BR"
        );

        call.enqueue(new Callback<ResultMovieJSON>() {
            @Override
            public void onResponse(Call<ResultMovieJSON> call, Response<ResultMovieJSON> response) {
                if (response.body() != null)
                    resultAsync.resultMovie(response.body());
            }

            @Override
            public void onFailure(Call<ResultMovieJSON> call, Throwable t) {
                Log.e(LOG, t.getMessage());
            }
        });

        return null;
    }
}
