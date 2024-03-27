package me.nex.itp.params.implementation;

import me.nex.itp.params.PlaceholderParams;
import org.jetbrains.annotations.NotNull;

/**
 * Implementation of {@link PlaceholderParams} representing parameters for a placeholder.<p>
 * These parameters include the type and value of the placeholder.
 */
public record PlaceholderParamsImpl(
        @NotNull String type,
        @NotNull String value
) implements PlaceholderParams {
}
