package me.nex.itp.params;

import me.nex.itp.params.implementation.CharacterVisitorParamsImpl;
import net.minecraft.text.CharacterVisitor;
import net.minecraft.text.Style;
import org.jetbrains.annotations.NotNull;

/**
 * Represents parameters passed to a character visitor.
 * These parameters include the index, style, and Unicode code point of the visited character.
 *
 * @see net.minecraft.text.CharacterVisitor
 */
public interface CharacterVisitorParams {

    /**
     * Retrieves the {@code index} of the visited character in the sequence.
     *
     * @return the index of the visited character
     */
    int index();

    /**
     * Retrieves the {@code style} associated with the visited character.
     *
     * @return the {@link Style} of the visited character
     */
    Style style();

    /**
     * Retrieves the {@code Unicode} code point of the visited character.
     *
     * @return the Unicode code point of the visited character
     */
    int codePoint();

    /**
     * Accepts a {@link CharacterVisitor} and invokes its accept method with the parameters of this instance.
     *
     * @param characterVisitor the character visitor to accept
     * @return {@code true} if the visitor accepts the parameters, {@code false} otherwise
     */
    default boolean accept(CharacterVisitor characterVisitor) {
        return characterVisitor.accept(
                this.index(), this.style(), this.codePoint()
        );
    }

    /**
     * Creates an instance of {@link CharacterVisitorParams} with the specified parameters.
     *
     * @param index     the index of the visited character in the sequence
     * @param codePoint the Unicode code point of the visited character
     * @param style     the style associated with the visited character
     * @return an instance of {@link CharacterVisitorParams} with the specified parameters
     */
    static CharacterVisitorParams of(int index, int codePoint, @NotNull Style style) {
        return new CharacterVisitorParamsImpl(index, codePoint, style);
    }
}