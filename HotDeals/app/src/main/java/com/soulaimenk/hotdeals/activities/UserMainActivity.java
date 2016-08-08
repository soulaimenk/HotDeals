package com.soulaimenk.hotdeals.activities;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.gson.Gson;
import com.soulaimenk.hotdeals.Constants;
import com.soulaimenk.hotdeals.R;
import com.soulaimenk.hotdeals.adapters.ViewPagerAdapter;
import com.soulaimenk.hotdeals.fragments.DiscountFragment;
import com.soulaimenk.hotdeals.fragments.FavoritesFragment;
import com.soulaimenk.hotdeals.fragments.NewsFragment;
import com.soulaimenk.hotdeals.wrappers.UserType;

/**
 * Created by Soulaimen on 19/07/2016.
 */
public class UserMainActivity extends AppCompatActivity {
    private static final String TAG = "UserMainActivity";
    //UserType
    UserType mUserType;
    private Context context = this;
    //FireBase Auth
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseAuth mAuth;
    //ViewPager
    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private int[] tabIcons = {
            R.mipmap.ic_launcher,
            R.mipmap.ic_launcher,
            R.mipmap.ic_launcher
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_user);
        //Get bundle userType
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            Log.e(TAG, "User Json: " + extras.getString(Constants.USER_TYPE_TAG));
            mUserType = new Gson().fromJson(extras.getString(Constants.USER_TYPE_TAG), UserType.class);
        }

        //Firebase Auth listener
        mAuth = FirebaseAuth.getInstance();
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
                   // Intent authenticationIntent = new Intent(context, AuthenticationActivity.class);
                  //  startActivity(authenticationIntent);
                    finish();
                }
                // ...
            }
        };
        mAuth.addAuthStateListener(mAuthListener);

        //Set ViewPager
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
        setupTabIcons();
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        Bundle userTypeBundle = new Bundle();
        userTypeBundle.putString(Constants.USER_TYPE_TAG, new Gson().toJson(mUserType));

        Fragment newsFragment = new NewsFragment();
        Fragment discountFragment = new DiscountFragment();
        Fragment favoritesFragment = new FavoritesFragment();

        discountFragment.setArguments(userTypeBundle);
        favoritesFragment.setArguments(userTypeBundle);
        newsFragment.setArguments(userTypeBundle);

        viewPagerAdapter.addFragment(discountFragment, "Discounts");
        viewPagerAdapter.addFragment(newsFragment, "News");
        viewPagerAdapter.addFragment(favoritesFragment, "Favorites");

        viewPager.setAdapter(viewPagerAdapter);
    }

    private void setupTabIcons() {
        tabLayout.getTabAt(0).setIcon(tabIcons[0]);
        tabLayout.getTabAt(1).setIcon(tabIcons[1]);
        tabLayout.getTabAt(2).setIcon(tabIcons[2]);
    }

}
