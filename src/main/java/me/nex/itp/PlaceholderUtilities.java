package me.nex.itp;

import me.nex.itp.mixin.TextRendererAccessor;
import me.nex.itp.params.PlaceholderParams;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.FontStorage;
import net.minecraft.client.font.Glyph;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.NotNull;

public final class PlaceholderUtilities {

    public static boolean isValidPlaceholderDefinition(@NotNull String content) {
        if (!content.startsWith("$") || !content.endsWith("$") || content.length() < 3) {
            return false;
        }
        final String placeholderContent = content.substring(1, content.length() - 1);
        return placeholderContent.matches("[a-zA-Z0-9_]+\\+[a-zA-Z0-9_.\\-:]+");
    }

    public static PlaceholderParams getPlaceholderParams(@NotNull String content) {
        final String placeholderContent = content.substring(1, content.length() - 1);
        final String[] split = placeholderContent.split("\\+");

        if (split.length < 2) {
            throw new IllegalArgumentException("Invalid placeholder definition: " + content);
        }

        final String type = split[0];
        final StringBuilder value = new StringBuilder();
        for (int i = 1; i < split.length; i++) {
            value.append(split[i]);
        }
        
        return PlaceholderParams.of(type, value.toString());
    }

    public static float getAdvance(MinecraftClient minecraftClient, Identifier font, int codePoint) {
        final TextRendererAccessor textRendererAccessor = (TextRendererAccessor) minecraftClient.textRenderer;

        final FontStorage fontStorage = textRendererAccessor.getFontStorageAccessor().apply(font);
        final Glyph glyph = fontStorage.getGlyph(codePoint, textRendererAccessor.getValidateAdvance());

        return glyph.getAdvance();
    }
}
