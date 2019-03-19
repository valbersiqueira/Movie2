package br.com.valber.movie;

import android.arch.lifecycle.ViewModelProviders;
import android.os.AsyncTask;
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
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.squareup.picasso.Picasso;

import br.com.valber.movie.adapter.AdapterTrailer;
import br.com.valber.movie.database.movie.MovieViewModel;
import br.com.valber.movie.entity.Movie;
import br.com.valber.movie.json.JsonConverterVideo;
import br.com.valber.movie.json.MoviesService;
import br.com.valber.movie.json.ResultVideo;
import br.com.valber.movie.utils.SendObjeto;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

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

    public static class DetailFragment extends Fragment {

        private View view;
        private Unbinder unbinder;
        private Menu menu;
        private MovieViewModel movieViewModel;
        private Boolean isMovieSave = false;
        private AdapterTrailer adapterTrailer;
        private LinearLayoutManager layoutManager;

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
        private RecyclerView recyclerView;

        @Nullable
        @Override
        public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            view = inflater.inflate(R.layout.detail_fragment, container, false);

            unbinder = ButterKnife.bind(this, view);

            Picasso.get().load(BuildConfig.OPEN_URL_IMAGEN + movie.getBackdropPath()).into(imagem);
            Picasso.get().load(BuildConfig.OPEN_URL_IMAGEN + movie.getPosterPath()).into(imgCard);
            title.setText(movie.getTitle());
            descricao.setText(movie.getOverview());
            votos.setText(movie.getVoteCount()+"");
            setHasOptionsMenu(true);
            movieViewModel = ViewModelProviders.of(getActivity()).get(MovieViewModel.class);
            movieViewModel.getMovie(movie).observe(getActivity(), movie1 -> {
                if (movie1 != null) {
                    isMovieSave = true;
                    Log.d("TESTE", "passou aqui");
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

    }

}
