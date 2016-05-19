package com.mygdx.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.VertexAttributes.Usage;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.IntAttribute;
import com.badlogic.gdx.graphics.g3d.attributes.TextureAttribute;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;

public class SkyBox {
  private ModelInstance modelInstance;

  public SkyBox() {
    Material material = new Material();
    material.set(new IntAttribute(IntAttribute.CullFace),
        TextureAttribute.createDiffuse(new Texture("background.jpg")));
    float depth = Constant.unit * 100;
    float width = depth;
    float height = depth;

    modelInstance =
        new ModelInstance(new ModelBuilder().createBox(width, height, depth, material,
            Usage.Position | Usage.Normal | Usage.TextureCoordinates));
  }

  public void render(ModelBatch modelBatch) {
    modelBatch.render(modelInstance);
  }
}
