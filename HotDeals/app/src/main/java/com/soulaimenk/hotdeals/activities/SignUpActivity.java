package com.soulaimenk.hotdeals.activities;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.soulaimenk.hotdeals.Constants;
import com.soulaimenk.hotdeals.R;
import com.soulaimenk.hotdeals.wrappers.UserType;

/**
 * Created by Soulaimen on 18/07/2016.
 */
public class SignUpActivity extends Activity implements View.OnClickListener {
    private static final String TAG = "SignUpAct";
    private Context context;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    private Button mSignUpBtn;
    private ImageButton mUserTypeBtn;
    private ImageButton mShopTypeBtn;
    private String mEmailStr;
    private String mPasswordStr;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        context = this;
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mSignUpBtn = (Button) findViewById(R.id.signup);
        mSignUpBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.signup) {
            mEmailStr = ((EditText) findViewById(R.id.email)).getText().toString();
            mPasswordStr = ((EditText) findViewById(R.id.password)).getText().toString();
            Log.i(TAG, "Email: " + mEmailStr + " Password: " + mPasswordStr);
            if (!mEmailStr.equals("") && !mPasswordStr.equals("")) {
                mAuth.createUserWithEmailAndPassword(mEmailStr.trim(), mPasswordStr.trim())
                        .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                Log.d(TAG, "createUserWithEmail:onComplete:" + task.isSuccessful());
                                if (!task.isSuccessful()) {
                                    Toast.makeText(context, "Authentication failed.",
                                            Toast.LENGTH_SHORT).show();
                                } else {
                                    SignInNewUser(mEmailStr.trim(), mPasswordStr.trim());
                                }
                            }
                        });
            }
        }
        if (v.getId() == R.id.userType) {
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            if (user != null) {
                Toast.makeText(SignUpActivity.this, TAG + " UserUid: " + user.getUid(), Toast.LENGTH_SHORT).show();
                UserType userType = new UserType(user.getUid(), Constants.TYPE_USER);
                mDatabase.child(Constants.FB_USER_TYPE).child(user.getUid()).setValue(userType);
                Intent userMainActivityIntent = new Intent(context, UserMainActivity.class);
                userMainActivityIntent.putExtra(Constants.USER_TYPE_TAG, Constants.TYPE_USER);
                startActivity(userMainActivityIntent);
                finish();
            }
        }
        if (v.getId() == R.id.shopType) {
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            if (user != null) {
                Toast.makeText(SignUpActivity.this, TAG + " UserUid: " + user.getUid(), Toast.LENGTH_SHORT).show();
                UserType userType = new UserType(user.getUid(), Constants.TYPE_SHOP);
                mDatabase.child(Constants.FB_USER_TYPE).child(user.getUid()).setValue(userType);
                Intent shopMainActivityIntent = new Intent(context, ShopMainActivity.class);
                shopMainActivityIntent.putExtra(Constants.USER_TYPE_TAG, Constants.TYPE_SHOP);
                startActivity(shopMainActivityIntent);
                finish();
            }
        }
    }

    private void SignInNewUser(String mEmailStr, String mPasswordStr) {
        mAuth.signInWithEmailAndPassword(mEmailStr, mPasswordStr)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, "signInWithEmail:onComplete:" + task.isSuccessful());

                        if (!task.isSuccessful()) {
                            Log.w(TAG, "signInWithEmail", task.getException());
                            Toast.makeText(context, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        } else if (task.isSuccessful()) {
                            ChooseTypeOfUser();
                        }
                        // ...
                    }
                });
    }

    private void ChooseTypeOfUser() {
        Toast.makeText(SignUpActivity.this, "Choosing User Type", Toast.LENGTH_SHORT).show();
        Dialog typeChooserDialog = new Dialog(this);
        typeChooserDialog.setCancelable(false);
        typeChooserDialog.setContentView(R.layout.dialog_signup_user_type);
        mUserTypeBtn = (ImageButton) typeChooserDialog.findViewById(R.id.userType);
        mShopTypeBtn = (ImageButton) typeChooserDialog.findViewById(R.id.shopType);

        mUserTypeBtn.setOnClickListener(this);
        mShopTypeBtn.setOnClickListener(this);

        typeChooserDialog.show();
    }


}
