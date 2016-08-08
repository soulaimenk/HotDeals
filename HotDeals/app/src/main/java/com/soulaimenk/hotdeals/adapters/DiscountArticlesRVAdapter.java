package com.soulaimenk.hotdeals.adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.CountDownTimer;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.lid.lib.LabelImageView;
import com.soulaimenk.hotdeals.Constants;
import com.soulaimenk.hotdeals.R;
import com.soulaimenk.hotdeals.Utils;
import com.soulaimenk.hotdeals.wrappers.DiscountArticle;
import com.soulaimenk.hotdeals.wrappers.Favorite;
import com.soulaimenk.hotdeals.wrappers.Like;
import com.soulaimenk.hotdeals.wrappers.UserType;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by Soulaimen on 02/08/2016.
 */
public class DiscountArticlesRVAdapter extends RecyclerView.Adapter<DiscountArticlesRVAdapter.DiscountArticleViewHolder> {
    Context context;
    String TAG = "DiscountArticlesRVAdapter";
    FirebaseUser user;
    List<DiscountArticle> discountArticles;
    UserType mUserType;
    private List<String> mFavoriteList = new ArrayList<String>();
    private List<String> mLikeList = new ArrayList<String>();
    private DatabaseReference mDatabase;
    //countdown
    private CountDownTimer countDownTimer;
    private long timeInMillis;
    private long hours;
    private long minutes;
    private long secondes;

    public DiscountArticlesRVAdapter(List<DiscountArticle> discountArticles, UserType userType, Context context) {
        this.context = context;
        this.mUserType = userType;
        this.discountArticles = discountArticles;
    }

