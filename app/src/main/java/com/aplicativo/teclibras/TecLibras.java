package com.aplicativo.teclibras;

import android.content.SharedPreferences;
import android.inputmethodservice.InputMethodService;
import android.inputmethodservice.Keyboard;
import android.inputmethodservice.KeyboardView;
import android.media.AudioManager;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputConnection;
import android.widget.LinearLayout;
import android.widget.ScrollView;

public class TecLibras extends InputMethodService implements KeyboardView.OnKeyboardActionListener {
    private KeyboardView kv;
    private Keyboard keyboard, numbers;
    private Integer shiftCount = 0;
    private boolean isCaps = false;

    static final Integer TEMA_CLARO = 0;
    static final Integer TEMA_ESCURO = 1;

    SharedPreferences temaPref;

    //TODO Função que cria o keyboard
    @Override
    public View onCreateInputView() {
        //LinearLayout myView = (LinearLayout) View.inflate(this, R.layout.custom_layout, null);
        //ScrollView scrollView = (ScrollView) myView.findViewById(R.id.keyboard_scroll);
        temaPref = getApplicationContext().getSharedPreferences("temaPref", MODE_PRIVATE);
        int temaSelecionado = temaPref.getInt("temaSelecionado", 0);

        if (temaSelecionado == TEMA_CLARO) {
            kv = (KeyboardView) getLayoutInflater().inflate(R.layout.keyboard, null);
            keyboard = new Keyboard(this, R.xml.qwerty);
        }

        if (temaSelecionado == TEMA_ESCURO) {
            kv = (KeyboardView) getLayoutInflater().inflate(R.layout.keyboard, null);
            keyboard = new Keyboard(this, R.xml.qwerty_novo);
        }

        kv.setKeyboard(keyboard);
        kv.setOnKeyboardActionListener(this);

        //scrollView.addView(kv);
        //return myView;
        return kv;


    }

    @Override
    public void onStartInputView(EditorInfo info, boolean restarting) {
        setInputView(onCreateInputView());
    }

    @Override
    public void onPress(int primaryCode) {

        //switch (primaryCode)
    }

    @Override
    public void onRelease(int primaryCode) {

    }

    //TODO Função para tratamento das letras
    @Override
    public void onKey(int primaryCode, int[] keyCodes) {
        temaPref = getApplicationContext().getSharedPreferences("temaPref", MODE_PRIVATE);
        int temaSelecionado = temaPref.getInt("temaSelecionado", 0);
        InputConnection ic = getCurrentInputConnection();
        playClick(primaryCode);
        switch (primaryCode) {
            case Keyboard.KEYCODE_DELETE:
                ic.deleteSurroundingText(1, 0);
                break;
            case Keyboard.KEYCODE_SHIFT:
                this.shiftCount++;
                if (this.shiftCount < 2) {
                    isCaps = !isCaps;
                    keyboard.setShifted(isCaps);
                }

                if (this.shiftCount > 2) {
                    isCaps = false;
                    keyboard.setShifted(isCaps);
                }

                kv.invalidateAllKeys();

                break;

            case Keyboard.KEYCODE_DONE:
                ic.sendKeyEvent(new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_ENTER));
                break;
            case -3:
                if (temaSelecionado == TEMA_CLARO) {
                    keyboard = new Keyboard(this, R.xml.number);
                }

                if (temaSelecionado == TEMA_ESCURO) {
                    keyboard = new Keyboard(this, R.xml.number_novo);
                }

                kv.setKeyboard(keyboard);
                kv.setOnKeyboardActionListener(this);
                break;
            case 3:
                if (temaSelecionado == TEMA_CLARO) {
                    keyboard = new Keyboard(this, R.xml.qwerty);
                }

                if (temaSelecionado == TEMA_ESCURO) {
                    keyboard = new Keyboard(this, R.xml.qwerty_novo);
                }

                kv.setKeyboard(keyboard);
                kv.setOnKeyboardActionListener(this);
                break;
            case -100:
                keyboard = new Keyboard(this, R.xml.number_novo);
                kv.setKeyboard(keyboard);
                kv.setOnKeyboardActionListener(this);
                break;
            default:
                char code = (char) primaryCode;
                if (Character.isLetter(code) && isCaps) {
                    code = Character.toUpperCase(code);
                }
                ic.commitText(String.valueOf(code), 1);

                if (this.shiftCount <= 1) {
                    this.isCaps = false;
                    keyboard.setShifted(this.isCaps);
                    this.shiftCount = 0;
                }

                if (this.shiftCount > 2) {
                    this.isCaps = false;
                    keyboard.setShifted(this.isCaps);
                    this.shiftCount = 0;
                }
        }

    }

    @Override
    public void onText(CharSequence text) {

    }

    @Override
    public void swipeLeft() {
        if (isCaps) {
            keyboard = new Keyboard(this, R.xml.number);
            //Creates a new keyboard object.
            kv.setKeyboard(keyboard);
            //Assigns the new keyboard object to the keyboard view
            kv.invalidateAllKeys();
            //Makes android redraw the keyboard view, with the new layout.
            isCaps = false;
        } else {
            keyboard = new Keyboard(this, R.xml.qwerty);
            kv.setKeyboard(keyboard);
            kv.invalidateAllKeys();
            isCaps = true;
        }
    }

    @Override
    public void swipeRight() {
        if (isCaps) {
            keyboard = new Keyboard(this, R.xml.number);
            //Creates a new keyboard object.
            kv.setKeyboard(keyboard);
            //Assigns the new keyboard object to the keyboard view
            kv.invalidateAllKeys();
            //Makes android redraw the keyboard view, with the new layout.
            isCaps = false;
        } else {
            keyboard = new Keyboard(this, R.xml.qwerty);
            kv.setKeyboard(keyboard);
            kv.invalidateAllKeys();
            isCaps = true;
        }
    }

    @Override
    public void swipeDown() {

    }

    @Override
    public void swipeUp() {

    }

    //TODO Função para Click do botão
    private void playClick(int keyCode) {
        AudioManager am = (AudioManager) getSystemService(AUDIO_SERVICE);
        switch (keyCode) {
            case 32:
                am.playSoundEffect(AudioManager.FX_KEYPRESS_SPACEBAR);
                break;
            case Keyboard.KEYCODE_DONE:
            case 10:
                am.playSoundEffect(AudioManager.FX_KEYPRESS_RETURN);
                break;
            case Keyboard.KEYCODE_DELETE:
                am.playSoundEffect(AudioManager.FX_KEYPRESS_DELETE);
                break;
            default:
                am.playSoundEffect(AudioManager.FX_KEYPRESS_STANDARD);
        }
    }

}
