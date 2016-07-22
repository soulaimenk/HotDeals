package com.soulaimenk.hotdeals.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.soulaimenk.hotdeals.R;

public class MainActivity extends FragmentActivity {
    private static final String TAG = "MainActivity";
    private Context context = this;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseAuth mAuth;
    private Button signOutBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_main);
        mAuth = FirebaseAuth.getInstance();
        signOutBtn = (Button) findViewById(R.id.signOut);
        signOutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
            }
        });
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                } else {
                    // User is signed out
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                    Intent authenticationIntent = new Intent(context, AuthenticationActivity.class);
                    startActivity(authenticationIntent);
                    finish();
                }
                // ...
            }
        };
    mAuth.addAuthStateListener(mAuthListener);
        super.onCreate(savedInstanceState);
    }
}
