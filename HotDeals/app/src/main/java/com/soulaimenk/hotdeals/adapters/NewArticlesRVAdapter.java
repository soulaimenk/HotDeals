package com.soulaimenk.hotdeals.adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
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
import com.soulaimenk.hotdeals.Constants;
import com.soulaimenk.hotdeals.R;
import com.soulaimenk.hotdeals.Utils;
import com.soulaimenk.hotdeals.singletons.GetFirebaseDataSingleton;
import com.soulaimenk.hotdeals.wrappers.Favorite;
import com.soulaimenk.hotdeals.wrappers.Like;
import com.soulaimenk.hotdeals.wrappers.NewArticle;
import com.soulaimenk.hotdeals.wrappers.UserType;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Soulaimen on 20/07/2016.
 */
public class NewArticlesRVAdapter extends RecyclerView.Adapter<NewArticlesRVAdapter.NewProductViewHolder> {
    Context context;
    FirebaseUser user;
    List<NewArticle> newProducts;
    UserType mUserType;
    List<String> mFavoriteList = new ArrayList<String>();
    List<String> mLikeList = new ArrayList<String>();
    private DatabaseReference mDatabase;

    public NewArticlesRVAdapter(List<NewArticle> newProducts, UserType userType, Context context) {
        this.context = context;
        this.mUserType = userType;
        this.newProducts = newProducts;
    }

    @Override
    public NewArticlesRVAdapter.NewProductViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.new_article_cardview, parent, false);
        NewProductViewHolder pvh = new NewProductViewHolder(v);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        user = FirebaseAuth.getInstance().getCurrentUser();
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
        return pvh;
    }

    @Override
    public void onBindViewHolder(final NewArticlesRVAdapter.NewProductViewHolder holder, final int position) {
        final int pos = position;
        if (mFavoriteList.contains(newProducts.get(position).getUid())) {
            holder.favoriteBtn.setTag(context.getString(R.string.selected));
            holder.favoriteBtn.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.favorite_on));
        } else {
            holder.favoriteBtn.setTag(context.getString(R.string.not_selected));
            holder.favoriteBtn.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.favorite_off));
        }

        if (mLikeList.contains(newProducts.get(position).getUid())) {
            holder.likeBtn.setTag(context.getString(R.string.selected));
            holder.likeBtn.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.like_on));
        } else {
            holder.likeBtn.setTag(context.getString(R.string.not_selected));
            holder.likeBtn.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.like_off));
        }


        //  holder.likeBtn.setTag(context.getString(R.string.not_selected));
        holder.newProductTitle.setText(newProducts.get(position).getTitle());
        holder.newProductDate.setText(Utils.FormatDate(newProducts.get(position).getDate()));
        holder.newProductLikesCount.setText(String.valueOf(newProducts.get(position).getLikes()));
        holder.coverImage.setImageBitmap(Utils.decodeBase64(newProducts.get(position).getImageBase64()));
        holder.favoriteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("CardViewAdapter", "Item Name: " + newProducts.get(pos).getTitle());
                //not selected
                if (holder.favoriteBtn.getTag().equals(context.getString(R.string.not_selected))) {
                    holder.favoriteBtn.setTag(context.getString(R.string.selected));
                    holder.favoriteBtn.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.favorite_on));
                    mDatabase.child(Constants.FB_USER_TYPE).child(user.getUid()).child(Constants.FB_FAVORITES).setPriority(newProducts.get(pos).getUid());
                    mDatabase.child(Constants.FB_USER_TYPE).child(user.getUid()).child(Constants.FB_FAVORITES).child(newProducts.get(pos).getUid()).setValue(new Favorite(Constants.TYPE_NEWS, newProducts.get(pos).getUid()));
                    mFavoriteList.add(newProducts.get(pos).getUid());
                } else if (holder.favoriteBtn.getTag().equals(context.getString(R.string.selected))) {
                    holder.favoriteBtn.setTag(context.getString(R.string.not_selected));
                    holder.favoriteBtn.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.favorite_off));
                    mDatabase.child(Constants.FB_USER_TYPE).child(user.getUid()).child(Constants.FB_FAVORITES).child(newProducts.get(pos).getUid()).removeValue();
                    mFavoriteList.remove(newProducts.get(pos).getUid());
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
                    mDatabase.child(Constants.FB_USER_TYPE).child(user.getUid()).child(Constants.FB_LIKES).setPriority(newProducts.get(pos).getUid());
                    mDatabase.child(Constants.FB_USER_TYPE).child(user.getUid()).child(Constants.FB_LIKES).child(newProducts.get(pos).getUid()).setValue(new Like(Constants.TYPE_NEWS, newProducts.get(pos).getUid()));
                    //Add like +1
                    newProducts.get(pos).setLikes(newProducts.get(pos).getLikes() + 1);
                    mDatabase.child(Constants.FB_NEW_ARTICLE).child(newProducts.get(pos).getUid()).child(Constants.FB_LIKES).setValue(newProducts.get(pos).getLikes());
                    holder.newProductLikesCount.setText(String.valueOf(newProducts.get(position).getLikes()));
                    mLikeList.add(newProducts.get(pos).getUid());
                } else {
                    holder.likeBtn.setTag(context.getString(R.string.not_selected));
                    holder.likeBtn.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.like_off));
                    mDatabase.child(Constants.FB_USER_TYPE).child(user.getUid()).child(Constants.FB_LIKES).child(newProducts.get(pos).getUid()).removeValue();
                    //remove like -1
                    newProducts.get(pos).setLikes(newProducts.get(pos).getLikes() - 1);
                    mDatabase.child(Constants.FB_NEW_ARTICLE).child(newProducts.get(pos).getUid()).child(Constants.FB_LIKES).setValue(newProducts.get(pos).getLikes());
                    holder.newProductLikesCount.setText(String.valueOf(newProducts.get(position).getLikes()));
                    mLikeList.remove(newProducts.get(pos).getUid());
                }
            }
        });

        holder.openInMapBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri gmmIntentUri = Uri.parse("geo:" + newProducts.get(pos).getLatitude() + "," + newProducts.get(pos).getLongitude() + "?q=" + newProducts.get(pos).getLatitude() + "," + newProducts.get(pos).getLongitude() + "( +" + newProducts.get(position).getTitle() + ")");
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
        Log.e("new Article Adapter", "getItemCount :" + GetFirebaseDataSingleton.getInstance().getNewArticles().size());
        return GetFirebaseDataSingleton.getInstance().getNewArticles().size();
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


}
