package com.mygdx.game;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Json;

public class Score {
  private int highLevel;
  private List<Integer> scores;

  public Score() {
    super();
    highLevel = 0;
    scores = new ArrayList<Integer>();
    scores.add(new Integer(0));
  }

  public void updateLevel(int level, int score) {
    if (level == highLevel + 1) {
      scores.add(new Integer(score));
      highLevel++;
    } else if (level <= highLevel) {
      if (score > scores.get(level)) {
        scores.remove(level);
        scores.add(level, new Integer(score));
      }
    }
  }

  public int getScore(int level) {
    return scores.get(level).intValue();
  }

  public int getHighLevel() {
    return highLevel;
  }

  public void setHighLevel(int highLevel) {
    this.highLevel = highLevel;
  }

  public List<Integer> getScores() {
    return scores;
  }

  public void setScores(List<Integer> scores) {
    this.scores = scores;
  }

  public void saveData() {
    Json json = new Json();
    json.setUsePrototypes(false);
    FileHandle output = Gdx.files.external("highScore.json");
    output.writeString(json.toJson(this), false);
  }

  public static Score getData() {
    Json json = new Json();
    json.setUsePrototypes(false);
    FileHandle input = Gdx.files.external("highScore.json");
    if (input.exists())
      return json.fromJson(Score.class, input);
    else
      return new Score();
  }
}
