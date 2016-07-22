package com.soulaimenk.hotdeals.fragments;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.soulaimenk.hotdeals.Constants;
import com.soulaimenk.hotdeals.R;
import com.soulaimenk.hotdeals.wrappers.NewProduct;

/**
 * Created by Soulaimen on 22/07/2016.
 */
public class AddNewsFragment extends Fragment {
    private Context context;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    //Views
    private Button mSignOutBtn;
    private Button mSaveBtn;


    public AddNewsFragment() {
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
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.tab_add_news, container, false);
        // Get views
        mSignOutBtn = (Button) view.findViewById(R.id.signOut);
        mSaveBtn = (Button) view.findViewById(R.id.save);

        // SignOut Btn action
        mSignOutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Toast.makeText(context, "Sign out", Toast.LENGTH_SHORT).show();
            }
        });

        // SaveBtn action
        mSaveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NewProduct newProduct = new NewProduct("Mag AZERTY", "Description  ", "18/05/2016", Constants.LATITUDE_TEST, Constants.LONGITUDE_TEST, Constants.IMAGE_BASE64_TEST);
                String key = mDatabase.child(Constants.FB_NEW_PRODUCT).push().getKey();
                newProduct.setUid(key);
                mDatabase.child(Constants.FB_NEW_PRODUCT).child(key).setValue(newProduct);
            }
        });
        return view;
    }
}
