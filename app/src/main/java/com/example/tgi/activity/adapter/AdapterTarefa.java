package com.example.tgi.activity.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tgi.R;
import com.example.tgi.activity.model.Tarefa;

import java.util.List;

public class AdapterTarefa extends RecyclerView.Adapter<AdapterTarefa.MyViewHolder> {


    private  List<Tarefa>  listaTarefas;
    Context context;


    public AdapterTarefa(List<Tarefa> lista, Context context) {
    this.listaTarefas = lista;
        this.context = context;

    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemLista = LayoutInflater.from(parent.getContext())
                                        .inflate(R.layout.lista_tarefa_adapter, parent,false);


        return new MyViewHolder(itemLista);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        Tarefa tarefa = listaTarefas.get(position);
        holder.titulo.setText( tarefa.getNomeTarefa() );
        holder.data.setText(tarefa.getData());

    }

    @Override
    public int getItemCount() {
        return this.listaTarefas.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {


        TextView titulo;
        TextView data;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            titulo = itemView.findViewById(R.id.textTitulo);
            data = itemView.findViewById(R.id.textData);
        }
    }

}
