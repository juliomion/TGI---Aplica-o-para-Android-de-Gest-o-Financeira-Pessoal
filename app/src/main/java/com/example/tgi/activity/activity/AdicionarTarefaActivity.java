package com.example.tgi.activity.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.tgi.R;
import com.example.tgi.activity.config.ConfiguracaoFireBase;
import com.example.tgi.activity.helper.DateCustom;
import com.example.tgi.activity.model.Tarefa;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;

public class AdicionarTarefaActivity extends AppCompatActivity {

    private EditText campoData, campoCategoria;
    private FirebaseAuth autenticacao = ConfiguracaoFireBase.getFirebaseAutenticacao();
    private Button botaoAdicionar;
    private Tarefa tarefa;
    private DatabaseReference firebaseRef = ConfiguracaoFireBase.getFirebase();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adicionar_tarefa);


        campoCategoria = findViewById(R.id.editCategoriaTarefa);
        campoData = findViewById(R.id.editDataTarefa);

        campoData.setText(DateCustom.dataAtual());

    }


    public void salvarTarefa (View view) {

        if (validarCamposReceita()){
            tarefa = new Tarefa();
            String data = campoData.getText().toString();
            String categoria = campoCategoria.getText().toString();

            tarefa.setNomeTarefa(categoria);
            tarefa.setData (data);



            tarefa.salvar(data);
            finish();
        }
    }

    public boolean validarCamposReceita () {




        String textoData = campoData.getText().toString();
        String textoCategoria = campoCategoria.getText().toString();


        if (!textoCategoria.isEmpty()) {
            if (!textoData.isEmpty()) {


                return true;


            }else {
                    Toast.makeText(AdicionarTarefaActivity.this,
                            "A data não foi preenchida!",
                            Toast.LENGTH_SHORT).show();
                    return false;
                }


            }
        else {
            Toast.makeText(AdicionarTarefaActivity.this,
                    "A tarefa não foi preenchida",
                    Toast.LENGTH_SHORT).show();
            return false;
        }

    }





}
