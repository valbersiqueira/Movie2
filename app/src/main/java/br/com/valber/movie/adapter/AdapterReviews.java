package br.com.valber.movie.adapter;

import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.recyclerview.extensions.ListAdapter;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import br.com.valber.movie.R;
import br.com.valber.movie.json.ReviewsJSON;

public class AdapterReviews extends ListAdapter<ReviewsJSON, AdapterReviews.MyViewHoler>{

    public AdapterReviews() {
        super(DIFF_CALLBACK);
    }

    @NonNull
    @Override
    public MyViewHoler onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_review, parent, false);
        return new MyViewHoler(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHoler holder, int position) {
        ReviewsJSON review = getItem(position);
        holder.author.setText(review.getAuthor());
        holder.comentario.setText(review.getContent());
    }

    static final DiffUtil.ItemCallback<ReviewsJSON> DIFF_CALLBACK =
            new DiffUtil.ItemCallback<ReviewsJSON>() {
                @Override
                public boolean areItemsTheSame(@NonNull ReviewsJSON oldItem, @NonNull ReviewsJSON newItem) {
                    return oldItem.getId() != newItem.getId();
                }

                @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                @Override
                public boolean areContentsTheSame(@NonNull ReviewsJSON oldItem, @NonNull ReviewsJSON newItem) {
                    return oldItem.equals(newItem.getId());
                }
            };

    class MyViewHoler extends RecyclerView.ViewHolder {
        TextView author, comentario;
        public MyViewHoler(@NonNull View itemView) {
            super(itemView);
            author = itemView.findViewById(R.id.autor_rvw_text);
            comentario = itemView.findViewById(R.id.comentario_rvw_text);
        }
    }
}
