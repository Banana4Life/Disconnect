package de.cubeisland.games.resource.bag;

import com.badlogic.gdx.Files;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import de.cubeisland.games.resource.ResourceBag;

import java.lang.reflect.Field;

public class Textures extends ResourceBag<Texture> {
    public Texture tilemap;
    public Texture floor;
    public Texture wallbottom;
    public Texture wallleft;
    public Texture wallleftbottom;
    public Texture walllefttop;
    public Texture wallright;
    public Texture wallrightbottom;
    public Texture wallrighttop;
    public Texture walltop;

    public Textures(Files.FileType fileType) {
        super(fileType);
    }

    @Override
    protected Texture load(FileHandle basedir, Field field) {
        return new Texture(basedir.child(fieldToPath(field) + ".png"));
    }
}