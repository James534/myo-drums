package MIDI_Package;

import MIDI_Package.Screens.SplashScreen;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class MIDI_Main extends Game {

    public static final int WIDTH = 1280;
    public static final int HEIGHT = 720;

    public static final String fileName = "core/assets/";

	@Override
	public void create () {
        setScreen (new SplashScreen());
	}
}
