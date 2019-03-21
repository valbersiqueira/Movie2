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
import android.widget.TextView;

import br.com.valber.movie.R;
import br.com.valber.movie.json.MovieVideoJSON;

public class AdapterTrailer extends ListAdapter<MovieVideoJSON, AdapterTrailer.TrailerViewHolder> {

    private ClickMovieListen clickMovieListen;

    public AdapterTrailer() {
        super(DIFF_CALLBACK);
    }

    public static final DiffUtil.ItemCallback<MovieVideoJSON>  DIFF_CALLBACK =
            new DiffUtil.ItemCallback<MovieVideoJSON>() {
                @Override
                public boolean areItemsTheSame(@NonNull MovieVideoJSON oldItem, @NonNull MovieVideoJSON newItem) {
                    return oldItem.getId() != newItem.getId();
                }

                @TargetApi(Build.VERSION_CODES.KITKAT)
                @Override
                public boolean areContentsTheSame(@NonNull MovieVideoJSON oldItem, @NonNull MovieVideoJSON newItem) {
                    return oldItem.equals(newItem);
                }
            };

    @NonNull
    @Override
    public TrailerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.iten_trailer, parent, false);
        return new TrailerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TrailerViewHolder holder, int position) {
        int valor = position + 1;
        holder.nameTrailer.setText("Trailer "+valor);
    }

    class TrailerViewHolder extends RecyclerView.ViewHolder {
        TextView nameTrailer;
        public TrailerViewHolder(@NonNull View itemView) {
            super(itemView);
            nameTrailer = itemView.findViewById(R.id.trailer_name_txt);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
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

        void itemClickListen(MovieVideoJSON movie);
    }
}
