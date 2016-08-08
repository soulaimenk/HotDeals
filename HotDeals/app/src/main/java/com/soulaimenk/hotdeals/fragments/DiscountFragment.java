package com.soulaimenk.hotdeals.fragments;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;
import com.soulaimenk.hotdeals.Constants;
import com.soulaimenk.hotdeals.R;
import com.soulaimenk.hotdeals.adapters.DiscountArticlesRVAdapter;
import com.soulaimenk.hotdeals.wrappers.DiscountArticle;
import com.soulaimenk.hotdeals.wrappers.UserType;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Soulaimen on 19/07/2016.
 */
public class DiscountFragment extends Fragment {
    private Context context;
    private View mView;
    private RecyclerView mDiscountRV;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    private UserType mUserType;

    public DiscountFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this.getActivity().getBaseContext();
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        Bundle userTypeBundle = this.getArguments();
        if (userTypeBundle != null) {
            mUserType = new Gson().fromJson(userTypeBundle.getString(Constants.USER_TYPE_TAG), UserType.class);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.tab_discount, container, false);
        this.mView = view;
        //Get Views
        mDiscountRV = (RecyclerView) mView.findViewById(R.id.discountRV);

        // mNewsRV.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(context);
        mDiscountRV.setLayoutManager(llm);

        final List<DiscountArticle> discountArticles = new ArrayList<>();
        final DiscountArticlesRVAdapter adapter = new DiscountArticlesRVAdapter(discountArticles, mUserType, context);
        mDiscountRV.setAdapter(adapter);
        Log.e("zzz","zzz");

        //FireBase: newProduct Listener
        mDatabase.child(Constants.FB_DISCOUNT_ARTICLE).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                DiscountArticle discountArticle = dataSnapshot.getValue(DiscountArticle.class);
                discountArticles.add(discountArticle);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                DiscountArticle discountArticle = dataSnapshot.getValue(DiscountArticle.class);
                discountArticles.remove(discountArticle);
                adapter.notifyItemRemoved(discountArticles.indexOf(discountArticle));
                Toast.makeText(context, "Child Removed", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        return mView;
    }
}
