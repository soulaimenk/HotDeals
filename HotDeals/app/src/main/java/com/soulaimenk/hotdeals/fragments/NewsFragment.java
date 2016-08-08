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
import com.soulaimenk.hotdeals.adapters.NewArticlesRVAdapter;
import com.soulaimenk.hotdeals.wrappers.NewArticle;
import com.soulaimenk.hotdeals.wrappers.UserType;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Soulaimen on 19/07/2016.
 */
public class NewsFragment extends Fragment {

    private Context context;
    private View mView;
    private RecyclerView mNewsRV;
    private Button mSignOutBtn;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    private UserType mUserType;

    public NewsFragment() {
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
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.tab_news, container, false);
        this.mView = view;
        //Get Views
        mNewsRV = (RecyclerView) mView.findViewById(R.id.newArticlesRV);
        mSignOutBtn = (Button) mView.findViewById(R.id.signOut);

        //SignOut Btn action
        mSignOutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Toast.makeText(context, "Sign out", Toast.LENGTH_SHORT).show();
            }
        });
        // mNewsRV.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(context);
        mNewsRV.setLayoutManager(llm);

        final List<NewArticle> newArticles = new ArrayList<>();
        final NewArticlesRVAdapter adapter = new NewArticlesRVAdapter(newArticles, mUserType, context);
        mNewsRV.setAdapter(adapter);


        //FireBase: newProduct Listener
        mDatabase.child(Constants.FB_NEW_ARTICLE).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                NewArticle newArticle = dataSnapshot.getValue(NewArticle.class);
                newArticles.add(newArticle);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                NewArticle newProduct = dataSnapshot.getValue(NewArticle.class);
                newArticles.remove(newProduct);
                adapter.notifyItemRemoved(newArticles.indexOf(newProduct));
                Toast.makeText(context, "Child Removed", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        //  newArticles.add(new NewArticle("Magasin ABC", "Description Description Description ", "15/12/2015", Constants.LATITUDE_TEST, Constants.LONGITUDE_TEST, Constants.IMAGE_BASE64_TEST));
        //  newArticles.add(new NewArticle("Magasin ABCD", "Description Description Description ", "15/12/2015", Constants.LATITUDE_TEST, Constants.LONGITUDE_TEST, Constants.IMAGE_BASE64_TEST));
        //  newArticles.add(new NewArticle("Magasin ABC", "Description Description Description ", "15/12/2015", Constants.LATITUDE_TEST, Constants.LONGITUDE_TEST, Constants.IMAGE_BASE64_TEST));


        return mView;
    }
}
