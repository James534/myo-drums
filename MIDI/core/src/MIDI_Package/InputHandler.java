package MIDI_Package;

import MIDI_Package.Screens.MainScreen;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;

/**
 * Created by James on 9/20/2014.
 */
public class InputHandler implements InputProcessor{

    private MainScreen mainScreen;

    public InputHandler(MainScreen mainScreen){
        this.mainScreen = mainScreen;
    }

    @Override
    public boolean keyDown(int keycode) {
        switch (keycode){
            case (Input.Keys.Q):
                mainScreen.playSound(0);
                break;
            case (Input.Keys.W):
                mainScreen.playSound(1);
                break;
            case (Input.Keys.E):
                mainScreen.playSound(2);
                break;
            case (Input.Keys.R):
                mainScreen.playSound(3);
                break;
            case (Input.Keys.A):
                mainScreen.playSound(4);
                break;
            case (Input.Keys.S):
                mainScreen.playSound(5);
                break;
            case (Input.Keys.D):
                mainScreen.playSound(6);
                break;
            case (Input.Keys.C):
                mainScreen.playSound(7);
                break;
            case (Input.Keys.I):
                mainScreen.startRecord(true);
                break;
            case (Input.Keys.O):
                mainScreen.startRecord(false);
                break;
            case (Input.Keys.P):
                mainScreen.startPlayback();
                break;
        }
        return true;
    }

    @Override
    public boolean keyUp(int keycode) {
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        return false;
    }
}
