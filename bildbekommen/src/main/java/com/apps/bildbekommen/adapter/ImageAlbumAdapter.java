package com.apps.bildbekommen.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.apps.bildbekommen.Bekommen;
import com.apps.bildbekommen.R;
import com.apps.bildbekommen.model.ImageAlbum;
import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class ImageAlbumAdapter extends RecyclerView.Adapter<ImageAlbumAdapter.ViewHolder>{
    private Context context;
    public ArrayList<ImageAlbum> album_list;
    public int index = -1;
    public int first_select = 0;

    public ImageAlbumAdapter() {
    }

    public ImageAlbumAdapter(Context context, ArrayList<ImageAlbum> album_list) {
        this.context = context;
        this.album_list = album_list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.album_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        holder.tv_title.setText(album_list.get(position).getTitle());
        Glide.with(context).load(album_list.get(position).getImages()).into(holder.iv_artwork);

        holder.iv_artwork.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                index = position;
                ((Bekommen)context).onAlbumListImageClick(position);
                notifyDataSetChanged();
                notifyItemRangeChanged(position, album_list.size());
            }
        });

        if (first_select == 0)
        {
            first_select = -1;
            ((Bekommen)context).onAlbumListImageClick(position);
            holder.selecttion.setVisibility(View.VISIBLE);
            holder.iv_selectedimg.setVisibility(View.VISIBLE);
            index = 0;
        }

        if (index == position)
        {
            holder.selecttion.setVisibility(View.VISIBLE);
            holder.iv_selectedimg.setVisibility(View.VISIBLE);
            index = -1;
        }
        else
        {
            holder.selecttion.setVisibility(View.GONE);
            holder.iv_selectedimg.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return album_list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        public TextView tv_title;
        public ImageView iv_artwork,iv_selectedimg;
        public RelativeLayout selecttion;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_title = itemView.findViewById(R.id.tv_title);
            selecttion = itemView.findViewById(R.id.selecttion);
            iv_artwork = itemView.findViewById(R.id.iv_artwork);
            iv_selectedimg = itemView.findViewById(R.id.iv_selectedimg);
        }
    }
}
