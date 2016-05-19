package com.mygdx.game;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.List;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

public class MenuScreen implements Screen {
  private Stage stage;
  private Skin skin;

  private List<String> lst;
  private ScrollPane sPane;
  private TextButton buttonPlay;
  private TextButton buttonExit;
  private TextButton buttonToggleSound;
  private TextButton buttonCredits;
  private TextButton buttonHelp;
  private Image soundIcon;
  private Image background;
  private Table table;
  private Bloxorz game;

  public MenuScreen(Bloxorz game) {
    super();
    this.game = game;
    skin = game.getSkin();
  }

  @Override
  public void show() {
    stage = new Stage();

    java.util.List<String> gameLevels = new ArrayList<String>();
    if (game.getScore().getScore(0) != 0) {
      for (int i = 0; i <= game.getScore().getHighLevel(); i++) {
        gameLevels.add("Stage-" + String.valueOf(i + 1) + " /Best: " + game.getScore().getScore(i));
      }
      gameLevels.add("Stage-" + String.valueOf(game.getScore().getHighLevel() + 2));
    } else {
      gameLevels.add("Stage-" + 1);
    }

    lst = new List<String>(skin);
    lst.setItems(gameLevels.toArray(new String[gameLevels.size()]));
    sPane = new ScrollPane(lst, skin);
    buttonPlay = new TextButton("Start", skin);
    buttonExit = new TextButton("Exit", skin);
    buttonToggleSound = new TextButton("Toggle Sound", skin);
    buttonCredits = new TextButton("Credits", skin);
    buttonHelp = new TextButton("Help", skin);

    soundIcon = new Image();
    if (game.isSoundEnabled()) {
      soundIcon.setDrawable(skin.getDrawable("soundOn"));
    } else {
      soundIcon.setDrawable(skin.getDrawable("soundOff"));
    }
    background =
        new Image(new TextureRegionDrawable(new TextureRegion(new Texture(
            Gdx.files.internal("StartScreen.png")))));
    background.setBounds(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

    buttonPlay.addListener(new ClickListener() {

      @Override
      public void clicked(InputEvent event, float x, float y) {
        super.clicked(event, x, y);
        if (game.isSoundEnabled())
          game.getPressedSound().play();
        game.setScreen(GameScreen.createGameScreen(lst.getSelectedIndex() + 1, game));
      }

    });

    buttonToggleSound.addListener(new ClickListener() {

      @Override
      public void clicked(InputEvent event, float x, float y) {
        super.clicked(event, x, y);
        if (game.isSoundEnabled())
          game.getPressedSound().play();
        game.setSoundEnabled((game.isSoundEnabled()) ? false : true);
        if (game.isSoundEnabled()) {
          soundIcon.setDrawable(skin.getDrawable("soundOn"));
        } else {
          soundIcon.setDrawable(skin.getDrawable("soundOff"));
        }
      }

    });

    buttonHelp.addListener(new ClickListener() {

      @Override
      public void clicked(InputEvent event, float x, float y) {
        super.clicked(event, x, y);
        if (game.isSoundEnabled())
          game.getPressedSound().play();
        game.setScreen(new HelpScreen(game));
        dispose();
      }

    });

    buttonCredits.addListener(new ClickListener() {

      @Override
      public void clicked(InputEvent event, float x, float y) {
        super.clicked(event, x, y);
        if (game.isSoundEnabled())
          game.getPressedSound().play();
        game.setScreen(new CreditsScreen(game));
        dispose();
      }

    });

    buttonExit.addListener(new ClickListener() {

      @Override
      public void clicked(InputEvent event, float x, float y) {
        super.clicked(event, x, y);
        game.getScore().saveData();
        Gdx.app.exit();
      }

    });
    table = new Table();
    table.add(buttonPlay).pad(5).colspan(2).row();
    table.add(buttonToggleSound).pad(5);
    table.add(soundIcon).pad(5).row();
    table.add(buttonHelp).pad(5).colspan(2).row();
    table.add(buttonCredits).pad(5).colspan(2).row();
    table.add(buttonExit).pad(5).colspan(2).row();

    table.setBounds(Gdx.graphics.getWidth() * 2 / 5, 0, Gdx.graphics.getWidth() * 3 / 5,
        Gdx.graphics.getHeight());
    sPane.setBounds(0, 0, Gdx.graphics.getWidth() * 2 / 5, Gdx.graphics.getHeight());

    stage.addActor(background);
    stage.addActor(table);
    stage.addActor(sPane);

    Gdx.input.setInputProcessor(stage);
  }

  @Override
  public void render(float delta) {
    Gdx.gl.glClearColor(0, 0, 0, 1);
    Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
    stage.act();
    stage.draw();
  }

  @Override
  public void dispose() {
    stage.dispose();
  }

  @Override
  public void resize(int width, int height) {}

  @Override
  public void pause() {}

  @Override
  public void resume() {}

  @Override
  public void hide() {}
}
