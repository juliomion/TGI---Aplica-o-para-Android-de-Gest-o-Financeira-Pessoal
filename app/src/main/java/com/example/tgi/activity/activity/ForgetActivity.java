package com.example.tgi.activity.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.Toolbar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.tgi.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ForgetActivity extends AppCompatActivity {

    private EditText mail;
    private Button forget;
    FirebaseAuth firebaseAuth;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget);

        mail = findViewById(R.id.editMail);
        forget = findViewById(R.id.buttonForget);

        firebaseAuth = FirebaseAuth.getInstance();






        forget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                firebaseAuth.sendPasswordResetEmail(mail.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {


                        if (task.isSuccessful()){

                            Toast.makeText(ForgetActivity.this, "Senha enviada para seu email",Toast.LENGTH_LONG).show();
                            voltarTela();
                        }else{

                            Toast.makeText(ForgetActivity.this, task.getException().getMessage(),Toast.LENGTH_LONG).show();

                        }

                    }
                });
            }
        });


    }

    public void voltarTela(){
        startActivity( new Intent(this, LoginActivity.class));
    }
}
