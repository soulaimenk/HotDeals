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
import com.soulaimenk.hotdeals.Constants;
import com.soulaimenk.hotdeals.R;
import com.soulaimenk.hotdeals.adapters.NewProductsRVAdapter;
import com.soulaimenk.hotdeals.wrappers.NewProduct;

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

    public NewsFragment() {
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
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.tab_news, container, false);
        this.mView = view;
        //Get Views
        mNewsRV = (RecyclerView) mView.findViewById(R.id.newProductsRV);
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

        final List<NewProduct> newProducts = new ArrayList<>();
        final NewProductsRVAdapter adapter = new NewProductsRVAdapter(newProducts, context);
        mNewsRV.setAdapter(adapter);


        //newProduct Listener
        mDatabase.child(Constants.FB_NEW_PRODUCT).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                NewProduct newProduct = dataSnapshot.getValue(NewProduct.class);
                newProducts.add(newProduct);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                NewProduct newProduct = dataSnapshot.getValue(NewProduct.class);
                newProducts.remove(newProduct);
                adapter.notifyItemRemoved(newProducts.indexOf(newProduct));
                Toast.makeText(context, "Child Removed", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        //  newProducts.add(new NewProduct("Magasin ABC", "Description Description Description ", "15/12/2015", Constants.LATITUDE_TEST, Constants.LONGITUDE_TEST, Constants.IMAGE_BASE64_TEST));
        //  newProducts.add(new NewProduct("Magasin ABCD", "Description Description Description ", "15/12/2015", Constants.LATITUDE_TEST, Constants.LONGITUDE_TEST, Constants.IMAGE_BASE64_TEST));
        //  newProducts.add(new NewProduct("Magasin ABC", "Description Description Description ", "15/12/2015", Constants.LATITUDE_TEST, Constants.LONGITUDE_TEST, Constants.IMAGE_BASE64_TEST));


        return mView;
    }
}
