package me.nex.itp.params;

import me.nex.itp.params.implementation.PlaceholderParamsImpl;
import org.jetbrains.annotations.NotNull;

/**
 * Represents {@code parameters} for a placeholder.<p>
 * These parameters include the type and value of the placeholder.
 *
 * @see CharacterVisitorParams
 */
public interface PlaceholderParams {

    /**
     * Retrieves the {@code type} of the placeholder.
     *
     * @return the type of the placeholder
     */
    String type();

    /**
     * Retrieves the {@code value} of the placeholder.
     *
     * @return the value of the placeholder
     */
    String value();

    /**
     * Creates an instance of {@link PlaceholderParams} with the specified parameters.
     *
     * @param type  the type of the placeholder
     * @param value the value of the placeholder
     * @return an instance of {@link PlaceholderParams} with the specified parameters
     */
    static PlaceholderParams of(@NotNull String type, @NotNull String value) {
        return new PlaceholderParamsImpl(type, value);
    }
}