    @Override
    public DiscountArticlesRVAdapter.DiscountArticleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.discount_article_cardview, parent, false);
        DiscountArticleViewHolder discountArticleViewHolder = new DiscountArticleViewHolder(v);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        user = FirebaseAuth.getInstance().getCurrentUser();
        Log.v(TAG, "onCreateViewHolder");
        if (mUserType.getFavorites() != null) {
            for (Favorite favorite : mUserType.getFavorites().values()) {
                mFavoriteList.add(favorite.getUid());
            }
        }
        if (mUserType.getLikes() != null) {
            for (Like like : mUserType.getLikes().values()) {
                mLikeList.add(like.getUid());
            }
        }
        return discountArticleViewHolder;
    }

    @Override
    public void onBindViewHolder(final DiscountArticlesRVAdapter.DiscountArticleViewHolder holder,int position) {
        Log.e("onBindViewHolder","getAdapterPosition : "+holder.getAdapterPosition());
        Log.e("onBindViewHolder","Position : "+position);
        Log.v(TAG, "onBindViewHolder");
        if (mFavoriteList.contains(discountArticles.get(position).getUid())) {
            holder.favoriteBtn.setTag(context.getString(R.string.selected));
            holder.favoriteBtn.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.favorite_on));
        } else {
            holder.favoriteBtn.setTag(context.getString(R.string.not_selected));
            holder.favoriteBtn.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.favorite_off));
        }

        if (mLikeList.contains(discountArticles.get(position).getUid())) {
            holder.likeBtn.setTag(context.getString(R.string.selected));
            holder.likeBtn.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.like_on));
        } else {
            holder.likeBtn.setTag(context.getString(R.string.not_selected));
            holder.likeBtn.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.like_off));
        }

        //CountDown
        timeInMillis = Utils.GetRemainigTimeInMillis(discountArticles.get(position).getDate(), discountArticles.get(position).getPeriodInHours());
        countDownTimer = new CountDownTimer(timeInMillis, 1000) {
            public void onTick(long millisUntilFinished) {

                // Toast.makeText(context, "seconds remaining: " + millisUntilFinished / 1000, Toast.LENGTH_SHORT).show();

                hours = TimeUnit.MILLISECONDS.toHours(millisUntilFinished);
                holder.countDownHoursTV.setText(String.valueOf(hours));

                minutes = TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millisUntilFinished));
                holder.countDownMinutesTV.setText(String.valueOf(minutes));

                secondes = TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) -
                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished));
                holder.countDownSecondsTV.setText(String.valueOf(secondes));
            }

            public void onFinish() {
            }

        }.start();

        holder.discountProductTitleTV.setText(discountArticles.get(position).getTitle());
        holder.discountProductLikesCount.setText(String.valueOf(discountArticles.get(position).getLikes()));
        holder.coverImage.setImageBitmap(Utils.decodeBase64(discountArticles.get(position).getImageBase64()));
        holder.coverImage.setLabelText("- " + discountArticles.get(position).getPercentage() + " %");
        holder.coverImage.setLabelBackgroundColor(ContextCompat.getColor(context, (Utils.GetColorPerPercentage(discountArticles.get(position).getPercentage()))));
        holder.favoriteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("CardViewAdapter", "Item Name: " + discountArticles.get(holder.getAdapterPosition()).getTitle());
                //not selected
                if (holder.favoriteBtn.getTag().equals(context.getString(R.string.not_selected))) {
                    holder.favoriteBtn.setTag(context.getString(R.string.selected));
                    holder.favoriteBtn.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.favorite_on));
                    mDatabase.child(Constants.FB_USER_TYPE).child(user.getUid()).child(Constants.FB_FAVORITES).setPriority(discountArticles.get(holder.getAdapterPosition()).getUid());
                    mDatabase.child(Constants.FB_USER_TYPE).child(user.getUid()).child(Constants.FB_FAVORITES).child(discountArticles.get(holder.getAdapterPosition()).getUid()).setValue(new Favorite(Constants.TYPE_DISCOUNTS, discountArticles.get(holder.getAdapterPosition()).getUid()));
                    mFavoriteList.add(discountArticles.get(holder.getAdapterPosition()).getUid());

                } else if (holder.favoriteBtn.getTag().equals(context.getString(R.string.selected))) {
                    holder.favoriteBtn.setTag(context.getString(R.string.not_selected));
                    holder.favoriteBtn.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.favorite_off));
                    mDatabase.child(Constants.FB_USER_TYPE).child(user.getUid()).child(Constants.FB_FAVORITES).child(discountArticles.get(holder.getAdapterPosition()).getUid()).removeValue();
                    mFavoriteList.remove(discountArticles.get(holder.getAdapterPosition()).getUid());

                }
            }
        });

        holder.likeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("Context ", context.getPackageName());
                if (holder.likeBtn.getTag().equals(context.getString(R.string.not_selected))) {
                    holder.likeBtn.setTag(context.getString(R.string.selected));
                    holder.likeBtn.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.like_on));
                    mDatabase.child(Constants.FB_USER_TYPE).child(user.getUid()).child(Constants.FB_LIKES).setPriority(discountArticles.get(holder.getAdapterPosition()).getUid());
                    mDatabase.child(Constants.FB_USER_TYPE).child(user.getUid()).child(Constants.FB_LIKES).child(discountArticles.get(holder.getAdapterPosition()).getUid()).setValue(new Like(Constants.TYPE_DISCOUNTS, discountArticles.get(holder.getAdapterPosition()).getUid()));
                    //Add like +1
                    discountArticles.get(holder.getAdapterPosition()).setLikes(discountArticles.get(holder.getAdapterPosition()).getLikes() + 1);
                    mDatabase.child(Constants.FB_DISCOUNT_ARTICLE).child(discountArticles.get(holder.getAdapterPosition()).getUid()).child(Constants.FB_LIKES).setValue(discountArticles.get(holder.getAdapterPosition()).getLikes());
                    holder.discountProductLikesCount.setText(String.valueOf(discountArticles.get(holder.getAdapterPosition()).getLikes()));
                    mLikeList.add(discountArticles.get(holder.getAdapterPosition()).getUid());
                } else {
                    holder.likeBtn.setTag(context.getString(R.string.not_selected));
                    holder.likeBtn.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.like_off));
                    mDatabase.child(Constants.FB_USER_TYPE).child(user.getUid()).child(Constants.FB_LIKES).child(discountArticles.get(holder.getAdapterPosition()).getUid()).removeValue();
                    //remove like -1
                    discountArticles.get(holder.getAdapterPosition()).setLikes(discountArticles.get(holder.getAdapterPosition()).getLikes() - 1);
                    mDatabase.child(Constants.FB_DISCOUNT_ARTICLE).child(discountArticles.get(holder.getAdapterPosition()).getUid()).child(Constants.FB_LIKES).setValue(discountArticles.get(holder.getAdapterPosition()).getLikes());
                    holder.discountProductLikesCount.setText(String.valueOf(discountArticles.get(holder.getAdapterPosition()).getLikes()));
                    mLikeList.remove(discountArticles.get(holder.getAdapterPosition()).getUid());
                }
            }
        });

        holder.openInMapBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri gmmIntentUri = Uri.parse("geo:" + discountArticles.get(holder.getAdapterPosition()).getLatitude() + "," + discountArticles.get(holder.getAdapterPosition()).getLongitude() + "?q=" + discountArticles.get(holder.getAdapterPosition()).getLatitude() + "," + discountArticles.get(holder.getAdapterPosition()).getLongitude() + "( +" + discountArticles.get(holder.getAdapterPosition()).getTitle() + ")");
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
        return discountArticles.size();
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