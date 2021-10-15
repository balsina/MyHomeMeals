package com.example.tfm_mei.ui.productList;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.EditText;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tfm_mei.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class ProductsList extends AppCompatActivity {

    String nombre, idlista;
    FloatingActionButton addProduct;
    RecyclerView recyclerView;
    TextView empty_view;
    FirebaseAuth fAuth;
    FirebaseUser usuario;
    FirebaseFirestore fStore;
    String idUsuario, nombreProducto;
    List<Product> productoslista = new ArrayList<>();
    private final Map<Product, String> dict = new HashMap<>();
    public ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_products_list);

        if (getSupportActionBar() != null) getSupportActionBar().hide();

        Intent i = getIntent();
        nombre = i.getStringExtra("Name");
        idlista = i.getStringExtra("ID");

        TextView textView = findViewById(R.id.nombrelista);
        textView.setText(nombre);

        addProduct = findViewById(R.id.addProduct);

        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();

        usuario = fAuth.getCurrentUser();
        idUsuario = fAuth.getCurrentUser().getUid();

        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        empty_view = findViewById(R.id.empty_view);

        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Loading...");
        progressDialog.show();
        retrieveData();

        addProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder dialog = new AlertDialog.Builder(ProductsList.this);
                dialog.setTitle("Add product");
                EditText editText = new EditText(ProductsList.this);
                editText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_WORDS);
                editText.setHint("Product name");
                dialog.setView(editText);

                dialog.setPositiveButton("Add", (dialog12, which) -> { });
                dialog.setNegativeButton("Cancel", (dialog1, which) -> dialog1.dismiss());

                AlertDialog alertDialog = dialog.create();
                alertDialog.show();

                alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(v1 -> {
                    nombreProducto = editText.getText().toString().trim();
                    if (nombreProducto.equals("")) {
                        editText.setError("Please enter product name");
                    } else {
                        Product producto = new Product (nombreProducto);
                        fStore.collection("listasproductos")
                                .document(idlista)
                                .collection("Productos")
                                .add(producto)
                                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                            @Override
                            public void onSuccess(DocumentReference documentReference) {
                                Toast.makeText(ProductsList.this, "The product has been added correctly", Toast.LENGTH_SHORT).show();
                                retrieveData();
                            }
                        });
                        alertDialog.dismiss();
                    }
                });

            }
        });
    }

    public void eliminarproducto(String idproducto, String idlista, FirebaseFirestore fAuth) {
        fAuth.collection("listasproductos").document(idlista).collection("Productos").document(idproducto)
                .delete()
                .addOnSuccessListener(aVoid -> Toast.makeText(ProductsList.this, "Deleted product", Toast.LENGTH_SHORT).show())
                .addOnFailureListener(e -> Toast.makeText(ProductsList.this, "Failed deleation product", Toast.LENGTH_SHORT).show());
    }

    public void editarproducto(String idlista, String idproduct, FirebaseFirestore fAuth){
        AlertDialog.Builder dialog = new AlertDialog.Builder(ProductsList.this);
        dialog.setTitle("Change the product name");
        EditText editText = new EditText(ProductsList.this);
        editText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_WORDS);
        editText.setHint("New product name");
        dialog.setView(editText);
        dialog.setPositiveButton("Accept", (dialog12, which) -> { });
        dialog.setNegativeButton("Cancel", (dialog1, which) -> dialog1.dismiss());

        AlertDialog alertDialog = dialog.create();
        alertDialog.show();
        alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(v -> {
            nombreProducto = editText.getText().toString().trim();
            if (nombreProducto.equals("")) {
                editText.setError("Please enter product name");
            } else {
                fStore.collection("listasproductos").document(idlista).collection("Productos").document(idproduct)
                        .set(new Product(nombreProducto)).addOnSuccessListener(aVoid -> Toast.makeText(ProductsList.this, "Product actualized", Toast.LENGTH_SHORT).show()).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(ProductsList.this, "Failed product actualization" + e, Toast.LENGTH_SHORT).show();
                    }
                });
                retrieveData();
            }
        });
    }

    public void retrieveData(){
        productoslista.clear();
        dict.clear();
        FirebaseFirestore.getInstance().collection("listasproductos")
                .document(idlista)
                .collection("Productos")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        List<DocumentSnapshot> l = Objects.requireNonNull(task.getResult()).getDocuments();
                        if (l.isEmpty()){
                            empty_view.setVisibility(View.VISIBLE);
                        }
                        for (DocumentSnapshot document : l) {
                            Product pro = document.toObject(Product.class);
                            productoslista.add(pro);
                            dict.put(pro, document.getId());
                        }
                        recyclerView.setHasFixedSize(true);
                        recyclerView.setLayoutManager(new LinearLayoutManager(ProductsList.this));
                        ProductAdapter producto = new ProductAdapter(productoslista);
                        producto.setOnItemLongClickListener((position, view) -> {
                            PopupMenu popup = new PopupMenu(ProductsList.this, view);
                            popup.getMenuInflater().inflate(R.menu.popup_menu, popup.getMenu());
                            popup.setOnMenuItemClickListener(item -> {
                                switch (item.getItemId()){
                                    case R.id.editar:
                                        Toast.makeText(ProductsList.this, "Editar", Toast.LENGTH_SHORT).show();
                                        editarproducto(idlista, dict.get(productoslista.get(position)), fStore);
                                        break;
                                    case R.id.eliminar:
                                        Toast.makeText(ProductsList.this, "Eliminar", Toast.LENGTH_SHORT).show();
                                        eliminarproducto(dict.get(productoslista.get(position)), idlista, fStore);
                                        break;
                                }
                                return true;
                            });
                            popup.show();
                            return true;
                        });
                        recyclerView.setAdapter(producto);
                        progressDialog.dismiss();
                    }
                });
    }

}