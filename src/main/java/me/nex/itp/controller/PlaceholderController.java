package me.nex.itp.controller;

import me.nex.itp.PlaceholderUtilities;
import me.nex.itp.params.CharacterVisitorParams;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.CharacterVisitor;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Unmodifiable;

import java.util.Collection;

/**
 * This interface represents a {@code controller} for placeholders, allowing manipulation of placeholder parameters
 * and definitions within a system.
 */
public interface PlaceholderController {

    /**
     * Retrieves the {@link MatrixStack} associated with this {@code placeholder} controller.
     *
     * @return the MatrixStack
     */
    MatrixStack getMatrixStack();

    /**
     * Retrieves an unmodifiable {@code collection} of character visitor parameters associated with this placeholder controller.
     *
     * @return an {@link Unmodifiable} collection of character visitor parameters
     */
    @Unmodifiable
    Collection<CharacterVisitorParams> getParams();

    /**
     * Adds a character visitor {@code parameter} to this placeholder controller.
     *
     * @param param the character visitor parameter to add
     */
    void addParam(CharacterVisitorParams param);

    /**
     * Clears all character visitor parameters associated with this placeholder controller.
     */
    void clearParams();

    /**
     * Checks if this placeholder {@code controller} is currently defining a placeholder.
     *
     * @return {@code true} if this controller is defining a placeholder, {@code false} otherwise
     */
    boolean isPlaceholderDefinition();

    /**
     * Sets whether this controller is defining a placeholder.
     *
     * @param placeholderDefinition {@code true} if defining a placeholder, {@code false} otherwise
     */
    void setPlaceholderDefinition(boolean placeholderDefinition);

    /**
     * Toggles the placeholder definition status of this controller.<p>
     * If currently defining a {@code placeholder}, it will switch to not defining, and vice versa.
     */
    default void togglePlaceholderDefinition() {
        this.setPlaceholderDefinition(!this.isPlaceholderDefinition());
    }

    /**
     * Retrieves the offset associated with this placeholder controller.
     *
     * @return the offset value
     */
    float getOffset();

    /**
     * Sets the offset value for this placeholder controller.
     *
     * @param offset the offset value to set
     */
    void setOffset(float offset);

    /**
     * Adds the specified offset value to the current offset of this placeholder controller.
     *
     * @param offset the offset value to add
     */
    void addOffset(float offset);

    /**
     * Empties the placeholder definition, {@code resetting} the state of this controller.
     * If a default visitor is provided, it applies the visitor to each parameter and adjusts the offset accordingly.
     *
     * @param minecraftClient the Minecraft client instance
     * @param defaultVisitor  the default character visitor to apply (can be {@code null})
     * @see CharacterVisitorParams
     */
    default void emptyPlaceholderDefinition(MinecraftClient minecraftClient, @Nullable CharacterVisitor defaultVisitor) {
        this.setPlaceholderDefinition(false);

        for (final CharacterVisitorParams param : this.getParams()) {
            if (defaultVisitor != null) {
                param.accept(defaultVisitor);
            }
            this.addOffset(PlaceholderUtilities.getAdvance(
                    minecraftClient,
                    param.style().getFont(),
                    param.codePoint()
            ));
        }
        this.clearParams();
    }

    /**
     * Resets the state of this placeholder controller, clearing all parameters and resetting the offset to zero.
     */
    default void reset() {
        this.setPlaceholderDefinition(false);
        this.clearParams();
        this.setOffset(0);
    }
}
