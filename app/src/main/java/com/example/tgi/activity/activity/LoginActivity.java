package com.example.tgi.activity.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.tgi.R;
import com.example.tgi.activity.config.ConfiguracaoFireBase;
import com.example.tgi.activity.model.Usuario;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;


public class LoginActivity extends AppCompatActivity {

    private EditText campoEmail, campoSenha;
    private Button botaoEntrar;
    private Usuario usuario;
    private FirebaseAuth autenticacao;
    SignInButton signInButton;
    GoogleSignInClient mGoogleSignInClient;
    int RC_SIGN_IN = 0;
    private String TAG = "PrincipalActivity";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        autenticacao = FirebaseAuth.getInstance();
       // signInButton = findViewById(R.id.google_sign_in);
        campoEmail = findViewById(R.id.editEmail);
        campoSenha = findViewById(R.id.editSenha);
        botaoEntrar = findViewById(R.id.buttonEntrar);

        botaoEntrar.setOnClickListener(new View.OnClickListener() {
            @Override


            public void onClick(View v) {

                String textoEmail = campoEmail.getText().toString();
                String textoSenha = campoSenha.getText().toString();





                //////AQUI ESTAMOS VALIDANDO SE EMAIL FOI PREENCHIDO, SE É VALIDO OU SE ESTÁ AUTORIZADO A ENTRAR////////////////////
                if ( !textoEmail.isEmpty() ) {
                    if ( !textoSenha.isEmpty()) {

                    usuario = new Usuario();
                    usuario.setEmail( textoEmail );
                    usuario.setSenha( textoSenha );
                    validadarLogin();
                    }else {
                        Toast.makeText(LoginActivity.this,
                                "Preencha sua Senha!",
                                Toast.LENGTH_SHORT).show();
                    }
                }else {
                    Toast.makeText( LoginActivity.this,
                            "Preencha seu Email!",
                            Toast.LENGTH_SHORT).show();
                }

            }
        });

       /* ////////METODO PARA LOGIN DA CONTA GOOGLE//////////
        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (view.getId()){
                    case R.id.google_sign_in:
                        signIn();
                        break;
                }
            }
        });

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();



        mGoogleSignInClient = GoogleSignIn.getClient(this, gso); */

    }

   /* private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        /// AQUI É O RESULTADO QUE RETORNADO LANÇAMENTO DA INTENT////
        /// DO GoogleSignClient.getSignInIntent/////

        if (requestCode == RC_SIGN_IN) {
            ////A TAREFA QUE RETORTA DAQUIE É SEMPRE CONCLUIDA POIS SEMPRE SERA DIFERENTE DE 0 /////

            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult( task );


        }
    }

    public void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
       try {
           GoogleSignInAccount account = completedTask.getResult(ApiException.class);
           ///// SE O LOGIN FOR UM SUCESSO, APARECER NA TELA////

           Intent intent = new Intent(LoginActivity.this, PrincipalActivity.class);
           startActivity(intent);
           FirebaseGoogleAuth(account);


       } catch (ApiException e){
           Log.w("error", "Log falho=codifo" + e.getStatusCode());
           FirebaseGoogleAuth(null);
       }
    }

    public void  FirebaseGoogleAuth(GoogleSignInAccount acct) {

        AuthCredential authCredential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        autenticacao.signInWithCredential(authCredential).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    Toast.makeText(LoginActivity.this, "successful",Toast.LENGTH_SHORT).show();
                    FirebaseUser user = autenticacao.getCurrentUser();
                    updateUI(user);

                }else{

                    Toast.makeText(LoginActivity.this, "failed", Toast.LENGTH_SHORT).show();
                    updateUI(null);
                }
            }
        });

    }


    private void updateUI(FirebaseUser fUser) {

        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(getApplicationContext());
        if(account != null) {
            String personName = account.getDisplayName();
            String personGivenName = account.getGivenName();
            String personFamilyName = account.getFamilyName();
            String personId = account.getId();
            Uri personPhoto = account.getPhotoUrl();
            String personEmail = account.getEmail();



        }



    } */





    public void validadarLogin(){

        autenticacao = ConfiguracaoFireBase.getFirebaseAutenticacao();
        autenticacao.signInWithEmailAndPassword(
                usuario.getEmail(),
                usuario.getSenha()

        ).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {


                if ( task.isSuccessful() ){

                    abrirTelaPrincipal();



                }else{

                    String excecao = "";
                    try {
                        throw task.getException();
                    }catch ( FirebaseAuthInvalidUserException e ) {
                        excecao = "Usuário não está cadastrado.";
                    }catch ( FirebaseAuthInvalidCredentialsException e ){
                        excecao = "E-mail e senha não correspondem a um usuário cadastrado";
                    }catch (Exception e){
                        excecao = "Erro ao cadastrar usuário: "  + e.getMessage();
                        e.printStackTrace();
                    }

                    Toast.makeText(LoginActivity.this,
                            excecao,
                            Toast.LENGTH_SHORT).show();

                }
            }
        });

    }


    public void abrirTelaPrincipal(){
        startActivity(new Intent(this, PrincipalActivity.class));
        finish();
    }

}
