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
import com.badlogic.gdx.utils.Array;

import java.util.LinkedList;

/**
 * Created by James on 9/20/2014.
 */
public class MainScreen implements Screen {
    private Stage stage;

    private Skin skin;
    private Image background;
    private ImageButton[] images;       //crash cymbol, hightom, lowtom, ride cymbol
    private Sound[] sounds;             //hihat, snare, basedrum
    private int[] fadeOut;
    private static final int MAX_FADE = 3 ;

    private InputMultiplexer multiplexer;
    private InputHandler inputHandler;


    private int counter;
    private ImageButton recordButton;
    private boolean record;
    private boolean playback;
    private LinkedList<Integer> recording;

    public MainScreen(){
        stage = new Stage();
        Texture bg = new Texture(Gdx.files.internal(MIDI_Main.fileName + "Pictures/drum.png"));
        background = new Image(bg);

        skin = new Skin(Gdx.files.internal(MIDI_Main.fileName + "uiskin.json"));
        recording = new LinkedList<Integer>();

        //sounds
        sounds = new Sound[8];
        sounds[0] = Gdx.audio.newSound(Gdx.files.internal(MIDI_Main.fileName + "Sound/crashCymbal.wav"));
        sounds[1] = Gdx.audio.newSound(Gdx.files.internal(MIDI_Main.fileName + "Sound/highTom1.wav"));
        sounds[2] = Gdx.audio.newSound(Gdx.files.internal(MIDI_Main.fileName + "Sound/highTom2.wav"));
        sounds[3] = Gdx.audio.newSound(Gdx.files.internal(MIDI_Main.fileName + "Sound/rideCymbal.wav"));
        sounds[4] = Gdx.audio.newSound(Gdx.files.internal(MIDI_Main.fileName + "Sound/lowTom.wav"));
        sounds[5] = Gdx.audio.newSound(Gdx.files.internal(MIDI_Main.fileName + "Sound/Hihat.wav"));
        sounds[6] = Gdx.audio.newSound(Gdx.files.internal(MIDI_Main.fileName + "Sound/Snare1.wav"));
        sounds[7] = Gdx.audio.newSound(Gdx.files.internal(MIDI_Main.fileName + "Sound/Base1.wav"));

        //textures and stuff
        images = new ImageButton[8];
        images[0] = new ImageButton(generateStyle("null0", "crashCymbal"));
        images[1] = new ImageButton(generateStyle("null1", "highTom1"));
        images[2] = new ImageButton(generateStyle("null2", "highTom2"));
        images[3] = new ImageButton(generateStyle("null3", "rideCymbal"));
        images[4] = new ImageButton(generateStyle("null4", "hiHat"));
        images[5] = new ImageButton(generateStyle("null5", "snare"));
        images[6] = new ImageButton(generateStyle("null6", "lowTom"));
        images[7] = new ImageButton(generateStyle("null7", "base"));
        fadeOut = new int[8];
        for (int i = 0; i < images.length; i++) {
            fadeOut[i] = 0;
            final int n = i;
            images[i].addListener(new ClickListener() {
                public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                    sounds[n].play();
                    return true;
                }
            });
        }

        images[0].setPosition(201, 603);
        images[1].setPosition(415, 438);
        images[2].setPosition(576, 435);
        images[3].setPosition(727, 560);
        images[4].setPosition(82, 468);
        images[5].setPosition(312, 307);
        images[6].setPosition(728, 313);
        images[7].setPosition(519, 171);

        recordButton = new ImageButton(generateStyle("notRecording", "Recording"));
        recordButton.addListener(new ClickListener() {
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                startRecord(!record);
                return true;
            }
            });
        recordButton.setPosition(950, 650);

        stage.addActor(background);
        for (int i = 0; i < images.length; i++){
            stage.addActor(images[i]);
        }
        stage.addActor(recordButton);

        multiplexer = new InputMultiplexer();
        inputHandler = new InputHandler(this);
        multiplexer.addProcessor(inputHandler);
        multiplexer.addProcessor(stage);

        Gdx.input.setInputProcessor(multiplexer);
    }

    private int temp;
    private void update(){
        if (record){
            recording.add(-1);
        }else if (playback && counter < recording.size()){
            temp = recording.get(counter);
            if (temp != -1){
                playSound(temp);
            }
            counter++;
            if (counter == recording.size()) {
                playback = false;
                counter = 0;
            }
        }
    }

    public void startRecord(boolean isTrue){
        record = isTrue;
        recordButton.setChecked(isTrue);
        if (isTrue){
            recording.clear();
        }
    }
    public void startPlayback(){
        startRecord(false);
        playback = true;
        counter = 0;
    }


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
        if (record){
            recording.add(id);
        }
    }

    private void resetButtons(){
        for (int i = 0; i < images.length; i++){
            if (images[i].isChecked()){
                fadeOut[i]++;
                if (fadeOut[i] >= MAX_FADE) {
                    fadeOut[i] = 0;
                    images[i].toggle();
                }
            }
        }
    }

    @Override
    public void show() {
    }

    @Override
    public void render(float delta) {
        update();
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
