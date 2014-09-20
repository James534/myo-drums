package MIDI_Package.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import MIDI_Package.MIDI_Main;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
        config.width = MIDI_Main.WIDTH;
        config.height = MIDI_Main.HEIGHT;
		new LwjglApplication(new MIDI_Main(), config);
	}
}
