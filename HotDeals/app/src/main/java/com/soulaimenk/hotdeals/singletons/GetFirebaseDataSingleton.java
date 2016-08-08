package com.soulaimenk.hotdeals.singletons;

import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.soulaimenk.hotdeals.Constants;
import com.soulaimenk.hotdeals.wrappers.NewArticle;
import com.soulaimenk.hotdeals.wrappers.UserType;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Soulaimen on 05/08/2016.
 */
public class GetFirebaseDataSingleton {

    private static GetFirebaseDataSingleton ourInstance = new GetFirebaseDataSingleton();
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    private UserType mUserType;
    private List<NewArticle> newArticles = new ArrayList<>();

    private GetFirebaseDataSingleton() {
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.child(Constants.FB_NEW_ARTICLE).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                NewArticle newArticle = dataSnapshot.getValue(NewArticle.class);
                newArticles.add(newArticle);
                Log.e("Singleton", newArticle.getTitle());
                // adapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                NewArticle newProduct = dataSnapshot.getValue(NewArticle.class);
                newArticles.remove(newProduct);
                Log.e("Singleton", "Removed");
                //  adapter.notifyItemRemoved(newArticles.indexOf(newProduct));
                //  Toast.makeText(context, "Child Removed", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public static GetFirebaseDataSingleton getInstance() {
        return ourInstance;
    }

    public List<NewArticle> getNewArticles() {
        return newArticles;
    }

    public void setNewArticles(List<NewArticle> newArticles) {
        this.newArticles = newArticles;
    }
}
