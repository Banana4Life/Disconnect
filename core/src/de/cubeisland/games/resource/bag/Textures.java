package de.cubeisland.games.resource.bag;

import com.badlogic.gdx.Files;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import de.cubeisland.games.resource.ResourceBag;

import java.lang.reflect.Field;

public class Textures extends ResourceBag<TextureRegion> {
    public TextureRegion floor;
    public TextureRegion wall;
    public TextureRegion wallbottom;
    public TextureRegion wallbottomleft;
    public TextureRegion wallbottomright;
    public TextureRegion wallbottomboth;
    public TextureRegion wallboth;
    public TextureRegion wallbothtop;
    public TextureRegion wallleft;
    public TextureRegion wallleftbottom;
    public TextureRegion walllefttop;
    public TextureRegion wallright;
    public TextureRegion wallrightbottom;
    public TextureRegion wallrighttop;
    public TextureRegion walltop;
    public TextureRegion walltopleft;
    public TextureRegion walltopright;
    public TextureRegion walltopboth;
    public TextureRegion dividerleft;
    public TextureRegion dividerright;
    public TextureRegion iteminhand;
    public TextureRegion logo;
    public TextureRegion exittipp;
    public TextureRegion activatoron;
    public TextureRegion activatoroff;

    public Textures(Files.FileType fileType) {
        super(fileType);
    }

    @Override
    protected TextureRegion load(FileHandle basedir, Field field) {
        return new TextureRegion(new Texture(basedir.child(fieldToPath(field) + ".png")));
    }
}
