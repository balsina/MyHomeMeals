package com.example.tfm_mei.ui.auth;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.example.tfm_mei.MainActivity;
import com.example.tfm_mei.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public class Registration extends AppCompatActivity {

    EditText nombre, correo, contraseña, contraseña2;
    Button mRegisterBtn;
    TextView mLoginBtn;
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    String idUsuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        if (getSupportActionBar() != null) getSupportActionBar().hide();

        nombre = findViewById(R.id.Rnombre);
        correo = findViewById(R.id.Rcorreo);
        contraseña = findViewById(R.id.Rcontra);
        contraseña2 = findViewById(R.id.Rcontra2);
        mRegisterBtn = findViewById(R.id.registerBtn);
        mLoginBtn = findViewById(R.id.Rlogin);

        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();


        if (fAuth.getCurrentUser() !=null){
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
            finish();
        }

        mRegisterBtn.setOnClickListener(v -> {
            String correoelectro = correo.getText().toString().trim();
            String contra = contraseña.getText().toString().trim();
            String contra2 = contraseña2.getText().toString().trim();
            String nombrep = nombre.getText().toString().trim();

            if (!correoelectro.isEmpty() && !contra.isEmpty() && !contra2.isEmpty()){
                if (contraseña.length() >= 6 && contra.equals(contra2)) {
                    fAuth.createUserWithEmailAndPassword(correoelectro, contra).addOnCompleteListener(task -> {
                        if (task.isSuccessful()){
                            Toast.makeText(Registration.this, "User correctly created", Toast.LENGTH_SHORT).show();
                            idUsuario = fAuth.getCurrentUser().getUid();
                            DocumentReference documentReference = fStore.collection("usuarios").document(idUsuario);
                            Map<String, Object> user = new HashMap<>();
                            user.put("Nombre", nombrep);
                            user.put("Correo", correoelectro);
                            documentReference.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(@NotNull Void aVoid) {
                                }
                            });
                            startActivity(new Intent(getApplicationContext(), MainActivity.class));
                        }
                        else {
                            FirebaseAuthException e = (FirebaseAuthException )task.getException();
                            Toast.makeText(Registration.this, "Failed Registration: " +e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
                else{
                    Toast.makeText(Registration.this, "At least 6 characters are required for the password", Toast.LENGTH_SHORT).show();
                }
            }
            else{
                Toast.makeText(Registration.this, "All fields must be filled", Toast.LENGTH_SHORT).show();
            }
        });

        mLoginBtn.setOnClickListener(v -> startActivity(new Intent(getApplicationContext(), Login.class)));

    }
}