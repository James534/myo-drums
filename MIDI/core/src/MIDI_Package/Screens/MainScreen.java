package MIDI_Package.Screens;

import MIDI_Package.InputHandler;
import MIDI_Package.MIDI_Main;
import com.badlogic.gdx.*;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton.ImageButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Button.ButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

import javax.xml.soap.Text;

/**
 * Created by James on 9/20/2014.
 */
public class MainScreen implements Screen {
    private Stage stage;

    private Skin skin;
    private Image background;
    private ImageButton[] drums;
    private Sound[] snare;
    private ImageButton[] hihat;
    private Sound[] hiHat;
    private ImageButton baseDrum;
    private Sound[] base;

    private InputMultiplexer multiplexer;
    private InputHandler inputHandler;

    public MainScreen(){
        stage = new Stage();
        Texture bg = new Texture(Gdx.files.internal(MIDI_Main.fileName + "Pictures/bg.png"));
        background = new Image(bg);

        skin = new Skin(Gdx.files.internal(MIDI_Main.fileName + "uiskin.json"));

        //sounds
        snare = new Sound[4];
        hiHat = new Sound[4];
        base = new Sound[4];
        for (int i = 0; i < 4; i++){
            snare[i] = Gdx.audio.newSound(Gdx.files.internal(MIDI_Main.fileName + "Sound/Snare"+(i+1)+".wav"));
            hiHat[i] = Gdx.audio.newSound(Gdx.files.internal(MIDI_Main.fileName + "Sound/Hihat"+(i+1)+".wav"));
            base[i] = Gdx.audio.newSound(Gdx.files.internal(MIDI_Main.fileName + "Sound/Base"+(i+1)+".wav"));
        }

        //textures and stuff
        Texture drum = new Texture(Gdx.files.internal(MIDI_Main.fileName + "Pictures/Drum0.png"));
        TextureRegion drumsUp = new TextureRegion(drum);
        drum  = new Texture(Gdx.files.internal(MIDI_Main.fileName + "Pictures/Drum1.png"));
        TextureRegion drumsDown = new TextureRegion(drum);

        //style
        ImageButtonStyle style = new ImageButtonStyle(skin.get(ButtonStyle.class));
        style.imageUp = new TextureRegionDrawable(drumsUp);
        //style.imageDown = new TextureRegionDrawable(drumsDown);
        style.imageChecked = new TextureRegionDrawable(drumsDown);

        drums = new ImageButton[4];
        for (int i = 0; i < drums.length; i++){
            drums[i] = new ImageButton(style);
            final int n = i;
            drums[i].addListener(new ClickListener(){
                public boolean touchDown(InputEvent event, float x, float y, int pointer, int button){
                    snare[n].play();
                    return true;
                }
            });
        }

        style = new ImageButtonStyle(skin.get(ButtonStyle.class));
        Texture symbol = new Texture(Gdx.files.internal(MIDI_Main.fileName + "Symbol0.png"));
        drumsUp = new TextureRegion(symbol);
        style.imageUp = new TextureRegionDrawable(drumsUp);
        symbol = new Texture(Gdx.files.internal(MIDI_Main.fileName + "Symbol1.png"));
        drumsDown = new TextureRegion(symbol);
        style.imageChecked = new TextureRegionDrawable(drumsDown);
        hihat = new ImageButton[4];
        for (int i = 0; i < 4; i++) {
            hihat[i] = new ImageButton(style);
            final int n = i;
            hihat[i].addListener(new ClickListener() {
                public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                    hiHat[n].play();
                    return true;
                }
            });
        }

        drums[0].setPosition(50, 100);
        drums[1].setPosition(350, 200);
        drums[2].setPosition(650, 200);
        drums[3].setPosition(950, 100);

        stage.addActor(background);
        stage.addActor(drums[0]);
        stage.addActor(drums[1]);
        stage.addActor(drums[2]);
        stage.addActor(drums[3]);
        stage.addActor(hihat[0]);

        multiplexer = new InputMultiplexer();
        inputHandler = new InputHandler(this);
        multiplexer.addProcessor(inputHandler);
        multiplexer.addProcessor(stage);

        Gdx.input.setInputProcessor(multiplexer);
    }

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
            counter = -1;
        }
        counter++;
    }

    public void playSound(int id){
        if (id < 0) return;
        if (id < 4){
            hiHat[id].play();
            hihat[id].setChecked(true);
        }else if (id < 8){
            snare[id%4].play();
            drums[id%4].setChecked(true);
        }else if (id < 12){
            base[id%4].play();
        }
    }

    private void resetButtons(){
        for (int i = 0; i < 4; i++){
            if (drums[i].isChecked())
                drums[i].toggle();
            if (hihat[i].isChecked())
                hihat[i].toggle();
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
