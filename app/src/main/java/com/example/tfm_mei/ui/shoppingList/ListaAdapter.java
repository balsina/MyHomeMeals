package com.example.tfm_mei.ui.shoppingList;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tfm_mei.R;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class ListaAdapter extends RecyclerView.Adapter<ListaAdapter.ViewHolder> {
    private List<Lista> mDataset;
    private ClickListener clickListener;
    private MyLongClickListener myLongClickListener;

    public ListaAdapter(List<Lista> myDataset) {
        mDataset = myDataset;
        clickListener = (position, v) -> { };
        myLongClickListener = (position, v) -> false;
    }

    @NonNull
    @Override
    public ListaAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        @SuppressLint("InflateParams") View itemLayoutView = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.item_lista_compra, null);
        itemLayoutView.setLayoutParams(new RecyclerView.LayoutParams(
                RecyclerView.LayoutParams.MATCH_PARENT,
                RecyclerView.LayoutParams.WRAP_CONTENT
        ));
        return new ViewHolder(itemLayoutView);
    }

    @Override
    public void onBindViewHolder(@NonNull ListaAdapter.ViewHolder holder, int position) {
        Lista ex = mDataset.get(position);
        holder.textName.setText(ex.getName());
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    public void clear() {
        mDataset.clear();
    }

    public void addAll(List<Lista> lista) {
        mDataset = lista;
    }

    public void setOnItemClickListener(ClickListener clickListener) {
        this.clickListener = clickListener;
    }

    public void setOnItemLongClickListener(MyLongClickListener clickListener) {
        this.myLongClickListener = clickListener;
    }

    public interface ClickListener {
        void onItemClick(int position, View v);
    }

    public interface MyLongClickListener {
        boolean onItemLongClick(int position, View v);
    }


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
        @BindView(R.id.nombre_lista)
        TextView textName;

        public ViewHolder(View itemLayoutView) {
            super(itemLayoutView);
            ButterKnife.bind(this, itemView);
            itemLayoutView.setOnClickListener(this);
            itemLayoutView.setOnLongClickListener(this);
        }

        @Override
        public void onClick(View v) {
            clickListener.onItemClick(getPosition(), v);
        }

        @Override
        public boolean onLongClick(View v) {
            myLongClickListener.onItemLongClick(getPosition(), v);
            return true;
        }
    }
}