package com.noon.a7pets.Productscategory;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.firebase.ui.database.SnapshotParser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.noon.a7pets.Cart;
import com.noon.a7pets.IndividualProduct;
import com.noon.a7pets.models.GenericProductModel;
import com.noon.a7pets.networksync.CheckInternetConnection;
import com.squareup.picasso.Picasso;

import com.noon.a7pets.R;
/**
 * Created by kshitij on 22/1/18.
 */

public class Dogs extends AppCompatActivity {


    //created for firebaseui android tutorial by Vamsi Tallapudi

    private RecyclerView mRecyclerView;
    private StaggeredGridLayoutManager mLayoutManager;
    private LottieAnimationView tv_no_item;
    private FirebaseRecyclerAdapter adapter;

    //Getting reference to Firebase Database
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference mDatabaseReference = database.getReference();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dogs);

//        Toolbar toolbar = findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
//
//
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        getSupportActionBar().setDisplayShowHomeEnabled(true);

        //check Internet Connection
        new CheckInternetConnection(this).checkConnection();


        //Initializing our Recyclerview
        mRecyclerView = findViewById(R.id.my_recycler_view);
        tv_no_item = findViewById(R.id.tv_no_cards);


        if (mRecyclerView != null) {
            //to enable optimization of recyclerview
            mRecyclerView.setHasFixedSize(true);
        }
        //using staggered grid pattern in recyclerview
        mLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setHasFixedSize(true);
        fetch();


//        //Say Hello to our new FirebaseUI android Element, i.e., FirebaseRecyclerAdapter
////        final FirebaseRecyclerAdapter<GenericProductModel, Cards.MovieViewHolder> adapter = new FirebaseRecyclerAdapter<GenericProductModel, Cards.MovieViewHolder>(
////                GenericProductModel.class,
////                R.layout.cards_cardview_layout,
////                Cards.MovieViewHolder.class,
////                //referencing the node where we want the database to store the data from our Object
////                mDatabaseReference.child("Products").child("Calendar").getRef()
////        ) {
////            @Override
////            protected void populateViewHolder(final Cards.MovieViewHolder viewHolder, final GenericProductModel model, final int position) {
////                if (tv_no_item.getVisibility() == View.VISIBLE) {
////                    tv_no_item.setVisibility(View.GONE);
////                }
////                viewHolder.cardname.setText(model.getCardname());
////                viewHolder.cardprice.setText("₹ " + Float.toString(model.getCardprice()));
////                Picasso.get().load(model.getCardimage()).into(viewHolder.cardimage);
////
////                viewHolder.mView.setOnClickListener(new View.OnClickListener() {
////                    @Override
////                    public void onClick(View v) {
////                        Intent intent = new Intent(Calendars.this, IndividualProduct.class);
////                        intent.putExtra("product", getItem(position));
////                        startActivity(intent);
////                    }
////                });
////            }
////        };

    }






    private void fetch() {
        Query query = FirebaseDatabase.getInstance()
                .getReference()
                .child("Dogs");

        FirebaseRecyclerOptions<GenericProductModel> options =
                new FirebaseRecyclerOptions.Builder<GenericProductModel>()
                        .setQuery(query, new SnapshotParser<GenericProductModel>() {
                            @NonNull
                            @Override
                            public GenericProductModel parseSnapshot(@NonNull DataSnapshot snapshot) {
                                return new GenericProductModel(
                                        snapshot.child("id").getValue().toString(),
                                        snapshot.child("name").getValue().toString(),
                                        snapshot.child("img").getValue().toString(),
                                        snapshot.child("description").getValue().toString(),
                                        snapshot.child("price").getValue().toString());
                            }
                        })
                        .build();

        adapter = new FirebaseRecyclerAdapter<GenericProductModel, ViewHolder>(options) {
            @Override
            public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.cards_cardview_layout, parent, false);

                return new ViewHolder(view);
            }


            @Override
            protected void onBindViewHolder(ViewHolder holder, final int position, GenericProductModel model) {
                if (tv_no_item.getVisibility() == View.VISIBLE) {
                    tv_no_item.setVisibility(View.GONE);
                }
                holder.cardname.setText(model.getCardname());
                holder.cardprice.setText("₹ " +(model.getCardprice()));
                Picasso.with(getApplicationContext()).load(model.getCardimage()).into(holder.cardimage);


                holder.cardView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(Dogs.this, IndividualProduct.class);
                        intent.putExtra("product", getItem(position));
                        startActivity(intent);
                    }
                });
            }

        };
        mRecyclerView.setAdapter(adapter);
    }


    public void viewCart(View view) {
        startActivity(new Intent(Dogs.this, Cart.class));
        finish();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView cardname;
        ImageView cardimage;
        TextView cardprice;
        CardView cardView;
        View mView;

        public ViewHolder(View v) {
            super(v);
            mView = v;
            cardView = v.findViewById(R.id.card_view_individual);
            cardname = v.findViewById(R.id.cardcategory);
            cardimage = v.findViewById(R.id.cardimage);
            cardprice = v.findViewById(R.id.cardprice);
        }
    }

    public void Notifications(View view) {
        //startActivity(new Intent(Dogs.this, NotificationActivity.class));
        finish();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();

        //check Internet Connection
        new CheckInternetConnection(this).checkConnection();
    }

    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }
}