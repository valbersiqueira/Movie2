package br.com.valber.movie;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import br.com.valber.movie.adapter.AdapterReviews;
import br.com.valber.movie.adapter.AdapterTrailer;
import br.com.valber.movie.asyncs.AsyncReviews;
import br.com.valber.movie.asyncs.AsyncVideo;
import br.com.valber.movie.database.movie.MovieViewModel;
import br.com.valber.movie.entity.Movie;
import br.com.valber.movie.json.MovieVideoJSON;
import br.com.valber.movie.json.ResultReviewsJSON;
import br.com.valber.movie.json.ResultVideoJSON;
import br.com.valber.movie.utils.ResultAsync;
import br.com.valber.movie.utils.ResultReviewsAsync;
import br.com.valber.movie.utils.SendObjeto;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class DetalheMovieActivity extends AppCompatActivity {

    private static Movie movie;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalhe_movie);

        movie = getIntent().getExtras().getParcelable(SendObjeto.SEND_MOVIE);

        getSupportFragmentManager().beginTransaction()
                .add(R.id.container_detail, new DetailFragment())
                .commit();

    }

    public static class DetailFragment extends Fragment implements ResultAsync, ResultReviewsAsync {

        private View view;
        private Unbinder unbinder;
        private Menu menu;
        private MovieViewModel movieViewModel;
        private Boolean isMovieSave = false;
        private AdapterTrailer adapterTrailer;
        private AdapterReviews adapterReviews;
        private LinearLayoutManager layoutManager;
        private LinearLayoutManager layoutManagerReviews;

        @BindView(R.id.txt_title_dtl)
        TextView title;

        @BindView(R.id.txt_descricao_dt)
        TextView descricao;

        @BindView(R.id.txt_vote_dt)
        TextView votos;

        @BindView(R.id.img_detail)
        ImageView imagem;

        @BindView(R.id.img_card_dt)
        ImageView imgCard;

        @BindView(R.id.recycle_trailers)
        RecyclerView recyclerView;

        @BindView(R.id.recy_reviews)
        RecyclerView recyclerViewReviews;


        @Nullable
        @Override
        public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            view = inflater.inflate(R.layout.detail_fragment, container, false);
            unbinder = ButterKnife.bind(this, view);
            setHasOptionsMenu(true);

            Picasso.get().load(BuildConfig.OPEN_URL_IMAGEN + movie.getBackdropPath()).into(imagem);
            Picasso.get().load(BuildConfig.OPEN_URL_IMAGEN + movie.getPosterPath()).into(imgCard);
            title.setText(movie.getTitle());
            descricao.setText(movie.getOverview());
            votos.setText(movie.getVoteCount()+"");


            this.startRecyTrailer();
            this.startRecyReviews();

            adapterTrailer.setOnclickListen(new AdapterTrailer.ClickMovieListen() {
                @Override
                public void itemClickListen(MovieVideoJSON trailer) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(BuildConfig.OPEN_URL_YOUTUBE_TWO+trailer.getKey())));
                }
            });


            new AsyncVideo(this).execute(movie.getId());
            new AsyncReviews(this).execute(movie.getId());

            movieViewModel = ViewModelProviders.of(getActivity()).get(MovieViewModel.class);
            movieViewModel.getMovie(movie).observe(getActivity(), movie1 -> {
                if (movie1 != null) {
                    isMovieSave = true;
                } else {
                    isMovieSave = false;
            }
            });
            return view;
        }

        @Override
        public void onDestroyView() {
            super.onDestroyView();
            unbinder.unbind();
        }

        @Override
        public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
            getActivity().getMenuInflater().inflate(R.menu.detalhe, menu);
            this.menu = menu;
            if (isMovieSave)
                changeMenu(0);
        }

        @Override
        public boolean onOptionsItemSelected(MenuItem item) {
            switch (item.getItemId()) {
                case android.R.id.home:
                    getActivity().finish();
                    return true;
                case R.id.no_save_heart:
                    if (!isMovieSave) {
                        movieViewModel.save(movie);
                    }
                    changeMenu(0);
                    return true;
                case R.id.save_heart:
                    movieViewModel.delete(movie);
                    changeMenu(1);
                    return true;
                default:
                    return true;
            }
        }

        private void changeMenu(int value){
            if (value == 0) {
                menu.getItem(0).setVisible(false);
                menu.getItem(1).setVisible(true);
            } else {
                menu.getItem(0).setVisible(true);
                menu.getItem(1).setVisible(false);
            }
        }

        @Override
        public void resultMovie(Object object, String consultar) {
            if (object != null) {
                ResultVideoJSON result = (ResultVideoJSON) object;
                List<MovieVideoJSON> resultTrailes = result.getMovieVideoJSONS();
                if (!resultTrailes.isEmpty()) {
                    adapterTrailer.submitList(resultTrailes);
                }
            }
        }

        private void startRecyReviews() {
            layoutManagerReviews = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
            recyclerViewReviews.setLayoutManager(layoutManagerReviews);
            recyclerViewReviews.setNestedScrollingEnabled(false);
            recyclerViewReviews.setHasFixedSize(false);
            adapterReviews = new AdapterReviews();
            recyclerViewReviews.setAdapter(adapterReviews);
        }

        private void startRecyTrailer() {
            layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
            recyclerView.setLayoutManager(layoutManager);
            recyclerView.setNestedScrollingEnabled(false);
            recyclerView.setHasFixedSize(false);
            adapterTrailer = new AdapterTrailer();
            recyclerView.setAdapter(adapterTrailer);
        }

        @Override
        public void resultReviews(Object object) {
            ResultReviewsJSON resultReviews = (ResultReviewsJSON) object;
            adapterReviews.submitList(resultReviews.getReviewsJSONS());
        }
    }

}
