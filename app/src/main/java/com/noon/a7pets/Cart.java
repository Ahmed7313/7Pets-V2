package com.noon.a7pets;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.firebase.ui.database.SnapshotParser;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.noon.a7pets.Productscategory.Dogs;
import com.noon.a7pets.models.GenericProductModel;
import com.noon.a7pets.models.SingleProductModel;
import com.noon.a7pets.networksync.CheckInternetConnection;
import com.noon.a7pets.usersession.UserSession;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

public class Cart extends AppCompatActivity {

    //to get user session data
    private UserSession session;
    private HashMap<String,String> user;
    private String name,email,photo,mobile;
    private RecyclerView mRecyclerView;
    private StaggeredGridLayoutManager mLayoutManager;

    private String  currentUserID;
    //Getting reference to Firebase Database
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference mDatabaseReference = database.getReference();
    private LottieAnimationView tv_no_item;
    private LinearLayout activitycartlist;
    private LottieAnimationView emptycart;

    private ArrayList<SingleProductModel> cartcollect;
    private String totalcost= "0";
    private String totalproducts = "0";


    public FirebaseRecyclerAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

//        Toolbar toolbar = findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
//        toolbar.setTitle("Cart");
//
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        getSupportActionBar().setDisplayShowHomeEnabled(true);


        //check Internet Connection
        new CheckInternetConnection(this).checkConnection();

        //retrieve session values and display on listviews
        getValues();

        //SharedPreference for Cart Value
        session = new UserSession(getApplicationContext());

        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        currentUserID = mAuth.getCurrentUser().getUid();

        //validating session
        session.isLoggedIn();

        mRecyclerView = findViewById(R.id.recyclerview);
        tv_no_item = findViewById(R.id.tv_no_cards);
        activitycartlist = findViewById(R.id.activity_cart_list);
        emptycart = findViewById(R.id.empty_cart);
        cartcollect = new ArrayList<>();

        if (mRecyclerView != null) {
            //to enable optimization of recyclerview
            mRecyclerView.setHasFixedSize(true);
        }
        //using staggered grid pattern in recyclerview
        mLayoutManager = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setHasFixedSize(true);
        fetch();

        if(session.getCartValue()>0) {
            fetch();
        }else if(session.getCartValue() == 0)  {
            tv_no_item.setVisibility(View.GONE);
            activitycartlist.setVisibility(View.GONE);
            emptycart.setVisibility(View.VISIBLE);
        }
    }

