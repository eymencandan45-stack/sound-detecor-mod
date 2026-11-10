package com.example.sounddetector.mixin;

import com.example.sounddetector.SoundTracker;
import net.minecraft.client.sound.SoundInstance;
import net.minecraft.client.sound.SoundSystem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(SoundSystem.class)
public class SoundEngineMixin {

    @Inject(method = "play(Lnet/minecraft/client/sound/SoundInstance;)V", at = @At("HEAD"))
    private void onPlaySound(SoundInstance sound, CallbackInfo ci) {
        if (sound == null || sound.getId() == null) return;

        String soundId = sound.getId().toString();
        double x = sound.getX();
        double y = sound.getY();
        double z = sound.getZ();
        float volume = sound.getVolume();
        float pitch = sound.getPitch();

        // Register sound event instantly with max sensitivity
        SoundTracker.addSound(soundId, x, y, z, volume, pitch);
    }
}
