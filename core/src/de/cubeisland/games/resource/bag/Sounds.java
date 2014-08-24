package de.cubeisland.games.resource.bag;

import com.badlogic.gdx.Files;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.files.FileHandle;
import de.cubeisland.games.resource.ResourceBag;

import java.lang.reflect.Field;

public class Sounds extends ResourceBag<Sound> {

    public Sound step;

    public Sounds(Files.FileType fileType) {
        super(fileType);
    }

    @Override
    protected Sound load(FileHandle basedir, Field field) {
        return Gdx.audio.newSound(basedir.child(fieldToPath(field) + ".mp3"));
    }
}
