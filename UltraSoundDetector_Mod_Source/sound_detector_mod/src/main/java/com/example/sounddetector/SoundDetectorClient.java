package com.example.sounddetector;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.util.math.Vec3d;

import java.util.List;

public class SoundDetectorClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        HudRenderCallback.EVENT.register(this::renderHUD);
    }

    private void renderHUD(DrawContext drawContext, float tickDelta) {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.player == null || client.world == null) return;

        TextRenderer textRenderer = client.textRenderer;
        Vec3d playerPos = client.player.getPos();

        List<SoundTracker.DetectedSound> sounds = SoundTracker.getActiveSounds();

        // Render Title
        drawContext.drawText(textRenderer, "=== ULTRA SOUND DETECTOR HUD ===", 10, 10, 0x00FF00, true);

        int yOffset = 22;
        int maxLines = 12;
        int count = 0;

        for (int i = sounds.size() - 1; i >= 0 && count < maxLines; i--) {
            SoundTracker.DetectedSound s = sounds.get(i);
            
            double dist = Math.sqrt(
                Math.pow(s.x - playerPos.x, 2) +
                Math.pow(s.y - playerPos.y, 2) +
                Math.pow(s.z - playerPos.z, 2)
            );

            String formattedName = s.name.replace("minecraft:", "");
            String info = String.format("[%.1fm] %s | Pos: %.0f, %.0f, %.0f | Vol: %.2f", 
                dist, formattedName, s.x, s.y, s.z, s.volume);

            // Dynamic color fading based on age
            long age = System.currentTimeMillis() - s.timestamp;
            int alpha = (int) (255 * (1.0 - (double) age / 4000.0));
            alpha = Math.max(20, Math.min(255, alpha));
            int color = (alpha << 24) | 0x00FFFF;

            drawContext.drawText(textRenderer, info, 10, yOffset, color, true);
            yOffset += 10;
            count++;
        }
    }
}
