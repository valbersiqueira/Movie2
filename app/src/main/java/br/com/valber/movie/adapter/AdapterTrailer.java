package br.com.valber.movie.adapter;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v7.recyclerview.extensions.ListAdapter;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import br.com.valber.movie.BuildConfig;
import br.com.valber.movie.R;
import br.com.valber.movie.json.MovieVideoJSON;

public class AdapterTrailer extends ListAdapter<MovieVideoJSON, AdapterTrailer.TrailerViewHolder> {

    private Context context;

    public AdapterTrailer(Context context) {
        super(DIFF_CALLBACK);
        this.context = context;
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
        MovieVideoJSON video = getItem(position);
        holder.btn.setText(video.getName() + " - site: "+video.getSite());
    }

    class TrailerViewHolder extends RecyclerView.ViewHolder {
        Button btn;
        public TrailerViewHolder(@NonNull View itemView) {
            super(itemView);

            btn = itemView.findViewById(R.id.btn_trailer);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION){
                        MovieVideoJSON trailer =  getItem(position);
                        context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(BuildConfig.OPEN_URL_YOUTUBE_TWO+trailer.getKey())));
                    }
                }
            });
        }
    }
}
