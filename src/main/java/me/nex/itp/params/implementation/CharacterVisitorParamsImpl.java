package me.nex.itp.params.implementation;

import me.nex.itp.params.CharacterVisitorParams;
import net.minecraft.text.Style;
import org.jetbrains.annotations.NotNull;

/**
 * Implementation of {@link CharacterVisitorParams} representing parameters passed to a character visitor.
 * These parameters include the index, style, and Unicode code point of the visited character.
 */
public record CharacterVisitorParamsImpl(
        int index,
        int codePoint,
        @NotNull Style style
) implements CharacterVisitorParams {
}
