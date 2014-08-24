package de.cubeisland.games.resource.bag;

import com.badlogic.gdx.Files;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import de.cubeisland.games.resource.ResourceBag;

import java.lang.reflect.Field;

public class Animations extends ResourceBag<Animation> {
    public Animation characterfront;
    public Animation characterback;
    public Animation characterside;
    public Animation characterleft;
    public Animation characterright;

    public Animations(Files.FileType fileType) {
        super(fileType);
    }

    @Override
    protected Animation load(FileHandle basedir, Field field) {
        if (field.getName().equals("characterleft") || field.getName().equals("characterright")) {
            return null;
        }

        TextureRegion[][] keyFrames2D = TextureRegion.split(new Texture(basedir.child(fieldToPath(field) + ".png")), 16, 16);

        int rows = keyFrames2D.length;
        int cols = keyFrames2D[0].length;

        TextureRegion[] keyFrames = new TextureRegion[rows * cols];
        int index = 0;
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                keyFrames[index++] = keyFrames2D[i][j];
            }
        }

        return new Animation(0.1f, keyFrames);
    }
}
