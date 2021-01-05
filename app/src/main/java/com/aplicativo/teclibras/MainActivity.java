package com.aplicativo.teclibras;

import android.content.Intent;

        import android.os.Bundle;
        import android.view.View;
        import android.view.inputmethod.InputMethodManager;
        import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);

            Button ativar = (Button) findViewById(R.id.ativar);
            Button selecionar = (Button) findViewById(R.id.selecionar);

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
        }
    }

