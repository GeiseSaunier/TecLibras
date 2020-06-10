package edu.ufam.icet.poundkeyboard;

import android.app.Service;
import android.content.Intent;
import android.inputmethodservice.InputMethodService;
import android.inputmethodservice.Keyboard;
import android.inputmethodservice.KeyboardView;
import android.media.AudioManager;
import android.os.IBinder;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputConnection;
import android.view.inputmethod.InputMethod;
import android.widget.CheckBox;
import android.widget.ImageView;


public class  PoundKeyboard extends InputMethodService implements KeyboardView.OnKeyboardActionListener{
    private KeyboardView kv;
    private Keyboard keyboard,numbers;

    private boolean isCaps=false;
    //TODO Função que cria o keyboard
    @Override
    public View onCreateInputView() {
        kv = (KeyboardView)getLayoutInflater().inflate(R.layout.keyboard,null);
        keyboard = new Keyboard(this, R.xml.qwerty);
        kv.setKeyboard(keyboard);
        kv.setOnKeyboardActionListener(this);

        return kv;
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
        InputConnection ic = getCurrentInputConnection();
        playClick(primaryCode);
        switch(primaryCode){
            case Keyboard.KEYCODE_DELETE :
                ic.deleteSurroundingText(1, 0);
                break;
            case Keyboard.KEYCODE_SHIFT:
                isCaps = !isCaps;
                keyboard.setShifted(isCaps);
                kv.invalidateAllKeys();
                break;
            case Keyboard.KEYCODE_DONE:
                ic.sendKeyEvent(new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_ENTER));
                break;
            case -3 :
                keyboard = new Keyboard(this, R.xml.number);
                kv.setKeyboard(keyboard);
                kv.setOnKeyboardActionListener(this);
                break;
            case 3 :
                keyboard = new Keyboard(this, R.xml.qwerty);
                kv.setKeyboard(keyboard);
                kv.setOnKeyboardActionListener(this);
                break;
            case -100 :
                keyboard = new Keyboard(this, R.xml.emoji1);
                kv.setKeyboard(keyboard);
                kv.setOnKeyboardActionListener(this);
                break;
            default:
                char code = (char)primaryCode;
                if(Character.isLetter(code) && isCaps){
                    code = Character.toUpperCase(code);
                }
                ic.commitText(String.valueOf(code),1);
        }

    }

    @Override
    public void onText(CharSequence text) {

    }

    @Override
    public void swipeLeft() {
        if(isCaps)
        {
            keyboard = new Keyboard(this, R.xml.number);
            //Creates a new keyboard object.
            kv.setKeyboard(keyboard);
            //Assigns the new keyboard object to the keyboard view
            kv.invalidateAllKeys();
            //Makes android redraw the keyboard view, with the new layout.
            isCaps = false;
        }
        else
        {
            keyboard = new Keyboard(this, R.xml.qwerty);
            kv.setKeyboard(keyboard);
            kv.invalidateAllKeys();
            isCaps = true;
        }
    }

    @Override
    public void swipeRight() {
        if(isCaps)
        {
            keyboard = new Keyboard(this, R.xml.number);
            //Creates a new keyboard object.
            kv.setKeyboard(keyboard);
            //Assigns the new keyboard object to the keyboard view
            kv.invalidateAllKeys();
            //Makes android redraw the keyboard view, with the new layout.
            isCaps = false;
        }
        else
        {
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
    private void playClick(int keyCode){
        AudioManager am = (AudioManager)getSystemService(AUDIO_SERVICE);
        switch(keyCode){
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
            default: am.playSoundEffect(AudioManager.FX_KEYPRESS_STANDARD);
        }
    }
}
