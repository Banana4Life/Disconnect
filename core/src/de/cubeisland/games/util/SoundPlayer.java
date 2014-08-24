package de.cubeisland.games.util;

import com.badlogic.gdx.audio.Sound;

public class SoundPlayer {
    private final Sound sound;

    public SoundPlayer(Sound sound) {
        if (sound == null) {
            throw new IllegalArgumentException("The sound may not be null!");
        }
        this.sound = sound;
    }

    public SoundInstance start () {
        return new SoundInstance(this.sound.play());
    }

    public SoundInstance start(float volume) {
        return new SoundInstance(this.sound.play(volume));
    }

    public class SoundInstance {
        private final long id;
        private boolean looping = false;
        private boolean paused = false;

        public SoundInstance(long id) {
            this.id = id;
        }

        public SoundInstance pause() {
            sound.pause(id);
            this.paused = true;
            return this;
        }

        public SoundInstance resume() {
            sound.resume(id);
            this.paused = false;
            return this;
        }

        public boolean isPaused() {
            return this.paused;
        }

        public void stop() {
            sound.stop(id);
        }

        public SoundInstance volume(float volume) {
            sound.setVolume(id, volume);
            return this;
        }

        public SoundInstance looping(boolean looping) {
            sound.setLooping(id, looping);
            this.looping = looping;
            return this;
        }

        public boolean isLooping() {
            return this.looping;
        }
    }
}
