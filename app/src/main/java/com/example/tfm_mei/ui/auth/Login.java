package com.example.tfm_mei.ui.auth;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.example.tfm_mei.MainActivity;
import com.example.tfm_mei.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;

public class Login extends AppCompatActivity {

    EditText correo, contraseña;
    Button mLoginBtn;
    TextView registroBtn,forgotpwdBtn;
    FirebaseAuth fAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        if (getSupportActionBar() != null) getSupportActionBar().hide();

        correo = findViewById(R.id.Lcorreo);
        contraseña = findViewById(R.id.Lcontra);
        mLoginBtn = findViewById(R.id.loginBtn);
        fAuth = FirebaseAuth.getInstance();
        registroBtn = findViewById(R.id.Lregistro);
        forgotpwdBtn = findViewById(R.id.Lforgotcontra);

        mLoginBtn.setOnClickListener(v -> {
            String correoelectro = correo.getText().toString().trim();
            String contra = contraseña.getText().toString().trim();
            if (!correoelectro.isEmpty() && !contra.isEmpty()){
                if (contraseña.length() >= 6) {
                    fAuth.signInWithEmailAndPassword(correoelectro, contra).addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Toast.makeText(Login.this, "Logged in succesfully", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getApplicationContext(), MainActivity.class));
                        } else {
                            FirebaseAuthException e = (FirebaseAuthException) task.getException();
                            Toast.makeText(Login.this, "Failed Login: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
            else{
                Toast.makeText(Login.this, "All fields must be filled", Toast.LENGTH_SHORT).show();
            }
        });

        registroBtn.setOnClickListener(v -> startActivity(new Intent(getApplicationContext(), Registration.class)));

        forgotpwdBtn.setOnClickListener(v -> {
            EditText resetMail = new EditText(v.getContext());
            AlertDialog.Builder passwordResetDialog = new AlertDialog.Builder(v.getContext());
            passwordResetDialog.setTitle("Restore password?");
            passwordResetDialog.setMessage("Enter your email to receive the link");
            passwordResetDialog.setView(resetMail);

            passwordResetDialog.setPositiveButton("Restore", (dialog, which) -> {
                String mail = resetMail.getText().toString();
                fAuth.sendPasswordResetEmail(mail).addOnSuccessListener(aVoid -> Toast.makeText(Login.this, "The link has been sent to your email",
                        Toast.LENGTH_SHORT).show()).addOnFailureListener(e -> Toast.makeText(Login.this, "Failed Login: " + e.getMessage(), Toast.LENGTH_SHORT).show());
            }).setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());

            passwordResetDialog.create().show();
        });



    }
}