package br.com.valber.movie.adapter;

import android.annotation.TargetApi;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v7.recyclerview.extensions.ListAdapter;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import br.com.valber.movie.BuildConfig;
import br.com.valber.movie.R;
import br.com.valber.movie.entity.Movie;


public class AdapterMovie extends ListAdapter<Movie, AdapterMovie.MyViewHoler> {

    private ClickMovieListen clickMovieListen;

    public AdapterMovie() {
        super(DIFF_CALLBACK);
    }

    public static final DiffUtil.ItemCallback<Movie>  DIFF_CALLBACK =
           new DiffUtil.ItemCallback<Movie>() {
               @Override
               public boolean areItemsTheSame(@NonNull Movie oldItem, @NonNull Movie newItem) {
                   return oldItem.getId() != newItem.getId();
               }

               @TargetApi(Build.VERSION_CODES.KITKAT)
               @Override
               public boolean areContentsTheSame(@NonNull Movie oldItem, @NonNull Movie newItem) {
                   return oldItem.equals(newItem);
               }
           };

    @NonNull
    @Override
    public MyViewHoler onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_movie_recy, parent, false);
        return new MyViewHoler(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHoler holder, int position) {
        final Movie movie = getItem(position);
        Picasso.get().load(BuildConfig.OPEN_URL_IMAGEN + movie.getPosterPath()).into(holder.img_movie);
        holder.btn_note.setText(String.valueOf(movie.getVoteAverage()));
    }


    public Movie getMovie(int positoon){
        return getItem(positoon);
    }

    public class MyViewHoler extends RecyclerView.ViewHolder{
        ImageView img_movie;
        Button btn_note;
        public MyViewHoler(@NonNull View item) {
            super(item);
            img_movie = item.findViewById(R.id.img_movie);
            btn_note = item.findViewById(R.id.btn_note);

            item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    if (clickMovieListen != null && position != RecyclerView.NO_POSITION){
                        clickMovieListen.itemClickListen(getItem(position));
                    }
                }
            });
        }
    }

    public void setOnclickListen(ClickMovieListen onclickListen){
        this.clickMovieListen = onclickListen;
    }

    public interface ClickMovieListen{

        void itemClickListen(Movie movie);
    }
}
