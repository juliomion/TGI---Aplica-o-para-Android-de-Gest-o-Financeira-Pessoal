package com.example.tgi.activity.model;

import com.example.tgi.activity.config.ConfiguracaoFireBase;
import com.example.tgi.activity.helper.Base64custom;
import com.example.tgi.activity.helper.DateCustom;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;

public class Tarefa {

    private  Long id;
    private  String nomeTarefa;
    private String data;
    private String key;



    public Tarefa () {

    }

    public void salvar(String dataEscolhida){

        FirebaseAuth autenticacao = ConfiguracaoFireBase.getFirebaseAutenticacao();
        String idUsuario = Base64custom.codificarBase64(autenticacao.getCurrentUser().getEmail());
        String mesAno = DateCustom.mesAnoDataEscolhida(dataEscolhida);
        DatabaseReference firebase = ConfiguracaoFireBase.getFirebase();
        firebase.child("Tarefa")
                .child(idUsuario)
                .child( mesAno )
                .push()
                .setValue( this );


    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNomeTarefa() {
        return nomeTarefa;
    }

    public void setNomeTarefa(String nomeTarefa) {
        this.nomeTarefa = nomeTarefa;
    }
}
