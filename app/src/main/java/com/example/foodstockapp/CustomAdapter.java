package com.example.foodstockapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.CustomViewHolder> implements View.OnClickListener{

    Context context;
    List<ItemTienda> items;
    private View.OnClickListener listener;

    public CustomAdapter(Context context, List<ItemTienda> items) {
        this.context = context;
        this.items = items;
    }

    @NonNull
    @Override
    public CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_view,parent,false);

        view.setOnClickListener(this);
        return new CustomViewHolder(view);
        //
    }

    @Override
    public void onBindViewHolder(@NonNull  CustomViewHolder holder, int position) {
        ItemTienda item = items.get(position);

        holder.emailView.setText(items.get(position).getNombre());
        Glide.with(context).load(item.getUrlImage()).into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void setOnClickListener(View.OnClickListener listener){
        this.listener=listener;
    }

    @Override
    public void onClick(View view) {
        if (listener!=null){
            listener.onClick(view);
        }
    }
    public class CustomViewHolder extends RecyclerView.ViewHolder {

        ImageView imageView;

        TextView emailView;

        public CustomViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageview);

            emailView = itemView.findViewById(R.id.email);
        }
    }
}
