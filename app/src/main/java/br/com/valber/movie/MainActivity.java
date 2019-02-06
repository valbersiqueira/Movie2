package br.com.valber.movie;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import br.com.valber.movie.adapter.AdapterMovie;
import br.com.valber.movie.asyncs.AsyncMovies;
import br.com.valber.movie.database.movie.MovieViewModel;
import br.com.valber.movie.entity.Movie;
import br.com.valber.movie.json.MovieJSON;
import br.com.valber.movie.json.ResultMovieJSON;
import br.com.valber.movie.utuls.ResultAsync;
import br.com.valber.movie.utuls.SendObjeto;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import okhttp3.internal.Util;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportFragmentManager().beginTransaction()
                .add(R.id.container_fragmente, new FragmenteMain())
                .commit();
    }

    public static class FragmenteMain extends Fragment implements ResultAsync {

        public static final String OBJ_SAVE_INSTANCE = "OBJ_SAVE_INSTANCE";

        private MovieViewModel movieViewModel;
        private Unbinder unbinder;
        private GridLayoutManager layoutManager;
        private AdapterMovie adapterMovie;
        private List<Movie> moviesSaveInstance;

        @BindView(R.id.swip_refresh)
        SwipeRefreshLayout refreshLayout;

        @BindView(R.id.recy_movie)
        RecyclerView recyclerView;

        @BindView(R.id.progress_recy)
        ProgressBar progressBar;

        @Nullable
        @Override
        public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable final Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.fragmente_main, container, false);

            unbinder = ButterKnife.bind(this, view);

            layoutManager = new GridLayoutManager(getContext(), getOfColuns());
            recyclerView.setHasFixedSize(true);
            recyclerView.setLayoutManager(layoutManager);
            adapterMovie = new AdapterMovie();
            adapterMovie.setOnclickListen(new AdapterMovie.ClickMovieListen() {
                @Override
                public void itemClickListen(Movie movie) {
                    Intent intent = new Intent(getActivity(), DetalheMovieActivity.class);
                    intent.putExtra(SendObjeto.SEND_MOVIE, movie);
                    startActivity(intent);
                }
            });
            recyclerView.setAdapter(adapterMovie);
            if (getPagePreference() != 1){
                savePreference(1);
            }
            final int page = getPagePreference();

            movieViewModel = ViewModelProviders.of(this).get(MovieViewModel.class);
            if (savedInstanceState != null) {
                moviesSaveInstance = savedInstanceState.getParcelableArrayList(OBJ_SAVE_INSTANCE);
            } else {
                executeAsyc(View.VISIBLE, page);
            }

            refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    executeAsyc(View.INVISIBLE, 1);
                }
            });

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                recyclerView.setOnScrollChangeListener(new View.OnScrollChangeListener() {
                    @Override
                    public void onScrollChange(View view, int i, int i1, int i2, int i3) {
                        GridLayoutManager layoutManager = (GridLayoutManager) recyclerView.getLayoutManager();
                        if (moviesSaveInstance.size() == layoutManager.findLastCompletelyVisibleItemPosition() +1){
                            int page = getPagePreference();
                            savePreference(page+1);
                            executeAsyc(View.VISIBLE, page+1);
                        }
                    }
                });
            } else {
                recyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
                    @Override
                    public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                        super.onScrolled(recyclerView, dx, dy);
                        GridLayoutManager layoutManager = (GridLayoutManager) recyclerView.getLayoutManager();
                        if (moviesSaveInstance.size() == layoutManager.findLastCompletelyVisibleItemPosition() +1){
                            recyclerView.setVisibility(View.INVISIBLE);
                            int page = getPagePreference();
                            savePreference(page+1);
                            executeAsyc(View.VISIBLE, page+1);
                        }
                    }
                });
            }
            setHasOptionsMenu(true);
            return view;
        }

        @Override
        public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
            getActivity().getMenuInflater().inflate(R.menu.settings, menu);
        }

        @Override
        public void onDestroyView() {
            super.onDestroyView();
            unbinder.unbind();
        }

        @Override
        public void onSaveInstanceState(Bundle outState) {
            outState.putParcelableArrayList(OBJ_SAVE_INSTANCE, (ArrayList<? extends Parcelable>) moviesSaveInstance);
            super.onSaveInstanceState(outState);
        }

        private void executeAsyc(int valueProgressBar, int page) {
            movieViewModel.getDao().deleteAll();
            progressBar.setVisibility(valueProgressBar);
            new AsyncMovies(this).execute(page);
        }

        @Override
        public void resultMovie(Object object) {
            progressBar.setVisibility(View.INVISIBLE);
            refreshLayout.setRefreshing(false);
            ResultMovieJSON json = (ResultMovieJSON) object;
            if (moviesSaveInstance == null){
                moviesSaveInstance = preencherMovieBd(json.getMovies());
            } else {
                moviesSaveInstance.addAll(preencherMovieBd(json.getMovies()));
            }
            adapterMovie.submitList(moviesSaveInstance);
        }

        private int getOfColuns() {
            DisplayMetrics metrics = new DisplayMetrics();
            getActivity().getWindowManager().getDefaultDisplay().getMetrics(metrics);
            int wintDriver = 400;
            int width = metrics.widthPixels;
            int nColuns = (width / wintDriver);
            if (nColuns < 2) return 2;
            return nColuns;
        }

        private List<Movie> preencherMovieBd(List<MovieJSON> jsonList) {
            List<Movie> movies = new ArrayList<>();
            for (MovieJSON m : jsonList) {
                Movie movie = new Movie();
                movie.setId(m.getId());
                movie.setVoteCount(m.getVoteCount());
                movie.setVoteAverage(m.getVoteAverage());
                movie.setTitle(m.getTitle());
                movie.setPopularity(m.getPopularity());
                movie.setPosterPath(m.getPosterPath());
                movie.setOriginalTitle(m.getOriginalTitle());
                movie.setOriginalLanguage(m.getOriginalLanguage());
                movie.setBackdropPath(m.getBackdropPath());
                movie.setAdult(m.getAdult());
                movie.setOverview(m.getOverview());
                movie.setReleaseDate(m.getReleaseDate());
                movie.setFavoriti(false);
                movie.setDataSave(new Date());
                movies.add(movie);
            }
            return movies;
        }

        @Override
        public boolean onOptionsItemSelected(MenuItem item) {
            switch (item.getItemId()) {
                case R.id.polupares:
                    return true;
                case R.id.favoritos:
                    return true;
                case R.id.votados:
                    return true;
                default:
                    return true;
            }
        }

        private void savePreference(int value){
            SharedPreferences preferences = getActivity().getPreferences(Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = preferences.edit();
            editor.putInt(String.valueOf(R.string.put_page_key), value);
            editor.commit();
        }

        private int getPagePreference(){
            SharedPreferences preferences = getActivity().getPreferences(Context.MODE_PRIVATE);
            int defaultValue = 1;
            return preferences.getInt(String.valueOf(R.string.put_page_key), defaultValue);
        }

    }


}
