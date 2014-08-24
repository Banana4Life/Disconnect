package de.cubeisland.games.resource.bag;

import com.badlogic.gdx.Files;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.files.FileHandle;
import de.cubeisland.games.resource.ResourceBag;
import de.cubeisland.games.util.SoundPlayer;

import java.lang.reflect.Field;

public class Sounds extends ResourceBag<SoundPlayer> {

    public SoundPlayer step;
    public SoundPlayer door;
    public SoundPlayer timer;
    public SoundPlayer pickup;

    public Sounds(Files.FileType fileType) {
        super(fileType);
    }

    @Override
    protected SoundPlayer load(FileHandle basedir, Field field) {
        return new SoundPlayer(Gdx.audio.newSound(basedir.child(fieldToPath(field) + ".mp3")));
    }
}
