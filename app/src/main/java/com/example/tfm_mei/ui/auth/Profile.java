package com.example.tfm_mei.ui.auth;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import com.example.tfm_mei.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class Profile extends Fragment {

    TextView nombre, correo;
    Button deleteuser, logout;
    FirebaseAuth fAuth;
    FirebaseUser usuario;
    FirebaseFirestore fStore;
    String idUsuario;
    public ProgressDialog progressDialog;


    public Profile () {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        progressDialog = new ProgressDialog(getActivity());
        View v = inflater.inflate(R.layout.activity_profile, container, false);
        addData (v);
        return v;
    }

    private void addData(View v) {
        nombre = v.findViewById(R.id.Pnombre);
        correo = v.findViewById(R.id.Pcorreo);
        deleteuser = v.findViewById(R.id.deleteaccount);
        logout = v.findViewById(R.id.logout);

        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();

        usuario = fAuth.getCurrentUser();
        idUsuario = fAuth.getCurrentUser().getUid();

        progressDialog.setTitle("Loading...");
        progressDialog.show();

        DocumentReference docRef = fStore.collection("usuarios").document(idUsuario);
        docRef.get().addOnSuccessListener(documentSnapshot -> {
            nombre.setText(documentSnapshot.getString("Nombre"));
            correo.setText(documentSnapshot.getString("Correo"));
            progressDialog.dismiss();
        });

        logout.setOnClickListener(v12 -> {
            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(getActivity(), Login.class));
            getActivity().finish();
        });

        deleteuser.setOnClickListener(v1 -> {
            AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
            dialog.setTitle("Are you sure you want to delete the account?");
            dialog.setMessage("If you delete your account you will not be able to recover the data");

            dialog.setPositiveButton("Delete", (dialog1, which) -> usuario.delete().addOnSuccessListener(aVoid -> {
                Toast.makeText(getActivity(), "Account deleted", Toast.LENGTH_SHORT).show();
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(getActivity(), Login.class));
                getActivity().finish();
            }).addOnFailureListener(e -> Toast.makeText(getActivity(), "Account deletion failed: " + e.getMessage(),
                    Toast.LENGTH_SHORT).show())).setNegativeButton("Cancel", (dialog12, which) -> dialog12.cancel());
            dialog.create().show();
        });
    }

}