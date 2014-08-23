package de.cubeisland.games.resource.bag;

import com.badlogic.gdx.Files;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import de.cubeisland.games.resource.ResourceBag;

import java.lang.reflect.Field;

public class Textures extends ResourceBag<Texture> {
    public Texture floor;
    public Texture wall;
    public Texture wallbottom;
    public Texture wallbottomleft;
    public Texture wallbottomright;
    public Texture wallbottomboth;
    public Texture wallboth;
    public Texture wallbothtop;
    public Texture wallleft;
    public Texture wallleftbottom;
    public Texture walllefttop;
    public Texture wallright;
    public Texture wallrightbottom;
    public Texture wallrighttop;
    public Texture walltop;
    public Texture walltopleft;
    public Texture walltopright;
    public Texture walltopboth;
    public Texture dividerleft;
    public Texture dividerright;

    public Textures(Files.FileType fileType) {
        super(fileType);
    }

    @Override
    protected Texture load(FileHandle basedir, Field field) {
        return new Texture(basedir.child(fieldToPath(field) + ".png"));
    }
}