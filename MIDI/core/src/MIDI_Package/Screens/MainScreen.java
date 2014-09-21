package MIDI_Package.Screens;

import MIDI_Package.InputHandler;
import MIDI_Package.MIDI_Main;
import com.badlogic.gdx.*;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton.ImageButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Button.ButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

/**
 * Created by James on 9/20/2014.
 */
public class MainScreen implements Screen {
    private Stage stage;

    private Skin skin;
    private Image background;
    private ImageButton[] images;       //crash cymbol, hightom, lowtom, ride cymbol
    private Sound[] sounds;             //hihat, snare, basedrum

    private InputMultiplexer multiplexer;
    private InputHandler inputHandler;

    public MainScreen(){
        stage = new Stage();
        Texture bg = new Texture(Gdx.files.internal(MIDI_Main.fileName + "Pictures/bg.png"));
        background = new Image(bg);

        skin = new Skin(Gdx.files.internal(MIDI_Main.fileName + "uiskin.json"));

        //sounds
        sounds = new Sound[7];
        sounds[0] = Gdx.audio.newSound(Gdx.files.internal(MIDI_Main.fileName + "Sound/crashCymbal.wav"));
        sounds[1] = Gdx.audio.newSound(Gdx.files.internal(MIDI_Main.fileName + "Sound/highTom.wav"));
        sounds[2] = Gdx.audio.newSound(Gdx.files.internal(MIDI_Main.fileName + "Sound/lowTom.wav"));
        sounds[3] = Gdx.audio.newSound(Gdx.files.internal(MIDI_Main.fileName + "Sound/rideCymbal.wav"));
        sounds[4] = Gdx.audio.newSound(Gdx.files.internal(MIDI_Main.fileName + "Sound/Hihat.wav"));
        sounds[5] = Gdx.audio.newSound(Gdx.files.internal(MIDI_Main.fileName + "Sound/Snare1.wav"));
        sounds[6] = Gdx.audio.newSound(Gdx.files.internal(MIDI_Main.fileName + "Sound/Base1.wav"));

        //textures and stuff
        images = new ImageButton[7];
        images[0] = new ImageButton(generateStyle("DrumS0", "DrumS1"));
        images[1] = new ImageButton(generateStyle("DrumS0", "DrumS1"));
        images[2] = new ImageButton(generateStyle("DrumS0", "DrumS1"));
        images[3] = new ImageButton(generateStyle("DrumS0", "DrumS1"));
        images[4] = new ImageButton(generateStyle("DrumS0", "DrumS1"));
        images[5] = new ImageButton(generateStyle("DrumS0", "DrumS1"));
        images[6] = new ImageButton(generateStyle("DrumS0", "DrumS1"));
        for (int i = 0; i < images.length; i++) {
            final int n = i;
            images[i].addListener(new ClickListener() {
                public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                    sounds[n].play();
                    return true;
                }
            });
        }

        images[0].setPosition(140, 400);
        images[1].setPosition(390, 400);
        images[2].setPosition(640, 400);
        images[3].setPosition(890, 400);
        images[4].setPosition(250, 150);
        images[5].setPosition(600, 150);
        images[6].setPosition(900, 150);

        stage.addActor(background);
        for (int i = 0; i < images.length; i++){
            stage.addActor(images[i]);
        }

        multiplexer = new InputMultiplexer();
        inputHandler = new InputHandler(this);
        multiplexer.addProcessor(inputHandler);
        multiplexer.addProcessor(stage);

        Gdx.input.setInputProcessor(multiplexer);
    }

    /*
    private int counter;
    private int delay = 7;
    private void update() {
        if (counter == delay) {
            hiHat[0].play();
            base[0].play();
        }else if (counter == delay * 3) {
            hiHat[0].play();
        }else if (counter == delay * 4){
            base[0].play();
        }else if (counter == delay*5){
            hiHat[0].play();
            snare[0].play();
        }else if (counter == delay*7){
            hiHat[0].play();
            base[0].play();
        }else if (counter == delay*9){
            hiHat[0].play();
        }else if (counter == delay*11){
            hiHat[0].play();
            base[0].play();
        }else if (counter == delay*13){
            hiHat[0].play();
            snare[0].play();
        }else if (counter == delay*15){
            hiHat[0].play();
            counter = 0;
        }
        counter++;
    }*/

    private ImageButtonStyle generateStyle(String f1, String f2){
        Texture texture = new Texture(Gdx.files.internal(MIDI_Main.fileName + "Pictures/"+f1+".png"));
        TextureRegion up = new TextureRegion(texture);
        texture  = new Texture(Gdx.files.internal(MIDI_Main.fileName + "Pictures/"+f2+".png"));
        TextureRegion down = new TextureRegion(texture);

        //style
        ImageButtonStyle style = new ImageButtonStyle(skin.get(ButtonStyle.class));
        style.imageUp = new TextureRegionDrawable(up);
        //style.imageDown = new TextureRegionDrawable(drumsDown);
        style.imageChecked = new TextureRegionDrawable(down);
        return style;
    }

    public void playSound(int id){
        if (id < 0)return;
        sounds[id].play();
        images[id].toggle();
    }

    private void resetButtons(){
        for (int i = 0; i < images.length; i++){
            if (images[i].isChecked())
                images[i].toggle();
        }
    }

    @Override
    public void show() {
    }

    @Override
    public void render(float delta) {
        //update();
        Gdx.gl.glClearColor(0,0,0,1);               //sets color to black
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);   //clear the batch
        stage.act();
        stage.draw();
        resetButtons();
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height);
    }

    @Override
    public void resume() {
    }

    @Override
    public void pause() {
    }

    @Override
    public void hide() {
    }

    @Override
    public void dispose() {
    }
}
