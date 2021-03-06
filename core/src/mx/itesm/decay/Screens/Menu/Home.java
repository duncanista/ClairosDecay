package mx.itesm.decay.Screens.Menu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import mx.itesm.decay.Decay;
import mx.itesm.decay.Generators.GenericButton;
import mx.itesm.decay.Generators.GenericScreen;
import mx.itesm.decay.Screens.IntroScreen;
import mx.itesm.decay.Screens.LoadingScreen;
import mx.itesm.decay.Screens.Maps.FirstLevel;
import mx.itesm.decay.Screens.Maps.ThirdLevel;
import mx.itesm.decay.Screens.Screens;

public class Home extends GenericScreen{
    private final Decay game;

    private Texture menuBuildings;
    private Texture menuBackground;
    private Texture menuLogo;

    private GenericButton buttonStart;
    private GenericButton buttonStart1;

    private GenericButton characterSelection;


    private GenericButton buttonHelp;
    private GenericButton buttonSettings;

    private Stage sceneMenu;


    float menuBuildingsY;
    float menuBuildingsX = 200;

    float timeCounter = 0;

    public static Music menuTheme;


    //Loading
    private AssetManager manager;
    private boolean menuBoolean;



    public Home(Decay game) {
        this.game = game;
        manager = game.getAssetManager();
        menuBoolean = false;
    }

    @Override
    public void show() {
        // create stage
        sceneMenu = new Stage(view);

        loadBackground();
        loadButtons();
        loadMusic();

        //we pass the input control to the scene
        Gdx.input.setInputProcessor(sceneMenu);
        Gdx.input.setCatchBackKey(false);

    }

    public void loadMusic(){
        menuTheme = manager.get("Music/MainMenu.mp3");
        // help button
        if(Decay.prefs.getString("music").equals("ON")){
            menuTheme.play();
        }
    }

    public void loadBackground(){
        // creating background & logo
        menuBuildings = manager.get("menu/cd-menu-buildings.png");
        menuBackground = manager.get("menu/cd-menu-background.png");
        menuLogo = manager.get("cd-logo.png");
        // setting infinite building moving
        menuBuildings.setWrap(Texture.TextureWrap.Repeat, Texture.TextureWrap.Repeat);
    }

    public void loadButtons(){
        // press start button
        Texture textureStart = manager.get("menu/cd-button-start.png");
        buttonStart = new GenericButton(textureStart);
        buttonStart.setPosition(WIDTH/2 - buttonStart.getWidth()/2, HEIGHT/8 - buttonStart.getHeight()/2);
        buttonStart.setScale(-1200,500);
        buttonStart.getImageButton().addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                // response

                //menuTheme.stop();
                menuBoolean = true;
                game.setScreen(new LoadingScreen(game,Screens.LEVEL_ONE));

            }
        });

        buttonStart1 = new GenericButton(textureStart);
        buttonStart1.setPosition(WIDTH/2 - buttonStart.getWidth()/2, HEIGHT/8 - buttonStart.getHeight()/2);
        buttonStart1.setScale(1200,500);
        buttonStart1.getImageButton().addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                // response

                //menuTheme.stop();
                menuBoolean = true;
                game.setScreen(new LoadingScreen(game,Screens.LEVEL_ONE));

            }
        });






        // help button
        Texture textureHelp = manager.get("menu/cd-button-help.png");
        buttonHelp = new GenericButton(textureHelp);
        buttonHelp.setPosition(WIDTH - buttonHelp.getWidth() * 3/2f, HEIGHT - buttonHelp.getHeight() * 3/2f);
        buttonHelp.getImageButton().addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                // response
                game.setScreen(new About(game));

            }
        });
        // settings button
        Texture textureSettings = manager.get("menu/cd-button-settings.png");
        buttonSettings = new GenericButton(textureSettings);
        buttonSettings.setPosition(WIDTH - buttonSettings.getWidth()*3/2f, HEIGHT - buttonSettings.getHeight() * 6/2f);
        buttonSettings.getImageButton().addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                // response
                game.setScreen(new Settings(game));

            }
        });

        // characters selection button
        Texture textureCharacter = manager.get("menu/cd-button-character.png");
        characterSelection = new GenericButton(textureCharacter);
        characterSelection.setPosition(WIDTH - characterSelection.getWidth()*3/2f, HEIGHT - characterSelection.getHeight() * 9/2f);
        characterSelection.getImageButton().addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                // response
                game.setScreen(new CharacterSelection(game));

            }
        });


        sceneMenu.addActor(buttonStart.getImageButton());
        sceneMenu.addActor(buttonStart1.getImageButton());
        sceneMenu.addActor(buttonHelp.getImageButton());
        sceneMenu.addActor(buttonSettings.getImageButton());
        sceneMenu.addActor(characterSelection.getImageButton());

    }

    public void blinkButtonStart(){
        if(timeCounter > 0.75 && timeCounter < 1.5){
            buttonStart.remove();
        }else if(timeCounter > 1.5 && timeCounter < 2.25){
            sceneMenu.addActor(buttonStart.getImageButton());
            timeCounter = 0;
        }
    }



    @Override
    public void render(float delta) {
        timeCounter += delta;
        deleteScreen();
        blinkButtonStart();


        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        menuBuildingsX += 0.20f;

        menuBuildingsY = menuBuildings.getHeight() - HEIGHT - HEIGHT/10;
        batch.draw(menuBackground, 0, 0);

        batch.draw(menuLogo, (WIDTH/2 - menuLogo.getWidth()/2), (HEIGHT/2 - menuLogo.getHeight()/2) * 1.20f);
        batch.draw(menuBuildings, 0, menuBuildingsY, (int)menuBuildingsX, 0, (int)WIDTH, (int)HEIGHT);

        batch.end();

        sceneMenu.draw();

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void dispose() {
        sceneMenu.dispose();
        batch.dispose();

        if (menuBoolean) {
            manager.unload("menu/cd-menu-buildings.png");
            manager.unload("menu/cd-menu-background.png");
            manager.unload("cd-logo.png");
            manager.unload("backgrounds/cd-simple-background.png");
            manager.unload("menu/cd-menu-buildings.png");
            manager.unload("UI/simple-screen.png");
            manager.unload("menu/cd-about-instructions.png");
            manager.unload("menu/cd-about-title.png");
            manager.unload("menu/cd-settings-monitor.png");
            manager.unload("menu/cd-settings-title.png");
            manager.unload("menu/cd-button-start.png");
            manager.unload("menu/cd-button-help.png");
            manager.unload("menu/cd-button-settings.png");
            manager.unload("menu/cd-button-character.png");
            manager.unload("menu/cd-button-back.png");
            manager.unload("menu/cd-about-us.png");
            manager.unload("Music/MainMenu.mp3");
            menuTheme.dispose();

        }
    }

}
