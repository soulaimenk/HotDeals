package com.soulaimenk.hotdeals.adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.soulaimenk.hotdeals.R;
import com.soulaimenk.hotdeals.Utils;
import com.soulaimenk.hotdeals.wrappers.NewProduct;

import java.util.List;

/**
 * Created by Soulaimen on 20/07/2016.
 */
public class NewProductsRVAdapter extends RecyclerView.Adapter<NewProductsRVAdapter.PersonViewHolder> {
    Context context;


    public static class PersonViewHolder extends RecyclerView.ViewHolder {
        CardView cv;
        TextView NewProductTitle;
        TextView NewProductDate;
        ImageView coverImage;
        ImageView favoriteBtn;
        ImageView likeBtn;
        ImageView openInMapBtn;

        PersonViewHolder(View itemView) {
            super(itemView);
            cv = (CardView) itemView.findViewById(R.id.newProductCV);
            NewProductTitle = (TextView) itemView.findViewById(R.id.newProductTitle);
            coverImage = (ImageView) itemView.findViewById(R.id.image_card_cover);
            NewProductDate = (TextView) itemView.findViewById(R.id.newProductDate);
            favoriteBtn = (ImageView) itemView.findViewById(R.id.image_action_favorite);
            likeBtn = (ImageView) itemView.findViewById(R.id.image_action_like);
            openInMapBtn = (ImageView) itemView.findViewById(R.id.image_action_map);
        }
    }

    List<NewProduct> newProducts;

    public NewProductsRVAdapter(List<NewProduct> newProducts, Context context) {
        this.context = context;
        this.newProducts = newProducts;
    }

    @Override
    public NewProductsRVAdapter.PersonViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.news_cardview, parent, false);
        PersonViewHolder pvh = new PersonViewHolder(v);
        return pvh;
    }

    @Override
    public void onBindViewHolder(final NewProductsRVAdapter.PersonViewHolder holder, final int position) {
        final int pos = position;
        holder.NewProductTitle.setText(newProducts.get(position).getTitle());
        holder.NewProductDate.setText(newProducts.get(position).getDate());
        holder.coverImage.setImageBitmap(Utils.decodeBase64(newProducts.get(position).getImageBase64()));
        holder.favoriteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("CardViewAdapter", "Item Name: " + newProducts.get(pos).getTitle());
            }
        });

        holder.likeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("Context ", context.getPackageName());
                holder.likeBtn.setImageDrawable(context.getDrawable(R.drawable.like_on));
            }
        });

        holder.openInMapBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri gmmIntentUri = Uri.parse("geo:" + newProducts.get(pos).getLatitude() + "," + newProducts.get(pos).getLongitude() + "?q=" + newProducts.get(pos).getLatitude() + "," + newProducts.get(pos).getLongitude() + "( +" + newProducts.get(position).getTitle()+")");
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                mapIntent.setPackage("com.google.android.apps.maps");
                if (mapIntent.resolveActivity(context.getPackageManager()) != null) {
                    v.getContext().startActivity(mapIntent);
                } else {
                    Toast.makeText(context, "You should install Google Maps", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public int getItemCount() {
        return newProducts.size();
    }


}
