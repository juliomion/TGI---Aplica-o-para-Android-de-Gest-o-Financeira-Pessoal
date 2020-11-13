package com.example.tgi.activity.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.tgi.R;
import com.example.tgi.activity.config.ConfiguracaoFireBase;
import com.example.tgi.activity.helper.Base64custom;
import com.example.tgi.activity.helper.DateCustom;
import com.example.tgi.activity.model.Movimentacao;
import com.example.tgi.activity.model.Usuario;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

public class ReceitasActivity extends AppCompatActivity {



    private TextInputEditText campoData, campoCategoria, campoDescricao;
    private EditText campoValor;
    private Movimentacao movimentacao;
    private DatabaseReference firebaseRef = ConfiguracaoFireBase.getFirebase();
    private FirebaseAuth autenticacao = ConfiguracaoFireBase.getFirebaseAutenticacao();
    private Double receitaTotal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receitas);

        campoValor = findViewById(R.id.editValor);
        campoData = findViewById(R.id.editData);
        campoCategoria = findViewById(R.id.editCategoria);
        campoDescricao = findViewById(R.id.editDescricao);

        //DATA ATUAL//
        campoData.setText(DateCustom.dataAtual());

        //recupera receita total//
        recuperarReceitaTotal();
    }

    public void salvarReceita (View view) {

        if (validarCamposReceita()){
            movimentacao = new Movimentacao();
            String data = campoData.getText().toString();
            Double valorRecuperado = Double.parseDouble(campoValor.getText().toString());

            movimentacao.setValor(valorRecuperado);
            movimentacao.setCategoria(campoCategoria.getText().toString());
            movimentacao.setDescricao (campoDescricao.getText().toString());
            movimentacao.setData (data);
            movimentacao.setTipo("r");


            Double receitaAtualizada = receitaTotal + valorRecuperado;
            atualizarReceita( receitaAtualizada );

            movimentacao.salvar(data);
            finish();
        }
    }

    public boolean validarCamposReceita () {



        String textoValor = campoValor.getText().toString();
        String textoData = campoData.getText().toString();
        String textoCategoria = campoCategoria.getText().toString();
        String textoDescricao = campoDescricao.getText().toString();

        if (!textoValor.isEmpty()) {
            if (!textoData.isEmpty()) {
                if (!textoCategoria.isEmpty()) {
                    if (!textoDescricao.isEmpty()) {

                        return true;

                    }else {
                        Toast.makeText(ReceitasActivity.this,
                                "A descrição não foi preenchida!",
                                Toast.LENGTH_SHORT).show();
                        return false;
                    }


                }else {
                    Toast.makeText(ReceitasActivity.this,
                            "A categoria não foi preenchida!",
                            Toast.LENGTH_SHORT).show();
                    return false;
                }


            }else {
                Toast.makeText(ReceitasActivity.this,
                        "A data não foi preenchida!",
                        Toast.LENGTH_SHORT).show();
                return false;
            }


        }else {
            Toast.makeText(ReceitasActivity.this,
                    "Valor não foi preenchido!",
                    Toast.LENGTH_SHORT).show();
            return false;
        }

    }

    public void recuperarReceitaTotal (){

        String emailUsuario = autenticacao.getCurrentUser().getEmail();
        String idUsuario = Base64custom.codificarBase64( emailUsuario);
        DatabaseReference usuarioRef = firebaseRef.child("usuarios").child(idUsuario);

        usuarioRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Usuario usuario = dataSnapshot.getValue(Usuario.class);
                receitaTotal = usuario.getReceitaTotal();


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void atualizarReceita (Double receita) {
        String emailUsuario = autenticacao.getCurrentUser().getEmail();
        String idUsuario = Base64custom.codificarBase64( emailUsuario);
        DatabaseReference usuarioRef = firebaseRef.child("usuarios").child(idUsuario);

        usuarioRef.child("receitaTotal").setValue(receita);
    }



}
