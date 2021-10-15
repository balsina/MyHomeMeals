package com.example.tfm_mei.ui.recipelist;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import com.example.tfm_mei.R;
import com.example.tfm_mei.ui.recipes.FilterActivity;

public class RecipeListFragment extends Fragment {

    String type, parameter;
    public RecyclerView recyclerView;
    public ProgressDialog progressDialog;
    public TextView title;

    private static final String ARG_COLUMN_COUNT = "column-count";

    private int mColumnCount = 1;

    public RecipeListFragment(String type, String parameter) {
        this.type = type;
        this.parameter = parameter;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.filter:
                Intent intent = new Intent(getContext(), FilterActivity.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        progressDialog = new ProgressDialog(getActivity());
        setHasOptionsMenu(true);

        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_recipe_list, container, false);
        addData(v);
        return v;
    }

    private void addData(View v) {

        Context context = v.getContext();
        recyclerView = v.findViewById(R.id.recipes_list);
        if (mColumnCount <= 1) {
            recyclerView.setLayoutManager(new LinearLayoutManager(context));
        } else {
            recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
        }

        progressDialog.setTitle("Loading...");
        progressDialog.show();

        title = v.findViewById(R.id.recetas_resultado_busqueda);

        new FetchInformationRecipeList(this).execute(type, parameter);

    }

}

