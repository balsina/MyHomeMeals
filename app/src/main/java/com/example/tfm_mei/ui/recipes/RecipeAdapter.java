package com.example.tfm_mei.ui.recipes;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.tfm_mei.R;

import java.util.List;

public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.ViewHolder> {
    private List<Ingrediente> mIngredients;

    public RecipeAdapter(List<Ingrediente> myDataset) {
        mIngredients = myDataset;
    }

    @Override
    public RecipeAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemLayoutView = LayoutInflater.from(parent.getContext()).inflate
                (R.layout.item_lista_ingrediente, parent, false);
        itemLayoutView.setLayoutParams(new RecyclerView.LayoutParams(
                RecyclerView.LayoutParams.MATCH_PARENT,
                RecyclerView.LayoutParams.WRAP_CONTENT
        ));
        return new ViewHolder(itemLayoutView);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mIngredients.get(position);
        holder.ingredientName.setText(mIngredients.get(position).name);
        holder.ingredientQant.setText(mIngredients.get(position).quantity);
    }

    @Override
    public int getItemCount() {
        return mIngredients.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView ingredientName;
        public TextView ingredientQant;
        public Ingrediente mItem;

        public ViewHolder(View itemLayoutView) {
            super(itemLayoutView);
            ingredientName = itemLayoutView.findViewById(R.id.ingredient_name);
            ingredientQant = itemLayoutView.findViewById(R.id.ingredient_quantity);
        }
    }
}