//    private void populateRecyclerView() {
//
//        //Say Hello to our new FirebaseUI android Element, i.e., FirebaseRecyclerAdapter
//        final FirebaseRecyclerAdapter<SingleProductModel,MovieViewHolder> adapter = new FirebaseRecyclerAdapter<SingleProductModel, MovieViewHolder>(
//                SingleProductModel.class,
//                R.layout.cart_item_layout,
//                MovieViewHolder.class,
//                //referencing the node where we want the database to store the data from our Object
//                mDatabaseReference.child("cart").child(mobile).getRef()
//        ) {
//            @Override
//            protected void populateViewHolder(final MovieViewHolder viewHolder, final SingleProductModel model, final int position) {
//                if(tv_no_item.getVisibility()== View.VISIBLE){
//                    tv_no_item.setVisibility(View.GONE);
//                }
//                viewHolder.cardname.setText(model.getPrname());
//                viewHolder.cardprice.setText("₹ "+model.getPrprice());
//                viewHolder.cardcount.setText("Quantity : "+model.getNo_of_items());
//                Picasso.with(Cart.this).load(model.getPrimage()).into(viewHolder.cardimage);
//
//                totalcost += model.getNo_of_items()*Float.parseFloat(model.getPrprice());
//                totalproducts += model.getNo_of_items();
//                cartcollect.add(model);
//
//                viewHolder.carddelete.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        Toast.makeText(Cart.this,getItem(position).getPrname(),Toast.LENGTH_SHORT).show();
//                        getRef(position).removeValue();
//                        session.decreaseCartValue();
//                        startActivity(new Intent(Cart.this,Cart.class));
//                        finish();
//                    }
//                });
//            }
//        };
//        mRecyclerView.setAdapter(adapter);
//    }

    private void fetch() {
        Query query = FirebaseDatabase.getInstance()
                .getReference()
                .child("Users")
                .child(currentUserID)
                .child("Cart");

        FirebaseRecyclerOptions<SingleProductModel> options =
                new FirebaseRecyclerOptions.Builder<SingleProductModel>()
                        .setQuery(query, new SnapshotParser<SingleProductModel>() {
                            @NonNull
                            @Override
                            public SingleProductModel parseSnapshot(@NonNull DataSnapshot snapshot) {
                                return new SingleProductModel(
                                        snapshot.child("prid").getValue().toString(),
                                        snapshot.child("no_of_items").getValue().toString(),
                                        snapshot.child("useremail").getValue().toString(),
                                        snapshot.child("usermobile").getValue().toString(),
                                        snapshot.child("prname").getValue().toString(),
                                        snapshot.child("prprice").getValue().toString(),
                                        snapshot.child("primage").getValue().toString(),
                                        snapshot.child("prdesc").getValue().toString(),
                                        snapshot.child("message_header").getValue().toString(),
                                        snapshot.child("message_body").getValue().toString());
                            }
                        })
                        .build();

        adapter = new FirebaseRecyclerAdapter<SingleProductModel, ViewHolder>(options) {
            @Override
            public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.cart_item_layout, parent, false);

                return new ViewHolder(view);
            }


            @Override
            protected void onBindViewHolder(ViewHolder holder, final int position, SingleProductModel model) {
                if(tv_no_item.getVisibility()== View.VISIBLE){
                    tv_no_item.setVisibility(View.GONE);
                }
                holder.cardname.setText(model.getPrname());
                holder.cardprice.setText("₹ "+model.getPrprice());
                holder.cardcount.setText("Quantity : "+model.getNo_of_items());
                Picasso.with(Cart.this).load(model.getPrimage()).into(holder.cardimage);

                //totalcost += String.valueOf(Float.parseFloat(model.getNo_of_items())*Float.parseFloat(model.getPrprice()));
                totalproducts += model.getNo_of_items();
                cartcollect.add(model);

                holder.carddelete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(Cart.this,getItem(position).getPrname(),Toast.LENGTH_SHORT).show();
                        getRef(position).removeValue();
                        session.decreaseCartValue();
                        startActivity(new Intent(Cart.this,Cart.class));
                        finish();
                    }
                });
            }

        };
        mRecyclerView.setAdapter(adapter);
    }



