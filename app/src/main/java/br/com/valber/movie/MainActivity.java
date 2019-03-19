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
import br.com.valber.movie.utils.ResultAsync;
import br.com.valber.movie.utils.SendObjeto;
import br.com.valber.movie.utils.UtilsView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

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
        private Boolean isFavorite= false;
        private Boolean isAtualizarList = true;

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

            layoutManager = new GridLayoutManager(getContext(),
                    UtilsView.getNumberColumns(new DisplayMetrics(), getActivity()));
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
            recyclerView.swapAdapter(adapterMovie, false);
            if (getPagePreference() != 1){
                savePreference(1);
            }
            final int page = getPagePreference();

            movieViewModel = ViewModelProviders.of(getActivity()).get(MovieViewModel.class);
            if (savedInstanceState != null) {
                moviesSaveInstance = savedInstanceState.getParcelableArrayList(OBJ_SAVE_INSTANCE);
            } else {
                executeAsyc(View.VISIBLE, page);
            }

            refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    if (!isFavorite) {
                        isAtualizarList = true;
                        executeAsyc(View.INVISIBLE, 1);
                    } else {
                        refreshLayout.setRefreshing(false);
                    }
                }
            });

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                recyclerView.setOnScrollChangeListener(new View.OnScrollChangeListener() {
                    @Override
                    public void onScrollChange(View view, int i, int i1, int i2, int i3) {
                        GridLayoutManager layoutManager = (GridLayoutManager) recyclerView.getLayoutManager();
                        if (!isFavorite) {
                            if (moviesSaveInstance.size() == layoutManager.findLastCompletelyVisibleItemPosition() + 1) {
                                carregarScroll();
                            }
                        }
                    }
                });
            } else {
                recyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
                    @Override
                    public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                        super.onScrolled(recyclerView, dx, dy);
                        GridLayoutManager layoutManager = (GridLayoutManager) recyclerView.getLayoutManager();
                        if (!isFavorite) {
                            if (moviesSaveInstance.size() == layoutManager.findLastCompletelyVisibleItemPosition() + 1) {
                                carregarScroll();
                            }
                        }
                    }
                });
            }
            setHasOptionsMenu(true);
            return view;
        }

        @Override
        public void onResume() {
            super.onResume();
            adapterMovie.notifyItemRangeRemoved(0, adapterMovie.getItemCount());
        }

        @Override
        public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
            getActivity().getMenuInflater().inflate(R.menu.settings, menu);
        }

        @Override
        public void onDestroyView() {
            super.onDestroyView();
            savePreference(1);
            unbinder.unbind();
        }

        @Override
        public void onSaveInstanceState(Bundle outState) {
            outState.putParcelableArrayList(OBJ_SAVE_INSTANCE, (ArrayList<? extends Parcelable>) moviesSaveInstance);
            super.onSaveInstanceState(outState);
        }

        private void executeAsyc(int valueProgressBar, int page) {
            progressBar.setVisibility(valueProgressBar);
            new AsyncMovies(this).execute(page);
        }

        @Override
        public void resultMovie(Object object) {
            progressBar.setVisibility(View.INVISIBLE);
            recyclerView.setVisibility(View.VISIBLE);
            refreshLayout.setRefreshing(false);
            ResultMovieJSON json = (ResultMovieJSON) object;
            if (moviesSaveInstance == null){
                moviesSaveInstance = preencherMovieBd(json.getMovies());
            } else {
                if (isAtualizarList) {
                    moviesSaveInstance.clear();
                }
                moviesSaveInstance.addAll(preencherMovieBd(json.getMovies()));
            }
            adapterMovie.submitList(moviesSaveInstance);
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
                    isFavorite = false;
                    if (moviesSaveInstance != null && moviesSaveInstance.size() > 0){
                        adapterMovie.notifyItemRangeRemoved(0, adapterMovie.getItemCount());
                        adapterMovie.submitList(moviesSaveInstance);
                    } else {
                        recyclerView.setVisibility(View.INVISIBLE);
                        executeAsyc(View.VISIBLE, getPagePreference());
                    }
                    return true;
                case R.id.favoritos:
                    preencherFavoritos();
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

        private void preencherFavoritos() {
            isFavorite = true;
            recyclerView.setVisibility(View.VISIBLE);
            adapterMovie.notifyItemRangeRemoved(0, adapterMovie.getItemCount());
            movieViewModel.getAll().observe(getActivity(), movies -> {
                adapterMovie.submitList(movies);
            });
        }

        private void carregarScroll() {
            int page = getPagePreference() + 1;
            savePreference(page);
            isFavorite = false;
            isAtualizarList = false;
            executeAsyc(View.INVISIBLE, page);
        }

    }


}
