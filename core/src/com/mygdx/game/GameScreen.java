/*******************************************************************************
 * Copyright 2011 See AUTHORS file.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 ******************************************************************************/

package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.mygdx.game.map.MapGenerator;

/**
 * See: http://blog.xoppa.com/basic-3d-using-libgdx-2/
 * 
 * @author Xoppa
 */

public class GameScreen implements Screen {
  GameControl gameControl;

  private GameInputProcessor gameInputProcessor;

  private Bloxorz game;

  private float timeDelay;
  String restartStr = "Restart";
  String nextLevelStr = "Next Level";
  String mainMenuString = "Main Menu";
  String scoreStr = "Score: ";
  String levelStr = "Level: ";
  int level = 1;
  int score = 0;

  private class FinishGame {
    public Stage stage;
    private Skin skin;

    public FinishGame(final boolean isWin) {
    	String btnText=isWin?"Next Level":"Restart";
      stage = new Stage(new ScreenViewport());

      skin = new Skin(Gdx.files.internal("data/uiskin.json"));
      Button buttonMulti = new TextButton(btnText, skin, "default");
      Button buttonMainMenu = new TextButton(mainMenuString, skin, "default");
      buttonMulti.addListener(new ChangeListener() {

        @Override
        public void changed(ChangeEvent event, Actor actor) {
          // TODO Auto-generated method stub
          if (isWin) {
            gameControl.gamestate = GAMESTATE.NEXTLEVEL;
          } else
            gameControl.gamestate = GAMESTATE.RESTART;

        }
      });
      buttonMainMenu.addListener(new ChangeListener() {

        @Override
        public void changed(ChangeEvent event, Actor actor) {
          game.setScreen(new MenuScreen(game));
          dispose();
        }
      });
      Texture texture = new Texture(Gdx.files.internal("FinishScreen.png"));
      TextureRegion textureRegion = new TextureRegion(texture);
      TextureRegionDrawable bg = new TextureRegionDrawable(textureRegion);

      Table table = new Table();
      table.setFillParent(true);
      table.setBackground(bg);
      if (btnText.equals(nextLevelStr)) {

        Label scoreLabel = new Label(scoreStr + score, skin);
        table.add(scoreLabel);
      }
      
      float btnWidth=Gdx.graphics.getWidth()/4;
      float btnHeight=Gdx.graphics.getHeight()/8;
      table.row();
      Label levelLabel = new Label(levelStr + String.valueOf(level), skin);
      table.add(levelLabel);
      table.row();
      table.add(buttonMulti).width(btnWidth).height(btnHeight);
      table.row();
      table.add(buttonMainMenu).width(btnWidth).height(btnHeight);
      table.pack();
      stage.addActor(table);
    }

    public void render() {
      stage.act(Gdx.graphics.getDeltaTime());
      stage.draw();

    }

  }

  FinishGame finishGame;

  static GameScreen createGameScreen(Bloxorz game) {
    return new GameScreen(1, game);
  }

  static GameScreen createGameScreen(int level, Bloxorz game) {
    return new GameScreen(level, game);
  }

  public GameScreen(int level, Bloxorz game) {
    super();
    this.game = game;
    this.level = level;
    timeDelay = 0;
    finishGame = new FinishGame(false);
    newGame(true, level);
  }

  private void newGame(boolean newMap, int level) {
    MapGenerator mapGenerator = null;
    if (newMap == false) {
      mapGenerator = gameControl.mapTile.map;
      gameControl.dispose();
      gameControl = GameControl.createGameControl(mapGenerator);
    } else {
      if (gameControl != null)
        gameControl.dispose();
      gameControl = GameControl.createGameControl(level);
    }
    gameControl.gamestate = GAMESTATE.PLAYING;
    gameInputProcessor = new GameInputProcessor(this);
    InputMultiplexer inputMultiplexer = new InputMultiplexer();
    inputMultiplexer.addProcessor(gameInputProcessor);
    inputMultiplexer.addProcessor(new GestureDetector(gameInputProcessor));
    Gdx.input.setInputProcessor(inputMultiplexer);
  }

  public void render() {
    Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
    Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT
        | (Gdx.graphics.getBufferFormat().coverageSampling ? GL20.GL_COVERAGE_BUFFER_BIT_NV : 0));

    if (gameControl.gamestate == GAMESTATE.WIN || gameControl.gamestate == GAMESTATE.LOOSE) {
      timeDelay += Gdx.graphics.getDeltaTime();
      if (timeDelay > 1) {
        if (gameControl.gamestate == GAMESTATE.LOOSE)
          finishGame = new FinishGame(false);
        else if (gameControl.gamestate == GAMESTATE.WIN) {
          // set score
          int count = gameControl.brick.moveCount;
          score = Math.round(1f / count * 1000);
          game.getScore().updateLevel(level - 1, score);
          finishGame = new FinishGame(true);
        }
        timeDelay = 0;
        gameControl.gamestate = GAMESTATE.FINISH;
        Gdx.input.setInputProcessor(finishGame.stage);
      }
    }
    if (gameControl.gamestate == GAMESTATE.NEXTLEVEL) {
      level++;
      newGame(true, level);
      timeDelay = 0;
      gameControl.gamestate = GAMESTATE.PLAYING;
    } else if (gameControl.gamestate == GAMESTATE.RESTART) {
      newGame(false, level);
      timeDelay = 0;
      gameControl.gamestate = GAMESTATE.PLAYING;
    } else if (gameControl.gamestate == GAMESTATE.FINISH) {
      finishGame.render();
    } else if (gameControl.gamestate == GAMESTATE.PLAYING || gameControl.gamestate == GAMESTATE.WIN
        || gameControl.gamestate == GAMESTATE.LOOSE) {
      gameControl.update();
    }
  }

  @Override
  public void dispose() {
    gameControl.dispose();
  }

  @Override
  public void resize(int width, int height) {
    finishGame.stage.getViewport().update(width, height, true);
  }

  @Override
  public void pause() {}

  @Override
  public void resume() {}

  @Override
  public void show() {
    // TODO Auto-generated method stub

  }

  @Override
  public void render(float delta) {
    // TODO Auto-generated method stub
    render();

  }

  @Override
  public void hide() {
    // TODO Auto-generated method stub

  }
}
