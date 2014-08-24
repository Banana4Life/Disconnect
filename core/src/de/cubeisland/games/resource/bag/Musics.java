package de.cubeisland.games.resource.bag;

import com.badlogic.gdx.Files;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.files.FileHandle;
import de.cubeisland.games.resource.ResourceBag;

import java.lang.reflect.Field;

public class Musics extends ResourceBag<Music> {
    public Music maintheme;

    public Musics(Files.FileType fileType) {
        super(fileType);
    }

    @Override
    protected Music load(FileHandle basedir, Field field) {
        return Gdx.audio.newMusic(basedir.child(fieldToPath(field) + ".mp3"));
    }
}
