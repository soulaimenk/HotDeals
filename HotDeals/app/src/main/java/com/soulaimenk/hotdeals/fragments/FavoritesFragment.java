package com.soulaimenk.hotdeals.fragments;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.soulaimenk.hotdeals.R;

/**
 * Created by Soulaimen on 19/07/2016.
 */
public class FavoritesFragment extends Fragment {
    private Context context;

    private View mView;
    private RecyclerView mFavoriteRV;
    private Button mSignOutBtn;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;

    public FavoritesFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this.getActivity().getBaseContext();
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.tab_news, container, false);
        this.mView = view;
        //Get Views
        mFavoriteRV = (RecyclerView) mView.findViewById(R.id.favoriteRV);

        LinearLayoutManager llm = new LinearLayoutManager(context);
//        mFavoriteRV.setLayoutManager(llm);

      //  FavoritesRVAdapter favoritesRVAdapter = new FavoritesRVAdapter();
     //   mFavoriteRV.setAdapter(favoritesRVAdapter);
        return view;
    }
}
