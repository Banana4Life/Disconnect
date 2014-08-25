package de.cubeisland.games.resource.bag;

import com.badlogic.gdx.Files;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import de.cubeisland.games.resource.ResourceBag;

import java.lang.reflect.Field;

public class Animations extends ResourceBag<Animation> {
    public Animation characterleftfront;
    public Animation characterleftback;
    public Animation characterleftside;
    public Animation characterrightfront;
    public Animation characterrightback;
    public Animation characterrightside;
    public Animation securityfront;
    public Animation securityback;
    public Animation securityside;
    public Animation energybar;
    public Animation key;
    public Animation doorhorizontal;
    public Animation doorvertical;
    public Animation terminal;
    public Animation buttonpress;
    public Animation exit;

    public Animations(Files.FileType fileType) {
        super(fileType);
    }

    @Override
    protected Animation load(FileHandle basedir, Field field) {
        Texture tmp = new Texture(basedir.child(fieldToPath(field) + ".png"));
        TextureRegion[][] keyFrames2D = TextureRegion.split(tmp, tmp.getWidth(), 16);

        int rows = keyFrames2D.length;
        int cols = keyFrames2D[0].length;

        TextureRegion[] keyFrames = new TextureRegion[rows * cols];
        int index = 0;
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                keyFrames[index++] = keyFrames2D[i][j];
            }
        }

        if (field.getName().equals("doorhorizontal")) {
            return new Animation(0.025f, keyFrames);
        }
        return new Animation(0.1f, keyFrames);
    }
}
