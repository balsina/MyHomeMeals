package com.example.tfm_mei.ui.scannedProduct;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.tfm_mei.MainActivity;
import com.example.tfm_mei.R;

public class ScannedProduct extends AppCompatActivity {

    TextView sproduct, smarca, snota, singredientes, sgrasa, ssal, sazucar, sgrasasat, senergy;
    ImageView simagen, sicon;
    String bcode;

    public ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scanned_product);

        if (getSupportActionBar() != null) getSupportActionBar().hide();

        Intent i = getIntent();
        bcode = i.getStringExtra("Barcode");

        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Loading...");
        progressDialog.show();

        new FetchInformationScann(this).execute(bcode);

    }
}