package br.com.valber.movie.adapter;

import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v7.recyclerview.extensions.ListAdapter;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import br.com.valber.movie.json.MovieVideoJSON;

public class AdapterTrailer extends ListAdapter<MovieVideoJSON, AdapterTrailer.TrailerViewHolder> {

    private ClickListen clickMovieListen;


    public AdapterTrailer() {
        super(DIFF_CALBACK);
    }

    private static final DiffUtil.ItemCallback<MovieVideoJSON> DIFF_CALBACK =
            new DiffUtil.ItemCallback<MovieVideoJSON>() {
                @Override
                public boolean areItemsTheSame(@NonNull MovieVideoJSON oldItem, @NonNull MovieVideoJSON newItem) {
                    return oldItem.getId() != newItem.getId();
                }

                @Override
                public boolean areContentsTheSame(@NonNull MovieVideoJSON oldItem, @NonNull MovieVideoJSON newItem) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                        return oldItem.equals(newItem);
                    }
                    return oldItem.getKey().equals(newItem.getKey());
                }
            };

    @NonNull
    @Override
    public TrailerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull TrailerViewHolder holder, int position) {

    }

    class TrailerViewHolder extends RecyclerView.ViewHolder {
        public TrailerViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }

    public interface ClickListen {
        void itemClick(Object obj);
    }
}
