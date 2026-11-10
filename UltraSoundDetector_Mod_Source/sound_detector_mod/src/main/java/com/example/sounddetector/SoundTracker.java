package com.example.sounddetector;

import java.util.concurrent.CopyOnWriteArrayList;
import java.util.List;

public class SoundTracker {
    public static class DetectedSound {
        public String name;
        public double x, y, z;
        public float volume;
        public float pitch;
        public long timestamp;

        public DetectedSound(String name, double x, double y, double z, float volume, float pitch) {
            this.name = name;
            this.x = x;
            this.y = y;
            this.z = z;
            this.volume = volume;
            this.pitch = pitch;
            this.timestamp = System.currentTimeMillis();
        }
    }

    private static final List<DetectedSound> RECENT_SOUNDS = new CopyOnWriteArrayList<>();
    private static final long EXPIRE_TIME_MS = 4000; // 4 seconds

    public static void addSound(String name, double x, double y, double z, float volume, float pitch) {
        RECENT_SOUNDS.add(new DetectedSound(name, x, y, z, volume, pitch));
    }

    public static List<DetectedSound> getActiveSounds() {
        long now = System.currentTimeMillis();
        RECENT_SOUNDS.removeIf(sound -> (now - sound.timestamp) > EXPIRE_TIME_MS);
        return RECENT_SOUNDS;
    }
}
