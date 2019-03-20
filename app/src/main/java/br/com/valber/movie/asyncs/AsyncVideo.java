package br.com.valber.movie.asyncs;

import android.os.AsyncTask;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import br.com.valber.movie.BuildConfig;
import br.com.valber.movie.json.JsonConverterVideo;
import br.com.valber.movie.json.MoviesService;
import br.com.valber.movie.json.ResultVideoJSON;
import br.com.valber.movie.utils.ResultAsync;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class AsyncVideo extends AsyncTask<Integer, Void, Void> {

    private ResultAsync resultAsync;

    public AsyncVideo(ResultAsync resultAsync) {
        this.resultAsync = resultAsync;
    }

    @Override
    protected Void doInBackground(Integer... integers) {
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(ResultVideoJSON.class, new JsonConverterVideo())
                .create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BuildConfig.OPEN_URL_PATTERN)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        Integer id = integers[0];

        MoviesService service = retrofit.create(MoviesService.class);
        Call<ResultVideoJSON> call = service.getVideo(id,BuildConfig.OPEN_MOVIES_MAP_KEY);

        call.enqueue(new Callback<ResultVideoJSON>() {
            @Override
            public void onResponse(Call<ResultVideoJSON> call, Response<ResultVideoJSON> response) {
                if (response.body() != null) {
                    ResultVideoJSON movieVideo = response.body();
                    resultAsync.resultMovie(movieVideo);
                }
            }

            @Override
            public void onFailure(Call<ResultVideoJSON> call, Throwable throwable) {
                Log.e(AsyncMovies.class.getSimpleName(), throwable.getMessage());
            }
        });
        return null;
    }
}
