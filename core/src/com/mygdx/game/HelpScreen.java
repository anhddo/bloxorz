package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextArea;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

public class HelpScreen implements Screen {

  private Stage stage;
  private Skin skin;
  private TextArea textArea;
  private TextButton buttonBack;
  private Bloxorz game;

  public HelpScreen(Bloxorz game) {
    super();
    this.game = game;
    skin = game.getSkin();
  }

  @Override
  public void show() {
    stage = new Stage();
    buttonBack = new TextButton("Back", skin);
    buttonBack.setX(Gdx.graphics.getWidth() - 20 - buttonBack.getWidth());
    buttonBack.setY(20);
    String str = "";
    str = str.concat("Bloxorz Game\n");
    str = str.concat("Goal: Try to roll the woody block over the bricks to the black hole.\n");
    str = str.concat("Control: Drag your fingle to roll the block");
    textArea = new TextArea(str, skin);
    textArea.setX(20);
    textArea.setY(20 + buttonBack.getHeight());
    textArea.setWidth(Gdx.graphics.getWidth() - textArea.getX());
    textArea.setHeight(Gdx.graphics.getHeight() - 20 - textArea.getY());
    buttonBack.addListener(new ClickListener() {

      @Override
      public void clicked(InputEvent event, float x, float y) {
        super.clicked(event, x, y);
        if (game.isSoundEnabled())
          game.getPressedSound().play();
        game.setScreen(new MenuScreen(game));
        dispose();
      }
    });
    stage.addActor(textArea);
    stage.addActor(buttonBack);
    Gdx.input.setInputProcessor(stage);
  }

  @Override
  public void render(float delta) {
    Gdx.gl20.glClearColor(0, 0, 0, 1);
    Gdx.gl20.glClear(GL20.GL_COLOR_BUFFER_BIT);
    stage.act();
    stage.draw();
  }

  @Override
  public void resize(int width, int height) {}

  @Override
  public void pause() {}

  @Override
  public void resume() {}

  @Override
  public void hide() {}

  @Override
  public void dispose() {
    stage.dispose();
  }

}
