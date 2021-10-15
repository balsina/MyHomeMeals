package com.example.tfm_mei.ui.recipes;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.tfm_mei.R;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView;

public class RecipeActivity extends AppCompatActivity {

    public TextView nombre, area, instrucciones;
    public ImageView foto;
    public YouTubePlayerView video;
    public RecyclerView recyclerView;
    public ProgressDialog progressDialog;

    String type, parameter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipectivity);

        if (getSupportActionBar() != null) getSupportActionBar().hide();

        Intent i = getIntent();
        type = i.getStringExtra("type");
        parameter = i.getStringExtra("parameter");

        recyclerView = (RecyclerView) findViewById(R.id.ingredient_list);

        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Loading...");
        progressDialog.show();

        new FetchInformationRecipe(RecipeActivity.this).execute(type, parameter);
    }


}