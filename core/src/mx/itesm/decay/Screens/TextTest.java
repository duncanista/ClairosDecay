package mx.itesm.decay.Screens;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.rafaskoberg.gdx.typinglabel.TypingAdapter;
import com.rafaskoberg.gdx.typinglabel.TypingConfig;
import com.rafaskoberg.gdx.typinglabel.TypingLabel;

import mx.itesm.decay.Generators.GenericScreen;

import static com.badlogic.gdx.scenes.scene2d.actions.Actions.*;

public class TextTest extends GenericScreen {
    Skin        skin;
    Stage       stage;
    SpriteBatch batch;
    TypingLabel label;
    TypingLabel labelEvent;
    TextButton  buttonPause;
    TextButton  buttonResume;
    TextButton  buttonRestart;
    TextButton  buttonSkip;



    public void adjustTypingConfigs() {
        // Only allow two chars per frame
        TypingConfig.CHAR_LIMIT_PER_FRAME = 2;

        // Change color used by CLEARCOLOR token
        TypingConfig.DEFAULT_CLEAR_COLOR = Color.WHITE;

        // Force bitmap fonts to use color markup
        TypingConfig.FORCE_COLOR_MARKUP_BY_DEFAULT = true;
    }

    public TypingLabel createTypingLabel() {
        // Create text with tokens
        final StringBuilder text = new StringBuilder();
        text.append("{SLOWER}{COLOR=SCARLET}{EASE=-8;2;1} Welcome,{WAIT} {VAR=title}!{ENDEASE}");
        text.append("{FAST}\n\n");
        text.append("{RESET} This is a {SHAKE}simple test{ENDSHAKE} to show you");
        text.append("{COLOR=ROYAL} {JUMP}how to make dialogues {SLOW}fun again!{ENDJUMP}{WAIT}");
        text.append("{NORMAL}{CLEARCOLOR}{SICK} With this library{ENDSICK} you can control the flow of the text with");
        text.append("{COLOR=#ff0000} {SHAKE=0.5;2;3}tokens{ENDSHAKE},{CLEARCOLOR}{WAIT=0.7}");
        text.append("{SPEED=2.50}{COLOR=LIME} making the text go really fast{WAIT}");
        text.append("{SPEED=0.25}{COLOR=FOREST} or {WAVE=0.66}extremely slow.{ENDWAVE}");
        text.append("{RESET} You can also wait for a {SHAKE=1;2;2}second{ENDSHAKE}{WAIT=1} {SHAKE=1;2;3}or two{ENDSHAKE}{WAIT=2},");
        text.append("{COLOR=LIME} just to catch an event in code{EVENT=sample}!{WAIT}");
        text.append("{NORMAL}\n\n");
        text.append("{COLOR=GOLDENROD}{SLOWER} {WAVE=2;1;1;3}Imagine the possibilities! =D{ENDWAVE}");

        // Create label
        final TypingLabel label = new TypingLabel(text, skin);

        // Make the label wrap to new lines, respecting the table's layout.
        label.setWrap(true);

        // Set variable replacements for the {VAR} token
        label.setVariable("title", "curious human");

        // Set an event listener for when the {EVENT} token is reached and for the char progression ends.
        label.setTypingListener(new TypingAdapter() {
            @Override
            public void event(String event) {
                System.out.println("Event: " + event);

                labelEvent.restart("{FASTER}{COLOR=GRAY}Event:{WAIT=0.1}{COLOR=LIME} " + event);
                labelEvent.clearActions();
                labelEvent.addAction(
                        sequence(
                                visible(true),
                                alpha(0),
                                alpha(1, 0.25f, Interpolation.pow2In),
                                delay(0.5f),
                                alpha(0, 2f, Interpolation.pow2)
                        )
                );
            }

            @Override
            public void end() {
                System.out.println("End");
            }
        });

        // Finally parse tokens in the label text.
        label.parseTokens();

        return label;
    }

    public void update(float delta) {
        stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
    }


    @Override
    public void show() {
        adjustTypingConfigs();

        batch = new SpriteBatch();
        skin = new Skin(Gdx.files.internal("skins/uiskin.json"));
        skin.getAtlas().getTextures().iterator().next().setFilter(TextureFilter.Nearest, TextureFilter.Nearest);
        float scale = 1;
        skin.getFont("default-font").getData().setScale(scale);
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);

        Table table = new Table();
        stage.addActor(table);
        table.setFillParent(true);

        label = createTypingLabel();

        labelEvent = new TypingLabel("", skin);
        labelEvent.setAlignment(Align.left, Align.center);
        labelEvent.pause();
        labelEvent.setVisible(false);

        buttonPause = new TextButton("Pause", skin);
        buttonPause.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                label.pause();
            }
        });

        buttonResume = new TextButton("Resume", skin);
        buttonResume.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                label.resume();
            }
        });

        buttonRestart = new TextButton("Restart", skin);
        buttonRestart.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                label.restart();
            }
        });

        buttonSkip = new TextButton("Skip", skin);
        buttonSkip.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                label.skipToTheEnd();
            }
        });

        table.pad(50f);
        table.debugCell();
        table.add(label).colspan(4).growX();
        table.row();
        table.add(labelEvent).colspan(4).align(Align.center);
        table.row().uniform().expand().growX().space(40).center();
        table.add(buttonPause, buttonResume, buttonRestart, buttonSkip);

        table.pack();
        Table.debugCellColor.set(Color.BLUE);
    }

    @Override
    public void render(float delta) {
        update(Gdx.graphics.getDeltaTime());

        Gdx.gl.glClearColor(0.1f, 0.1f, 0.1f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void dispose() {
        stage.dispose();
        skin.dispose();
    }



}
