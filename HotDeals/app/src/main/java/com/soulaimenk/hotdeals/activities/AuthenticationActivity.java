package com.soulaimenk.hotdeals.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.soulaimenk.hotdeals.Constants;
import com.soulaimenk.hotdeals.R;
import com.soulaimenk.hotdeals.wrappers.UserType;

/**
 * Created by Soulaimen on 18/07/2016.
 */
public class AuthenticationActivity extends Activity implements View.OnClickListener, GoogleApiClient.OnConnectionFailedListener {
    private static final String TAG = "AuthentificationAct";
    private static final int RC_SIGN_IN = 9001;
    private GoogleApiClient mGoogleApiClient;
    private Context context = this;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;
    private Button mSignInBtn;
    private Button mGoogleSignUpBtn;
    private Button mSignUpBtn;
    private TextView mEmailTV;
    private TextView mPasswordTV;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authentication);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();

        mEmailTV = (TextView) findViewById(R.id.email);
        mPasswordTV = (TextView) findViewById(R.id.password);
        mSignInBtn = (Button) findViewById(R.id.signin);
        mSignInBtn.setOnClickListener(this);
        mGoogleSignUpBtn = (Button) findViewById(R.id.googleSignup);
        mGoogleSignUpBtn.setOnClickListener(this);
        mSignUpBtn = (Button) findViewById(R.id.signup);
        mSignUpBtn.setOnClickListener(this);
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                    RedirectUser(mAuth);
                } else {
                    // User is signed out
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                }
                // ...
            }
        };
        // ...
    }

    private void RedirectUser(FirebaseAuth mAuth) {
        Toast.makeText(AuthenticationActivity.this, mAuth.getCurrentUser().getUid(), Toast.LENGTH_SHORT).show();
        Log.e(TAG, "UserType class:" + (mAuth.getCurrentUser().getUid()));
        if (mAuth.getCurrentUser() != null) {
            mDatabase.child(Constants.FB_USER_TYPE).child(mAuth.getCurrentUser().getUid()).addListenerForSingleValueEvent(
                    new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            UserType userType = dataSnapshot.getValue(UserType.class);
                            if (userType != null) {
                                Log.e(TAG, "UserType class:" + userType.toString());
                                Toast.makeText(AuthenticationActivity.this, "User: " + userType.getUid() + "Type: " + userType.getType(), Toast.LENGTH_SHORT).show();
                                switch (userType.getType()) {
                                    case Constants.TYPE_USER:
                                        Intent userMainActivityIntent = new Intent(context, UserMainActivity.class);
                                        userMainActivityIntent.putExtra(Constants.USER_TYPE_TAG, Constants.TYPE_USER);
                                        startActivity(userMainActivityIntent);
                                        finish();
                                        break;
                                    case Constants.TYPE_SHOP:
                                        Intent shopMainActivityIntent = new Intent(context, ShopMainActivity.class);
                                        shopMainActivityIntent.putExtra(Constants.USER_TYPE_TAG, Constants.TYPE_SHOP);
                                        startActivity(shopMainActivityIntent);
                                        finish();
                                        break;
                                }

                            } else {
                               /* Intent authenticationActivityIntent = new Intent(context, AuthenticationActivity.class);
                                startActivity(authenticationActivityIntent);
                                finish();*/
                                Toast.makeText(AuthenticationActivity.this, "Authentication : UserType Null", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            Log.w(TAG, "getUser:onCancelled", databaseError.toException());
                        }
                    });
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.signin) {
            String emailStr = mEmailTV.getText().toString();
            String passwordStr = mPasswordTV.getText().toString();
            if (!emailStr.isEmpty() && !passwordStr.isEmpty()) {
                mAuth.signInWithEmailAndPassword(emailStr.trim(), passwordStr.trim())
                        .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                Log.d(TAG, "signInWithEmail:onComplete:" + task.isSuccessful());

                                if (!task.isSuccessful()) {
                                    Log.w(TAG, "signInWithEmail", task.getException());
                                    Toast.makeText(context, "Authentication failed.",
                                            Toast.LENGTH_SHORT).show();
                                } else {
                                    RedirectUser(mAuth);
                                }
                            }
                        });
            } else {
                //Empty fields
            }
        }

        if (v.getId() == R.id.signup) {
            Intent signUpIntent = new Intent(this, SignUpActivity.class);
            startActivity(signUpIntent);
            finish();
        }

        if (v.getId() == R.id.googleSignup) {
            GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                    .requestIdToken(getString(R.string.default_web_client_id))
                    .requestEmail()
                    .build();

            Toast.makeText(AuthenticationActivity.this, getString(R.string.default_web_client_id), Toast.LENGTH_LONG).show();
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    /*Enable Manage*/
                    .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                    .build();

            Toast.makeText(AuthenticationActivity.this, mGoogleApiClient.isConnected() + " " + mGoogleApiClient.isConnecting(), Toast.LENGTH_SHORT).show();
            mAuth = FirebaseAuth.getInstance();

            Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
            startActivityForResult(signInIntent, RC_SIGN_IN);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {

            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if (result.isSuccess()) {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = result.getSignInAccount();
                firebaseAuthWithGoogle(account);
            } else {

                // Google Sign In failed, update UI appropriately
                // ...
                Toast.makeText(context, result.getStatus().toString(), Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        Log.d(TAG, "firebaseAuthWithGoogle:" + acct.getId());

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, "signInWithCredential:onComplete:" + task.isSuccessful());
                        if (!task.isSuccessful()) {
                            Log.w(TAG, "signInWithCredential", task.getException());
                            Toast.makeText(context, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                        // ...
                    }
                });
    }
}
