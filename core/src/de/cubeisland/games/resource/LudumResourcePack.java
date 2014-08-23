package de.cubeisland.games.resource;

import de.cubeisland.engine.reflect.Reflector;
import de.cubeisland.games.resource.bag.*;

import static com.badlogic.gdx.Files.FileType;

public class LudumResourcePack extends Resources {
    public final Textures textures;

    public LudumResourcePack(FileType fileType, Reflector reflector) {
        this.textures = new Textures(fileType);
    }
}
