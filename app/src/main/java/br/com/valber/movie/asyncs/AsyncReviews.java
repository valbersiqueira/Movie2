package br.com.valber.movie.asyncs;

import android.os.AsyncTask;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import br.com.valber.movie.BuildConfig;
import br.com.valber.movie.json.JsonConverterReviews;
import br.com.valber.movie.json.MoviesService;
import br.com.valber.movie.json.ResultReviewsJSON;
import br.com.valber.movie.utils.ResultAsync;
import br.com.valber.movie.utils.ResultReviewsAsync;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class AsyncReviews extends AsyncTask<Integer, Void, Void> {

    private ResultReviewsAsync resultAsync;
    public static final String LOG = AsyncMovies.class.getSimpleName();

    public AsyncReviews(ResultReviewsAsync resultAsync) {
        this.resultAsync = resultAsync;
    }

    @Override
    protected Void doInBackground(Integer... values) {
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(ResultReviewsJSON.class, new JsonConverterReviews())
                .create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BuildConfig.OPEN_URL_PATTERN)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        MoviesService service = retrofit.create(MoviesService.class);
        Call<ResultReviewsJSON> call = service.getReviews(
                values[0],
                BuildConfig.OPEN_MOVIES_MAP_KEY,
                "pt-BR"
        );

        call.enqueue(new Callback<ResultReviewsJSON>() {
            @Override
            public void onResponse(Call<ResultReviewsJSON> call, Response<ResultReviewsJSON> response) {
                if (response.body() != null)
                    resultAsync.resultMovie(response.body());
            }

            @Override
            public void onFailure(Call<ResultReviewsJSON> call, Throwable t) {
                Log.e(LOG, t.getMessage());
            }
        });

        return null;
    }
}