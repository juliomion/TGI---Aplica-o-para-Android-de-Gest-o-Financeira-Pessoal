package com.example.tgi.activity.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tgi.R;
import com.example.tgi.activity.adapter.AdapterTarefa;
import com.example.tgi.activity.config.ConfiguracaoFireBase;
import com.example.tgi.activity.helper.Base64custom;
import com.example.tgi.activity.model.Tarefa;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnMonthChangedListener;

import java.util.ArrayList;
import java.util.List;

public class TarefasActivity extends AppCompatActivity {


    private RecyclerView recyclerView;
    private AdapterTarefa adapterTarefa;
    private DatabaseReference firebaseRef = ConfiguracaoFireBase.getFirebase();
    private List<Tarefa> listaTarefas = new  ArrayList<>();
    private MaterialCalendarView calendarView2;
    private DatabaseReference tarefaRef ;
    private FirebaseAuth autenticacao = ConfiguracaoFireBase.getFirebaseAutenticacao();
    private String mesAnoSelecionado;
    private ValueEventListener valueEventListenerTarefas;
    private Tarefa tarefa;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tarefas);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        calendarView2 = findViewById(R.id.calendarTarefa);
        recyclerView = findViewById(R.id.recyclerTarefa);
        configuraCalendarView();
        swipe();

        //Configurar adapter

        adapterTarefa = new AdapterTarefa(listaTarefas,this);

        //Configurar RecyclerView
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter( adapterTarefa);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(getApplicationContext(),AdicionarTarefaActivity.class);
                startActivity(intent);


            }
        });
    }

    public void swipe() {

        ItemTouchHelper.Callback itemTouch = new ItemTouchHelper.Callback() {
            @Override
            public int getMovementFlags(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
                int dragflags = ItemTouchHelper.ACTION_STATE_IDLE;
                int swipeFlags = ItemTouchHelper.START | ItemTouchHelper.END;
                return makeMovementFlags(dragflags, swipeFlags);
            }

            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {

            excluirMOvimentacao(viewHolder);

            }
        };

        new ItemTouchHelper(itemTouch).attachToRecyclerView(recyclerView);

    }

    public void excluirMOvimentacao(final RecyclerView.ViewHolder viewHolder){

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setTitle("Excluir Tarefa");
        alertDialog.setMessage("Você já concluiu sua tarera?");
        alertDialog.setCancelable(false);


        alertDialog.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                int position = viewHolder.getAdapterPosition();
                tarefa = listaTarefas.get(position);
                String emailUsuario = autenticacao.getCurrentUser().getEmail();
                String idUsuario = Base64custom.codificarBase64(emailUsuario);
                tarefaRef = firebaseRef.child("Tarefa")
                        .child(idUsuario)
                        .child(mesAnoSelecionado);
                tarefaRef.child(tarefa.getKey()).removeValue();
                adapterTarefa.notifyItemRemoved(position);

                Toast.makeText(TarefasActivity.this,
                        "Parabéns, tarefa concluida",
                        Toast.LENGTH_LONG).show();

            }
        });

        alertDialog.setNegativeButton("Não", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                Toast.makeText(TarefasActivity.this,
                        "Tarefa não concluida",
                        Toast.LENGTH_LONG).show();


                adapterTarefa.notifyDataSetChanged();
            }
        });

        AlertDialog alert = alertDialog.create();
        alert.show();
    }

    public void recuperarMovimentacao() {
        String emailUsuario = autenticacao.getCurrentUser().getEmail();
        String idUsuario = Base64custom.codificarBase64(emailUsuario);
        tarefaRef = firebaseRef.child("Tarefa")
                .child(idUsuario)
                .child(mesAnoSelecionado);


        valueEventListenerTarefas = tarefaRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                listaTarefas.clear();
                for (DataSnapshot dados: dataSnapshot.getChildren()) {

                    Tarefa tarefa = dados.getValue(Tarefa.class);
                    tarefa.setKey(dados.getKey());
                    listaTarefas.add( tarefa);

                }


                adapterTarefa.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void configuraCalendarView() {


        CharSequence meses [] = {"Janeiro","Fevereiro","Março","Abril","Maio","Juno","Julho","Agosto","Setembro","Outubro","Novembro","Dezembro"};
        calendarView2.setTitleMonths(meses);
        CalendarDay dataAtual = calendarView2.getCurrentDate()  ;
        String mesSelecionado = String.format("%02d",(dataAtual.getMonth() +1));
        mesAnoSelecionado = String.valueOf( mesSelecionado +  "" + dataAtual.getYear());
        calendarView2.setOnMonthChangedListener(new OnMonthChangedListener() {
            @Override
            public void onMonthChanged(MaterialCalendarView widget, CalendarDay date) {
                String mesSelecionado = String.format("%02d",(date.getMonth() +1));
                mesAnoSelecionado = String.valueOf(mesSelecionado +  "" + date.getYear());
                tarefaRef.removeEventListener(valueEventListenerTarefas);
                recuperarMovimentacao();
            }
        });
    }

    @Override
    protected void onStart() {
        recuperarMovimentacao();
        super.onStart();


    }

    @Override
    protected void onStop() {
        super.onStop();
        tarefaRef.removeEventListener(valueEventListenerTarefas);
    }
}

