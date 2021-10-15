package com.example.tfm_mei.ui.recipelist;

import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.tfm_mei.R;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RecipeListAdapter extends RecyclerView.Adapter<RecipeListAdapter.ViewHolder> {
    private List<RecipeListItem> mRecipes;
    private ClickListener clickListener;
    private Context context;


    public RecipeListAdapter(List<RecipeListItem> myDataset, Context c) {
        mRecipes = myDataset;
        context = c;
        clickListener = (position, v) -> {
            Toast.makeText(context, "a", Toast.LENGTH_LONG).show();
        };
    }

    @Override
    public RecipeListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemLayoutView = LayoutInflater.from(parent.getContext()).inflate
                (R.layout.item_lista_receta, parent, false);
        itemLayoutView.setLayoutParams(new RecyclerView.LayoutParams(
                RecyclerView.LayoutParams.MATCH_PARENT,
                RecyclerView.LayoutParams.WRAP_CONTENT
        ));
        return new ViewHolder(itemLayoutView);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mRecipes.get(position);
        holder.mRecipeName.setText(mRecipes.get(position).name);
        Glide.with(context).load(mRecipes.get(position).url).into(holder.mRecipeImage);
    }

    public interface ClickListener {
        void onItemClick(int position, View v);
    }

    public void setOnItemClickListener(ClickListener clickListener) {
        this.clickListener = clickListener;
    }

    @Override
    public int getItemCount() {
        return mRecipes.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        @BindView(R.id.recipe_name)

        public TextView mRecipeName;
        public ImageView mRecipeImage;
        public RecipeListItem mItem;

        public ViewHolder(View itemLayoutView) {
            super(itemLayoutView);
            ButterKnife.bind(this, itemView);
            itemLayoutView.setOnClickListener(this);
            mRecipeName = (TextView) itemLayoutView.findViewById(R.id.recipe_name);
            mRecipeImage = (ImageView) itemLayoutView.findViewById(R.id.recipe_image);
        }

        @Override
        public void onClick(View v) { clickListener.onItemClick(getPosition(), v); }

    }
}