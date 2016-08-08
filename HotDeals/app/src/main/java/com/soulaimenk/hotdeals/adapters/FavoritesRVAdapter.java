package com.soulaimenk.hotdeals.adapters;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.lid.lib.LabelImageView;
import com.soulaimenk.hotdeals.R;

/**
 * Created by Soulaimen on 02/08/2016.
 */
public class FavoritesRVAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    public static class NewProductViewHolder extends RecyclerView.ViewHolder {
        CardView cv;
        TextView newProductTitle;
        TextView newProductDate;
        TextView newProductLikesCount;
        ImageView coverImage;
        ImageView favoriteBtn;
        ImageView likeBtn;
        ImageView openInMapBtn;


        NewProductViewHolder(View itemView) {
            super(itemView);
            cv = (CardView) itemView.findViewById(R.id.newArticleCV);
            newProductTitle = (TextView) itemView.findViewById(R.id.newArticleTitle);
            coverImage = (ImageView) itemView.findViewById(R.id.image_card_cover);
            newProductDate = (TextView) itemView.findViewById(R.id.newArticleDate);
            favoriteBtn = (ImageView) itemView.findViewById(R.id.image_action_favorite);
            likeBtn = (ImageView) itemView.findViewById(R.id.image_action_like);
            newProductLikesCount = (TextView) itemView.findViewById(R.id.newArticleLikesCount);
            openInMapBtn = (ImageView) itemView.findViewById(R.id.image_action_map);
        }
    }

    public static class DiscountArticleViewHolder extends RecyclerView.ViewHolder {
        CardView cv;
        TextView discountProductTitleTV;
        TextView countDownHoursTV;
        TextView countDownMinutesTV;
        TextView countDownSecondsTV;
        TextView discountProductLikesCount;
        LabelImageView coverImage;
        ImageView favoriteBtn;
        ImageView likeBtn;
        ImageView openInMapBtn;

        DiscountArticleViewHolder(View itemView) {
            super(itemView);
            cv = (CardView) itemView.findViewById(R.id.discountArticleCV);
            discountProductTitleTV = (TextView) itemView.findViewById(R.id.discountArticleTitle);
            coverImage = (LabelImageView) itemView.findViewById(R.id.image_card_cover);
            countDownHoursTV = (TextView) itemView.findViewById(R.id.countDownHours);
            countDownMinutesTV = (TextView) itemView.findViewById(R.id.countDownMinutes);
            countDownSecondsTV = (TextView) itemView.findViewById(R.id.countDownSeconds);
            favoriteBtn = (ImageView) itemView.findViewById(R.id.image_action_favorite);
            likeBtn = (ImageView) itemView.findViewById(R.id.image_action_like);
            discountProductLikesCount = (TextView) itemView.findViewById(R.id.discountArticleLikesCount);
            openInMapBtn = (ImageView) itemView.findViewById(R.id.image_action_map);
            Log.v("ViewHolder", "ViewHolder INIT");
        }
    }
}
