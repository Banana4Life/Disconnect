package de.cubeisland.games.resource;

import de.cubeisland.engine.reflect.Reflector;
import de.cubeisland.games.resource.bag.Animations;
import de.cubeisland.games.resource.bag.Musics;
import de.cubeisland.games.resource.bag.Textures;

import static com.badlogic.gdx.Files.FileType;

public class LudumResourcePack extends Resources {
    public final Textures textures;
    public final Animations animations;
    public final Musics musics;

    public LudumResourcePack(FileType fileType, Reflector reflector) {
        this.textures = new Textures(fileType);
        this.animations = new Animations(fileType);
        this.musics = new Musics(fileType);
    }
}
