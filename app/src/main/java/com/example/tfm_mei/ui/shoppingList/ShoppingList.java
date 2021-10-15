package com.example.tfm_mei.ui.shoppingList;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tfm_mei.ui.productList.ProductsList;
import com.example.tfm_mei.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class ShoppingList extends Fragment {

    FloatingActionButton addList;
    RecyclerView recyclerView;
    TextView empty_view;
    FirebaseAuth fAuth;
    FirebaseUser usuario;
    FirebaseFirestore fStore;
    String idUsuario, nombreListaCompra, idListaCompra;
    List<Lista> listascompra = new ArrayList<>();
    private final Map<Lista, String> dict = new HashMap<>();
    public ProgressDialog progressDialog;


    public ShoppingList() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        progressDialog = new ProgressDialog(getActivity());
        View v = inflater.inflate(R.layout.fragment_shopping_list, container, false);
        addData(v);
        return v;
    }

    public void eliminarlista(String idlistae, String idlista, FirebaseFirestore fAuth){
        fAuth.collection("listasusuarios").document(idUsuario).collection("Nombre").document(idlistae)
                .delete()
                .addOnSuccessListener(aVoid -> Toast.makeText(getActivity(), "Deleted list", Toast.LENGTH_SHORT).show())
                .addOnFailureListener(e -> Toast.makeText(getActivity(), "Deletion list failed", Toast.LENGTH_SHORT).show());
        fAuth.collection("listasproductos").document(idlista)
                .delete()
                .addOnSuccessListener(aVoid -> Toast.makeText(getActivity(), "Deleted list", Toast.LENGTH_SHORT).show())
                .addOnFailureListener(e -> Toast.makeText(getActivity(), "Deletion list failed", Toast.LENGTH_SHORT).show());
        retrieveData();
    }

    public void editarlista(String idlistae, String idlista, FirebaseFirestore fStore) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
        dialog.setTitle("Change the shopping list name");
        EditText editText = new EditText(getActivity());
        editText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_WORDS);
        editText.setHint("New name");
        dialog.setView(editText);
        dialog.setPositiveButton("Accept", (dialog12, which) -> { });
        dialog.setNegativeButton("Cancel", (dialog1, which) -> dialog1.dismiss());

        AlertDialog alertDialog = dialog.create();
        alertDialog.show();
        alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(v -> {
            nombreListaCompra = editText.getText().toString().trim();
            if (nombreListaCompra.equals("")) {
                editText.setError("Please enter list name");
            } else {
                fStore.collection("listasusuarios").document(idUsuario).collection("Nombre").document(idlistae)
                        .set(new Lista(nombreListaCompra,idlista)).addOnSuccessListener(aVoid -> Toast.makeText(getActivity(), "Actualized list",
                        Toast.LENGTH_SHORT).show()).addOnFailureListener(e -> Toast.makeText(getActivity(), "Failed actualitzation list" + e, Toast.LENGTH_SHORT).show());
                fStore.collection("listasproductos")
                        .document(idlista)
                        .set(new Lista(nombreListaCompra,idlista))
                        .addOnSuccessListener(aVoid -> Toast.makeText(getActivity(), "Actualized list",
                        Toast.LENGTH_SHORT).show()).addOnFailureListener(e -> Toast.makeText(getActivity(),
                        "Failed actualitzation list" + e, Toast.LENGTH_SHORT).show());
                retrieveData();
                alertDialog.dismiss();
            }
        });

    }

    private void addData(View v) {
        addList = v.findViewById(R.id.addList);

        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();

        usuario = fAuth.getCurrentUser();
        idUsuario = fAuth.getCurrentUser().getUid();

        recyclerView = v.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        empty_view = v.findViewById(R.id.empty_view);

        progressDialog.setTitle("Loading...");
        progressDialog.show();

        retrieveData();

        addList.setOnClickListener(v1 -> {
            AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
            dialog.setTitle("Enter the code or the name of the shopping list");
            EditText editText = new EditText(getActivity());
            editText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_WORDS);
            editText.setHint("Name or code of the list");
            dialog.setView(editText);

            dialog.setPositiveButton("New list", (dialog12, which) -> { });

            dialog.setNeutralButton("Add list", (dialog12, which) -> {
                idListaCompra = editText.getText().toString().trim();
                idUsuario = fAuth.getCurrentUser().getUid();
                boolean b = false;
                for(int i = 0; i < listascompra.size() ; i++){
                    if(listascompra.get(i).getId().equals(idListaCompra))
                        b = true;
                }
                if(!b)
                    FirebaseFirestore.getInstance()
                            .collection("listasproductos")
                            .document(idListaCompra)
                            .get()
                            .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                Lista li = document.toObject(Lista.class);
                                li.setId(idListaCompra);
                                fStore.collection("listasusuarios")
                                        .document(idUsuario)
                                        .collection("Nombre")
                                        .add(li)
                                        .addOnSuccessListener(documentReference -> {
                                    Toast.makeText(getActivity(), "List added correctly", Toast.LENGTH_SHORT).show();
                                    retrieveData();
                                });
                            } else {
                                Toast.makeText(getActivity(), "The ID does not exist", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(getActivity(), "The ID does not exist", Toast.LENGTH_SHORT).show();
                        }
                    });
                else
                    Toast.makeText(getActivity(), "The list you are trying to add has already been added", Toast.LENGTH_SHORT).show();
            });
            dialog.setNegativeButton("Cancel", (dialog12, which) -> dialog12.dismiss());

            AlertDialog alertDialog = dialog.create();
            alertDialog.show();
            alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(v11 -> {
                nombreListaCompra = editText.getText().toString().trim();
                if (nombreListaCompra.equals("")) {
                    editText.setError("Please enter list name");
                } else {
                    idUsuario = fAuth.getCurrentUser().getUid();
                    Lista lista = new Lista(nombreListaCompra, "");
                    fStore.collection("listasproductos").add(lista).addOnSuccessListener(documentReference -> {
                        lista.setId(documentReference.getId());
                        fStore.collection("listasusuarios")
                                .document(idUsuario).collection("Nombre")
                                .add(lista)
                                .addOnSuccessListener(documentReference1 -> { Toast.makeText(getActivity(), "New list correctly created",
                                        Toast.LENGTH_SHORT).show();
                            retrieveData();
                        });
                    });
                    alertDialog.dismiss();
                }
            });
        });
    }

    private void retrieveData() {
        listascompra.clear();
        dict.clear();
        FirebaseFirestore.getInstance().collection("listasusuarios")
                .document(idUsuario)
                .collection("Nombre")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        List<DocumentSnapshot> l = Objects.requireNonNull(task.getResult()).getDocuments();
                        if (l.isEmpty()){
                            empty_view.setVisibility(View.VISIBLE);
                        }
                        for (DocumentSnapshot document : l) {
                            Lista li = document.toObject(Lista.class);
                            listascompra.add(li);
                            dict.put(li, document.getId());
                        }
                        recyclerView.setHasFixedSize(true);
                        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                        ListaAdapter lista = new ListaAdapter(listascompra);
                        lista.setOnItemClickListener((position, v) -> {
                            Intent i = new Intent(getActivity(), ProductsList.class);
                            Lista model = listascompra.get(position);
                            i.putExtra("Name", model.getName());
                            i.putExtra("ID", model.getId());
                            startActivity(i);
                        });
                        lista.setOnItemLongClickListener((position, view) -> {
                            PopupMenu popup = new PopupMenu(getContext(), view);
                            popup.getMenuInflater().inflate(R.menu.popup_menu_lista, popup.getMenu());
                            popup.setOnMenuItemClickListener(item -> {
                                switch (item.getItemId()){
                                    case R.id.editar:
                                        editarlista(dict.get(listascompra.get(position)), listascompra.get(position).getId(), fStore);
                                        break;
                                    case R.id.eliminar:
                                        eliminarlista(dict.get(listascompra.get(position)), listascompra.get(position).getId(), fStore);
                                        break;
                                    default:
                                        Intent i = new Intent(Intent.ACTION_SEND);
                                        i.setType("text/plain");
                                        i.putExtra(Intent.EXTRA_TEXT, "Hello!\n" + idUsuario+ "Has shared the shopping list: " + listascompra.get(position).getName() +
                                                " with you, add it with the code: " + listascompra.get(position).getId()+ ".");
                                        getActivity().startActivity(i);
                                        break;
                                }
                                return true;
                            });
                            popup.show();
                            Toast.makeText(getActivity(), listascompra.get(position).name, Toast.LENGTH_SHORT).show();
                            return true;
                        });
                        recyclerView.setAdapter(lista);
                        progressDialog.dismiss();
                    }
                });
    }

}
