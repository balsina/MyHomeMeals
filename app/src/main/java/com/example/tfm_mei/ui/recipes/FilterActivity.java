package com.example.tfm_mei.ui.recipes;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.example.tfm_mei.MainActivity;
import com.example.tfm_mei.R;

public class FilterActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    public static final String PARAMETER = "parameter";
    public static final String TYPE = "type";
    EditText search_recipe, search_by_ingredient;
    Button btn_search_recipe, btn_search_by_ingredient, btn_search_by_area, btn_category_vegan, btn_category_vegetarian;
    Spinner spinner_area;
    Object itemSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter);

        if (getSupportActionBar() != null) getSupportActionBar().hide();

        search_recipe = findViewById(R.id.edit_text_search_recipe);
        btn_search_recipe = findViewById(R.id.btn_search);
        search_by_ingredient = findViewById(R.id.edit_text_search_by_ingredient);
        btn_search_by_ingredient = findViewById(R.id.btn_search_by_ingredient);
        btn_search_by_area = findViewById(R.id.btn_search_by_area);
        spinner_area = findViewById(R.id.spinner_area);
        btn_category_vegan = findViewById(R.id.btn_category_vegan);
        btn_category_vegetarian = findViewById(R.id.btn_category_vegetarian);

        btn_search_recipe.setOnClickListener(v -> {
            String etSearch = search_recipe.getText().toString().trim();
            if (!etSearch.isEmpty()) {
                Intent intent = new Intent(FilterActivity.this, RecipeActivity.class);
                intent.putExtra(TYPE, "search");
                intent.putExtra(PARAMETER, etSearch);
                startActivity(intent);
            }
        });

        btn_search_by_ingredient.setOnClickListener(v -> {
            String etSearchByIngredient = search_by_ingredient.getText().toString().trim();
            if (!etSearchByIngredient.isEmpty()) {
                Intent intent = new Intent(FilterActivity.this, MainActivity.class);
                intent.putExtra(TYPE, "ingredient");
                intent.putExtra(PARAMETER, etSearchByIngredient);
                startActivity(intent);
            }
        });

        btn_category_vegan.setOnClickListener(v -> {
            Intent intent = new Intent(FilterActivity.this, MainActivity.class);
            intent.putExtra(TYPE, "category");
            intent.putExtra(PARAMETER, "Vegan");
            startActivity(intent);
        });

        btn_category_vegetarian.setOnClickListener(v -> {
            Intent intent = new Intent(FilterActivity.this, MainActivity.class);
            intent.putExtra(TYPE, "category");
            intent.putExtra(PARAMETER, "Vegetarian");
            startActivity(intent);
        });

        btn_search_by_area.setOnClickListener(v -> {
            String category = itemSpinner.toString();
            if (!category.isEmpty()) {
                Intent intent = new Intent(FilterActivity.this, MainActivity.class);
                intent.putExtra(TYPE, "area");
                intent.putExtra(PARAMETER, category);
                startActivity(intent);
            }
        });


        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.area_spinner_values, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_area.setAdapter(adapter);

        spinner_area.setOnItemSelectedListener(this);

    }

    public void onItemSelected(AdapterView<?> parent, View view,
                               int pos, long id) {
        this.itemSpinner = parent.getItemAtPosition(pos);
    }

    public void onNothingSelected(AdapterView<?> parent) {
    }

}