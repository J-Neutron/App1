package com.apps.bildbekommen.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.apps.bildbekommen.Bekommen;
import com.apps.bildbekommen.R;
import com.bumptech.glide.Glide;

import java.util.ArrayList;

import static com.apps.bildbekommen.bildbekommen.Bildbekommen.main_list;

public class SelectedImageListAdapter extends RecyclerView.Adapter<SelectedImageListAdapter.ViewHolder>{
    private Context context;
    private ArrayList<String> list;

    public SelectedImageListAdapter() {
    }

    public SelectedImageListAdapter(Context context, ArrayList<String> list) {
        this.context = context;
        this.list = list;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.selected_image_layout,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        Glide.with(context).load(list.get(position)).into(holder.iv_artwork);
        holder.checked.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (main_list.size() != 0) {
                    if (main_list.contains(list.get(position))) {
                        main_list.remove(list.get(position));
                        ((Bekommen)context).deleteImage(position);
                        notifyDataSetChanged();
                    }
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private LinearLayout checked;
        private ImageView iv_artwork;
        private CardView iv_image_open;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            iv_image_open = itemView.findViewById(R.id.iv_image_open);
            iv_artwork = itemView.findViewById(R.id.iv_artwork);
            checked = itemView.findViewById(R.id.checked);
        }
    }
}

