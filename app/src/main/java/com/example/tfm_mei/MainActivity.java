package com.example.tfm_mei;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.ashokvarma.bottomnavigation.BottomNavigationBar;
import com.ashokvarma.bottomnavigation.BottomNavigationItem;
import com.example.tfm_mei.ui.recipes.FilterActivity;
import com.example.tfm_mei.ui.recipelist.RecipeListFragment;
import com.example.tfm_mei.ui.scannedProduct.ScannedProduct;
import com.example.tfm_mei.ui.scanner.Scanner;
import com.example.tfm_mei.ui.auth.Profile;
import com.example.tfm_mei.ui.shoppingList.ShoppingList;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

public class MainActivity extends AppCompatActivity {

    String parameter, type;

    public String bcode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (getSupportActionBar() != null) getSupportActionBar().hide();

        Intent intent = getIntent();

        openFragment (new RecipeListFragment(intent.getStringExtra(FilterActivity.TYPE), intent.getStringExtra(FilterActivity.PARAMETER)));

        BottomNavigationBar bottomNavigationBar = findViewById(R.id.bottom_navigation_bar);

        bottomNavigationBar
                .addItem(new BottomNavigationItem(R.drawable.ic_chef__1_, "Recipes"))
                .addItem(new BottomNavigationItem(R.drawable.ic_outline_shopping_cart_24, "Shopping lists"))
                .addItem(new BottomNavigationItem(R.drawable.ic_barcode__1_, "Scanner"))
                .addItem(new BottomNavigationItem(R.drawable.ic_account_circle_black_24dp, "Profile"))
                .setFirstSelectedPosition(0)
                .setInActiveColor("#FF000000")
                .setBarBackgroundColor("#FF000000")
                .setActiveColor("#9ccc64")
                .initialise();

        bottomNavigationBar.setTabSelectedListener(new BottomNavigationBar.OnTabSelectedListener(){
            @Override
            public void onTabSelected(int position) {
                switch (position){
                    case 0:
                        openFragment (new RecipeListFragment(type, parameter));
                        break;
                    case 1:
                        openFragment(new ShoppingList());
                        break;
                    case 2:
                        openFragment(new Scanner());
                        break;
                    default:
                        openFragment(new Profile());
                        break;
                }
            }
            @Override
            public void onTabUnselected(int position) {
            }
            @Override
            public void onTabReselected(int position) {
            }
        });
    }

    public void openFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    public void scanCode(){
        IntentIntegrator integrator = new IntentIntegrator(MainActivity.this);
        integrator
                .setOrientationLocked(true)
                .setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES)
                .setBeepEnabled(true)
                .initiateScan();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if(result != null) {
            if(result.getContents() != null) {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage(result.getContents());
                builder.setTitle("Scan result");
                builder.setMessage("The product has been correctly scanned");
                bcode = result.getContents();

                builder.setPositiveButton("Scan Again", (dialog, which) -> scanCode());
                builder.setNeutralButton("Finish", (dialog, which) -> dialog.dismiss());
                builder.setNegativeButton("Show info", (dialog, which) -> {
                    Intent i = new Intent(MainActivity.this, ScannedProduct.class);
                    i.putExtra("Barcode", bcode);
                    startActivity(i);
                });

                AlertDialog dialog = builder.create();
                dialog.show();
            } else {
                Toast.makeText(this, "No results: ", Toast.LENGTH_LONG).show();

            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    public void filter(View view) {
        Intent i = new Intent(MainActivity.this, FilterActivity.class);
        startActivity(i);

    }
}