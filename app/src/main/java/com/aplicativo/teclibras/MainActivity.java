package com.aplicativo.teclibras;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {


    Integer temaSelecionado;
    SharedPreferences temaPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button ativar = (Button) findViewById(R.id.ativar);
        Button selecionar = (Button) findViewById(R.id.selecionar);
        Button btnTemas = (Button) findViewById(R.id.tema);

        temaPref = getApplicationContext().getSharedPreferences("temaPref", MODE_PRIVATE);
        temaSelecionado = temaPref.getInt("temaSelecionado", 0);

        final InputMethodManager imeManager = (InputMethodManager) getApplicationContext().getSystemService(INPUT_METHOD_SERVICE);

        ativar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(android.provider.Settings.ACTION_INPUT_METHOD_SETTINGS), 0);
            }
        });

        selecionar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imeManager.showInputMethodPicker();
            }
        });

        btnTemas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showOptionsDialog();
            }
        });
    }

    private void showOptionsDialog() {
        final String[] temas = {"Claro", "Escuro"};
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("Escolha o tema principal");
        builder.setSingleChoiceItems(temas, temaSelecionado, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                temaSelecionado = which;
                temaPref.edit().putInt("temaSelecionado", which).apply();
            }
        });

        builder.setPositiveButton("Selecionar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.show();
    }
}

