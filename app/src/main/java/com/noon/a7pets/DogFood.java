package com.noon.a7pets;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.os.Bundle;

import com.noon.a7pets.Products;
import com.noon.a7pets.ProductsAdapter;
import com.noon.a7pets.R;

import java.util.ArrayList;
import java.util.List;

public class DogFood extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ProductsAdapter adapter;
    private StaggeredGridLayoutManager manager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dog_food);

        recyclerView = findViewById(R.id.main_content_recycle);
        manager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(manager);

        List<Products> products = new ArrayList<>();
        products.add(new Products(R.drawable.pet1, "Popcata", "15"));
        products.add(new Products(R.drawable.pet2, "batasd", "15"));
        products.add(new Products(R.drawable.pet3, "asdj hasdj aslk", "15"));
        products.add(new Products(R.drawable.pet4, "anihasdas", "20"));
        products.add(new Products(R.drawable.pet5, "sdfsdfsdc", "36"));
        products.add(new Products(R.drawable.pet6, "asdasdas", "12"));
        products.add(new Products(R.drawable.pet7, "Mansgasd", "22"));
        products.add(new Products(R.drawable.pet8, "Rastasda", "25"));
        products.add(new Products(R.drawable.pet9, "Popcata", "65"));

        adapter = new ProductsAdapter(this, products);
        recyclerView.setAdapter(adapter);

    }
}