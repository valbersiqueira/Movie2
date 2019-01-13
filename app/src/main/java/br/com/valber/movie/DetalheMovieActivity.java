package br.com.valber.movie;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import br.com.valber.movie.entity.Movie;
import br.com.valber.movie.utuls.SendObjeto;
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

    public static class DetailFragment extends Fragment {

        private View view;
        private Unbinder unbinder;

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

        @Nullable
        @Override
        public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            view = inflater.inflate(R.layout.detail_fragment, container, false);

            unbinder = ButterKnife.bind(this, view);

            Picasso.get().load(BuildConfig.OPEN_URL_IMAGEN+movie.getBackdropPath()).into(imagem);
            Picasso.get().load(BuildConfig.OPEN_URL_IMAGEN+movie.getPosterPath()).into(imgCard);
            title.setText(movie.getTitle());
            descricao.setText(movie.getOverview());
            votos.setText(movie.getVoteCount()+"");

            return view;
        }

        @Override
        public void onDestroyView() {
            super.onDestroyView();
            unbinder.unbind();
        }


    }
}
