package com.example.tgi.activity.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.tgi.R;

import com.example.tgi.activity.config.ConfiguracaoFireBase;
import com.example.tgi.activity.helper.Base64custom;
import com.example.tgi.activity.model.Usuario;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;


public class CadastroActivity extends AppCompatActivity {

    private EditText campoNome, campoEmail, campoSenha;
    private Button botaoCadastrar;
    private FirebaseAuth autenticacao;
    private Usuario usuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro);


        campoNome = findViewById(R.id.editNome);
        campoEmail = findViewById(R.id.editEmail);
        campoSenha = findViewById(R.id.editSenha);
        botaoCadastrar = findViewById(R.id.buttonCadastrar);

        botaoCadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String textoNome = campoNome.getText().toString();
                String textoEmail = campoEmail.getText().toString();
                String textoSenha = campoSenha.getText().toString();

                //Aqui validamos se os campos estão preenchidos para o cadastro

                if ( !textoNome.isEmpty() ){
                    if ( !textoEmail.isEmpty() ) {
                        if ( !textoSenha.isEmpty()) {

                            usuario = new Usuario();
                            usuario.setNome( textoNome );
                            usuario.setEmail( textoEmail );
                            usuario.setSenha( textoSenha );
                            cadastrarUsuario();


                        }else {
                            Toast.makeText( CadastroActivity.this,
                                    "Preencha sua Senha!",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }else {
                        Toast.makeText( CadastroActivity.this,
                                "Preencha seu Email!",
                                Toast.LENGTH_SHORT).show();
                    }
                }else {
                    Toast.makeText( CadastroActivity.this,
                            "Preencha seu nome!",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    ///////////////////////////////AQUI ESTAMOS CADASTRANDO UM USUARIO NOVO/////////////////////////

    public void cadastrarUsuario(){

        autenticacao = ConfiguracaoFireBase.getFirebaseAutenticacao();
        autenticacao.createUserWithEmailAndPassword(
                usuario.getEmail(), usuario.getSenha()
        ).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if ( task.isSuccessful() ){

                        String idUsuario = Base64custom.codificarBase64( usuario.getEmail() );
                        usuario.setIdUsuario( idUsuario );
                        usuario.salvar();
                        finish();



                   /////////////////AQUI ESTAMOS TRATANDO AS EXCECOES DOS ERROS QUE OCORREM AO CADASTRAR UM NOVO USUARIO///////////////////////////////////
                }else {

                    String excecao = "";
                    try {
                        throw task.getException();
                    }catch ( FirebaseAuthWeakPasswordException e){
                        excecao = "Digite uma senha mais forte!";
                    }catch ( FirebaseAuthInvalidCredentialsException e){
                        excecao= "Por favor, digite um e-mail válido";
                    }catch ( FirebaseAuthUserCollisionException e){
                        excecao = "Este conta já foi cadastrada";
                    }catch (Exception e){
                        excecao = "Erro ao cadastrar usuário: "  + e.getMessage();
                        e.printStackTrace();
                    }

                    Toast.makeText(CadastroActivity.this,
                            excecao,
                            Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

}