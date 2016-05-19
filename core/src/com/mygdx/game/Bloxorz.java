package com.mygdx.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

public class Bloxorz extends Game {

  private TextureAtlas atlas;
  private Skin skin;
  private boolean soundEnabled;
  private Sound pressedSound;
  private Score score;

  @Override
  public void create() {
    atlas = new TextureAtlas(Gdx.files.internal("ui.pack"));
    skin = new Skin(Gdx.files.internal("skin.json"), atlas);
    soundEnabled = true;
    pressedSound = Gdx.audio.newSound(Gdx.files.internal("pressedSound.mp3"));
    score = Score.getData();
    this.setScreen(new MenuScreen(this));
  }

  @Override
  public void render() {
    super.render();
  }

  @Override
  public void dispose() {
    atlas.dispose();
    skin.dispose();
    super.dispose();
  }

  public TextureAtlas getAtlas() {
    return atlas;
  }

  public Skin getSkin() {
    return skin;
  }

  public boolean isSoundEnabled() {
    return soundEnabled;
  }

  public void setSoundEnabled(boolean soundEnabled) {
    this.soundEnabled = soundEnabled;
  }

  public Sound getPressedSound() {
    return pressedSound;
  }

  public Score getScore() {
    return score;
  }
}