//    private void populateRecyclerView() {
//        FirebaseAuth mAuth = FirebaseAuth.getInstance();
//        currentUserID = mAuth.getCurrentUser().getUid();
//        Query query = FirebaseDatabase.getInstance()
//                .getReference().child("Users")
//                .child(currentUserID).child("cart");
//
//        FirebaseRecyclerOptions<SingleProductModel> options =
//                new FirebaseRecyclerOptions.Builder<SingleProductModel>()
//                        .setQuery(query, new SnapshotParser<SingleProductModel>() {
//                            @NonNull
//                            @Override
//                            public SingleProductModel parseSnapshot(@NonNull DataSnapshot snapshot) {
//                                return new SingleProductModel(
//                                        snapshot.child("prid").getValue().toString(),
//                                        snapshot.child("no_of_items").getValue().toString(),
//                                        snapshot.child("useremail").getValue().toString(),
//                                        snapshot.child("usermobile").getValue().toString(),
//                                        snapshot.child("prname").getValue().toString(),
//                                        snapshot.child("prprice").getValue().toString(),
//                                        snapshot.child("primage").getValue().toString(),
//                                        snapshot.child("prdesc").getValue().toString(),
//                                        snapshot.child("message_header").getValue().toString(),
//                                        snapshot.child("message_body").getValue().toString());
//                            }
//                        })
//                        .build();
//
//        adapter = new FirebaseRecyclerAdapter<SingleProductModel, Cart.ViewHolder>(options) {
//            @Override
//            public Cart.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//                View view = LayoutInflater.from(parent.getContext())
//                        .inflate(R.layout.cart_item_layout, parent, false);
//
//                return new ViewHolder(view);
//            }
//
//
//            @Override
//            protected void onBindViewHolder(Cart.ViewHolder holder, final int position, SingleProductModel model) {
//                if(tv_no_item.getVisibility()== View.VISIBLE){
//                    tv_no_item.setVisibility(View.GONE);
//                }
//                holder.cardname.setText(model.getPrname());
//                holder.cardprice.setText("₹ "+model.getPrprice());
//                holder.cardcount.setText("Quantity : "+model.getNo_of_items());
//                Picasso.with(getApplicationContext()).load(model.getPrimage()).into(holder.cardimage);
//
//                float prodPrice = Float.valueOf(model.getPrprice());
//                float no_items = Float.valueOf(model.getNo_of_items());
//                totalcost += no_items * prodPrice;
//                totalproducts += model.getNo_of_items();
//                cartcollect.add(model);
//
//                holder.carddelete.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        Toast.makeText(Cart.this,getItem(position).getPrname(),Toast.LENGTH_SHORT).show();
//                        getRef(position).removeValue();
//                        session.decreaseCartValue();
//                        startActivity(new Intent(Cart.this,Cart.class));
//                        finish();
//                    }
//                });
//            }
//
//        };
//        mRecyclerView.setAdapter(adapter);
//    }




    //viewHolder for our Firebase UI
    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView cardname;
        ImageView cardimage;
        TextView cardprice;
        TextView cardcount;
        ImageView carddelete;

        View mView;

        public ViewHolder(View v) {
            super(v);
            mView = v;
            cardname = v.findViewById(R.id.cart_prtitle);
            cardimage = v.findViewById(R.id.image_cartlist);
            cardprice = v.findViewById(R.id.cart_prprice);
            cardcount = v.findViewById(R.id.cart_prcount);
            carddelete = v.findViewById(R.id.deletecard);
        }


    }

    public void checkout(View view) {
        Intent intent = new Intent(Cart.this,OrderDetails.class);
        intent.putExtra("totalprice",String.valueOf(totalcost));
        intent.putExtra("totalproducts",totalproducts);
        intent.putExtra("cartproducts",cartcollect);
        startActivity(intent);
        finish();
    }

    //viewHolder for our Firebase UI
    public static class MovieViewHolder extends RecyclerView.ViewHolder{

        TextView cardname;
        ImageView cardimage;
        TextView cardprice;
        TextView cardcount;
        ImageView carddelete;

        View mView;
        public MovieViewHolder(View v) {
            super(v);
            mView = v;
            cardname = v.findViewById(R.id.cart_prtitle);
            cardimage = v.findViewById(R.id.image_cartlist);
            cardprice = v.findViewById(R.id.cart_prprice);
            cardcount = v.findViewById(R.id.cart_prcount);
            carddelete = v.findViewById(R.id.deletecard);
        }
    }


    private void getValues() {

        //create new session object by passing application context
        session = new UserSession(getApplicationContext());

        //validating session
        session.isLoggedIn();

        //get User details if logged in
        user = session.getUserDetails();

        name = user.get(UserSession.KEY_NAME);
        email = user.get(UserSession.KEY_EMAIL);
        mobile = user.get(UserSession.KEY_MOBiLE);
        photo = user.get(UserSession.KEY_PHOTO);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    public void viewProfile(View view) {
        //startActivity(new Intent(Cart.this,Profile.class));
        finish();
    }

    @Override
    protected void onResume() {
        super.onResume();

        //check Internet Connection
        new CheckInternetConnection(this).checkConnection();

    }

    public void Notifications(View view) {

       // startActivity(new Intent(Cart.this,NotificationActivity.class));
        finish();
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