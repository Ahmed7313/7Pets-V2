package com.noon.a7pets;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import butterknife.BindView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;
import com.google.firebase.database.DatabaseReference;
import com.noon.a7pets.networksync.CheckInternetConnection;
import com.noon.a7pets.usersession.UserSession;

public class IndividualProduct extends AppCompatActivity {


    private ImageView productImage;
    private TextView productPrice;
    private TextView productName;
    private String usermobile, useremail;

    private int quantity = 1;
    private UserSession session;
    private DatabaseReference mDatabaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_individual_product);

        Intent intent = getIntent();
        String productNameI = intent.getStringExtra("name");
        String productPriceI = intent.getStringExtra("price");
        int productImgI = intent.getIntExtra("img", 0);

        //check Internet Connection
        new CheckInternetConnection(this).checkConnection();

        productImage = findViewById(R.id.productimage);
        productName = findViewById(R.id.productname);
        productPrice = findViewById(R.id.productprice);
//        Toolbar toolbar = findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        getSupportActionBar().setDisplayShowHomeEnabled(true);

        productImage.setImageResource(productImgI);
        productName.setText(productNameI);
        productPrice.setText(productPriceI);


    }
}
