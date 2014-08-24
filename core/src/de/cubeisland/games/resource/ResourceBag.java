package de.cubeisland.games.resource;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Disposable;

import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.badlogic.gdx.Files.FileType;

public abstract class ResourceBag<T> implements Disposable {
    protected final List<T> resources;
    private final Class<T> type;
    private boolean built = false;
    private FileType fileType = FileType.Internal;

    @SuppressWarnings("unchecked")
    protected ResourceBag(FileType fileType) {
        this.fileType = fileType;

        Type t = this.getClass().getGenericSuperclass();
        Type param = ((ParameterizedType) t).getActualTypeArguments()[0];
        if (!(param instanceof Class)) {
            throw new IllegalArgumentException("Field to detect our type!");
        }
        this.type = (Class<T>) param;
        this.resources = new ArrayList<>();
    }

    protected static String fieldToPath(Field field) {
        return fieldNameToPath(field.getName());
    }

    protected static String fieldNameToPath(String fieldName) {
        char[] chars = fieldName.toCharArray();
        if (chars.length == 0) {
            throw new IllegalArgumentException("Empty string is not a valid field name!");
        }

        StringBuilder path = new StringBuilder().append(Character.toLowerCase(chars[0]));

        char c;
        for (int i = 1; i < chars.length; i++) {
            c = chars[i];
            if (Character.isUpperCase(c)) {
                path.append(File.separatorChar);
                c = Character.toLowerCase(c);
            }
            path.append(c);
        }

        return path.toString();
    }

    public List<T> getResources() {
        return Collections.unmodifiableList(this.resources);
    }

    public void build() {
        if (built) {
            return;
        }
        built = true;
        FileHandle basedir = fileHandle(getClass().getSimpleName().toLowerCase());
        Field[] fields = this.getClass().getFields();

        for (Field field : fields) {
            if (Modifier.isPublic(field.getModifiers()) && this.type.isAssignableFrom(field.getType())) {
                try {
                    T resource = load(basedir, field);
                    field.set(this, resource);
                    this.resources.add(resource);
                } catch (IllegalAccessException e) {
                    throw new RuntimeException("Unable to access a resource field!", e);
                }
            }
        }
    }

    protected abstract T load(FileHandle basedir, Field field);

    protected FileHandle fileHandle(String path) {
        return Gdx.files.getFileHandle(path, fileType);
    }

    protected FileHandle fieldToFileHandle(Field field, FileHandle basedir) {
        String path = fieldToPath(field);
        if (basedir == null) {
            return fileHandle(path);
        }
        return basedir.child(path);
    }

    @Override
    public void dispose() {
        if (Disposable.class.isAssignableFrom(this.type)) {
            for (T resource : this.resources) {
                ((Disposable) resource).dispose();
            }
        }
    }

    public static class MissingResourceException extends RuntimeException {
        public MissingResourceException(String message) {
            super(message);
        }
    }
}
